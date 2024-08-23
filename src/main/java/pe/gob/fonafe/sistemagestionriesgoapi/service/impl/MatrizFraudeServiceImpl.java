/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.fonafe.sistemagestionriesgoapi.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import pe.gob.fonafe.sistemagestionriesgoapi.dao.IMatrizRiesgoDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dao.IMatrizFraudeDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTODetalleMatrizFraude;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOMatrizFraude;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOMatrizRiesgo;
import pe.gob.fonafe.sistemagestionriesgoapi.models.DetalleMatrizFraudeBean;
import pe.gob.fonafe.sistemagestionriesgoapi.models.DetalleMatrizRiesgoBean;
import pe.gob.fonafe.sistemagestionriesgoapi.models.MatrizFraudeBean;
import pe.gob.fonafe.sistemagestionriesgoapi.service.IMatrizFraudeService;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

/**
 *
 * @author CANVIA
 */
@Service
public class MatrizFraudeServiceImpl implements IMatrizFraudeService{

    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    IMatrizFraudeDao iMatrizFraudeDao;
    IMatrizRiesgoDao iMatrizRiesgoDao;

    public MatrizFraudeServiceImpl(IMatrizFraudeDao iMatrizFraudeDao) {
        this.iMatrizFraudeDao = iMatrizFraudeDao;
    }
    
    @Override
    public List<DTOMatrizFraude> listarBandejaMatrizFraude(int idEmpresa, int idPeriodo, int idMatrizNivel) {
    
          logger.info("Inicio MatrizFraudeServiceImpl - listarBandejaMatrizFraude");

        List<DTOMatrizFraude> listaBandejaMatrizFraude;

        try {
            
            listaBandejaMatrizFraude = iMatrizFraudeDao.listarBandejaMatrizFraude(idEmpresa, idPeriodo, idMatrizNivel);
        } catch (Exception ex) {
            listaBandejaMatrizFraude = new ArrayList<>();
            logger.error(ex.getMessage());
        }

        logger.info("Fin MatrizRiesgoServiceImpl - listarBandejaMatrizRiesgo");
        return listaBandejaMatrizFraude;
    
    }

    @Override
    public DTOMatrizFraude registrarMatrizFraude(MatrizFraudeBean matrizFraudeBean) {
    
       logger.info("Inicio de MatrizFraudeServiceImpl - registrarMatrizFraude");

        DTOMatrizFraude matrizFraude = new DTOMatrizFraude();
        DTOGenerico detalleMatrizFraude = null;

        try {

            detalleMatrizFraude = new DTOGenerico();

            matrizFraude = iMatrizFraudeDao.registrarMatrizFraude(matrizFraudeBean);

            if (matrizFraudeBean.getListaDetalleMatriz().size() > 0) {

                for (DetalleMatrizFraudeBean detalleMatrizFraudeBean : matrizFraudeBean.getListaDetalleMatriz()) {
                    detalleMatrizFraudeBean.setIdMatrizRiesgo(matrizFraude.getIdMatrizRiesgo());
                    detalleMatrizFraude = iMatrizFraudeDao.registrarDetalleMatrizFraude(detalleMatrizFraudeBean);
                }

            }

        } catch (Exception ex) {
            matrizFraude = new DTOMatrizFraude();
            matrizFraude.setIdMatrizRiesgo(0);
            matrizFraude.setCodigoResultado(SNConstantes.CODIGO_ERROR);
            logger.error(ex.getMessage());
        }

        logger.info("Fin de MatrizFraudeServiceImpl - registrarMatrizFraude");
        return matrizFraude;
    
    }

 

    @Override
    public Byte anularMatrizFraude(MatrizFraudeBean matrizFraudeBean) {
        
        logger.info("Inicio MatrizFraudeServiceImpl - anularMatrizFraude");

        Byte indicadorAnularMatriz;

        try {
            indicadorAnularMatriz = iMatrizFraudeDao.anularMatrizFraude(matrizFraudeBean);
        } catch (Exception ex) {
            indicadorAnularMatriz = 0;
            logger.error(ex.getMessage());
        }

        logger.info("Fin MatrizFraudeServiceImpl - anularMatrizFraude");
        return indicadorAnularMatriz;
    }

    @Override
    public DTOMatrizFraude obtenerMatrizFraude(Integer idMatrizFraude, Long idUsuario) {
        
       logger.info("Inicio MatrizFraudeServiceImpl - obtenerMatrizFraude");

        DTOMatrizFraude dtoMatrizFraude;
        DTOGenerico listaDetaMatrizFraude;

        try {
            dtoMatrizFraude = iMatrizFraudeDao.obtenerMatrizFraude(idMatrizFraude);
            listaDetaMatrizFraude = iMatrizFraudeDao.listarDetalleMatrizFraude(idMatrizFraude, idUsuario);
            dtoMatrizFraude.setListaDetalleMatriz(listaDetaMatrizFraude.getListado());
        } catch (Exception ex) {
            dtoMatrizFraude = new DTOMatrizFraude();
            logger.error(ex.getMessage());
        }

        logger.info("Fin MatrizFraudeServiceImpl - obtenerMatrizFraude");
        return dtoMatrizFraude;
    
    }

    @Override
    public DTOMatrizFraude actualizarMatrizFraude(MatrizFraudeBean matrizFraudeBean) {
        
        logger.info("Inicio MatrizFraudeServiceImpl - actualizaMatrizFraude ");

        DTOMatrizFraude matrizFraudeActual = null;
        DTODetalleMatrizFraude detaMatrizFraudeActual = null;
        DTOGenerico detaMatrizFraudeNuevo = null;

        try {

            matrizFraudeActual = new DTOMatrizFraude();
            detaMatrizFraudeActual = new DTODetalleMatrizFraude();
            detaMatrizFraudeNuevo = new DTOGenerico();

            matrizFraudeActual = iMatrizFraudeDao.actualizarMatrizFraude(matrizFraudeBean);

            if (matrizFraudeBean.getListaDetalleMatriz().size() > 0) {

                for (DetalleMatrizFraudeBean detalleMatrizFraudeBean : matrizFraudeBean.getListaDetalleMatriz()) {
                    if (detalleMatrizFraudeBean.getIdDetaMatrizFraude()== null) {
                        detalleMatrizFraudeBean.setIdMatrizRiesgo(matrizFraudeBean.getIdMatrizRiesgo());
                        detaMatrizFraudeNuevo = iMatrizFraudeDao.registrarDetalleMatrizFraude(detalleMatrizFraudeBean);
                    } else {
                        detaMatrizFraudeActual = iMatrizFraudeDao.actualizarDetaMatrizFraude(detalleMatrizFraudeBean);
                    }
                }

            }

        } catch (Exception ex) {
            matrizFraudeActual = new DTOMatrizFraude();
            matrizFraudeActual.setIdMatrizRiesgo(0);
            matrizFraudeActual.setCodigoResultado(SNConstantes.CODIGO_ERROR);
            logger.error(ex.getMessage());
        }

        logger.info("Fin MatrizFraudeServiceImpl - actualizaMatrizFraude");
        return matrizFraudeActual;
    }
    
}

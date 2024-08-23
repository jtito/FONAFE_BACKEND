/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.fonafe.sistemagestionriesgoapi.service.impl;

/**
 *
 * @author CANVIA
 */
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import pe.gob.fonafe.sistemagestionriesgoapi.dao.IMatrizAnticorrupcionDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.*;
import pe.gob.fonafe.sistemagestionriesgoapi.models.DetalleMatrizAnticorrupcionBean;
import pe.gob.fonafe.sistemagestionriesgoapi.models.MatrizRiesgoBean;

import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import java.util.ArrayList;
import java.util.List;
import pe.gob.fonafe.sistemagestionriesgoapi.models.MatrizAnticorrupcionBean;
import pe.gob.fonafe.sistemagestionriesgoapi.service.IMatrizAnticorrupcionService;

@Service
public class MatrizAnticorrupcionServiceImpl  implements IMatrizAnticorrupcionService {

    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    IMatrizAnticorrupcionDao iMatrizAnticDao;

    public MatrizAnticorrupcionServiceImpl(IMatrizAnticorrupcionDao iMatrizRiesgoDao) {
        this.iMatrizAnticDao = iMatrizRiesgoDao;
    }

    @Override
    public List<DTOMatrizAnticorrupcion> listarBandejaMatrizAntic(int idEmpresa, int idPeriodo, int idMatrizNivel) {
     
        logger.info("Inicio MatrizAnticServiceImpl - listarBandejaMatrizAntic");

        List<DTOMatrizAnticorrupcion> listaBandejaMatrizAntic;

        try {
            listaBandejaMatrizAntic = iMatrizAnticDao.listarBandejaMatrizAntic(idEmpresa, idPeriodo, idMatrizNivel);
        } catch (Exception ex) {
            listaBandejaMatrizAntic = new ArrayList<>();
            logger.error(ex.getMessage());
        }

        logger.info("Fin MatrizAnticServiceImpl - listarBandejaMatrizRiesgo");
        return listaBandejaMatrizAntic;
    
    }

    @Override
    public DTOMatrizAnticorrupcion registrarMatrizAnticorrupcion(MatrizAnticorrupcionBean matrizAnticBean) {
     
        logger.info("Inicio de MatrizAnticServiceImpl - registrarMatrizAntic");

        DTOMatrizAnticorrupcion matrizAntic = new DTOMatrizAnticorrupcion();
        DTOGenerico detalleMatrizAntic = null;

        try {

            detalleMatrizAntic = new DTOGenerico();

            matrizAntic = iMatrizAnticDao.registrarMatrizRiesgo(matrizAnticBean);

            if (matrizAnticBean.getListaDetalleMatrizAnticorrupcion().size() > 0) {

                for (DetalleMatrizAnticorrupcionBean detalleMatrizAnticBean : matrizAnticBean.getListaDetalleMatrizAnticorrupcion()) {
                    detalleMatrizAnticBean.setIdMatrizRiesgo(matrizAntic.getIdMatrizRiesgo());
                    detalleMatrizAntic = iMatrizAnticDao.registrarDetalleMatrizAnticorrupcion(detalleMatrizAnticBean);
                }

            }

        } catch (Exception ex) {
            matrizAntic = new DTOMatrizAnticorrupcion();
            matrizAntic.setIdMatrizRiesgo(0);
            matrizAntic.setCodigoResultado(SNConstantes.CODIGO_ERROR);
            logger.error("Error try "+ex.getMessage());
        }

        logger.info("Fin de MatrizAnticServiceImpl - registrarMatrizAntic");
        return matrizAntic;
    }

    @Override
    public DTOMatrizAnticorrupcion obtenerMatrizAnticorrupcion(Integer idMatrizRiesgo, Long idUsuario) {
      
        logger.info("Inicio MatrizAnticServiceImpl - obtenerMatrizAntic");

        DTOMatrizAnticorrupcion dtoMatrizAntic;
        DTOGenerico listaDetaMatrizAntic;

        try {
            dtoMatrizAntic = iMatrizAnticDao.obtenerMatrizAntic(idMatrizRiesgo);
            listaDetaMatrizAntic = iMatrizAnticDao.listarDetalleMatrizAnticorrupcion(idMatrizRiesgo, idUsuario);
            dtoMatrizAntic.setListaDetalleMatrizAnticorrupcion(listaDetaMatrizAntic.getListado());
        } catch (Exception ex) {
            dtoMatrizAntic = new DTOMatrizAnticorrupcion();
            logger.error(ex.getMessage());
        }

        logger.info("Fin MatrizRiesgoServiceImpl - obtenerMatrizRiesgo");
        return dtoMatrizAntic;
    
    }

    @Override
    public DTOMatrizAnticorrupcion actualizarMatrizAntic(MatrizAnticorrupcionBean matrizAnticBean) {
        
        logger.info("Inicio MatrizAnticServiceImpl - actualizaMatrizAntic");

        DTOMatrizAnticorrupcion matrizAnticActual = null;
        DTODetalleMatrizAnticorrupcion detaMatrizAnticActual = null;
        DTOGenerico detaMatrizAnticNuevo = null;

        try {

            matrizAnticActual = new DTOMatrizAnticorrupcion();
            detaMatrizAnticActual = new DTODetalleMatrizAnticorrupcion();
            detaMatrizAnticNuevo = new DTOGenerico();

            matrizAnticActual = iMatrizAnticDao.actualizarMatrizRiesgo(matrizAnticBean);

            if (matrizAnticBean.getListaDetalleMatrizAnticorrupcion().size() > 0) {

                for (DetalleMatrizAnticorrupcionBean detalleMatrizAnticBean : matrizAnticBean.getListaDetalleMatrizAnticorrupcion()) {
                    if (detalleMatrizAnticBean.getIdDetaMatrizAntic() == null) {
                        detalleMatrizAnticBean.setIdMatrizRiesgo(matrizAnticActual.getIdMatrizRiesgo());
                        detaMatrizAnticNuevo = iMatrizAnticDao.registrarDetalleMatrizAnticorrupcion(detalleMatrizAnticBean);
                    } else {
                        detaMatrizAnticActual = iMatrizAnticDao.actualizarDetaMatrizAnticorrupcion(detalleMatrizAnticBean);
                    }
                }

            }

        } catch (Exception ex) {
            matrizAnticActual = new DTOMatrizAnticorrupcion();
            matrizAnticActual.setIdMatrizRiesgo(0);
            matrizAnticActual.setCodigoResultado(SNConstantes.CODIGO_ERROR);
            logger.error(ex.getMessage());
        }

        logger.info("Fin MatrizAnticServiceImpl - actualizaMatrizRiesgo");
        return matrizAnticActual;
    
    }

    @Override
    public Byte anularMatrizAnticorrupcion(MatrizAnticorrupcionBean matrizAnticBean) {
        
       logger.info("Inicio MatrizAnticServiceImpl - anularMatrizAntic");

        Byte indicadorAnularMatriz;

        try {
            indicadorAnularMatriz = iMatrizAnticDao.anularMatrizRiesgo(matrizAnticBean);
        } catch (Exception ex) {
            indicadorAnularMatriz = 0;
            logger.error(ex.getMessage());
        }

        logger.info("Fin MatrizRiesgoServiceImpl - anularMatrizRiesgo");
        return 0;
    
    }

    @Override
    public DTODetalleMatrizAnticorrupcion obtenerDescripcion(String codRiesgo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

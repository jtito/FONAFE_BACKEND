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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.fonafe.sistemagestionriesgoapi.dao.IMatrizNivelDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOMatrizNivel;
import pe.gob.fonafe.sistemagestionriesgoapi.models.MatrizEventoBean;
import pe.gob.fonafe.sistemagestionriesgoapi.service.IMatrizEventoService;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import java.util.ArrayList;
import java.util.List;
import pe.gob.fonafe.sistemagestionriesgoapi.dao.IMatrizEventoDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTODetalleMatrizEvento;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOMatrizEvento;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOMatrizRiesgo;
import pe.gob.fonafe.sistemagestionriesgoapi.models.DetalleMatrizEventoBean;

@Service
public class MatrizEventoServiceImpl implements IMatrizEventoService{
    
    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    IMatrizEventoDao iMatrizEventoDao;

    public MatrizEventoServiceImpl(IMatrizEventoDao iMatrizEventoDao) {
        this.iMatrizEventoDao = iMatrizEventoDao;
    }

    @Override
    public List<DTOMatrizEvento> listarBandejaMatrizEvento(int idEmpresa, int idPeriodo, int idMatrizNivel) {
      
        logger.info("Inicio MatrizEventoServiceImpl - listarBandejaMatrizEvento");

        List<DTOMatrizEvento> listaBandejaMatrizEvento;

        try {
            listaBandejaMatrizEvento = iMatrizEventoDao.listarBandejaMatrizEvento(idEmpresa, idPeriodo, idMatrizNivel);
        } catch (Exception ex) {
            listaBandejaMatrizEvento = new ArrayList<>();
            logger.error(ex.getMessage());
        }

        logger.info("Fin MatrizEventoServiceImpl - listarBandejaMatrizEvento");
        return listaBandejaMatrizEvento;
    }

    @Override
    public DTOMatrizEvento registrarMatrizRiesgo(MatrizEventoBean matrizEventoBean) {
      
        logger.info("Inicio de MatrizEventoServiceImpl - registrarMatrizEvento");

        DTOMatrizEvento matrizEvento = new DTOMatrizEvento();
        DTOGenerico detalleMatrizEvento = null;

        try {

            detalleMatrizEvento = new DTOGenerico();

            matrizEvento = iMatrizEventoDao.registrarMatrizEvento(matrizEventoBean);

            if (matrizEventoBean.getListaDetalleMatriz().size() > 0) {

                for (DetalleMatrizEventoBean detalleMatrizEventoBean : matrizEventoBean.getListaDetalleMatriz()) {
                    detalleMatrizEventoBean.setIdMatrizRiesgo(matrizEvento.getIdMatrizRiesgo());
                    detalleMatrizEvento = iMatrizEventoDao.registrarDetalleMatrizEvento(detalleMatrizEventoBean);
                }

            }

        } catch (Exception ex) {
            matrizEvento = new DTOMatrizEvento();
            matrizEvento.setIdMatrizRiesgo(0);
            matrizEvento.setCodigoResultado(SNConstantes.CODIGO_ERROR);
            logger.error(ex.getMessage());
        }

        logger.info("Fin de MatrizEventoServiceImpl - registrarMatrizEvento");
        return matrizEvento;
    }



    @Override
    public DTOMatrizEvento obtenerMatrizRiesgo(Integer idMatrizEvento, Long idUsuario) {
     
        logger.info("Inicio MatrizEventoServiceImpl - obtenerMatrizEvento");

        DTOMatrizEvento dtoMatrizEvento;
        DTOGenerico listaDetaMatrizEvento;

        try {
            dtoMatrizEvento = iMatrizEventoDao.obtenerMatrizEvento(idMatrizEvento);
            listaDetaMatrizEvento = iMatrizEventoDao.listarDetalleMatrizEvento(idMatrizEvento, idUsuario);
            dtoMatrizEvento.setListaDetalleMatriz(listaDetaMatrizEvento.getListado());
        } catch (Exception ex) {
            dtoMatrizEvento = new DTOMatrizEvento();
            logger.error(ex.getMessage());
        }

        logger.info("Fin MatrizEventoServiceImpl - obtenerMatrizEvento");
        return dtoMatrizEvento;
    }

    @Override
    public DTOMatrizEvento actualizarMatrizRiesgo(MatrizEventoBean matrizEventoBean) {
   
        logger.info("Inicio MatrizEventoServiceImpl - actualizaMatrizEvento");

        DTOMatrizEvento matrizEventoActual = null;
        DTODetalleMatrizEvento detaMatrizEventoActual = null;
        DTOGenerico detaMatrizEventoNuevo = null;

        try {

            matrizEventoActual = new DTOMatrizEvento();
            detaMatrizEventoActual = new DTODetalleMatrizEvento();
            detaMatrizEventoNuevo = new DTOGenerico();

            matrizEventoActual = iMatrizEventoDao.actualizarMatrizEvento(matrizEventoBean);

            if (matrizEventoBean.getListaDetalleMatriz().size() > 0) {

                for (DetalleMatrizEventoBean detalleMatrizEventoBean : matrizEventoBean.getListaDetalleMatriz()) {
                    if (detalleMatrizEventoBean.getIdDetaMatrizEvento() == null) {
                        detalleMatrizEventoBean.setIdMatrizRiesgo(matrizEventoBean.getIdMatrizRiesgo());
                        detaMatrizEventoNuevo = iMatrizEventoDao.registrarDetalleMatrizEvento(detalleMatrizEventoBean);
                    } else {
                        detaMatrizEventoActual = iMatrizEventoDao.actualizarDetaMatrizEvento(detalleMatrizEventoBean);
                    }
                }

            }

        } catch (Exception ex) {
            matrizEventoActual = new DTOMatrizEvento();
            matrizEventoActual.setIdMatrizRiesgo(0);
            matrizEventoActual.setCodigoResultado(SNConstantes.CODIGO_ERROR);
            logger.error(ex.getMessage());
        }

        logger.info("Fin MatrizEventoServiceImpl - actualizaMatrizEvento");
        return matrizEventoActual;
    }

    @Override
    public Byte anularMatrizEvento(MatrizEventoBean matrizEventoBean) {
       logger.info("Inicio MatrizEventoServiceImpl - anularMatrizEvento");

        Byte indicadorAnularMatriz;

        try {
            indicadorAnularMatriz = iMatrizEventoDao.anularMatrizEvento(matrizEventoBean);
        } catch (Exception ex) {
            indicadorAnularMatriz = 0;
            logger.error(ex.getMessage());
        }

        logger.info("Fin MatrizEventoServiceImpl - anularMatrizEvento");
        return indicadorAnularMatriz;
    }

    @Override
    public DTODetalleMatrizEvento obtenerDescripcion(String codRiesgo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

package pe.gob.fonafe.sistemagestionriesgoapi.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import pe.gob.fonafe.sistemagestionriesgoapi.dao.IMatrizContinuidadDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.*;
import pe.gob.fonafe.sistemagestionriesgoapi.models.DetalleMatrizContinuidadBean;
import pe.gob.fonafe.sistemagestionriesgoapi.models.MatrizRiesgoContinuidadBean;
import pe.gob.fonafe.sistemagestionriesgoapi.service.IMatrizContinuidadService;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import java.util.ArrayList;
import java.util.List;

@Service
public class MatrizContinuidadServiceImpl implements IMatrizContinuidadService {

    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    IMatrizContinuidadDao iMatrizContinuidadDao;

    public MatrizContinuidadServiceImpl(IMatrizContinuidadDao iMatrizContinuidadDao) {
        this.iMatrizContinuidadDao = iMatrizContinuidadDao;
    }

    @Override
    public List<DTOMatrizContinuidad> listarBandejaMatrizContinuidad(int idEmpresa, int idPeriodo, int idMatrizNivel) {
        logger.info("Inicio MatrizContinuidadServiceImpl - listarBandejaMatrizContinuidad");

        List<DTOMatrizContinuidad> listaBandejaMatrizContinuidad;

        try {
            listaBandejaMatrizContinuidad = iMatrizContinuidadDao.listarBandejaMatrizContinuidad(idEmpresa, idPeriodo, idMatrizNivel);
        } catch (Exception ex) {
            listaBandejaMatrizContinuidad = new ArrayList<>();
            logger.error(ex.getMessage());
        }

        logger.info("Fin MatrizContinuidadServiceImpl - listarBandejaMatrizContinuidad");
        return listaBandejaMatrizContinuidad;
    }

    @Override
    public DTOMatrizContinuidad registrarMatrizContinuidad(MatrizRiesgoContinuidadBean matrizRiesgoContinuidadBean) {
        logger.info("Inicio de MatrizContinuidadServiceImpl - registrarMatrizCOntinuidad");

        DTOMatrizContinuidad matrizContinuidad = new DTOMatrizContinuidad();
        DTOGenerico detalleMatrizRiesgo = null;

        try {

            detalleMatrizRiesgo = new DTOGenerico();

            matrizContinuidad = iMatrizContinuidadDao.registrarMatrizRiesgo(matrizRiesgoContinuidadBean);

            if (matrizRiesgoContinuidadBean.getListaDetalleMatrizContinuidad().size() > 0) {

                for (DetalleMatrizContinuidadBean detalleMatrizContinuidadBean : matrizRiesgoContinuidadBean.getListaDetalleMatrizContinuidad()) {
                    detalleMatrizContinuidadBean.setIdMatrizRiesgo(matrizContinuidad.getIdMatrizRiesgo());
                    detalleMatrizRiesgo = iMatrizContinuidadDao.registrarDetalleMatrizContinuidad(detalleMatrizContinuidadBean);
                }

            }

        } catch (Exception ex) {
            matrizContinuidad = new DTOMatrizContinuidad();
            matrizContinuidad.setIdMatrizRiesgo(0L);
            matrizContinuidad.setCodigoResultado(SNConstantes.CODIGO_ERROR);
            logger.error(ex.getMessage());
        }

        logger.info("Fin de MatrizContinuidadServiceImpl - registrarMatrizContinuidad");
        return matrizContinuidad;
    }

    @Override
    public DTOMatrizContinuidad obtenerMatrizContinuidad(Integer idMatrizRiesgo, Long idUsuario) {
        logger.info("Inicio MatrizContinuidadServiceImpl - obtenerMatrizContinuidad");

        DTOMatrizContinuidad dtoMatrizRiesgo;
        DTOGenerico listaDetaMatrizRiesgo;

        try {
            dtoMatrizRiesgo = iMatrizContinuidadDao.obtenerMatrizRiesgo(idMatrizRiesgo);
            listaDetaMatrizRiesgo = iMatrizContinuidadDao.listarDetalleMatrizContinuidad(idMatrizRiesgo, idUsuario);
            dtoMatrizRiesgo.setListaDetalleMatrizContinuidad(listaDetaMatrizRiesgo.getListado());
        } catch (Exception ex) {
            dtoMatrizRiesgo = new DTOMatrizContinuidad();
            logger.error(ex.getMessage());
        }

        logger.info("Fin MatrizContinuidadServiceImpl - obtenerMatrizContinuidad");
        return dtoMatrizRiesgo;
    }

    @Override
    public DTOMatrizContinuidad actualizarMatrizContinuidad(MatrizRiesgoContinuidadBean matrizRiesgoBean) {
        logger.info("Inicio MatrizRiesgoServiceImpl - actualizaMatrizRiesgo");

        DTOMatrizContinuidad matrizRiesgoActual = null;
        DTODetalleMatrizContinuidad detaMatrizRiesgoActual = null;
        DTOGenerico detaMatrizRiesgoNuevo = null;

        try {

            matrizRiesgoActual = new DTOMatrizContinuidad();
            detaMatrizRiesgoActual = new DTODetalleMatrizContinuidad();
            detaMatrizRiesgoNuevo = new DTOGenerico();

            matrizRiesgoActual = iMatrizContinuidadDao.actualizarMatrizRiesgo(matrizRiesgoBean);

            if (matrizRiesgoBean.getListaDetalleMatrizContinuidad().size() > 0) {

                for (DetalleMatrizContinuidadBean detalleMatrizRiesgoBean : matrizRiesgoBean.getListaDetalleMatrizContinuidad()) {
                    if (detalleMatrizRiesgoBean.getIdDetaMatrizContinuidad() == null) {
                        detalleMatrizRiesgoBean.setIdMatrizRiesgo(matrizRiesgoBean.getIdMatrizRiesgo());
                        detaMatrizRiesgoNuevo = iMatrizContinuidadDao.registrarDetalleMatrizContinuidad(detalleMatrizRiesgoBean);
                    } else {
                        detaMatrizRiesgoActual = iMatrizContinuidadDao.actualizarDetaMatrizContinuidad(detalleMatrizRiesgoBean);
                    }
                }

            }

        } catch (Exception ex) {
            matrizRiesgoActual = new DTOMatrizContinuidad();
            matrizRiesgoActual.setIdMatrizRiesgo(0L);
            matrizRiesgoActual.setCodigoResultado(SNConstantes.CODIGO_ERROR);
            logger.error(ex.getMessage());
        }

        logger.info("Fin MatrizRiesgoServiceImpl - actualizaMatrizRiesgo");
        return matrizRiesgoActual;
    }
}
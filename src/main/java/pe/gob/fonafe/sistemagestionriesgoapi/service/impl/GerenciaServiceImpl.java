package pe.gob.fonafe.sistemagestionriesgoapi.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.fonafe.sistemagestionriesgoapi.dao.IGerenciaDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGerencia;
import pe.gob.fonafe.sistemagestionriesgoapi.models.GerenciaBean;
import pe.gob.fonafe.sistemagestionriesgoapi.service.IGerenciaService;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

@Service
@Transactional
public class GerenciaServiceImpl implements IGerenciaService {

    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    final IGerenciaDao iGerenciaDao;

    public GerenciaServiceImpl(IGerenciaDao iGerenciaDao) {
        this.iGerenciaDao = iGerenciaDao;
    }

    @Override
    public DTOGerencia registrarGerencia(GerenciaBean gerenciaBean) {
        logger.info("Inicio de GerenciaServiceImpl - registrarGerencia");

        DTOGerencia dtoGerencia;

        try {
            dtoGerencia = iGerenciaDao.registrarGerencia(gerenciaBean);
        } catch (Exception ex) {
            dtoGerencia = new DTOGerencia();
            dtoGerencia.setIdGerencia(0L);
            dtoGerencia.setCodigoResultado(SNConstantes.CODIGO_ERROR);
            logger.error(ex.getMessage());
        }

        logger.info("Fin de GerenciaServiceImpl - registrarGerencia");
        return dtoGerencia;
    }

    @Override
    public DTOGerencia obtenerGerencia(Long idGerencia) {
        logger.info("Inicio GerenciaServiceImpl - obtenerGerencia");

        DTOGerencia dtoGerencia;

        try {
            dtoGerencia = iGerenciaDao.obtenerGerencia(idGerencia);
        } catch (Exception ex) {
            dtoGerencia = new DTOGerencia();
            logger.error(ex.getMessage());
        }

        logger.info("Fin GerenciaServiceImpl - obtenerGerencia");
        return dtoGerencia;
    }

    @Override
    public DTOGenerico listarGerencias(Long idEmpresa, Long idSede) {
        logger.info("Inicio GerenciaServiceImpl - listarGerencias");

        DTOGenerico dtoGenerico;

        try {
            dtoGenerico = iGerenciaDao.listarGerencias(idEmpresa, idSede);
        } catch (Exception ex) {
            dtoGenerico = new DTOGenerico();
            logger.error(ex.getMessage());
        }

        logger.info("Fin GerenciaServiceImpl - listarGerencia");
        return dtoGenerico;
    }

    @Override
    public DTOGerencia actualizarGerencia(GerenciaBean gerenciaBean) {
        logger.info("Inicio GerenciaServiceImpl - actualizaGerencia");

        DTOGerencia dtoGerencia = null;

        try {
            dtoGerencia = new DTOGerencia();

            dtoGerencia = iGerenciaDao.actualizarGerencia(gerenciaBean);
        } catch (Exception ex) {
            dtoGerencia = new DTOGerencia();
            dtoGerencia.setIdGerencia(0L);
            dtoGerencia.setCodigoResultado(SNConstantes.CODIGO_ERROR);
            logger.error(ex.getMessage());
        }

        logger.info("Fin GerenciaServiceImpl - actualizarGerencia");
        return dtoGerencia;
    }

    @Override
    public Byte anularGerencia(GerenciaBean gerenciaBean) {
        logger.info("Inicio GerenciaServiceImpl - anularGerencia");

        Byte indicadorAnularGerencia;

        try {
            indicadorAnularGerencia = iGerenciaDao.anularGerencia(gerenciaBean);
        } catch (Exception ex) {
            indicadorAnularGerencia = 0;
            logger.error(ex.getMessage());
        }

        logger.info("Fin GerenciaServiceImpl - anularGerencia");
        return indicadorAnularGerencia;
    }
}

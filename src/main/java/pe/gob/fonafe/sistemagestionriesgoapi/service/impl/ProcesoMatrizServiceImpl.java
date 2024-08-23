package pe.gob.fonafe.sistemagestionriesgoapi.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.fonafe.sistemagestionriesgoapi.dao.IProcesoMatrizDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOProcesoMatriz;
import pe.gob.fonafe.sistemagestionriesgoapi.models.ProcesoMatrizBean;
import pe.gob.fonafe.sistemagestionriesgoapi.service.IProcesoMatrizService;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

@Service
@Transactional
public class ProcesoMatrizServiceImpl implements IProcesoMatrizService {

    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    final IProcesoMatrizDao iProcesoMatrizDao;

    public ProcesoMatrizServiceImpl(IProcesoMatrizDao iProcesoMatrizDao) {
        this.iProcesoMatrizDao = iProcesoMatrizDao;
    }

    @Override
    public DTOProcesoMatriz registrarProcesoMatriz(ProcesoMatrizBean procesoMatrizBean) {
        logger.info("Inicio de ProcesoMatrizServiceImpl - registrarProcesoMatriz");

        DTOProcesoMatriz dtoProcesoMatriz;

        try {
            dtoProcesoMatriz = iProcesoMatrizDao.registrarProcesoMatriz(procesoMatrizBean);
        } catch (Exception ex) {
            dtoProcesoMatriz = new DTOProcesoMatriz();
            dtoProcesoMatriz.setIdProcesoMatriz(0L);
            dtoProcesoMatriz.setCodigoResultado(SNConstantes.CODIGO_ERROR);
            logger.error(ex.getMessage());
        }

        logger.info("Fin de ProcesoMatrizServiceImpl - registrarProcesoMatriz");
        return dtoProcesoMatriz;
    }

    @Override
    public DTOProcesoMatriz obtenerProcesoMatriz(Long idProcesoMatriz) {
        logger.info("Inicio ProcesoMatrizServiceImpl - obtenerProcesoMatriz");

        DTOProcesoMatriz dtoProcesoMatriz;

        try {
            dtoProcesoMatriz = iProcesoMatrizDao.obtenerProcesoMatriz(idProcesoMatriz);
        } catch (Exception ex) {
            dtoProcesoMatriz = new DTOProcesoMatriz();
            logger.error(ex.getMessage());
        }

        logger.info("Fin ProcesoMatrizServiceImpl - obtenerProcesoMatriz");
        return dtoProcesoMatriz;
    }

    @Override
    public DTOGenerico listarProcesoMatriz(Long idEmpresa) {
        logger.info("Inicio ProcesoMatrizServiceImpl - listarProcesoMatriz");

        DTOGenerico dtoGenerico;

        try {
            dtoGenerico = iProcesoMatrizDao.listarProcesoMatriz(idEmpresa);
        } catch (Exception ex) {
            dtoGenerico = new DTOGenerico();
            logger.error(ex.getMessage());
        }

        logger.info("Fin ProcesoMatrizServiceImpl - listarProcesoMatriz");
        return dtoGenerico;
    }

    @Override
    public DTOProcesoMatriz actualizarProcesoMatriz(ProcesoMatrizBean procesoMatrizBean) {
        logger.info("Inicio ProcesoMatrizServiceImpl - actualizarProcesoMatriz");

        DTOProcesoMatriz dtoProcesoMatriz;

        try {
            dtoProcesoMatriz = iProcesoMatrizDao.actualizarProcesoMatriz(procesoMatrizBean);
        } catch (Exception ex) {
            dtoProcesoMatriz = new DTOProcesoMatriz();
            dtoProcesoMatriz.setIdProcesoMatriz(0L);
            dtoProcesoMatriz.setCodigoResultado(SNConstantes.CODIGO_ERROR);
            logger.error(ex.getMessage());
        }

        logger.info("Fin ProcesoMatrizServiceImpl - actualizarProcesoMatriz");
        return dtoProcesoMatriz;
    }

    @Override
    public Byte anularProcesoMatriz(ProcesoMatrizBean procesoMatrizBean) {
        logger.info("Inicio ProcesoMatrizServiceImpl - anularProcesoMatriz");

        Byte indicadorAnularProcesoMatriz;

        try {
            indicadorAnularProcesoMatriz = iProcesoMatrizDao.anularProcesoMatriz(procesoMatrizBean);
        } catch (Exception ex) {
            indicadorAnularProcesoMatriz = 0;
            logger.error(ex.getMessage());
        }

        logger.info("Fin ProcesoMatrizServiceImpl - anularProcesoMatriz");
        return indicadorAnularProcesoMatriz;
    }
}

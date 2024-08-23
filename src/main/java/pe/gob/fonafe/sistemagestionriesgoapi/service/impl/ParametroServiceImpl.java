package pe.gob.fonafe.sistemagestionriesgoapi.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.fonafe.sistemagestionriesgoapi.dao.IParametroDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOEmpresa;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOParametro;
import pe.gob.fonafe.sistemagestionriesgoapi.models.ParametroBean;
import pe.gob.fonafe.sistemagestionriesgoapi.service.IParametroService;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

@Service
@Transactional
public class ParametroServiceImpl implements IParametroService {

    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    final IParametroDao iParametroDao;

    public ParametroServiceImpl(IParametroDao iParametroDao) {
        this.iParametroDao = iParametroDao;
    }


    @Override
    public DTOParametro registrarParametro(ParametroBean parametroBean) {
        logger.info("Inicio de ParametroServiceImpl - registrarParametro");

        DTOParametro dtoParametro;

        try {
            dtoParametro = iParametroDao.registrarParametro(parametroBean);
        } catch (Exception ex) {
            dtoParametro = new DTOParametro();
            dtoParametro.setIdParametro(0L);
            dtoParametro.setCodigoResultado(SNConstantes.CODIGO_ERROR);
            logger.error(ex.getMessage());
        }

        logger.info("Fin de ParametroServiceImpl - registrarParametro");
        return dtoParametro;
    }

    @Override
    public DTOParametro obtenerParametro(Long idParametro) {
        logger.info("Inicio de ParametroServiceImpl - obtenerParametro");

        DTOParametro dtoParametro;

        try {
            dtoParametro = iParametroDao.obtenerParametro(idParametro);
        }catch (Exception ex){
            dtoParametro = new DTOParametro();
            logger.error(ex.getMessage());
        }

        logger.info("Inicio de ParametroServiceImpl - obtenerParametro");
        return dtoParametro;
    }

    @Override
    public DTOGenerico listarParametro() {

        logger.info("Inicio de ParametroServiceImpl - listarParametro");

        DTOGenerico dtoGenerico;

        try {
            dtoGenerico = iParametroDao.listarParametro();
        } catch (Exception ex) {
            dtoGenerico = new DTOGenerico();
            logger.error(ex.getMessage());
        }

        logger.info("Fin de ParametroServiceImpl - listarParametro");
        return dtoGenerico;
    }

    @Override
    public DTOParametro actualizarParametro(ParametroBean parametroBean) {
        logger.info("Inicio de ParametroServiceImpl - actualizarParametro");

        DTOParametro dtoParametro = null;

        try {
            dtoParametro = new DTOParametro();

            dtoParametro = iParametroDao.actualizarParametro(parametroBean);
        } catch (Exception ex) {
            dtoParametro = new DTOParametro();
            dtoParametro.setIdParametro(0L);
            dtoParametro.setCodigoResultado(SNConstantes.CODIGO_ERROR);
            logger.error(ex.getMessage());
        }

        logger.info("Fin de ParametroServiceImpl - actualizarParametro");
        return dtoParametro;
    }

    @Override
    public Byte anularParametro(ParametroBean parametroBean) {
        logger.info("Inicio de ParametroServiceImpl - anularParametro");

        Byte indicadorAnularParametro;

        try {
            indicadorAnularParametro = iParametroDao.anularParametro(parametroBean);
        } catch (Exception ex) {
            indicadorAnularParametro = 0;
            logger.error(ex.getMessage());
        }

        logger.info("Fin de ParametroServiceImpl - anularParametro");
        return indicadorAnularParametro;
    }
}

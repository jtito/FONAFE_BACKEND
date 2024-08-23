package pe.gob.fonafe.sistemagestionriesgoapi.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.fonafe.sistemagestionriesgoapi.dao.IDetalleParametroDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTODetalleParametro;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOParametro;
import pe.gob.fonafe.sistemagestionriesgoapi.models.DetalleParametroBean;
import pe.gob.fonafe.sistemagestionriesgoapi.service.IDetalleParametroService;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class DetalleParametroServiceImpl implements IDetalleParametroService {

    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    final IDetalleParametroDao iDetalleParametroDao;

    public DetalleParametroServiceImpl(IDetalleParametroDao iDetalleParametroDao) {
        this.iDetalleParametroDao = iDetalleParametroDao;
    }


    @Override
    public DTODetalleParametro registrarDetalleParametro(DetalleParametroBean detalleParametroBean) {

        logger.info("Inicio de DetalleParametroServiceImpl - registrarDetalleParametro");

        DTODetalleParametro dtoDetalleParametro;

        try {
            dtoDetalleParametro = iDetalleParametroDao.registrarDetalleParametro(detalleParametroBean);
        } catch (Exception ex) {
            dtoDetalleParametro = new DTODetalleParametro();
            dtoDetalleParametro.setIdDetaParametro(0L);
            dtoDetalleParametro.setCodigoResultado(SNConstantes.CODIGO_ERROR);
            logger.error(ex.getMessage());
        }

        logger.info("Fin de DetalleParametroServiceImpl - registrarDetalleParametro");

        return dtoDetalleParametro;
    }

    @Override
    public DTODetalleParametro obtenerDetalleParametro(Long idDetalleParametro) {
        logger.info("Inicio de DetalleParametroServiceImpl - obtenerDetalleParametro");

        DTODetalleParametro dtoDetalleParametro;

        try {
            dtoDetalleParametro = iDetalleParametroDao.obtenerDetalleParametro(idDetalleParametro);
        }catch (Exception ex){
            dtoDetalleParametro = new DTODetalleParametro();
            logger.error(ex.getMessage());
        }

        logger.info("Fin de DetalleParametroServiceImpl - obtenerDetalleParametro");
        return dtoDetalleParametro;
    }

    @Override
    public DTOGenerico listarDetalleParametro(Long idParametro) {
        logger.info("Inicio de DetalleParametroServiceImpl - listarDetalleParametro");

        DTOGenerico dtoGenerico;

        try {
            dtoGenerico = iDetalleParametroDao.listarDetalleParametro(idParametro);
        } catch (Exception ex) {
            dtoGenerico = new DTOGenerico();
            logger.error(ex.getMessage());
        }

        logger.info("Fin de DetalleParametroServiceImpl - listarDetalleParametro");
        return dtoGenerico;
    }

    @Override
    public DTODetalleParametro actualizarDetalleParametro(DetalleParametroBean detalleParametroBean) {

        logger.info("Inicio de DetalleParametroServiceImpl - actualizarDetalleParametro");

        DTODetalleParametro dtoDetalleParametro = null;

        try {
            dtoDetalleParametro = new DTODetalleParametro();

            dtoDetalleParametro = iDetalleParametroDao.actualizarDetalleParametro(detalleParametroBean);
        } catch (Exception ex) {
            dtoDetalleParametro = new DTODetalleParametro();
            dtoDetalleParametro.setIdDetaParametro(0L);
            dtoDetalleParametro.setCodigoResultado(SNConstantes.CODIGO_ERROR);
            logger.error(ex.getMessage());
        }

        logger.info("Fin de DetalleParametroServiceImpl - actualizarDetalleParametro");

        return dtoDetalleParametro;
    }

    @Override
    public Byte anularDetalleParametro(DetalleParametroBean detalleParametroBean) {

        logger.info("Inicio de DetalleParametroServiceImpl - anularDetalleParametro");

        Byte indicadorAnularDetalleParametro;

        try {
            indicadorAnularDetalleParametro = iDetalleParametroDao.anularDetalleParametro(detalleParametroBean);
        } catch (Exception ex) {
            indicadorAnularDetalleParametro = 0;
            logger.error(ex.getMessage());
        }


        logger.info("Fin de DetalleParametroServiceImpl - anularDetalleParametro");

        return indicadorAnularDetalleParametro;
    }
}

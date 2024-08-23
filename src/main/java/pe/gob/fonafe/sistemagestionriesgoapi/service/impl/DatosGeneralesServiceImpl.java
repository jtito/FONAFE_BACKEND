package pe.gob.fonafe.sistemagestionriesgoapi.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.fonafe.sistemagestionriesgoapi.dao.IDatosGeneralesDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.service.IDatosGeneralesService;

@Service
@Transactional
public class DatosGeneralesServiceImpl implements IDatosGeneralesService {

    final IDatosGeneralesDao iDatosGeneralesDao;

    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    public DatosGeneralesServiceImpl(IDatosGeneralesDao iDatosGeneralesDao) {
        this.iDatosGeneralesDao = iDatosGeneralesDao;
    }

    @Override
    public DTOGenerico listarParametrosxCodigo(String codigo) {
        logger.info("Inicio de DatosGeneralesServiceImpl - listarParametrosxCodigo");

        DTOGenerico dtoGenerico;

        try {
            dtoGenerico = iDatosGeneralesDao.listarParametrosxCodigo(codigo);
        } catch (Exception ex) {
            dtoGenerico = new DTOGenerico<>();
            logger.error(ex.getMessage());
        }

        logger.info("Fin de DatosGeneralesServiceImpl - listarParametrosxCodigo");
        return dtoGenerico;
    }

}

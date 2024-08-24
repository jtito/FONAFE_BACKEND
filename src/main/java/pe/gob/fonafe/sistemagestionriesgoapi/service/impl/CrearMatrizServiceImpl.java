package pe.gob.fonafe.sistemagestionriesgoapi.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.fonafe.sistemagestionriesgoapi.dao.ICrearMatrizDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOCrearMatriz;
import pe.gob.fonafe.sistemagestionriesgoapi.service.ICrearMatrizService;

@Service
@Transactional
public abstract class CrearMatrizServiceImpl implements ICrearMatrizService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    final ICrearMatrizDao crearMatrizDao;

public CrearMatrizServiceImpl(ICrearMatrizDao crearMatrizDao) {
        this.crearMatrizDao = crearMatrizDao;
    }

    @Override
    public DTOCrearMatriz crearMatriz(DTOCrearMatriz dtoCrearMatriz) {
        return crearMatrizDao.crearMatriz(dtoCrearMatriz);
    }

    @Override
    public DTOCrearMatriz obtenerMatriz() {
        logger.info("obtenerMatriz");
        DTOCrearMatriz dtoCrearMatriz;
        try {
            dtoCrearMatriz = crearMatrizDao.obtenerCrearMatriz();
        } catch (Exception e) {
            logger.error("Error al obtener la matriz", e);
            dtoCrearMatriz = new DTOCrearMatriz();
            dtoCrearMatriz.setCodigoRiesgo("99");
            dtoCrearMatriz.setDescripcionRiesgo("Error al obtener la matriz");
        }
        logger.info("dtoCrearMatriz: {}", dtoCrearMatriz);
        return dtoCrearMatriz;
    }

    @Override
    public DTOCrearMatriz listarMatriz() {
        logger.info("listarMatriz");
        DTOCrearMatriz dtoCrearMatriz;
        try {
            dtoCrearMatriz = crearMatrizDao.obtenerCrearMatriz();
        } catch (Exception e) {
            logger.error("Error al listar la matriz", e);
            dtoCrearMatriz = new DTOCrearMatriz();
            dtoCrearMatriz.setCodigoRiesgo("99");
            dtoCrearMatriz.setDescripcionRiesgo("Error al listar la matriz");
        }
        logger.info("dtoCrearMatriz: {}", dtoCrearMatriz);
        return dtoCrearMatriz;
    }



}

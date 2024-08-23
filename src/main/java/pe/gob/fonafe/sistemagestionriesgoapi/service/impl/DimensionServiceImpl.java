package pe.gob.fonafe.sistemagestionriesgoapi.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.gob.fonafe.sistemagestionriesgoapi.dao.IDimensionDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTODimension;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOPeriodo;
import pe.gob.fonafe.sistemagestionriesgoapi.service.IDimensionService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class DimensionServiceImpl implements IDimensionService {

    @Autowired
    private IDimensionDao iDimensionDao;
    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    @Override
    public DTOGenerico listarDimension(Long p_ID_EMPRESA, Long p_ID_TIPO_CONTROL_RIESGO) {
        // TODO Auto-generated method stub
        DTOGenerico dTOGenerico;
        try {
            dTOGenerico = iDimensionDao.listarDimension(p_ID_EMPRESA, p_ID_TIPO_CONTROL_RIESGO);
        } catch (Exception e) {
            // TODO: handle exception
            dTOGenerico = new DTOGenerico<>();
            List<DTOPeriodo> listDtoPeriodo = new ArrayList<DTOPeriodo>();
            dTOGenerico.setCodigoResultado(new BigDecimal(0));
            dTOGenerico.setDescripcionResultado(e.getMessage());
            dTOGenerico.setListado(listDtoPeriodo);
        }
        return dTOGenerico;
    }

    @Override
    public DTODimension registrarDimension(DTODimension dimension) {
        // TODO Auto-generated method stub
        DTODimension dTODimension;
        try {
            dTODimension = iDimensionDao.registrarDimension(dimension);
        } catch (Exception e) {
            // TODO: handle exception
            dTODimension = new DTODimension();
            logger.error("Error dimension");
        }
        return dTODimension;
    }

    @Override
    public DTOGenerico<?> buscarDimension(Long p_id_DIM_RIESGO) {
        // TODO Auto-generated method stub

        DTOGenerico dTOGenerico = new DTOGenerico<>();
        try {
            dTOGenerico = iDimensionDao.buscarDimension(p_id_DIM_RIESGO);
        } catch (Exception e) {
            // TODO: handle exception
            dTOGenerico = new DTOGenerico<>();
            List<DTOPeriodo> listDtoPeriodo = new ArrayList<DTOPeriodo>();
            dTOGenerico.setCodigoResultado(new BigDecimal(0));
            dTOGenerico.setDescripcionResultado(e.getMessage());
            dTOGenerico.setListado(listDtoPeriodo);
            logger.error(e.getMessage());
        }
        return dTOGenerico;
    }

    @Override
    public DTODimension actualizarDimension(DTODimension dTODimension) {
        // TODO Auto-generated method stub
        DTODimension dtoDimension;
        try {
            dtoDimension = iDimensionDao.actualizarDimension(dTODimension);
        } catch (Exception e) {
            // TODO: handle exception
            dtoDimension = new DTODimension();
            logger.error(e.getMessage());
        }
        return dtoDimension;
    }

    @Override
    public DTODimension anularDimension(DTODimension dTODimension) {
        // TODO Auto-generated method stub
        DTODimension dtoDimension;
        try {
            dtoDimension = iDimensionDao.anularDimension(dTODimension);
        } catch (Exception e) {
            // TODO: handle exception
            dtoDimension = new DTODimension();
            logger.error(e.getMessage());
        }
        return dtoDimension;
    }

}

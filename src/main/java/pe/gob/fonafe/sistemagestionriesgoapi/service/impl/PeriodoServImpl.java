package pe.gob.fonafe.sistemagestionriesgoapi.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.fonafe.sistemagestionriesgoapi.dao.IPeriodoDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOPeriodo;
import pe.gob.fonafe.sistemagestionriesgoapi.service.IPeriodoService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PeriodoServImpl implements IPeriodoService {


    @Autowired
    private IPeriodoDao iPeriodoDao;

    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    @Override
    public DTOGenerico listarPeriodos(Long empresa, Long anio) {
        // TODO Auto-generated method stub
        logger.info("Inicio PeriodoServiceImpl - listarPeriodos");
        DTOGenerico dTOGenerico;
        try {
            dTOGenerico = iPeriodoDao.listarPeriodos(empresa, anio);
        } catch (Exception e) {
            // TODO: handle exception
            dTOGenerico = new DTOGenerico<>();
            List<DTOPeriodo> listDtoPeriodo = new ArrayList<DTOPeriodo>();
            dTOGenerico.setCodigoResultado(new BigDecimal(0));
            dTOGenerico.setDescripcionResultado(e.getMessage());
            dTOGenerico.setListado(listDtoPeriodo);
            logger.error(e.getMessage());
        }
        logger.info("Fin PeriodoServImpl - listarPeriodos");
        return dTOGenerico;
    }

    @Override
    public DTOGenerico buscarPeriodo(Long p_id_periodo) {
        // TODO Auto-generated method stub
        logger.info("Inicio PeriodoServiceImpl - buscarPeriodo");
        DTOGenerico dTOGenerico;
        DTOPeriodo dTOPeriodo;
        try {
            dTOGenerico = iPeriodoDao.buscarPeriodo(p_id_periodo);
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
    public DTOPeriodo registrarPeriodo(DTOPeriodo dTOPeriodo) {
        // TODO Auto-generated method stub
        logger.info("Inicio PeriodoServiceImpl - buscarPeriodo");
        DTOPeriodo dTOPeriodoR;
        try {
            dTOPeriodoR = iPeriodoDao.registrarPeriodo(dTOPeriodo);
        } catch (Exception e) {
            // TODO: handle exception
            dTOPeriodoR = new DTOPeriodo();
            logger.error("Error: " + e.getMessage());
        }
        return dTOPeriodoR;
    }

    @Override
    public DTOPeriodo actualizarPeriodo(DTOPeriodo dTOPeriodo) {
        // TODO Auto-generated method stub
        logger.info("Inicio PeriodoServiceImpl - buscarPeriodo");
        DTOPeriodo dTOPeriodoR;
        try {
            dTOPeriodoR = iPeriodoDao.actualizarPeriodo(dTOPeriodo);
        } catch (Exception e) {
            // TODO: handle exception
            dTOPeriodoR = new DTOPeriodo();
            logger.error(e.getMessage());
        }
        return dTOPeriodoR;
    }

    @Override
    public DTOPeriodo anularPeriodo(DTOPeriodo dTOPeriodo) {
        // TODO Auto-generated method stub
        logger.info("Inicio PeriodoServiceImpl - buscarPeriodo");
        DTOPeriodo dTOPeriodoR;
        try {
            dTOPeriodoR = iPeriodoDao.anularPeriodo(dTOPeriodo);
        } catch (Exception e) {
            // TODO: handle exception
            dTOPeriodoR = new DTOPeriodo();
            logger.error(e.getMessage());
        }
        return dTOPeriodoR;
    }

    @Override
    public DTOGenerico generarDePeriodo(Long idEmpresa, Long anio, Long idFrecuencia) {
        logger.info("Inicio de Inicio PeriodoServiceImpl - generarDePeriodo");

        DTOGenerico resultado = null;
        try
        {
            resultado = iPeriodoDao.generarDePeriodo(idEmpresa,anio,idFrecuencia);
        } catch (Exception ex){
            resultado = new DTOGenerico();
            logger.error(ex.getMessage());
        }

        logger.info("Fin de Inicio PeriodoServiceImpl - generarDePeriodo");
        return resultado;
    }


}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.fonafe.sistemagestionriesgoapi.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOEncuesta;
import pe.gob.fonafe.sistemagestionriesgoapi.models.EncuestaBean;
import pe.gob.fonafe.sistemagestionriesgoapi.service.IEncuestaService;
import pe.gob.fonafe.sistemagestionriesgoapi.service.IPreguntaService;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import pe.gob.fonafe.sistemagestionriesgoapi.dao.IEncuestaDao;

@Service
@Transactional
public class EncuestaServiceImpl implements IEncuestaService {


    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    final IEncuestaDao iEncuestaDao;

    final IPreguntaService iPreguntaService;

    public EncuestaServiceImpl(IEncuestaDao iEncuestaDao, IPreguntaService iPreguntaService) {
        this.iEncuestaDao = iEncuestaDao;
        this.iPreguntaService = iPreguntaService;
    }

    @Override
    public DTOEncuesta registrarEncuesta(EncuestaBean encuestaBean) {


        logger.info("Inicio de EncuestaServiceImpl - registrarEncuesta");

        DTOEncuesta dtoEncuesta;

        try {
            dtoEncuesta = iEncuestaDao.registrarEncuesta(encuestaBean);
        } catch (Exception ex) {
            dtoEncuesta = new DTOEncuesta();
            dtoEncuesta.setIdEncuesta(0L);
            dtoEncuesta.setCodigoResultado(SNConstantes.CODIGO_ERROR);
            logger.error(ex.getMessage());
        }

        logger.info("Fin de EncuestaServiceImpl - registrarEncuesta");
        return dtoEncuesta;
    }

    @Override
    public DTOEncuesta obtenerEncuesta(Long idEncuesta) {

        logger.info("Inicio EncuestaServiceImpl - obtenerEncuesta");

        DTOEncuesta dtoEncuesta;

        DTOGenerico listaPreguntas;

        try {
            dtoEncuesta = iEncuestaDao.obtenerEncuesta(idEncuesta);
            listaPreguntas = iPreguntaService.listarPreguntas(idEncuesta);
            dtoEncuesta.setListaPreguntas(listaPreguntas.getListado());
        } catch (Exception ex) {
            dtoEncuesta = new DTOEncuesta();
            logger.error(ex.getMessage());
        }

        logger.info("Fin EncuestaServiceImpl - obtenerEncuesta");
        return dtoEncuesta;

    }

    @Override
    public DTOGenerico listarEncuesta(Long idEmpresa, Long idPeriodo) {


        logger.info("Inicio EncuestaServiceImpl - listarEncuesta");

        DTOGenerico dtoGenerico;

        try {
            dtoGenerico = iEncuestaDao.listarEncuestas(idEmpresa, idPeriodo);
        } catch (Exception ex) {
            dtoGenerico = new DTOGenerico();
            logger.error(ex.getMessage());
        }

        logger.info("Fin EncuestaServiceImpl - listarEncuesta");
        return dtoGenerico;

    }

    @Override
    public DTOEncuesta actualizarEncuesta(EncuestaBean encuestaBean) {

        logger.info("Inicio EncuestaServiceImpl - actualizaEncuesta");

        DTOEncuesta dtoEncuesta = null;

        try {
            dtoEncuesta = new DTOEncuesta();

            dtoEncuesta = iEncuestaDao.actualizarEncuesta(encuestaBean);
        } catch (Exception ex) {
            dtoEncuesta = new DTOEncuesta();
            dtoEncuesta.setIdEncuesta(0L);
            dtoEncuesta.setCodigoResultado(SNConstantes.CODIGO_ERROR);
            logger.error(ex.getMessage());
        }

        logger.info("Fin EncuestaServiceImpl - actualizarEncuesta");
        return dtoEncuesta;

    }

    @Override
    public Byte anularEncuesta(EncuestaBean encuestaBean) {

        logger.info("Inicio EncuestaServiceImpl - anularEncuesta");

        Byte indicadorAnularEncuesta;

        try {
            indicadorAnularEncuesta = iEncuestaDao.anularEncuesta(encuestaBean);
        } catch (Exception ex) {
            indicadorAnularEncuesta = 0;
            logger.error(ex.getMessage());
        }

        logger.info("Fin EncuestaServiceImpl - anularEncuesta");
        return indicadorAnularEncuesta;

    }

}

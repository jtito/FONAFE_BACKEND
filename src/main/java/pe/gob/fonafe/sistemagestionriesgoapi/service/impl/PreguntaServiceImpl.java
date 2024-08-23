/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.fonafe.sistemagestionriesgoapi.service.impl;

/**
 * @author joell
 */

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.fonafe.sistemagestionriesgoapi.dao.IRespuestaDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.*;
import pe.gob.fonafe.sistemagestionriesgoapi.models.RespuestaBean;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import pe.gob.fonafe.sistemagestionriesgoapi.dao.IPreguntaDao;
import pe.gob.fonafe.sistemagestionriesgoapi.models.PreguntaBean;
import pe.gob.fonafe.sistemagestionriesgoapi.service.IPreguntaService;

import java.util.List;

@Service
@Transactional
public class PreguntaServiceImpl implements IPreguntaService {


    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    final IPreguntaDao iPreguntaDao;

    final IRespuestaDao iRespuestaDao;

    public PreguntaServiceImpl(IPreguntaDao iPreguntaDao, IRespuestaDao iRespuestaDao) {
        this.iPreguntaDao = iPreguntaDao;
        this.iRespuestaDao = iRespuestaDao;
    }

    @Override
    public DTOPregunta registrarPregunta(PreguntaBean preguntaBean) {


        logger.info("Inicio de PreguntaServiceImpl - registrarPregunta");

        DTOPregunta dtoPregunta;

        try {
            dtoPregunta = iPreguntaDao.registrarPregunta(preguntaBean);
        } catch (Exception ex) {
            dtoPregunta = new DTOPregunta();
            dtoPregunta.setIdPregunta(0L);
            dtoPregunta.setCodigoResultado(SNConstantes.CODIGO_ERROR);
            logger.error(ex.getMessage());
        }

        logger.info("Fin de PreguntaServiceImpl - registrarPregunta");
        return dtoPregunta;

    }

    @Override
    public DTOPregunta obtenerPregunta(Long idPregunta) {

        logger.info("Inicio PreguntaServiceImpl - obtenerPregunta");

        DTOPregunta dtoPregunta;

        try {
            dtoPregunta = iPreguntaDao.obtenerPregunta(idPregunta);
        } catch (Exception ex) {
            dtoPregunta = new DTOPregunta();
            logger.error(ex.getMessage());
        }

        logger.info("Fin PreguntaServiceImpl - obtenerPregunta");
        return dtoPregunta;

    }

    @Override
    public DTOGenerico listarPreguntas(Long idEncuesta) {

        logger.info("Inicio PreguntaServiceImpl - listarPregunta");

        DTOGenerico dtoGenerico;

        DTOGenerico listaRespuestas;

        List<DTOPregunta> listaPreguntas;

        try {
            dtoGenerico = iPreguntaDao.listarPreguntas(idEncuesta);

            listaPreguntas = dtoGenerico.getListado();

            for (DTOPregunta pregunta : listaPreguntas) {
                listaRespuestas = iRespuestaDao.listarRespuestas(pregunta.getIdPregunta());
                pregunta.setListaRespuestas(listaRespuestas.getListado());
            }

            dtoGenerico.setListado(listaPreguntas);

        } catch (Exception ex) {
            dtoGenerico = new DTOGenerico();
            logger.error(ex.getMessage());
        }

        logger.info("Fin PreguntaServiceImpl - listarPregunta");
        return dtoGenerico;
    }

    @Override
    public DTOPregunta actualizarPregunta(PreguntaBean preguntaBean) {

        logger.info("Inicio PreguntaServiceImpl - actualizaPregunta");

        DTOPregunta dtoPregunta = null;

        try {
            dtoPregunta = new DTOPregunta();

            dtoPregunta = iPreguntaDao.actualizarPregunta(preguntaBean);
        } catch (Exception ex) {
            dtoPregunta = new DTOPregunta();
            dtoPregunta.setIdPregunta(0L);
            dtoPregunta.setCodigoResultado(SNConstantes.CODIGO_ERROR);
            logger.error(ex.getMessage());
        }

        logger.info("Fin PreguntaServiceImpl - actualizarPregunta");
        return dtoPregunta;

    }

    @Override
    public Byte anularPregunta(PreguntaBean preguntaBean) {

        logger.info("Inicio PreguntaServiceImpl - anularPregunta");

        Byte indicadorAnularPregunta;

        try {
            indicadorAnularPregunta = iPreguntaDao.anularPregunta(preguntaBean);
        } catch (Exception ex) {
            indicadorAnularPregunta = 0;
            logger.error(ex.getMessage());
        }

        logger.info("Fin PreguntaServiceImpl - anularPregunta");
        return indicadorAnularPregunta;

    }

    @Override
    public DTOGenerico registrarPreguntas(DTOPreguntas preguntas) {

        DTOGenerico dtoGeneric = null;
        DTOPregunta dtoPregunta = null;
        DTORespuesta dtoRespuesta = null;
        try {
            dtoGeneric = new DTOGenerico();
            for (PreguntaBean pregunta : preguntas.getListaPreguntas()) {
                dtoPregunta = iPreguntaDao.registrarPregunta(pregunta);
                logger.info("Registro Pregunta Service - " + dtoPregunta.getCodigoResultado());

                if (pregunta.getListaRespuestas().size() > 0) {

                    for (RespuestaBean respuestaBean : pregunta.getListaRespuestas()) {
                        respuestaBean.setIdPregunta(dtoPregunta.getIdPregunta());
                        dtoRespuesta = iRespuestaDao.registrarRespuesta(respuestaBean);
                    }
                }
            }
            dtoGeneric.setCodigoResultado(dtoPregunta.getCodigoResultado());
            dtoGeneric.setDescripcionResultado(dtoPregunta.getDescripcionResultado());
        } catch (Exception ex) {
            ;
            logger.error("Error " + ex.getMessage());
        }

        logger.info("Fin de PreguntaServiceImpl - registrarPreguntas");
        return dtoGeneric;
    }

    @Override
    public DTOGenerico actualizarPreguntas(DTOPreguntas preguntas) {

        DTOGenerico dtoGeneric = null;
        DTOPregunta dtoPregunta = null;
        DTORespuesta dtoRespuesta = null;
        try {
            dtoGeneric = new DTOGenerico();
            Long idanular = 0L;

            for (PreguntaBean pregunta : preguntas.getListaPreguntas()) {
                if (pregunta.getIdPregunta() == null) {
                    dtoPregunta = iPreguntaDao.registrarPregunta(pregunta);
                } else {
                    dtoPregunta = iPreguntaDao.actualizarPregunta(pregunta);
                }

                if (pregunta.getListaRespuestas().size() > 0) {

                    for (RespuestaBean respuestaBean : pregunta.getListaRespuestas()) {
                        if (respuestaBean.getIdRespuesta() == null) {
                            respuestaBean.setIdPregunta(dtoPregunta.getIdPregunta());
                            dtoRespuesta = iRespuestaDao.registrarRespuesta(respuestaBean);
                        } else {
                            dtoRespuesta = iRespuestaDao.actualizarRespuesta(respuestaBean);
                        }
                    }
                }
                logger.info("Registro Pregunta Service - " + dtoPregunta.getCodigoResultado());
            }

            /*if(preguntas.getLstPreguntaDel().size()>0) {
                for (PreguntaBean pregunta : preguntas.getLstPreguntaDel()) {

                    idanular = iPreguntaDao.anularPregunta(pregunta).longValue();


                    logger.info("Anular Pregunta Service - " + idanular);


                }
            }*/

            dtoGeneric.setCodigoResultado(dtoPregunta.getCodigoResultado());
            dtoGeneric.setDescripcionResultado(dtoPregunta.getDescripcionResultado());
        } catch (Exception ex) {
            ;
            logger.error("Error " + ex.getMessage());
        }

        logger.info("Fin de PreguntaServiceImpl - actualizar");
        return dtoGeneric;

    }


}

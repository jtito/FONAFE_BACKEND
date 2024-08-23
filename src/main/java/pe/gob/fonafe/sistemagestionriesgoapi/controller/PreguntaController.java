/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.fonafe.sistemagestionriesgoapi.controller;

/**
 * @author joell
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOPregunta;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOPreguntas;
import pe.gob.fonafe.sistemagestionriesgoapi.models.PreguntaBean;
import pe.gob.fonafe.sistemagestionriesgoapi.service.IPreguntaService;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/gestionriesgo/pregunta")
public class PreguntaController {

    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    final IPreguntaService iPreguntaService;

    public PreguntaController(IPreguntaService iPreguntaService) {
        this.iPreguntaService = iPreguntaService;
    }


    @PostMapping(value = "/registrarPregunta", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para registrar una pregunta", notes = "Registra una pregunta.")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> registrarPregunta(@RequestBody PreguntaBean preguntaBean) {
        logger.info("Inicio de PreguntaController - registrarPregunta");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonPeticion = gson.toJson(preguntaBean);
        logger.info("Peticion : {}", jsonPeticion);

        Map<String, Object> responseResultado = new HashMap<>();
        DTOPregunta dtoPregunta;
        try {
            dtoPregunta = iPreguntaService.registrarPregunta(preguntaBean);

            if (dtoPregunta.getCodigoResultado().compareTo(SNConstantes.CODIGO_EXITO) != 0) {
                responseResultado.put(SNConstantes.CODIGO, dtoPregunta.getCodigoResultado());
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R_BD);
                responseResultado.put(SNConstantes.ID_PREGUNTA, 0L);
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_PREGUNTA
                        + SNConstantes.MENSAJE_EXITO_REGISTRAR);
                responseResultado.put(SNConstantes.ID_PREGUNTA, dtoPregunta.getIdPregunta());
            }

        } catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            responseResultado.put(SNConstantes.ID_PREGUNTA, 0L);

            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado : {}", jsonResultado);

        logger.info("Fin de PreguntaController - registrarPregunta");
        return new ResponseEntity<>(responseResultado, HttpStatus.CREATED);
    }


    @PostMapping(value = "/registrarPreguntas", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para registrar preguntas", notes = "Registra preguntas.")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> registrarPreguntas(@RequestBody DTOPreguntas preguntas) {
        logger.info("Inicio de PreguntaController - registrarPreguntas");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonPeticion = gson.toJson(preguntas);
        logger.info("Peticion : {}", jsonPeticion);

        Map<String, Object> responseResultado = new HashMap<>();
        DTOGenerico dtoGenerico;
        try {
            dtoGenerico = iPreguntaService.registrarPreguntas(preguntas);

            if (dtoGenerico.getCodigoResultado().compareTo(SNConstantes.CODIGO_EXITO) != 0) {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_PREGUNTAS
                        + "no se pudieron registrar");
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_PREGUNTAS
                        + SNConstantes.MENSAJE_EXITO_REGISTRAR_MULTIPLE);
            }

        } catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_PREGUNTAS
                    + "no se pudieron registrar");

            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado : {}", jsonResultado);

        logger.info("Fin de PreguntaController - registrarPreguntas");
        return new ResponseEntity<>(responseResultado, HttpStatus.CREATED);
    }


    @PutMapping(value = "/actualizarPreguntas", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para actualizar preguntas", notes = "Actualizar preguntas.")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> actualizarPreguntas(@RequestBody DTOPreguntas preguntas) {
        logger.info("Inicio de PreguntaController - actualizarPreguntas");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonPeticion = gson.toJson(preguntas);
        logger.info("Peticion : {}", jsonPeticion);

        Map<String, Object> responseResultado = new HashMap<>();
        DTOGenerico dtoGenerico;
        try {
            dtoGenerico = iPreguntaService.actualizarPreguntas(preguntas);

            if (dtoGenerico.getCodigoResultado().compareTo(SNConstantes.CODIGO_EXITO) != 0) {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_PREGUNTAS
                        + "no se pudieron actualizar");
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_PREGUNTAS
                        + SNConstantes.MENSAJE_EXITO_ACTUALIZAR_MULTIPLE);
            }

        } catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_PREGUNTAS
                    + "no se pudieron actualizar");

            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado : {}", jsonResultado);

        logger.info("Fin de PreguntaController - actualizarPreguntas");
        return new ResponseEntity<>(responseResultado, HttpStatus.CREATED);
    }


    @GetMapping(value = "/listarPregunta/{idEncuesta}")
    @ApiOperation(value = "Endpoint para listar las pregunta", notes = "Retorna la lista de preguntas.")
    public ResponseEntity<?> listarPreguntas(@PathVariable Long idEncuesta) {
        logger.info("Inicio PreguntaController - listarPregunta");

        Map<String, Object> responseResultado = new HashMap<>();

        DTOGenerico dtoGenerico = new DTOGenerico();
        try {
            dtoGenerico = iPreguntaService.listarPreguntas(idEncuesta);

            if (dtoGenerico.getCodigoResultado().compareTo(SNConstantes.CODIGO_EXITO) != 0) {
                responseResultado.put(SNConstantes.CODIGO, dtoGenerico.getCodigoResultado());
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R_BD);
                responseResultado.put("listaPreguntas", dtoGenerico.getListado());
            } else {
                responseResultado.put(SNConstantes.CODIGO, dtoGenerico.getCodigoResultado());
                responseResultado.put(SNConstantes.MENSAJE, dtoGenerico.getDescripcionResultado());
                responseResultado.put("listaPreguntas", dtoGenerico.getListado());
            }
        } catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado: {}", jsonResultado);

        logger.info("Fin PreguntaController - listarPreguntas");
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }


    @GetMapping(value = "/obtenerPregunta/{idPregunta}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para obtener la pregunta", notes = "Retorna una pregunta.")
    public ResponseEntity<?> obtenerPregunta(@PathVariable Long idPregunta) {
        logger.info("Inicio PreguntaController - obtenerPregunta");

        Map<String, Object> responseResultado = new HashMap<>();
        DTOPregunta dtoPregunta;
        try {
            dtoPregunta = iPreguntaService.obtenerPregunta(idPregunta);
            if (dtoPregunta.getIdPregunta() == null) {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
                responseResultado.put("Pregunta", "No existe ninguna Pregunta");
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_EXITO);
                responseResultado.put("Pregunta", dtoPregunta);
            }
        } catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado {}", jsonResultado);

        logger.info("Fin PreguntaController - obtenerPregunta");
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }


    @PutMapping(value = "/actualizarPregunta", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para actualizar una pregunta", notes = "Actualiza una pregunta.")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> actualizarpregunta(@RequestBody PreguntaBean preguntaBean) {
        logger.info("Inicio PreguntaController - actualizarPregunta");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonPeticion = gson.toJson(preguntaBean);
        logger.info("Peticion : {}", jsonPeticion);

        Map<String, Object> responseResultado = new HashMap<>();
        DTOPregunta dtoPregunta;

        try {
            dtoPregunta = iPreguntaService.actualizarPregunta(preguntaBean);
            if (dtoPregunta.getCodigoResultado().compareTo(SNConstantes.CODIGO_EXITO) != 0) {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R_BD);
                responseResultado.put(SNConstantes.ID_PREGUNTA, 0L);
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_PREGUNTA
                        + SNConstantes.MENSAJE_EXITO_ACTUALIZAR);
                responseResultado.put(SNConstantes.ID_PREGUNTA, preguntaBean.getIdPregunta());
            }
        } catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            responseResultado.put(SNConstantes.ID_PREGUNTA, 0L);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado : {}", jsonResultado);

        logger.info("Fin PreguntaController - actualizarPregunta");
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }


    @PostMapping(value = "/anularPregunta", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para anular una pregunta", notes = "Anula una pregunta.")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> anularPregunta(@RequestBody PreguntaBean preguntaBean) {
        logger.info("Inicio PreguntaController - anularPregunta");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonPeticion = gson.toJson(preguntaBean);
        logger.info("Peticion : {}", jsonPeticion);

        Map<String, Object> responseResultado = new HashMap<>();
        DTOPregunta dtoPregunta;

        try {
            dtoPregunta = iPreguntaService.obtenerPregunta(preguntaBean.getIdPregunta());
            Byte indicadorAnularPregunta = iPreguntaService.anularPregunta(preguntaBean);

            if (indicadorAnularPregunta != 1) {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_EXITO);
                responseResultado.put(SNConstantes.ID_PREGUNTA, 0L);
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_PREGUNTA + SNConstantes.MENSAJE_EXITO_ANULAR);
                responseResultado.put(SNConstantes.ID_PREGUNTA, dtoPregunta.getIdPregunta());
            }
        } catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String jsonResultado = gson.toJson(dtoPregunta);
        logger.info("Resultado : {}", jsonResultado);

        logger.info("Fin PreguntaController - anularPregunta");
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }

}

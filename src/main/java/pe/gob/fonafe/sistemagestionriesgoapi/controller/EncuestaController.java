/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.fonafe.sistemagestionriesgoapi.controller;

/**
 *
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
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOEncuesta;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.models.EncuestaBean;
import pe.gob.fonafe.sistemagestionriesgoapi.service.IEncuestaService;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/gestionriesgo/encuesta")
public class EncuestaController {

    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    final IEncuestaService iEncuestaService;

    public EncuestaController(IEncuestaService iEncuestaService) {
        this.iEncuestaService = iEncuestaService;
    }
    
    
    @PostMapping(value = "/registrarEncuesta", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para registrar una encuesta", notes = "Registra una encuesta.")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> registrarEncuesta(@RequestBody EncuestaBean encuestaBean){
        logger.info("Inicio de EncuestaController - registrarEncuesta");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonPeticion = gson.toJson(encuestaBean);
        logger.info("Peticion : {}", jsonPeticion);

        Map<String,Object> responseResultado = new HashMap<>();
        DTOEncuesta dTOEncuesta;
        try {
            dTOEncuesta = iEncuestaService.registrarEncuesta(encuestaBean);

            if (dTOEncuesta.getCodigoResultado().compareTo(SNConstantes.CODIGO_EXITO) != 0){
                responseResultado.put(SNConstantes.CODIGO,dTOEncuesta.getCodigoResultado());
                responseResultado.put(SNConstantes.MENSAJE,SNConstantes.MENSAJE_ERR0R_BD);
                responseResultado.put(SNConstantes.ID_ENCUESTA,0L);
            }else {
                responseResultado.put(SNConstantes.CODIGO,SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE,SNConstantes.MENSAJE_ENCUESTA
                        + SNConstantes.MENSAJE_EXITO_REGISTRAR);
                responseResultado.put(SNConstantes.ID_ENCUESTA,dTOEncuesta.getIdEncuesta());
            }

        }catch (Exception ex){
            responseResultado.put(SNConstantes.CODIGO,SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE,SNConstantes.MENSAJE_ERR0R);
            responseResultado.put(SNConstantes.ID_ENCUESTA,0L);

            return new ResponseEntity<>(responseResultado,HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado : {}", jsonResultado);

        logger.info("Fin de EncuestaController - registrarEncuesta");
        return new ResponseEntity<>(responseResultado,HttpStatus.CREATED);
    }
    
    
    
    @GetMapping(value = "/listarEncuestas/{idEmpresa}/{idPeriodo}")
    @ApiOperation(value = "Endpoint para listar las encuestas", notes = "Retorna la lista de encuestas.")
    public ResponseEntity<?> listarEncuestas(@PathVariable Long idEmpresa, @PathVariable Long idPeriodo){
        logger.info("Inicio EncuestaController - listarEncuesta");

        Map<String,Object> responseResultado = new HashMap<>();

        DTOGenerico dtoGenerico = new DTOGenerico();
        try {
            dtoGenerico = iEncuestaService.listarEncuesta(idEmpresa, idPeriodo);

            if (dtoGenerico.getCodigoResultado().compareTo(SNConstantes.CODIGO_EXITO) != 0){
                responseResultado.put(SNConstantes.CODIGO,dtoGenerico.getCodigoResultado());
                responseResultado.put(SNConstantes.MENSAJE,SNConstantes.MENSAJE_ERR0R_BD);
                responseResultado.put("listaEncuestas",dtoGenerico.getListado());
            }else {
                responseResultado.put(SNConstantes.CODIGO, dtoGenerico.getCodigoResultado());
                responseResultado.put(SNConstantes.MENSAJE, dtoGenerico.getDescripcionResultado());
                responseResultado.put("listaEncuestas",dtoGenerico.getListado());
            }
        }catch (Exception ex){
            responseResultado.put(SNConstantes.CODIGO,SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE,SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado: {}",jsonResultado);

        logger.info("Fin EncuestaController - listarEncuestas");
        return new ResponseEntity<>(responseResultado,HttpStatus.ACCEPTED);
    }
    
    
    @GetMapping(value = "/obtenerEncuesta/{idEncuesta}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para obtener la encuesta", notes = "Retorna una encuesta.")
    public ResponseEntity<?> obtenerEncuesta(@PathVariable Long idEncuesta){
        logger.info("Inicio EncuestaController - obtenerEncuesta");

        Map<String,Object> responseResultado = new HashMap<>();
        DTOEncuesta dtoEncuesta;
        try {
            dtoEncuesta = iEncuestaService.obtenerEncuesta(idEncuesta);
            if ( dtoEncuesta.getIdEncuesta() == null){
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
                responseResultado.put("Encuesta", "No existe ninguna encuesta");
            }else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_EXITO);
                responseResultado.put("Encuesta", dtoEncuesta);
            }
        }catch (Exception ex){
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado,HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado {}", jsonResultado);

        logger.info("Fin EncuestaController - obtenerEncuesta");
        return new ResponseEntity<>(responseResultado,HttpStatus.ACCEPTED);
    }

    /**
     * Método que actualiza una encuesta
     *
     * @return actualizarEncuesta
     */
    @PutMapping(value = "/actualizarEncuesta", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para actualizar una encuesta", notes = "Actualiza una encuesta.")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> actualizarncuesta(@RequestBody EncuestaBean encuestaBean){
        logger.info("Inicio EncuestaController - actualizarEncuesta");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonPeticion = gson.toJson(encuestaBean);
        logger.info("Peticion : {}", jsonPeticion);

        Map<String,Object> responseResultado = new HashMap<>();
        DTOEncuesta dtoEncuesta;

        try {
            dtoEncuesta = iEncuestaService.actualizarEncuesta(encuestaBean);
            if (dtoEncuesta.getCodigoResultado().compareTo(SNConstantes.CODIGO_EXITO) != 0){
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R_BD);
                responseResultado.put(SNConstantes.ID_ENCUESTA, 0L);
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ENCUESTA
                        + SNConstantes.MENSAJE_EXITO_ACTUALIZAR);
                responseResultado.put(SNConstantes.ID_ENCUESTA, encuestaBean.getIdEncuesta());
            }
        }catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            responseResultado.put(SNConstantes.ID_ENCUESTA, 0L);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado : {}",jsonResultado);

        logger.info("Fin EncuestaController - actualizarEncuesta");
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }

    /**
     * Método que anula una encuesta
     *
     * @return anularEncuesta
     */
    @PostMapping(value = "/anularEncuesta", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para anular una encuesta", notes = "Anula una encuesta.")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> anularEncuesta(@RequestBody EncuestaBean encuestaBean){
        logger.info("Inicio EncuestaController - anularEncuesta");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonPeticion = gson.toJson(encuestaBean);
        logger.info("Peticion : {}", jsonPeticion);

        Map<String,Object> responseResultado = new HashMap<>();
        DTOEncuesta dtoEncuesta;

        try {
            dtoEncuesta = iEncuestaService.obtenerEncuesta(encuestaBean.getIdEncuesta());
            Byte indicadorAnularEncuesta = iEncuestaService.anularEncuesta(encuestaBean);

            if (indicadorAnularEncuesta != 1){
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_EXITO);
                responseResultado.put(SNConstantes.ID_ENCUESTA, 0L);
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ENCUESTA + SNConstantes.MENSAJE_EXITO_ANULAR);
                responseResultado.put(SNConstantes.ID_ENCUESTA, dtoEncuesta.getIdEncuesta());
            }
        }catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String jsonResultado = gson.toJson(dtoEncuesta);
        logger.info("Resultado : {}", jsonResultado);

        logger.info("Fin EncuestaController - anularEncuesta");
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }
    
    
}

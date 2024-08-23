/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.fonafe.sistemagestionriesgoapi.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOMatrizFraude;
import pe.gob.fonafe.sistemagestionriesgoapi.models.MatrizFraudeBean;
import pe.gob.fonafe.sistemagestionriesgoapi.service.IMatrizFraudeService;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOMatrizRiesgo;
import pe.gob.fonafe.sistemagestionriesgoapi.models.MatrizRiesgoBean;
import pe.gob.fonafe.sistemagestionriesgoapi.service.IMatrizFraudeService;

@RestController
@RequestMapping("/gestionriesgo/matrizFraude")
public class MatrizFraudeController {
    
    
    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    final IMatrizFraudeService iMatrizFraudeService;

    public MatrizFraudeController(IMatrizFraudeService iMatrizFraudeService) {
        this.iMatrizFraudeService = iMatrizFraudeService;
    }
    
    @GetMapping(value = "/listarBandejaMatrizFraude/{idEmpresa}/{idPeriodo}/{idMatrizNivel}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para listar la bandeja de matriz fraude", notes = "Registra una lista de matrices.")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> listarBandejaMatrizFraude(@PathVariable int idEmpresa, @PathVariable int idPeriodo, @PathVariable int idMatrizNivel) {
        logger.info("Inicio de MatrizraudeController - listarBandejaMatrizFraude");

        logger.info("Peticion : {} - {} - {}", idEmpresa, idPeriodo, idMatrizNivel);

        Map<String, Object> responseResultado = new HashMap<>();
        List<DTOMatrizFraude> listaBandejaMatrizFraude;
        try {
            listaBandejaMatrizFraude = iMatrizFraudeService.listarBandejaMatrizFraude(idEmpresa, idPeriodo, idMatrizNivel);

            if (listaBandejaMatrizFraude.isEmpty()) {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R_BD);
                responseResultado.put("listaBandejaMatrizFraude", 0L);
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_EXITO);
                responseResultado.put("listaBandejaMatrizFraude", listaBandejaMatrizFraude);
            }

        } catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado : {}", jsonResultado);

        logger.info("Fin de MatrizFraudeController - listarBandejaMatrizFraude");
        return new ResponseEntity<>(responseResultado, HttpStatus.CREATED);
    }

    /**
     * Método que registra una matriz riesgo y detalle
     *
     * @return registrarMatrizRiesgo
     */
    @PostMapping(value = "/registrarMatrizFraude", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para registrar una matriz fraude", notes = "Registra una matriz fraude.")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> registrarMatrizFraude(@RequestBody MatrizFraudeBean matrizFraudeBean) {
        logger.info("Inicio de MatrizFraudeController - registrarMatrizRiesgo");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonPeticion = gson.toJson(matrizFraudeBean);
        logger.info("Peticion : {}", jsonPeticion);

        Map<String, Object> responseResultado = new HashMap<>();
        DTOMatrizFraude dtoMatrizRiesgo;
        try {
            dtoMatrizRiesgo = iMatrizFraudeService.registrarMatrizFraude(matrizFraudeBean);

            if (dtoMatrizRiesgo.getCodigoResultado().compareTo(SNConstantes.CODIGO_EXITO) != 0) {
                responseResultado.put(SNConstantes.CODIGO, dtoMatrizRiesgo.getCodigoResultado());
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R_BD);
                responseResultado.put(SNConstantes.ID_MATRIZ_RIESGO, 0L);
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_MATRIZ_RIESGO
                        + SNConstantes.MENSAJE_EXITO_REGISTRAR);
                responseResultado.put(SNConstantes.ID_MATRIZ_RIESGO, dtoMatrizRiesgo.getIdMatrizRiesgo());
            }

        } catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            responseResultado.put(SNConstantes.ID_MATRIZ_RIESGO, 0L);

            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado : {}", jsonResultado);

        logger.info("Fin de MatrizFraudeController - registrarMatrizFraude");
        return new ResponseEntity<>(responseResultado, HttpStatus.CREATED);
    }
    
 

    /**
     * Método que retorna una matriz riesgo
     *
     * @return obtenerMatrizRiesgo
     */
    @GetMapping(value = "/obtenerMatrizFraude/{idMatrizFraude}/{idUsuario}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para obtener la matriz de fraude", notes = "Retorna una matriz de fraude.")
    public ResponseEntity<?> obtenerMatrizFraude(@PathVariable Integer idMatrizFraude, @PathVariable Long idUsuario) {
        logger.info("Inicio MatrizFraudeController - obtenerMatrizFraude");

        Map<String, Object> responseResultado = new HashMap<>();
        DTOMatrizFraude dtoMatrizFraude;
        try {
            dtoMatrizFraude = iMatrizFraudeService.obtenerMatrizFraude(idMatrizFraude, idUsuario);
            if (dtoMatrizFraude.getIdMatrizRiesgo() == null) {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
                responseResultado.put("MatrizFraude", "No existe ninguna matriz fraude");
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_EXITO);
                responseResultado.put("MatrizFraude", dtoMatrizFraude);
            }
        } catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado {}", jsonResultado);

        logger.info("Fin MatrizFraudeController - obtenerMatrizFraude");
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }

    /**
     * Método que actualiza una matriz riesgo
     *
     * @return actualizarMatrizRiesgo
     */
    @PutMapping(value = "/actualizarMatrizFraude", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para actualizar una matriz de fraude", notes = "Actualiza una matriz de fraude.")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> actualizarMatrizRiesgo(@RequestBody MatrizFraudeBean matrizFraudeBean) {
        logger.info("Inicio MatrizFraudeController - actualizarMatrizFraude");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonPeticion = gson.toJson(matrizFraudeBean);
        logger.info("Peticion : {}", jsonPeticion);

        Map<String, Object> responseResultado = new HashMap<>();
        DTOMatrizFraude dtoMatrizFraude;

        try {
            dtoMatrizFraude = iMatrizFraudeService.actualizarMatrizFraude(matrizFraudeBean);
            if (dtoMatrizFraude.getCodigoResultado().compareTo(SNConstantes.CODIGO_EXITO) != 0) {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R_BD);
                responseResultado.put(SNConstantes.ID_MATRIZ_FRAUDE, 0);
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_MATRIZ_RIESGO
                        + SNConstantes.MENSAJE_EXITO_ACTUALIZAR);
                responseResultado.put(SNConstantes.ID_MATRIZ_FRAUDE, dtoMatrizFraude.getIdMatrizRiesgo());
            }
        } catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            responseResultado.put(SNConstantes.ID_MATRIZ_FRAUDE, 0);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado : {}", jsonResultado);

        logger.info("Fin MatrizFraudeController - actualizarMatrizFraude");
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }

    /**
     * Método que anula una matriz de riesgo
     *
     * @return anularMatrizRiesgo
     */
    @PostMapping(value = "/anularMatrizFraude", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para anular una matriz de fraude", notes = "Anula una matriz de fraude.")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> anularMatrizFraude(@RequestBody MatrizFraudeBean matrizFraudeBean) {
        logger.info("Inicio MatrizFraudeController - anularMatrizFraude");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonPeticion = gson.toJson(matrizFraudeBean);
        logger.info("Peticion : {}", jsonPeticion);

        Map<String, Object> responseResultado = new HashMap<>();
        DTOMatrizFraude dtoMatrizFraude;

        try {
//            dtoMatrizFraude = iMatrizFraudeService.obtenerMatrizFraude(matrizFraudeBean.getIdMatrizRiesgo());
            Byte indicadorAnularMatriz = iMatrizFraudeService.anularMatrizFraude(matrizFraudeBean);

            if (indicadorAnularMatriz != 1) {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_EXITO);
                responseResultado.put(SNConstantes.ID_MATRIZ_FRAUDE, 0);
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_MATRIZ_RIESGO +
                        SNConstantes.MENSAJE_EXITO_ANULAR);
                responseResultado.put(SNConstantes.ID_MATRIZ_FRAUDE, matrizFraudeBean.getIdMatrizRiesgo());
            }
        } catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String jsonResultado = gson.toJson(matrizFraudeBean);
        logger.info("Resultado : {}", jsonResultado);

        logger.info("Fin MatrizFraudeController - anularMatrizFraude");
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }

    
}

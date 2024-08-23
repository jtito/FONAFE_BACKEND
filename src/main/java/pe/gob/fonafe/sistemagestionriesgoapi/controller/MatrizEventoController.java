/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.fonafe.sistemagestionriesgoapi.controller;

/**
 *
 * @author CANVIA
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
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOMatrizRiesgo;
import pe.gob.fonafe.sistemagestionriesgoapi.models.MatrizRiesgoBean;
import pe.gob.fonafe.sistemagestionriesgoapi.service.IMatrizEventoService;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTODetalleMatrizEvento;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTODetalleMatrizRiesgo;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOMatrizEvento;
import pe.gob.fonafe.sistemagestionriesgoapi.models.MatrizEventoBean;

@RestController
@RequestMapping("/gestionriesgo/matrizEvento")
public class MatrizEventoController {
    
    private static final Logger logger = LogManager.getLogger("GESTION_EVENTO_API");

    final IMatrizEventoService iMatrizEventoService;

    public MatrizEventoController(IMatrizEventoService iMatrizEventoService) {
        this.iMatrizEventoService = iMatrizEventoService;
    }
    
    
    @GetMapping(value = "/listarBandejaMatrizEvento/{idEmpresa}/{idPeriodo}/{idMatrizNivel}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para listar la bandeja de matriz evento", notes = "Registra una lista de matrices.")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> listarBandejaMatrizRiesgo(@PathVariable int idEmpresa, @PathVariable int idPeriodo, @PathVariable int idMatrizNivel) {
        logger.info("Inicio de MatrizEventoController - listarBandejaMatrizEvento");

        logger.info("Peticion : {} - {} - {}", idEmpresa, idPeriodo, idMatrizNivel);

        Map<String, Object> responseResultado = new HashMap<>();
        List<DTOMatrizEvento> listaBandejaMatrizEvento;
        try {
            listaBandejaMatrizEvento= iMatrizEventoService.listarBandejaMatrizEvento(idEmpresa, idPeriodo, idMatrizNivel);

            if (listaBandejaMatrizEvento.isEmpty()) {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R_BD);
                responseResultado.put("listaBandejaMatrizEvento", 0L);
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_EXITO);
                responseResultado.put("listaBandejaMatrizEvento", listaBandejaMatrizEvento);
            }

        } catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado : {}", jsonResultado);

        logger.info("Fin de MatrizRiesgoController - listarBandejaMatrizRiesgo");
        return new ResponseEntity<>(responseResultado, HttpStatus.CREATED);
    }
    
    @PostMapping(value = "/registrarMatrizEvento", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para registrar una matriz riesgo", notes = "Registra una matriz riesgo.")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> registrarMatrizRiesgo(@RequestBody MatrizEventoBean matrizEventoBean) {
        logger.info("Inicio de MatrizEventoController - registrarMatrizEvento");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonPeticion = gson.toJson(matrizEventoBean);
        logger.info("Peticion : {}", jsonPeticion);

        Map<String, Object> responseResultado = new HashMap<>();
        DTOMatrizEvento dtoMatrizRiesgo;
        try {
            dtoMatrizRiesgo = iMatrizEventoService.registrarMatrizRiesgo(matrizEventoBean);

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

        logger.info("Fin de MatrizRiesgoController - registrarMatrizRiesgo");
        return new ResponseEntity<>(responseResultado, HttpStatus.CREATED);
    }
    
 

    /**
     * Método que retorna una matriz riesgo
     *
     * @return obtenerMatrizRiesgo
     */
    @GetMapping(value = "/obtenerMatrizEvento/{idMatrizEvento}/{idUsuario}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para obtener la matriz de Evento", notes = "Retorna una matriz de evento.")
    public ResponseEntity<?> obtenerMatrizRiesgo(@PathVariable Integer idMatrizEvento, @PathVariable Long idUsuario) {
        logger.info("Inicio MatrizEventoController - obtenerMatrizEvento");

        Map<String, Object> responseResultado = new HashMap<>();
        DTOMatrizEvento dtoMatrizEvento;
        try {
            dtoMatrizEvento = iMatrizEventoService.obtenerMatrizRiesgo(idMatrizEvento, idUsuario);
            if (dtoMatrizEvento.getIdMatrizRiesgo() == null) {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
                responseResultado.put("MatrizRiesgo", "No existe ninguna matriz riesgo");
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_EXITO);
                responseResultado.put("MatrizRiesgo", dtoMatrizEvento);
            }
        } catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado {}", jsonResultado);

        logger.info("Fin MatrizEventoController - obtenerMatrizEvento");
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }
    
    
    @PutMapping(value = "/actualizarMatrizEvento", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para actualizar una matriz de evento", notes = "Actualiza una matriz de evento.")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> actualizarMatrizRiesgo(@RequestBody MatrizEventoBean matrizEventoBean) {
        logger.info("Inicio MatrizEventoController - actualizarMatrizEvento");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonPeticion = gson.toJson(matrizEventoBean);
        logger.info("Peticion : {}", jsonPeticion);

        Map<String, Object> responseResultado = new HashMap<>();
        DTOMatrizEvento dtoMatrizEvento;

        try {
            dtoMatrizEvento = iMatrizEventoService.actualizarMatrizRiesgo(matrizEventoBean);
            if (dtoMatrizEvento.getCodigoResultado().compareTo(SNConstantes.CODIGO_EXITO) != 0) {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R_BD);
                responseResultado.put(SNConstantes.ID_MATRIZ_RIESGO, 0);
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_MATRIZ_RIESGO
                        + SNConstantes.MENSAJE_EXITO_ACTUALIZAR);
                responseResultado.put(SNConstantes.ID_MATRIZ_RIESGO, dtoMatrizEvento.getIdMatrizRiesgo());
            }
        } catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            responseResultado.put(SNConstantes.ID_MATRIZ_RIESGO, 0);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado : {}", jsonResultado);

        logger.info("Fin MatrizEventooController - actualizarMatrizEvento");
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }
    
    
    @PostMapping(value = "/anularMatrizEvento", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para anular una matriz de evento", notes = "Anula una matriz de evento.")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> anularMatrizRiesgo(@RequestBody MatrizEventoBean matrizEventoBean) {
        logger.info("Inicio MatrizEventoController - anularMatrizEvento");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonPeticion = gson.toJson(matrizEventoBean);
        logger.info("Peticion : {}", jsonPeticion);

        Map<String, Object> responseResultado = new HashMap<>();
        DTOMatrizEvento dtoMatrizEvento;

        try {
//            dtoMatrizEvento = iMatrizEventoService.obtenerMatrizRiesgo(matrizEventoBean.getIdMatrizRiesgo());
            Byte indicadorAnularMatriz = iMatrizEventoService.anularMatrizEvento(matrizEventoBean);

            if (indicadorAnularMatriz != 1) {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_EXITO);
                responseResultado.put(SNConstantes.ID_MATRIZ_RIESGO, 0);
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_MATRIZ_RIESGO +
                        SNConstantes.MENSAJE_EXITO_ANULAR);
                responseResultado.put(SNConstantes.ID_MATRIZ_RIESGO, matrizEventoBean.getIdMatrizRiesgo());
            }
        } catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String jsonResultado = gson.toJson(matrizEventoBean);
        logger.info("Resultado : {}", jsonResultado);

        logger.info("Fin MatrizRiesgoController - anularMatrizRiesgo");
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }
    
    
   


    
}

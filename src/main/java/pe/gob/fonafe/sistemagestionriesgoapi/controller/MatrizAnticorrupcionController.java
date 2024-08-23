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
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOMatrizAnticorrupcion;
import pe.gob.fonafe.sistemagestionriesgoapi.models.MatrizAnticorrupcionBean;
import pe.gob.fonafe.sistemagestionriesgoapi.service.IMatrizAnticorrupcionService;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTODetalleMatrizAnticorrupcion;

@RestController
@RequestMapping("/gestionriesgo/matrizAnticorrupcion")
public class MatrizAnticorrupcionController {
    
    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    final IMatrizAnticorrupcionService iMatrizAnticorrupcionService;

    public MatrizAnticorrupcionController(IMatrizAnticorrupcionService iMatrizRiesgoService) {
        this.iMatrizAnticorrupcionService = iMatrizRiesgoService;
    }

    @GetMapping(value = "/listarBandejaMatrizAnticorrupcion/{idEmpresa}/{idPeriodo}/{idMatrizNivel}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para listar la bandeja de matriz riesgo", notes = "Registra una lista de matrices.")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> listarBandejaMatrizAnticorrupcion(@PathVariable int idEmpresa, @PathVariable int idPeriodo, @PathVariable int idMatrizNivel) {
        logger.info("Inicio de MatrizAnticorrupcionController - listarBandejaMatrizAnticorrupcion");

        logger.info("Peticion : {} - {} - {}", idEmpresa, idPeriodo, idMatrizNivel);

        Map<String, Object> responseResultado = new HashMap<>();
        List<DTOMatrizAnticorrupcion> listaBandejaMatrizRiesgo;
        try {
            listaBandejaMatrizRiesgo = iMatrizAnticorrupcionService.listarBandejaMatrizAntic(idEmpresa, idPeriodo, idMatrizNivel);

            if (listaBandejaMatrizRiesgo.isEmpty()) {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R_BD);
                responseResultado.put("listaBandejaMatrizRiesgo", 0L);
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_EXITO);
                responseResultado.put("listaBandejaMatrizRiesgo", listaBandejaMatrizRiesgo);
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

    /**
     * Método que registra una matriz riesgo y detalle
     *
     * @return registrarMatrizRiesgo
     */
    @PostMapping(value = "/registrarMatrizAnticorrupcion", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para registrar una matriz riesgo", notes = "Registra una matriz riesgo.")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> registrarMatrizAnticorrupcion(@RequestBody MatrizAnticorrupcionBean matrizRiesgoBean) {
        logger.info("Inicio de MatrizAnticorrupcionController - registrarMatrizRiesgo");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonPeticion = gson.toJson(matrizRiesgoBean);
        logger.info("Peticion : {}", jsonPeticion);

        Map<String, Object> responseResultado = new HashMap<>();
        DTOMatrizAnticorrupcion dtoMatrizRiesgo;
        try {
            dtoMatrizRiesgo = iMatrizAnticorrupcionService.registrarMatrizAnticorrupcion(matrizRiesgoBean);

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

        logger.info("Fin de MatrizAnticorrupcionController - registrarMatrizAnticorrupcion");
        return new ResponseEntity<>(responseResultado, HttpStatus.CREATED);
    }
    
    
    @GetMapping(value = "/obtenerMatrizAnticorrupcion/{idMatrizRiesgo}/{idUsuario}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para obtener la matriz de Anticorrupcion", notes = "Retorna una matriz de Anticorrupcion.")
    public ResponseEntity<?> obtenerMatrizAnticorrupcion(@PathVariable Integer idMatrizRiesgo, @PathVariable Long idUsuario) {
        logger.info("Inicio MatrizAnticorrupcionController - obtenerMatrizRiesgo");

        Map<String, Object> responseResultado = new HashMap<>();
        DTOMatrizAnticorrupcion dtoMatrizRiesgo;
        try {
            dtoMatrizRiesgo = iMatrizAnticorrupcionService.obtenerMatrizAnticorrupcion(idMatrizRiesgo, idUsuario);
            if (dtoMatrizRiesgo.getIdMatrizRiesgo() == null) {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
                responseResultado.put("MatrizRiesgo", "No existe ninguna matriz riesgo");
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_EXITO);
                responseResultado.put("MatrizRiesgo", dtoMatrizRiesgo);
            }
        } catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado {}", jsonResultado);

        logger.info("Fin MatrizAnticorrupcionController - obtenerMatrizAnticorrupcion");
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }
    
    
    @PutMapping(value = "/actualizarMatrizAnticorrupcion", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para actualizar una matriz de Anticorrupcion", notes = "Actualiza una matriz de Anticorrupcion.")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> actualizarMatrizRiesgo(@RequestBody MatrizAnticorrupcionBean matrizRiesgoBean) {
        logger.info("Inicio MatrizAnticorrupcionController - actualizarMatrizAnticorrupcion");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonPeticion = gson.toJson(matrizRiesgoBean);
        logger.info("Peticion : {}", jsonPeticion);

        Map<String, Object> responseResultado = new HashMap<>();
        DTOMatrizAnticorrupcion dtoMatrizRiesgo;

        try {
            dtoMatrizRiesgo = iMatrizAnticorrupcionService.actualizarMatrizAntic(matrizRiesgoBean);
            if (dtoMatrizRiesgo.getCodigoResultado().compareTo(SNConstantes.CODIGO_EXITO) != 0) {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R_BD);
                responseResultado.put(SNConstantes.ID_MATRIZ_RIESGO, 0);
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_MATRIZ_RIESGO
                        + SNConstantes.MENSAJE_EXITO_ACTUALIZAR);
                responseResultado.put(SNConstantes.ID_MATRIZ_RIESGO, dtoMatrizRiesgo.getIdMatrizRiesgo());
            }
        } catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            responseResultado.put(SNConstantes.ID_MATRIZ_RIESGO, 0);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado : {}", jsonResultado);

        logger.info("Fin MatrizAnticorrupcionController - actualizarMatrizAnticorrupcion");
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }
    
    
    @PostMapping(value = "/anularMatrizAnticorrupcion", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para anular una matriz de Anticorrupcion", notes = "Anula una matriz de Anticorrupcion.")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> anularMatrizAnticorrupcion(@RequestBody MatrizAnticorrupcionBean matrizRiesgoBean) {
        logger.info("Inicio MatrizAnticorrupcionController - anularMatrizAnticorrupcion");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonPeticion = gson.toJson(matrizRiesgoBean);
        logger.info("Peticion : {}", jsonPeticion);

        Map<String, Object> responseResultado = new HashMap<>();
        DTOMatrizAnticorrupcion dtoMatrizRiesgo;

        try {
//            dtoMatrizRiesgo = iMatrizAnticorrupcionService.obtenerMatrizAnticorrupcion(matrizRiesgoBean.getIdMatrizRiesgo());
            Byte indicadorAnularMatriz = iMatrizAnticorrupcionService.anularMatrizAnticorrupcion(matrizRiesgoBean);

            if (indicadorAnularMatriz != 1) {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_EXITO);
                responseResultado.put(SNConstantes.ID_MATRIZ_RIESGO, 0);
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_MATRIZ_RIESGO +
                        SNConstantes.MENSAJE_EXITO_ANULAR);
                responseResultado.put(SNConstantes.ID_MATRIZ_RIESGO, matrizRiesgoBean.getIdMatrizRiesgo());
            }
        } catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String jsonResultado = gson.toJson(matrizRiesgoBean);
        logger.info("Resultado : {}", jsonResultado);

        logger.info("Fin MatrizAnticorrupcionController - anularMatrizAnticorrupcion");
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }
    
}

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
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOMatrizNivel;
import pe.gob.fonafe.sistemagestionriesgoapi.models.MatrizNivelBean;
import pe.gob.fonafe.sistemagestionriesgoapi.service.IMatrizNivelService;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/gestionriesgo/nivelmatriz")
public class NivelMatrizController {

    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    final IMatrizNivelService iMatrizNivelService;

    public NivelMatrizController(IMatrizNivelService iMatrizNivelService) {
        this.iMatrizNivelService = iMatrizNivelService;
    }


    @PostMapping(value = "/registrarMatriz", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para registrar una Matriz", notes = "Registra una Matriz.")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> registrarMatriz(@RequestBody MatrizNivelBean matrizNivelBean) {
        logger.info("Inicio de MatrizNivelController - registrarMatriz");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonPeticion = gson.toJson(matrizNivelBean);
        logger.info("Peticion : {}", jsonPeticion);

        Map<String, Object> responseResultado = new HashMap<>();
        DTOMatrizNivel dtoMatrizNivel;

        try {
            dtoMatrizNivel = iMatrizNivelService.registrarMatriz(matrizNivelBean);

            if (!dtoMatrizNivel.getDescripcionMensaje().trim().equals(SNConstantes.DE_RESULT_OK)) {
                responseResultado.put(SNConstantes.CODIGO, dtoMatrizNivel.getCodigoMensaje());
                responseResultado.put(SNConstantes.MENSAJE, dtoMatrizNivel.getDescripcionMensaje());
                responseResultado.put(SNConstantes.ID_MATRIZNIVEL, 0L);
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_MATRIZ + matrizNivelBean.getDeMatrizNivel()
                        + SNConstantes.MENSAJE_EXITO_REGISTRAR);
                responseResultado.put(SNConstantes.ID_MATRIZNIVEL, dtoMatrizNivel.getIdMatrizNivel());
            }

        } catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            responseResultado.put(SNConstantes.ID_MATRIZNIVEL, 0L);

            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado : {}", jsonResultado);

        logger.info("Fin de MatrizNivelController - registrarMatriz");
        return new ResponseEntity<>(responseResultado, HttpStatus.CREATED);
    }


    @GetMapping(value = "/listarMatriz/{idEmpresa}/{idSede}/{idTipoMatriz}")
    @ApiOperation(value = "Endpoint para listar Matriz", notes = "Retorna la lista de Matrices.")
    public ResponseEntity<?> listarMatriz(@PathVariable Long idEmpresa, @PathVariable Long idSede,
                                          @PathVariable Long idTipoMatriz){
        logger.info("Inicio MatrizController - listarMatriz");


        Map<String, Object> responseResultado = new HashMap<>();

        List<DTOMatrizNivel> listamatriz;
        try {
            listamatriz = iMatrizNivelService.listarMatriz(idEmpresa, idSede, idTipoMatriz);
            if (listamatriz.isEmpty()) {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
                responseResultado.put("listaMatriz", listamatriz);
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_EXITO);
                responseResultado.put("listaMatriz", listamatriz);
            }
        } catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado: {}", jsonResultado);

        logger.info("Fin MatrizNivelController - listarMatriz");
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }


    @GetMapping(value = "/obtenerMatriz/{idMatriz}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para obtener la Matriz", notes = "Retorna una Matriz.")
    public ResponseEntity<?> obtenerMatriz(@PathVariable Long idMatriz) {
        logger.info("Inicio NivelMatrizController - obtenerMatriz");

        Map<String, Object> responseResultado = new HashMap<>();
        DTOMatrizNivel dtoMatrizNivel;
        try {
            dtoMatrizNivel = iMatrizNivelService.obtenerMatriz(idMatriz);
            if (dtoMatrizNivel.getIdMatrizNivel() == null) {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
                responseResultado.put("MatrizNivel", "No existe ninguna Matriz");
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_EXITO);
                responseResultado.put("MatrizNivel", dtoMatrizNivel);
            }
        } catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado {}", jsonResultado);

        logger.info("Fin MatrizNivelController - obtenerMatrizNivel");
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }

    /**
     * Método que actualiza un responsable
     *
     * @return actualizarResponsable
     */
    @PutMapping(value = "/actualizarMatrizNivel", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para actualizar una Matriz", notes = "Actualiza una Matriz.")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> actualizarMatriz(@RequestBody MatrizNivelBean matrizBean) {
        logger.info("Inicio MatrizNivelController - actualizarMatrizNivel");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonPeticion = gson.toJson(matrizBean);
        logger.info("Peticion : {}", jsonPeticion);

        Map<String, Object> responseResultado = new HashMap<>();
        DTOMatrizNivel dtoMatrizNivel;

        try {
            dtoMatrizNivel = iMatrizNivelService.actualizarMatriz(matrizBean);

            if (!dtoMatrizNivel.getDescripcionMensaje().trim().equals(SNConstantes.DE_RESULT_OK)) {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R_BD);
                responseResultado.put(SNConstantes.ID_MATRIZNIVEL, 0L);
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_MATRIZ + matrizBean.getDeMatrizNivel()
                        + SNConstantes.MENSAJE_EXITO_ACTUALIZAR);
                responseResultado.put(SNConstantes.ID_MATRIZNIVEL, matrizBean.getIdMatrizNivel());
            }
        } catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            responseResultado.put(SNConstantes.ID_MATRIZNIVEL, 0L);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado : {}", jsonResultado);

        logger.info("Fin MatrizNivelController - actualizarMatriz");
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }

    /**
     * Método que anula un responsable
     *
     * @return anularResponsable
     */
    @PostMapping(value = "/anularMatriz", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para anular un responsable", notes = "Anula un responsable.")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> anularMatriz(@RequestBody MatrizNivelBean matrizBean) {
        logger.info("Inicio MatrizNivelController - anularMatriz");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonPeticion = gson.toJson(matrizBean);
        logger.info("Peticion : {}", jsonPeticion);

        Map<String, Object> responseResultado = new HashMap<>();
        DTOMatrizNivel dtoMatrizNivel;

        try {
            dtoMatrizNivel = iMatrizNivelService.obtenerMatriz(matrizBean.getIdMatrizNivel());
            Byte indicadorAnularMatriz = iMatrizNivelService.anularMatriz(matrizBean);

            if (indicadorAnularMatriz != 1) {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_EXITO);
                responseResultado.put(SNConstantes.ID_RESPONSABLE, 0L);
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_MATRIZ + SNConstantes.MENSAJE_EXITO_ANULAR);
                responseResultado.put(SNConstantes.ID_MATRIZNIVEL, dtoMatrizNivel.getIdMatrizNivel());
            }
        } catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String jsonResultado = gson.toJson(dtoMatrizNivel);
        logger.info("Resultado : {}", jsonResultado);

        logger.info("Fin MatrizNivelController - anularMatriz");
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }

}

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
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOMatrizContinuidad;
import pe.gob.fonafe.sistemagestionriesgoapi.models.MatrizRiesgoContinuidadBean;
import pe.gob.fonafe.sistemagestionriesgoapi.service.IMatrizContinuidadService;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/gestionriesgo/matrizContinuidad")
public class MatrizContinuidadController {

    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    final IMatrizContinuidadService iMatrizContinuidadService;

    public MatrizContinuidadController(IMatrizContinuidadService iMatrizContinuidadService) {
        this.iMatrizContinuidadService = iMatrizContinuidadService;
    }

    @GetMapping(value = "/listarBandejaMatrizContinuidad/{idEmpresa}/{idPeriodo}/{idMatrizNivel}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para listar la bandeja de matriz riesgo de continuidad", notes = "Registra una lista de matrices de continuidad.")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> listarBandejaMatrizContinuidad(@PathVariable int idEmpresa, @PathVariable int idPeriodo, @PathVariable int idMatrizNivel) {
        logger.info("Inicio de MatrizContinuidadController - listarBandejaMatrizContinuidad");

        logger.info("Peticion : {} - {} - {}", idEmpresa, idPeriodo, idMatrizNivel);

        Map<String, Object> responseResultado = new HashMap<>();
        List<DTOMatrizContinuidad> listaBandejaMatrizContinuidad;
        try {
            listaBandejaMatrizContinuidad = iMatrizContinuidadService.listarBandejaMatrizContinuidad(idEmpresa, idPeriodo, idMatrizNivel);

            if (listaBandejaMatrizContinuidad.isEmpty()) {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R_BD);
                responseResultado.put("listaBandejaMatrizContinuidad", 0L);
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_EXITO);
                responseResultado.put("listaBandejaMatrizContinuidad", listaBandejaMatrizContinuidad);
            }

        } catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado : {}", jsonResultado);

        logger.info("Fin de MatrizContinuidadController - listarBandejaMatrizContinuidad");
        return new ResponseEntity<>(responseResultado, HttpStatus.CREATED);
    }

    /**
     * Método que registra una matriz riesgo y detalle
     *
     * @return registrarMatrizRiesgo
     */
    @PostMapping(value = "/registrarMatrizContinuidad", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para registrar una matriz continuidad", notes = "Registra una matriz continuidad.")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> registrarMatrizRiesgo(@RequestBody MatrizRiesgoContinuidadBean matrizRiesgoContinuidadBean) {
        logger.info("Inicio de MatrizContinuidadController - registrarMatrizContinuidad");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonPeticion = gson.toJson(matrizRiesgoContinuidadBean);
        logger.info("Peticion : {}", jsonPeticion);

        Map<String, Object> responseResultado = new HashMap<>();
        DTOMatrizContinuidad dtoMatrizContinuidad;
        try {
            dtoMatrizContinuidad = iMatrizContinuidadService.registrarMatrizContinuidad(matrizRiesgoContinuidadBean);

            if (dtoMatrizContinuidad.getCodigoResultado().compareTo(SNConstantes.CODIGO_EXITO) != 0) {
                responseResultado.put(SNConstantes.CODIGO, dtoMatrizContinuidad.getCodigoResultado());
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R_BD);
                responseResultado.put(SNConstantes.ID_MATRIZ_RIESGO, 0L);
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_MATRIZ_RIESGO
                        + SNConstantes.MENSAJE_EXITO_REGISTRAR);
                responseResultado.put(SNConstantes.ID_MATRIZ_RIESGO, dtoMatrizContinuidad.getIdMatrizRiesgo());
            }

        } catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            responseResultado.put(SNConstantes.ID_MATRIZ_RIESGO, 0L);

            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado : {}", jsonResultado);

        logger.info("Fin de MatrizContinuidadController - registrarMatrizContinuidad");
        return new ResponseEntity<>(responseResultado, HttpStatus.CREATED);
    }

    /**
     * Método que retorna una matriz riesgo
     *
     * @return obtenerMatrizRiesgo
     */
    @GetMapping(value = "/obtenerMatrizContinuidad/{idMatrizContinuidad}/{idUsuario}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para obtener la matriz de riesgo", notes = "Retorna una matriz de riesgo.")
    public ResponseEntity<?> obtenerMatrizContinuidad(@PathVariable Integer idMatrizContinuidad, @PathVariable Long idUsuario) {
        logger.info("Inicio MatrizContinuidadController - obtenerMatrizContuidad");

        Map<String, Object> responseResultado = new HashMap<>();
        DTOMatrizContinuidad dtoMatrizRiesgo;
        try {
            dtoMatrizRiesgo = iMatrizContinuidadService.obtenerMatrizContinuidad(idMatrizContinuidad, idUsuario);
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

        logger.info("Fin MatrizContinuidadController - obtenerMatrizContinuidad");
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }

    /**
     * Método que actualiza una matriz continuidad
     *
     * @return actualizarMatrizContinuidad
     */
    @PutMapping(value = "/actualizarMatrizContinuidad", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para actualizar una matriz de continuidad", notes = "Actualiza una matriz de continuidad.")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> actualizarMatrizContinuidad(@RequestBody MatrizRiesgoContinuidadBean matrizRiesgoContinuidadBeanBean) {
        logger.info("Inicio MatrizContinuidadController - actualizarMatrizContinuidad");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonPeticion = gson.toJson(matrizRiesgoContinuidadBeanBean);
        logger.info("Peticion : {}", jsonPeticion);

        Map<String, Object> responseResultado = new HashMap<>();
        DTOMatrizContinuidad dtoMatrizContinuidad;

        try {
            dtoMatrizContinuidad = iMatrizContinuidadService.actualizarMatrizContinuidad(matrizRiesgoContinuidadBeanBean);
            if (dtoMatrizContinuidad.getCodigoResultado().compareTo(SNConstantes.CODIGO_EXITO) != 0) {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R_BD);
                responseResultado.put(SNConstantes.ID_MATRIZ_CONTINUIDAD, 0);
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_MATRIZ_RIESGO
                        + SNConstantes.MENSAJE_EXITO_ACTUALIZAR);
                responseResultado.put(SNConstantes.ID_MATRIZ_CONTINUIDAD, dtoMatrizContinuidad.getIdMatrizRiesgo());
            }
        } catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            responseResultado.put(SNConstantes.ID_MATRIZ_RIESGO, 0);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado : {}", jsonResultado);

        logger.info("Fin MatrizContinuidadController - actualizarMatrizContinuidad");
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }
}

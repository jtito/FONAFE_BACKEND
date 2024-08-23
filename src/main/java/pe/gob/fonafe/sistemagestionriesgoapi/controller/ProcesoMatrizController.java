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
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOProcesoMatriz;
import pe.gob.fonafe.sistemagestionriesgoapi.models.ProcesoMatrizBean;
import pe.gob.fonafe.sistemagestionriesgoapi.service.IProcesoMatrizService;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/gestionriesgo/procesoMatriz")
public class ProcesoMatrizController {

    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    final IProcesoMatrizService iProcesoMatrizService;

    public ProcesoMatrizController(IProcesoMatrizService iProcesoMatrizService) {
        this.iProcesoMatrizService = iProcesoMatrizService;
    }

    /**
     * Método que registra un proceso matriz
     *
     * @return registrarProcesoMatriz
     */
    @PostMapping(value = "/registrarProcesoMatriz", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para registrar un proceso matriz", notes = "Registra un proceso matriz.")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> registrarProcesoMatriz(@RequestBody ProcesoMatrizBean procesoMatrizBean){
        logger.info("Inicio de ProcesoMatrizController - registrarProcesoMatriz");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonPeticion = gson.toJson(procesoMatrizBean);
        logger.info("Peticion : {}", jsonPeticion);

        Map<String,Object> responseResultado = new HashMap<>();
        DTOProcesoMatriz dtoProcesoMatriz;
        try {
            dtoProcesoMatriz = iProcesoMatrizService.registrarProcesoMatriz(procesoMatrizBean);

            if (dtoProcesoMatriz.getCodigoResultado().compareTo(SNConstantes.CODIGO_EXITO) != 0){
                responseResultado.put(SNConstantes.CODIGO,dtoProcesoMatriz.getCodigoResultado());
                responseResultado.put(SNConstantes.MENSAJE,SNConstantes.MENSAJE_ERR0R_BD);
                responseResultado.put(SNConstantes.ID_PROC_MATRIZ,0L);
            }else {
                responseResultado.put(SNConstantes.CODIGO,SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE,SNConstantes.MENSAJE_PROCESO_MATRIZ
                        + SNConstantes.MENSAJE_EXITO_REGISTRAR);
                responseResultado.put(SNConstantes.ID_PROC_MATRIZ,dtoProcesoMatriz.getIdProcesoMatriz());
            }

        }catch (Exception ex){
            responseResultado.put(SNConstantes.CODIGO,SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE,SNConstantes.MENSAJE_ERR0R);
            responseResultado.put(SNConstantes.ID_EMPRESA,0L);

            return new ResponseEntity<>(responseResultado,HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado : {}", jsonResultado);

        logger.info("Fin de ProcesoMatrizController - registrarProcesoMatriz");
        return new ResponseEntity<>(responseResultado,HttpStatus.CREATED);
    }

    /**
     * Método que retorna la lista total de procesos por matriz
     *
     * @return listarProcesoMatriz
     */
    @GetMapping(value = "/listarProcesoMatriz/{idEmpresa}")
    @ApiOperation(value = "Endpoint para listar procesos matriz", notes = "Retorna la lista de procesos matriz.")
    public ResponseEntity<?> listarProcesoMatriz(@PathVariable Long idEmpresa){
        logger.info("Inicio ProcesoMatrizController - listarProcesoMatriz");

        Map<String,Object> responseResultado = new HashMap<>();

        DTOGenerico dtoGenerico = new DTOGenerico();
        try {
            dtoGenerico = iProcesoMatrizService.listarProcesoMatriz(idEmpresa);

            if (dtoGenerico.getCodigoResultado().compareTo(SNConstantes.CODIGO_EXITO) != 0){
                responseResultado.put(SNConstantes.CODIGO,dtoGenerico.getCodigoResultado());
                responseResultado.put(SNConstantes.MENSAJE,SNConstantes.MENSAJE_ERR0R_BD);
                responseResultado.put("listaProcesoMatriz",dtoGenerico.getListado());
            }else {
                responseResultado.put(SNConstantes.CODIGO, dtoGenerico.getCodigoResultado());
                responseResultado.put(SNConstantes.MENSAJE, dtoGenerico.getDescripcionResultado());
                responseResultado.put("listaProcesoMatriz",dtoGenerico.getListado());
            }
        }catch (Exception ex){
            responseResultado.put(SNConstantes.CODIGO,SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE,SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado: {}",jsonResultado);

        logger.info("Fin ProcesoMatrizController - listarProcesoMatriz");
        return new ResponseEntity<>(responseResultado,HttpStatus.ACCEPTED);
    }

    /**
     * Método que retorna un proceso matriz
     *
     * @return obtenerProcesoMatriz
     */
    @GetMapping(value = "/obtenerProcesoMatriz/{idProcesoMatriz}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para obtener proceso matriz", notes = "Retorna un proceso matriz.")
    public ResponseEntity<?> obtenerProcesoMatriz(@PathVariable Long idProcesoMatriz){
        logger.info("Inicio ProcesoMatrizController - obtenerProcesoMatriz");

        Map<String,Object> responseResultado = new HashMap<>();
        DTOProcesoMatriz dtoProcesoMatriz;
        try {
            dtoProcesoMatriz = iProcesoMatrizService.obtenerProcesoMatriz(idProcesoMatriz);
            if ( dtoProcesoMatriz.getIdEmpresa() == null){
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
                responseResultado.put("ProcesoMatriz", "No existe ningun proceso matriz");
            }else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_EXITO);
                responseResultado.put("ProcesoMatriz", dtoProcesoMatriz);
            }
        }catch (Exception ex){
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado,HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado {}", jsonResultado);

        logger.info("Fin ProcesoMatrizController - obtenerProcesoMatriz");
        return new ResponseEntity<>(responseResultado,HttpStatus.ACCEPTED);
    }

    /**
     * Método que actualiza un proceso matriz
     *
     * @return actualizarProcesoMatriz
     */
    @PutMapping(value = "/actualizarProcesoMatriz", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para actualizar un proceso matriz", notes = "Actualiza un proceso matriz.")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> actualizarProcesoMatriz(@RequestBody ProcesoMatrizBean procesoMatrizBean){
        logger.info("Inicio ProcesoMatrizController - actualizarProcesoMatriz");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonPeticion = gson.toJson(procesoMatrizBean);
        logger.info("Peticion : {}", jsonPeticion);

        Map<String,Object> responseResultado = new HashMap<>();
        DTOProcesoMatriz dtoProcesoMatriz;

        try {
            dtoProcesoMatriz = iProcesoMatrizService.actualizarProcesoMatriz(procesoMatrizBean);
            if (dtoProcesoMatriz.getCodigoResultado().compareTo(SNConstantes.CODIGO_EXITO) != 0){
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R_BD);
                responseResultado.put(SNConstantes.ID_PROC_MATRIZ, 0L);
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_PROCESO_MATRIZ
                        + SNConstantes.MENSAJE_EXITO_ACTUALIZAR);
                responseResultado.put(SNConstantes.ID_PROC_MATRIZ, procesoMatrizBean.getIdProcesoMatriz());
            }
        }catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            responseResultado.put(SNConstantes.ID_PROC_MATRIZ, 0L);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado : {}",jsonResultado);

        logger.info("Fin ProcesoMatrizController - actualizarProcesoMatriz");
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }

    /**
     * Método que anula un proceso matriz
     *
     * @return anularProcesoMatriz
     */
    @PostMapping(value = "/anularProcesoMatriz", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para anular un proceso matriz", notes = "Anula un proceso matriz.")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> anularProcesoMatriz(@RequestBody ProcesoMatrizBean procesoMatrizBean){
        logger.info("Inicio ProcesoMatrizController - anularProcesoMatriz");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonPeticion = gson.toJson(procesoMatrizBean);
        logger.info("Peticion : {}", jsonPeticion);

        Map<String,Object> responseResultado = new HashMap<>();
        DTOProcesoMatriz dtoProcesoMatriz;

        try {
            dtoProcesoMatriz = iProcesoMatrizService.obtenerProcesoMatriz(procesoMatrizBean.getIdProcesoMatriz());
            Byte indicadorAnularProcesoMatriz = iProcesoMatrizService.anularProcesoMatriz(procesoMatrizBean);

            if (indicadorAnularProcesoMatriz != 1){
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_EXITO);
                responseResultado.put(SNConstantes.ID_PROC_MATRIZ, 0L);
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_PROCESO_MATRIZ +
                        SNConstantes.MENSAJE_EXITO_ANULAR);
                responseResultado.put(SNConstantes.ID_PROC_MATRIZ, dtoProcesoMatriz.getIdProcesoMatriz());
            }
        }catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String jsonResultado = gson.toJson(dtoProcesoMatriz);
        logger.info("Resultado : {}", jsonResultado);

        logger.info("Fin ProcesoMatrizController - anularProcesoMatriz");
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }
}

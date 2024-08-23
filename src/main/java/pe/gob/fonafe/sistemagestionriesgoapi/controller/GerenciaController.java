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
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGerencia;
import pe.gob.fonafe.sistemagestionriesgoapi.models.GerenciaBean;
import pe.gob.fonafe.sistemagestionriesgoapi.service.IGerenciaService;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/gestionriesgo/gerencia")
public class GerenciaController {

    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    final IGerenciaService iGerenciaService;

    public GerenciaController(IGerenciaService iGerenciaService) {
        this.iGerenciaService = iGerenciaService;
    }

    /**
     * Método que registra una gerencia
     *
     * @return registrarGerencia
     */
    @PostMapping(value = "/registrarGerencia", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para registrar una gerencia", notes = "Registra una gerencia.")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> registrarGerencia(@RequestBody GerenciaBean gerenciaBean){
        logger.info("Inicio de GerenciaController - registrarGerencia");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonPeticion = gson.toJson(gerenciaBean);
        logger.info("Peticion : {}", jsonPeticion);

        Map<String,Object> responseResultado = new HashMap<>();
        DTOGerencia dtoGerencia;
        try {
            dtoGerencia = iGerenciaService.registrarGerencia(gerenciaBean);

            if (dtoGerencia.getCodigoResultado().compareTo(SNConstantes.CODIGO_EXITO) != 0){
                responseResultado.put(SNConstantes.CODIGO,dtoGerencia.getCodigoResultado());
                responseResultado.put(SNConstantes.MENSAJE,SNConstantes.MENSAJE_ERR0R_BD);
                responseResultado.put(SNConstantes.ID_GERENCIA,0L);
            }else {
                responseResultado.put(SNConstantes.CODIGO,SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE,SNConstantes.MENSAJE_GERENCIA
                        + SNConstantes.MENSAJE_EXITO_REGISTRAR);
                responseResultado.put(SNConstantes.ID_GERENCIA,dtoGerencia.getIdGerencia());
            }

        }catch (Exception ex){
            responseResultado.put(SNConstantes.CODIGO,SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE,SNConstantes.MENSAJE_ERR0R);
            responseResultado.put(SNConstantes.ID_GERENCIA,0L);

            return new ResponseEntity<>(responseResultado,HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado : {}", jsonResultado);

        logger.info("Fin de GerenciaController - registrarGerencia");
        return new ResponseEntity<>(responseResultado,HttpStatus.CREATED);
    }

    /**
     * Método que retorna la lista total de gerencias
     *
     * @return listarGerencias
     */
    @GetMapping(value = "/listarGerencias/{idEmpresa}/{idSede}")
    @ApiOperation(value = "Endpoint para listar las gerencias", notes = "Retorna la lista de gerencias.")
    public ResponseEntity<?> listarGerencias(@PathVariable Long idEmpresa, @PathVariable Long idSede){
        logger.info("Inicio GerenciaController - listarGerencia");

        Map<String,Object> responseResultado = new HashMap<>();

        DTOGenerico dtoGenerico = new DTOGenerico();
        try {
            dtoGenerico = iGerenciaService.listarGerencias(idEmpresa,idSede);

            if (dtoGenerico.getCodigoResultado().compareTo(SNConstantes.CODIGO_EXITO) != 0){
                responseResultado.put(SNConstantes.CODIGO,dtoGenerico.getCodigoResultado());
                responseResultado.put(SNConstantes.MENSAJE,SNConstantes.MENSAJE_ERR0R_BD);
                responseResultado.put("listaGerencias",dtoGenerico.getListado());
            }else {
                responseResultado.put(SNConstantes.CODIGO, dtoGenerico.getCodigoResultado());
                responseResultado.put(SNConstantes.MENSAJE, dtoGenerico.getDescripcionResultado());
                responseResultado.put("listaGerencias",dtoGenerico.getListado());
            }
        }catch (Exception ex){
            responseResultado.put(SNConstantes.CODIGO,SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE,SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado: {}",jsonResultado);

        logger.info("Fin GerenciaController - listarGerencias");
        return new ResponseEntity<>(responseResultado,HttpStatus.ACCEPTED);
    }

    /**
     * Método que retorna una gerencia
     *
     * @return obtenerGerencia
     */
    @GetMapping(value = "/obtenerGerencia/{idGerencia}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para obtener la gerencia", notes = "Retorna una gerencia.")
    public ResponseEntity<?> obtenerGerencia(@PathVariable Long idGerencia){
        logger.info("Inicio GerenciaController - obtenerGerencia");

        Map<String,Object> responseResultado = new HashMap<>();
        DTOGerencia dtoGerencia;
        try {
            dtoGerencia = iGerenciaService.obtenerGerencia(idGerencia);
            if ( dtoGerencia.getIdGerencia() == null){
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
                responseResultado.put("Gerencia", "No existe ninguna gerencia");
            }else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_EXITO);
                responseResultado.put("Gerencia", dtoGerencia);
            }
        }catch (Exception ex){
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado,HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado {}", jsonResultado);

        logger.info("Fin GerenciaController - obtenerGerencia");
        return new ResponseEntity<>(responseResultado,HttpStatus.ACCEPTED);
    }

    /**
     * Método que actualiza una gerencia
     *
     * @return actualizarGerencia
     */
    @PutMapping(value = "/actualizarGerencia", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para actualizar una gerencia", notes = "Actualiza una gerencia.")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> actualizarGerencia(@RequestBody GerenciaBean gerenciaBean){
        logger.info("Inicio GerenciaController - actualizarGerencia");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonPeticion = gson.toJson(gerenciaBean);
        logger.info("Peticion : {}", jsonPeticion);

        Map<String,Object> responseResultado = new HashMap<>();
        DTOGerencia dtoGerencia;

        try {
            dtoGerencia = iGerenciaService.actualizarGerencia(gerenciaBean);
            if (dtoGerencia.getCodigoResultado().compareTo(SNConstantes.CODIGO_EXITO) != 0){
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R_BD);
                responseResultado.put(SNConstantes.ID_GERENCIA, 0L);
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_GERENCIA
                        + SNConstantes.MENSAJE_EXITO_ACTUALIZAR);
                responseResultado.put(SNConstantes.ID_GERENCIA, gerenciaBean.getIdGerencia());
            }
        }catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            responseResultado.put(SNConstantes.ID_GERENCIA, 0L);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado : {}",jsonResultado);

        logger.info("Fin GerenciaController - actualizarGerencia");
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }

    /**
     * Método que anula una gerencia
     *
     * @return anularGerencia
     */
    @PostMapping(value = "/anularGerencia", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para anular una gerencia", notes = "Anula una gerencia.")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> anularGerencia(@RequestBody GerenciaBean gerenciaBean){
        logger.info("Inicio GerenciaController - anularGerencia");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonPeticion = gson.toJson(gerenciaBean);
        logger.info("Peticion : {}", jsonPeticion);

        Map<String,Object> responseResultado = new HashMap<>();
        DTOGerencia dtoGerencia;

        try {
            dtoGerencia = iGerenciaService.obtenerGerencia(gerenciaBean.getIdGerencia());
            Byte indicadorAnularGerencia = iGerenciaService.anularGerencia(gerenciaBean);

            if (indicadorAnularGerencia != 1){
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_EXITO);
                responseResultado.put(SNConstantes.ID_GERENCIA, 0L);
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_GERENCIA + SNConstantes.MENSAJE_EXITO_ANULAR);
                responseResultado.put(SNConstantes.ID_GERENCIA, dtoGerencia.getIdGerencia());
            }
        }catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String jsonResultado = gson.toJson(dtoGerencia);
        logger.info("Resultado : {}", jsonResultado);

        logger.info("Fin GerenciaController - anularGerencia");
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }
}

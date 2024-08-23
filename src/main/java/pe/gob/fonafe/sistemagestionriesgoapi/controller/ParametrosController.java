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
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOParametro;
import pe.gob.fonafe.sistemagestionriesgoapi.models.ParametroBean;
import pe.gob.fonafe.sistemagestionriesgoapi.service.IDatosGeneralesService;
import pe.gob.fonafe.sistemagestionriesgoapi.service.IParametroService;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/gestionriesgo/parametro")
public class ParametrosController {

    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    final IDatosGeneralesService iDatosGeneralesService;

    final IParametroService iParametroService;

    public ParametrosController(IDatosGeneralesService iDatosGeneralesService, IParametroService iParametroService) {
        this.iDatosGeneralesService = iDatosGeneralesService;
        this.iParametroService = iParametroService;
    }


    @PostMapping(value = "/registrarParametro", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para registrar parametro", notes = "Registra un parametro.")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> registrarParametro(@RequestBody ParametroBean parametroBean){
        logger.info("Inicio de ParametrosController - registrarParametro");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonPeticion = gson.toJson(parametroBean);
        logger.info("Peticion : {}", jsonPeticion);

        Map<String,Object> responseResultado = new HashMap<>();
        DTOParametro dtoParametro;

        try {
            dtoParametro = iParametroService.registrarParametro(parametroBean);

            if (dtoParametro.getCodigoResultado().compareTo(SNConstantes.CODIGO_EXITO) != 0){
                responseResultado.put(SNConstantes.CODIGO,dtoParametro.getCodigoResultado());
                responseResultado.put(SNConstantes.MENSAJE,SNConstantes.MENSAJE_ERR0R_BD);
                responseResultado.put(SNConstantes.ID_PARAMETRO,0L);
            }else {
                responseResultado.put(SNConstantes.CODIGO,SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE,SNConstantes.MENSAJE_PARAMETRO
                        + SNConstantes.MENSAJE_EXITO_REGISTRAR);
                responseResultado.put(SNConstantes.ID_PARAMETRO,dtoParametro.getIdParametro());
            }

        }catch (Exception ex){
            responseResultado.put(SNConstantes.CODIGO,SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE,SNConstantes.MENSAJE_ERR0R);
            responseResultado.put(SNConstantes.ID_PARAMETRO,0L);

            return new ResponseEntity<>(responseResultado,HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado : {}", jsonResultado);

        logger.info("Fin de ParametrosController - registrarParametro");
        return new ResponseEntity<>(responseResultado,HttpStatus.CREATED);
    }


    @GetMapping(value = "/obtenerParametro/{idParametro}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para obtener parametro", notes = "Retorna parametro.")
    public ResponseEntity<?> obtenerParametro(@PathVariable Long idParametro){
        logger.info("Inicio de ParametrosController - obtenerParametro");

        Map<String,Object> responseResultado = new HashMap<>();
        DTOParametro dtoParametro;

        try {
            dtoParametro = iParametroService.obtenerParametro(idParametro);
            if ( dtoParametro.getIdParametro() == null){
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
                responseResultado.put("Parametro", "No existe ningun parametro");
            }else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_EXITO);
                responseResultado.put("Parametro", dtoParametro);
            }
        }catch (Exception ex){
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado,HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado {}", jsonResultado);

        logger.info("Fin de ParametrosController - obtenerParametro");
        return new ResponseEntity<>(responseResultado,HttpStatus.ACCEPTED);
    }

    @GetMapping(value = "/listarParametros")
    @ApiOperation(value = "Endpoint para listar los parametros", notes = "Retorna la lista de parametros.")
    public ResponseEntity<?> listarParametros(){
        logger.info("Inicio de ParametrosController - listarEmpresas");

        Map<String,Object> responseResultado = new HashMap<>();

        DTOGenerico dtoGenerico = new DTOGenerico();
        try {
            dtoGenerico = iParametroService.listarParametro();

            if (dtoGenerico.getCodigoResultado().compareTo(SNConstantes.CODIGO_EXITO) != 0){
                responseResultado.put(SNConstantes.CODIGO,dtoGenerico.getCodigoResultado());
                responseResultado.put(SNConstantes.MENSAJE,SNConstantes.MENSAJE_ERR0R_BD);
                responseResultado.put("listaParametros",dtoGenerico.getListado());
            }else {
                responseResultado.put(SNConstantes.CODIGO, dtoGenerico.getCodigoResultado());
                responseResultado.put(SNConstantes.MENSAJE, dtoGenerico.getDescripcionResultado());
                responseResultado.put("listaParametros",dtoGenerico.getListado());
            }
        }catch (Exception ex){
            responseResultado.put(SNConstantes.CODIGO,SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE,SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado: {}",jsonResultado);

        logger.info("Fin de ParametrosController - listarParametros");
        return new ResponseEntity<>(responseResultado,HttpStatus.ACCEPTED);
    }

    @PutMapping(value = "/actualizarParametro", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para actualizar un parametro", notes = "Actualiza un parametro.")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> actualizarParametro(@RequestBody ParametroBean parametroBean){

        logger.info("Inicio de ParametrosController - actualizarParametro");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonPeticion = gson.toJson(parametroBean);
        logger.info("Peticion : {}", jsonPeticion);

        Map<String,Object> responseResultado = new HashMap<>();
        DTOParametro dtoParametro;

        try {
            dtoParametro = iParametroService.actualizarParametro(parametroBean);
            if (dtoParametro.getCodigoResultado().compareTo(SNConstantes.CODIGO_EXITO) != 0){
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R_BD);
                responseResultado.put(SNConstantes.ID_PARAMETRO, 0L);
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_PARAMETRO
                        + SNConstantes.MENSAJE_EXITO_ACTUALIZAR);
                responseResultado.put(SNConstantes.ID_PARAMETRO, parametroBean.getIdParametro());
            }
        }catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            responseResultado.put(SNConstantes.ID_PARAMETRO, 0L);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado : {}",jsonResultado);

        logger.info("Fin de ParametrosController - actualizarParametro");

        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }

    @PostMapping(value = "/anularParametro", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para anular un parametro", notes = "Anula un parametro.")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> anularParametro(@RequestBody ParametroBean parametroBean){
        logger.info("Inicio de ParametrosController - anularParametro");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonPeticion = gson.toJson(parametroBean);
        logger.info("Peticion : {}", jsonPeticion);

        Map<String,Object> responseResultado = new HashMap<>();
        DTOParametro dtoParametro;

        try {
            dtoParametro = iParametroService.obtenerParametro(parametroBean.getIdParametro());
            Byte indicadorAnularParametro = iParametroService.anularParametro(parametroBean);

            if (indicadorAnularParametro != 1){
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_EXITO);
                responseResultado.put(SNConstantes.ID_PARAMETRO, 0L);
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_PARAMETRO + SNConstantes.MENSAJE_EXITO_ANULAR);
                responseResultado.put(SNConstantes.ID_PARAMETRO, dtoParametro.getIdParametro());
            }
        }catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String jsonResultado = gson.toJson(dtoParametro);
        logger.info("Resultado : {}", jsonResultado);

        logger.info("Fin de ParametrosController - anularParametro");

        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }

    /**
     * Método que retorna lista de Parámetros según código
     *
     * @return listarParametrosxCodigo
     */
    @GetMapping(value = "/listarParametrosxCodigo/{codigo}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para obtener lista de Parámetros por Código", notes = "Retorna lista de Parámetros.")
    public ResponseEntity<?> listarParametrosxCodigo(@PathVariable String codigo) {
        logger.info("Inicio de ParametrosController - listarParametrosxCodigo");

        Map<String, Object> responseResultado = new HashMap<>();

        DTOGenerico dtoGenerico = new DTOGenerico();

        try {
            dtoGenerico = iDatosGeneralesService.listarParametrosxCodigo(codigo);

            if (dtoGenerico.getCodigoResultado().compareTo(SNConstantes.CODIGO_EXITO) != 0) {
                responseResultado.put(SNConstantes.CODIGO, dtoGenerico.getCodigoResultado());
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R_BD);
                responseResultado.put("listaParametros", dtoGenerico.getListado());
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_EXITO);
                responseResultado.put("listaParametros", dtoGenerico.getListado());
            }
        } catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonRes = gson.toJson(responseResultado);
        logger.info("Resultado : {}", jsonRes);

        logger.info("Fin de ParametrosController - listarParametrosxCodigo");
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }


}

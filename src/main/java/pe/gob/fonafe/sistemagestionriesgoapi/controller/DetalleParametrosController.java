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
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTODetalleParametro;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOParametro;
import pe.gob.fonafe.sistemagestionriesgoapi.models.DetalleParametroBean;
import pe.gob.fonafe.sistemagestionriesgoapi.models.ParametroBean;
import pe.gob.fonafe.sistemagestionriesgoapi.service.IDetalleParametroService;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/gestionriesgo/detalleParametro")
public class DetalleParametrosController {

    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    final IDetalleParametroService iDetalleParametroService;

    public DetalleParametrosController(IDetalleParametroService iDetalleParametroService) {
        this.iDetalleParametroService = iDetalleParametroService;
    }

    @PostMapping(value = "/registrarDetalleParametro", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para registrar detalle parametro", notes = "Registra un detalle parametro.")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> registrarDetalleParametro(@RequestBody DetalleParametroBean detalleParametroBean){
        logger.info("Inicio de DetalleParametrosController - registrarDetalleParametro");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonPeticion = gson.toJson(detalleParametroBean);
        logger.info("Peticion : {}", jsonPeticion);

        Map<String,Object> responseResultado = new HashMap<>();
        DTODetalleParametro dtoDetalleParametro;

        try {
            dtoDetalleParametro = iDetalleParametroService.registrarDetalleParametro(detalleParametroBean);

            if (dtoDetalleParametro.getCodigoResultado().compareTo(SNConstantes.CODIGO_EXITO) != 0){
                responseResultado.put(SNConstantes.CODIGO,dtoDetalleParametro.getCodigoResultado());
                responseResultado.put(SNConstantes.MENSAJE,SNConstantes.MENSAJE_ERR0R_BD);
                responseResultado.put(SNConstantes.ID_DETALLE_PARAMETRO,0L);
            }else {
                responseResultado.put(SNConstantes.CODIGO,SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE,SNConstantes.MENSAJE_DETALLE_PARAMETRO
                        + SNConstantes.MENSAJE_EXITO_REGISTRAR);
                responseResultado.put(SNConstantes.ID_DETALLE_PARAMETRO, dtoDetalleParametro.getIdDetaParametro());
            }

        }catch (Exception ex){
            responseResultado.put(SNConstantes.CODIGO,SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE,SNConstantes.MENSAJE_ERR0R);
            responseResultado.put(SNConstantes.ID_DETALLE_PARAMETRO,0L);

            return new ResponseEntity<>(responseResultado,HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado : {}", jsonResultado);

        logger.info("Fin de DetalleParametrosController - registrarDetalleParametro");
        return new ResponseEntity<>(responseResultado,HttpStatus.CREATED);
    }


    @GetMapping(value = "/obtenerDetalleParametro/{idDetalleParametro}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para obtener detalleParametro", notes = "Retorna detalle de parametro.")
    public ResponseEntity<?> obtenerDetalleParametro(@PathVariable Long idDetalleParametro){
        logger.info("Inicio de DetalleParametrosController - obtenerDetalleParametro");

        Map<String,Object> responseResultado = new HashMap<>();

        DTODetalleParametro dtoDetalleParametro;

        try {

            dtoDetalleParametro = iDetalleParametroService.obtenerDetalleParametro(idDetalleParametro);
            if ( dtoDetalleParametro.getIdDetaParametro()== null){
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
                responseResultado.put("DetalleParametro", "No existe ningun detalle parametro");
            }else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_EXITO);
                responseResultado.put("DetalleParametro", dtoDetalleParametro);
            }
        }catch (Exception ex){
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado,HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado {}", jsonResultado);

        logger.info("Fin de DetalleParametrosController - obtenerDetalleParametro");

        return new ResponseEntity<>(responseResultado,HttpStatus.ACCEPTED);
    }


    @GetMapping(value = "/listarDetalleParametros/{idParametro}")
    @ApiOperation(value = "Endpoint para listar los detalles de parametros", notes = "Retorna la lista de detalles de parametros.")
    public ResponseEntity<?> listarDetalleParametros(@PathVariable Long idParametro){
        logger.info("Inicio de DetalleParametrosController - listarDetalleParametros");

        Map<String,Object> responseResultado = new HashMap<>();

        DTOGenerico dtoGenerico = new DTOGenerico();

        try {
            dtoGenerico = iDetalleParametroService.listarDetalleParametro(idParametro);

            if (dtoGenerico.getCodigoResultado().compareTo(SNConstantes.CODIGO_EXITO) != 0){
                responseResultado.put(SNConstantes.CODIGO,dtoGenerico.getCodigoResultado());
                responseResultado.put(SNConstantes.MENSAJE,SNConstantes.MENSAJE_ERR0R_BD);
                responseResultado.put("listaDetalleParametros",dtoGenerico.getListado());
            }else {
                responseResultado.put(SNConstantes.CODIGO, dtoGenerico.getCodigoResultado());
                responseResultado.put(SNConstantes.MENSAJE, dtoGenerico.getDescripcionResultado());
                responseResultado.put("listaDetalleParametros",dtoGenerico.getListado());
            }
        }catch (Exception ex){
            responseResultado.put(SNConstantes.CODIGO,SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE,SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado: {}",jsonResultado);

        logger.info("Fin de DetalleParametrosController - listarDetalleParametros");
        return new ResponseEntity<>(responseResultado,HttpStatus.ACCEPTED);
    }


    @PutMapping(value = "/actualizarDetalleParametro", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para actualizar detalles de un parametro", notes = "Actualiza detalle de un parametro.")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> actualizarDetalleParametro(@RequestBody DetalleParametroBean detalleParametroBean){

        logger.info("Inicio de DetalleParametrosController - actualizarDetalleParametro");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonPeticion = gson.toJson(detalleParametroBean);
        logger.info("Peticion : {}", jsonPeticion);

        Map<String,Object> responseResultado = new HashMap<>();
        DTODetalleParametro dtoDetalleParametro;

        try {
            dtoDetalleParametro = iDetalleParametroService.actualizarDetalleParametro(detalleParametroBean);
            if (dtoDetalleParametro.getCodigoResultado().compareTo(SNConstantes.CODIGO_EXITO) != 0){
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R_BD);
                responseResultado.put(SNConstantes.ID_DETALLE_PARAMETRO, 0L);
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_DETALLE_PARAMETRO
                        + SNConstantes.MENSAJE_EXITO_ACTUALIZAR);
                responseResultado.put(SNConstantes.ID_DETALLE_PARAMETRO, detalleParametroBean.getIdDetaParametro());
            }
        }catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            responseResultado.put(SNConstantes.ID_DETALLE_PARAMETRO, 0L);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado : {}",jsonResultado);

        logger.info("Fin de DetalleParametrosController - actualizarDetalleParametro");

        return new ResponseEntity<>(responseResultado,HttpStatus.ACCEPTED);
    }


    @PostMapping(value = "/anularDetalleParametro", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para anular un detalle parametro", notes = "Anula un detalle parametro.")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> anularDetalleParametro(@RequestBody DetalleParametroBean detalleParametroBean){
        logger.info("Inicio de DetalleParametrosController - anularDetalleParametro");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonPeticion = gson.toJson(detalleParametroBean);
        logger.info("Peticion : {}", jsonPeticion);

        Map<String,Object> responseResultado = new HashMap<>();
        DTODetalleParametro dtoDetalleParametro;

        try {
            dtoDetalleParametro = iDetalleParametroService.obtenerDetalleParametro(detalleParametroBean.getIdDetaParametro());
            Byte indicadorAnularDetalleParametro = iDetalleParametroService.anularDetalleParametro(detalleParametroBean);

            if (indicadorAnularDetalleParametro != 1){
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_EXITO);
                responseResultado.put(SNConstantes.ID_DETALLE_PARAMETRO, 0L);
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_DETALLE_PARAMETRO + SNConstantes.MENSAJE_EXITO_ANULAR);
                responseResultado.put(SNConstantes.ID_DETALLE_PARAMETRO, dtoDetalleParametro.getIdDetaParametro());
            }
        }catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String jsonResultado = gson.toJson(dtoDetalleParametro);
        logger.info("Resultado : {}", jsonResultado);


        logger.info("Fin de DetalleParametrosController - anularDetalleParametro");
        return new ResponseEntity<>(responseResultado,HttpStatus.ACCEPTED);
    }
}

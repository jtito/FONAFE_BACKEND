package pe.gob.fonafe.sistemagestionriesgoapi.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOPeriodo;
import pe.gob.fonafe.sistemagestionriesgoapi.service.IPeriodoService;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/gestionriesgo/periodo")
public class PeriodoController {

    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    @Autowired
    private IPeriodoService iPeriodoService;

    @GetMapping("/listarPeriodos/{empresa}/{anio}")
    @ApiOperation(value = "Endpoint para listarPeriodo", notes = "Retorna la lista de periodos.")
    public ResponseEntity<?> listarPeriodo(@PathVariable Long empresa, @PathVariable Long anio) {
        Map<String, Object> responseResultado = new HashMap<>();
        List<DTOPeriodo> listDtoPeriodo;
        DTOGenerico dtoGenerico = new DTOGenerico();
        try {
            dtoGenerico = iPeriodoService.listarPeriodos(empresa, anio);

            responseResultado.put(SNConstantes.CODIGO, dtoGenerico.getCodigoResultado());
            responseResultado.put(SNConstantes.MENSAJE, dtoGenerico.getDescripcionResultado());
            responseResultado.put("listaPeriodos", dtoGenerico.getListado());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado {}", jsonResultado);
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }


    @GetMapping("/buscarPeriodo/{p_id_periodo}")
    @ApiOperation(value = "Endpoint para buscarPeriodo x ID", notes = "Retorna el object de periodos.")
    public ResponseEntity<?> buscarPeriodo(@PathVariable Long p_id_periodo) {

        Map<String, Object> responseResultado = new HashMap<>();
        DTOGenerico dtoGenerico = new DTOGenerico();

//		DTOPeriodo dtoPeriodo = new DTOPeriodo();
        try {
            dtoGenerico = iPeriodoService.buscarPeriodo(p_id_periodo);
            responseResultado.put(SNConstantes.CODIGO, dtoGenerico.getCodigoResultado());
            responseResultado.put(SNConstantes.MENSAJE, dtoGenerico.getDescripcionResultado());
            responseResultado.put("Periodo", dtoGenerico.getListado().get(0));
        } catch (Exception e) {
            // TODO: handle exception
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado {}", jsonResultado);
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }

    @PostMapping(value = "/registrarPeriodo", produces = {MediaType.APPLICATION_JSON_VALUE})
    public DTOPeriodo registrarPeriodo(@RequestBody DTOPeriodo dTOPeriodo) {
        DTOPeriodo dtoPeriodo = new DTOPeriodo();
        try {
            dtoPeriodo = iPeriodoService.registrarPeriodo(dTOPeriodo);
            // System.out.println("hola");
        } catch (Exception e) {
            System.out.println("El error es: " + e.getMessage());
            // TODO: handle exception
        }
        return dtoPeriodo;
    }

    @PutMapping(value = "/actualizarPeriodo", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para actualizar un periodo", notes = "Actualiza un periodo.")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public DTOPeriodo actualizarPeriodo(@RequestBody DTOPeriodo dTOPeriodo) {
        DTOPeriodo dtoPeriodo = new DTOPeriodo();
        try {
            dtoPeriodo = iPeriodoService.actualizarPeriodo(dTOPeriodo);
        } catch (Exception e) {
            System.out.println("El error es: " + e.getMessage());
            // TODO: handle exception
        }
        return dtoPeriodo;
    }

    @PostMapping(value = "/anularPeriodo", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public DTOPeriodo anularPeriodo(@RequestBody DTOPeriodo dTOPeriodo) {
        DTOPeriodo dtoPeriodo = new DTOPeriodo();
        try {
            dtoPeriodo = iPeriodoService.anularPeriodo(dTOPeriodo);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return dtoPeriodo;
    }

    @GetMapping(value = "/generarDePeriodo/{idEmpresa}/{anio}/{idFrecuencia}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para obtener la descripcion de periodo", notes = "Retorna la descripcion de un periodo.")
    public ResponseEntity<?> generarDePeriodo(@PathVariable Long idEmpresa, @PathVariable Long anio,
                                              @PathVariable Long idFrecuencia) {
        logger.info("Inicio PeriodoController - generarDePeriodo");

        Map<String, Object> responseResultado = new HashMap<>();
        DTOGenerico dtoGenerico;
        try {
            dtoGenerico = iPeriodoService.generarDePeriodo(idEmpresa,anio,idFrecuencia);

            if (dtoGenerico.getCodigoResultado().compareTo(SNConstantes.CODIGO_EXITO) != 0) {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
                responseResultado.put("periodo", "No se generó el periodo");
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_EXITO);
                responseResultado.put("periodo", dtoGenerico.getDescripcionPeriodo());
                //prueba
                responseResultado.put("feIni", "01/01/2024");
                responseResultado.put("feFin", "01/01/2024");

            }
        } catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado {}", jsonResultado);

        logger.info("Fin PeriodoController - generarDePeriodo");
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }

}

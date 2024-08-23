package pe.gob.fonafe.sistemagestionriesgoapi.controller;

import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTODimension;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.service.IDimensionService;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("v1/controller")
public class DimensionController {

    @Autowired
    private IDimensionService iDimensionService;

    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    @GetMapping("/listarDimension/{p_ID_EMPRESA}/{p_ID_TIPO_CONTROL_RIESGO}")
    @ApiOperation(value = "Endpoint para listarDimension", notes = "Retorna la lista de dimensiones.")
    public ResponseEntity<?> listarDimension(@PathVariable Long p_ID_EMPRESA, @PathVariable Long p_ID_TIPO_CONTROL_RIESGO) {
        Map<String, Object> responseResultado = new HashMap<>();
        try {
            DTOGenerico dtoGenerico = iDimensionService.listarDimension(p_ID_EMPRESA, p_ID_TIPO_CONTROL_RIESGO);
            responseResultado.put(SNConstantes.CODIGO, dtoGenerico.getCodigoResultado());
            responseResultado.put(SNConstantes.MENSAJE, dtoGenerico.getDescripcionResultado());
            responseResultado.put("listaDimension", dtoGenerico.getListado());
        } catch (Exception e) {
            // TODO: handle exception
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }

    @PostMapping(value = "/registrarDimension", produces = {MediaType.APPLICATION_JSON_VALUE})
    public DTODimension registrarDimension(@RequestBody DTODimension dimension) {
        DTODimension dTODimension = new DTODimension();
        try {
            dTODimension = iDimensionService.registrarDimension(dimension);
            System.out.println("hola");
        } catch (Exception e) {
            System.out.println("El error es: " + e.getMessage());
            // TODO: handle exception
        }
        return dTODimension;
    }


    @GetMapping("/buscarDimension/{p_id_DIM_RIESGO}")
    @ApiOperation(value = "Endpoint para buscarDimension x ID", notes = "Retorna el object de dimension.")
    public ResponseEntity<?> buscarDimension(@PathVariable Long p_id_DIM_RIESGO) {

        Map<String, Object> responseResultado = new HashMap<>();
        DTOGenerico dtoGenerico = new DTOGenerico();

//		DTOPeriodo dtoPeriodo = new DTOPeriodo();
        try {
            dtoGenerico = iDimensionService.buscarDimension(p_id_DIM_RIESGO);
            responseResultado.put(SNConstantes.CODIGO, dtoGenerico.getCodigoResultado());
            responseResultado.put(SNConstantes.MENSAJE, dtoGenerico.getDescripcionResultado());
            responseResultado.put("Dimension", dtoGenerico.getListado().get(0));
        } catch (Exception e) {
            // TODO: handle exception
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }

    @PutMapping(value = "/actualizarDimension", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para actualizar un dimension", notes = "Actualiza un dimension.")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public DTODimension actualizarDimension(@RequestBody DTODimension dTODimension) {
        DTODimension dtoDimension = new DTODimension();
        try {
            dtoDimension = iDimensionService.actualizarDimension(dTODimension);
        } catch (Exception e) {
            System.out.println("El error es: " + e.getMessage());
            // TODO: handle exception
        }
        return dtoDimension;
    }

    @PostMapping(value = "/anularDimension", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public DTODimension anularDimension(@RequestBody DTODimension dTODimension) {
        DTODimension dtoDimension = new DTODimension();
        try {
            dtoDimension = iDimensionService.anularDimension(dTODimension);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("El error es: " + e.getMessage());
        }
        return dtoDimension;
    }

}

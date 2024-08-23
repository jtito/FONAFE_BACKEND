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
import pe.gob.fonafe.sistemagestionriesgoapi.dto.*;
import pe.gob.fonafe.sistemagestionriesgoapi.models.MatrizEvidenciaControlBean;
import pe.gob.fonafe.sistemagestionriesgoapi.models.MatrizRiesgoBean;
import pe.gob.fonafe.sistemagestionriesgoapi.service.IMatrizEventoService;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pe.gob.fonafe.sistemagestionriesgoapi.models.MatrizEventoBean;
import pe.gob.fonafe.sistemagestionriesgoapi.service.IDetalleGraficoService;

@RestController
@RequestMapping("/gestionriesgo/detalleGrafico")
public class DetalleGraficoController {
    
    private static final Logger logger = LogManager.getLogger("DETALLE_GRAFICO_API");

    final IDetalleGraficoService iDetalleGraficoService;

    public DetalleGraficoController(IDetalleGraficoService iDetalleGraficoService) {
        this.iDetalleGraficoService = iDetalleGraficoService;
    }
    
    
    @GetMapping(value = "/MatrizOperacionInhe/{idEmpresa}/{idPeriodo}/{idMatrizNivel}/{idTipoMatriz}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para listar la bandeja de matriz evento", notes = "Registra una lista de matrices.")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> listarMatrizOperacionInhe(@PathVariable int idEmpresa, @PathVariable int idPeriodo, @PathVariable int idMatrizNivel,
                                                       @PathVariable int idTipoMatriz) {
        logger.info("Inicio de DetalleGraficoController - listarMatrizOperacionInhe");

        logger.info("Peticion : {} - {} - {}", idEmpresa, idPeriodo, idMatrizNivel);

        Map<String, Object> responseResultado = new HashMap<>();
        List<DTODetalleGrafico> listaDetalleGrafico;
        try {
            listaDetalleGrafico= iDetalleGraficoService.listarCantidadRiesgosInhe(idEmpresa, idPeriodo, idMatrizNivel, idTipoMatriz);

            if ( listaDetalleGrafico.isEmpty()) {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R_BD);
                responseResultado.put("listaDetalleGrafico", 0L);
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_EXITO);
                responseResultado.put("listaDetalleGrafico", listaDetalleGrafico);
            }

        } catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado : {}", jsonResultado);

        logger.info("Fin de DetalleGraficoController - listarMatrizOperacionInhe");
        return new ResponseEntity<>(responseResultado, HttpStatus.CREATED);
    }
    
    
    @GetMapping(value = "/MatrizOperacionGer/{idEmpresa}/{idPeriodo}/{idMatrizNivel}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para listar la bandeja de matriz evento", notes = "Registra una lista de matrices.")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> listarMatrizOperacionGer(@PathVariable int idEmpresa, @PathVariable int idPeriodo, @PathVariable int idMatrizNivel) {
        logger.info("Inicio de DetalleGraficoController - listarMatrizOperacionGer");

        logger.info("Peticion : {} - {} - {}", idEmpresa, idPeriodo, idMatrizNivel);

        Map<String, Object> responseResultado = new HashMap<>();
        List<DTODetalleGrafico> listaDetalleGrafico;
        try {
            listaDetalleGrafico= iDetalleGraficoService.listarCantidadRiesgosGer(idEmpresa, idPeriodo, idMatrizNivel);

            if ( listaDetalleGrafico.isEmpty()) {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R_BD);
                responseResultado.put("listaDetalleGrafico", 0L);
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_EXITO);
                responseResultado.put("listaDetalleGrafico", listaDetalleGrafico);
            }

        } catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado : {}", jsonResultado);

        logger.info("Fin de DetalleGraficoController - listarMatrizOperacionGer");
        return new ResponseEntity<>(responseResultado, HttpStatus.CREATED);
    }
    
    
    @GetMapping(value = "/MatrizOperacionKri/{idEmpresa}/{idPeriodo}/{idMatrizNivel}/{idTipoMatriz}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para listar la bandeja de matriz evento", notes = "Registra una lista de matrices.")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> listarMatrizOperacionKri(@PathVariable int idEmpresa, @PathVariable int idPeriodo, @PathVariable int idMatrizNivel,
                                                      @PathVariable int idTipoMatriz) {
        logger.info("Inicio de DetalleGraficoController - listarMatrizOperacionKri");

        logger.info("Peticion : {} - {} - {}", idEmpresa, idPeriodo, idMatrizNivel);

        Map<String, Object> responseResultado = new HashMap<>();
        DTOGenerico dtoGenerico = new DTOGenerico();

        try {
            dtoGenerico = iDetalleGraficoService.listarCantidadRiesgosKri(idEmpresa, idPeriodo, idMatrizNivel, idTipoMatriz);

            if ( dtoGenerico.getCodigoResultado().compareTo(SNConstantes.CODIGO_EXITO) != 0) {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R_BD);
                responseResultado.put("listaDetalleGrafico", dtoGenerico.getListaDetalleGrafico());
                responseResultado.put("porcentajeCumplimiento", dtoGenerico.getPorcentajeCumplimiento());
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_EXITO);
                responseResultado.put("listaDetalleGrafico", dtoGenerico.getListaDetalleGrafico());
                responseResultado.put("porcentajeCumplimiento", dtoGenerico.getPorcentajeCumplimiento());
            }

        } catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado : {}", jsonResultado);

        logger.info("Fin de DetalleGraficoController - listarMatrizOperacionKri");
        return new ResponseEntity<>(responseResultado, HttpStatus.CREATED);
    }


    @GetMapping(value = "/matrizEvento/{idEmpresa}/{idPeriodo}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para reporte de la  matriz evento", notes = "Lista reporte de matriz eventos de perdida.")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> reporteMatrizEventos(@PathVariable Long idEmpresa, @PathVariable Long idPeriodo) {
        logger.info("Inicio de DetalleGraficoController - reporteMatrizEventos");

        Map<String, Object> responseResultado = new HashMap<>();

        DTOGenerico dtoGenerico = new DTOGenerico();

        try {

            dtoGenerico = iDetalleGraficoService.reporteMatrizEventos(idEmpresa, idPeriodo);

            if (dtoGenerico.getCodigoResultado().compareTo(SNConstantes.CODIGO_EXITO) != 0){
                responseResultado.put(SNConstantes.CODIGO,dtoGenerico.getCodigoResultado());
                responseResultado.put(SNConstantes.MENSAJE,SNConstantes.MENSAJE_ERR0R_BD);
                responseResultado.put("listaGraficoEvento",dtoGenerico.getListaGraficoEvento());
                responseResultado.put("listaGraficoCump",dtoGenerico.getListaGraficoCump());
                responseResultado.put("nuPerdida",dtoGenerico.getNuPerdida());
                responseResultado.put("porcentajeCumplimiento",dtoGenerico.getPorcentajeCumplimiento());
            }else {
                responseResultado.put(SNConstantes.CODIGO, dtoGenerico.getCodigoResultado());
                responseResultado.put(SNConstantes.MENSAJE, dtoGenerico.getDescripcionResultado());
                responseResultado.put("listaGraficoEvento",dtoGenerico.getListaGraficoEvento());
                responseResultado.put("listaGraficoCump",dtoGenerico.getListaGraficoCump());
                responseResultado.put("nuPerdida",dtoGenerico.getNuPerdida());
                responseResultado.put("porcentajeCumplimiento",dtoGenerico.getPorcentajeCumplimiento());
            }

        }catch (Exception ex){
            responseResultado.put(SNConstantes.CODIGO,SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE,SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado: {}",jsonResultado);

        logger.info("Fin de DetalleGraficoController - reporteMatrizEventos");
        return new ResponseEntity<>(responseResultado, HttpStatus.CREATED);
    }

    
}

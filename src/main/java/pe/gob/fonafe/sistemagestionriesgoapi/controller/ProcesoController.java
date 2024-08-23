/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOProceso;
import pe.gob.fonafe.sistemagestionriesgoapi.models.ProcesoBean;
import pe.gob.fonafe.sistemagestionriesgoapi.models.SubProcesoBean;
import pe.gob.fonafe.sistemagestionriesgoapi.service.IProcesoService;
import pe.gob.fonafe.sistemagestionriesgoapi.service.ISubProcesoService;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/gestionriesgo/proceso")
public class ProcesoController {

    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    final IProcesoService iProcesoService;
    final ISubProcesoService iSubProcesoService;

    public ProcesoController(IProcesoService iProcesoService, ISubProcesoService iSubProcesoService) {
        this.iProcesoService = iProcesoService;
        this.iSubProcesoService = iSubProcesoService;
    }


    @PostMapping(value = "/registrarProceso", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para registrar un Proceso", notes = "Registra un Proceso.")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> registrarProceso(@RequestBody DTOProceso procesoBean) {
        logger.info("Inicio de ProcesoController - registrarProceso");


        Map<String, Object> responseResultado = new HashMap<>();
        DTOGenerico dtoGenerico;

        try {
            dtoGenerico = iProcesoService.registrarProceso(procesoBean);

            if (dtoGenerico.getCodigoResultado().compareTo(SNConstantes.CODIGO_EXITO) == 0) {
                responseResultado.put(SNConstantes.CODIGO, dtoGenerico.getCodigoResultado());
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R_BD);
                responseResultado.put("listaProcesos", dtoGenerico.getListado());
            } else {
                responseResultado.put(SNConstantes.CODIGO, dtoGenerico.getCodigoResultado());
                responseResultado.put(SNConstantes.MENSAJE, "SE REGISTRARON LOS PROCESO");
                responseResultado.put("listaProcesos", dtoGenerico.getListado());
            }
        } catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado: {}", jsonResultado);

        logger.info("Fin ProcesoController - ");
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }

    @GetMapping(value = "/listarProceso/{idEmpresa}/{idTipoMatriz}")
    @ApiOperation(value = "Endpoint para listar Proceso", notes = "Retorna la lista de Procesos.")
    public ResponseEntity<?> listarProcesos(@PathVariable Long idEmpresa, @PathVariable Long idTipoMatriz) {
        logger.info("Inicio ProcesoController - listarProcesos");

        Map<String, Object> responseResultado = new HashMap<>();
        List<DTOProceso> listaproceso;
        try {
            listaproceso = iProcesoService.listarProceso(idEmpresa, idTipoMatriz);
            if (listaproceso.isEmpty()) {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_EXITO);
                responseResultado.put("listaProceso", listaproceso);
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_EXITO);
                responseResultado.put("listaProceso", listaproceso);
            }
        } catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado: {}", jsonResultado);

        logger.info("Fin ProcesoController - listarProcesos");
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }

    @GetMapping(value = "/listaProcesoMatriz/{idProcesoMatriz}")
    @ApiOperation(value = "Endpoint para listar Proceso", notes = "Retorna la lista de Procesos.")
    public ResponseEntity<?> listaProcesosMatriz(@PathVariable Long idProcesoMatriz) {
        logger.info("Inicio ProcesoController - listaProcesosMatriz");

        Map<String, Object> responseResultado = new HashMap<>();
        List<DTOProceso> listaproceso;
        try {
            listaproceso = iProcesoService.listaProcesosMatriz(idProcesoMatriz);
            if (listaproceso.isEmpty()) {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_EXITO);
                responseResultado.put("listaProceso", listaproceso);
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_EXITO);
                responseResultado.put("listaProceso", listaproceso);
            }
        } catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado: {}", jsonResultado);

        logger.info("Fin ProcesoController - listaProcesosMatriz");
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }

    @GetMapping(value = "/listarProcesoxMatriz/{idEmpresa}/{idSede}/{matrizNivel}/{idTipoMatriz}")
    @ApiOperation(value = "Endpoint para listar Proceso por matriz", notes = "Retorna la lista de Procesos.")
    public ResponseEntity<?> listarProcesosxMatriz(@PathVariable Long idEmpresa, @PathVariable Long idSede,
                                                   @PathVariable Long matrizNivel, @PathVariable Long idTipoMatriz) {
        logger.info("Inicio ProcesoController - listarProcesosxMatriz");

        Map<String, Object> responseResultado = new HashMap<>();

        DTOGenerico dtoGenerico = new DTOGenerico();
        try {
            dtoGenerico = iProcesoService.listarProcesosxMatriz(idEmpresa, idSede, matrizNivel, idTipoMatriz);

            if (dtoGenerico.getCodigoResultado().compareTo(SNConstantes.CODIGO_EXITO) != 0) {
                responseResultado.put(SNConstantes.CODIGO, dtoGenerico.getCodigoResultado());
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R_BD);
                responseResultado.put("listaProceso", dtoGenerico.getListado());
            } else {
                responseResultado.put(SNConstantes.CODIGO, dtoGenerico.getCodigoResultado());
                responseResultado.put(SNConstantes.MENSAJE, dtoGenerico.getDescripcionResultado());
                responseResultado.put("listaProceso", dtoGenerico.getListado());
            }
        } catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado: {}", jsonResultado);

        logger.info("Fin ProcesoController - listarProcesosxMatriz");
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }


    @GetMapping(value = "/obtenerProceso/{idProceso}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para obtener Proceso", notes = "Retorna un Proceso.")
    public ResponseEntity<?> obtenerProceso(@PathVariable Long idProceso) {
        logger.info("Inicio ProcesoController - obtenerProceso");

        Map<String, Object> responseResultado = new HashMap<>();
        DTOProceso dtoProceso;
        try {
            dtoProceso = iProcesoService.obtenerProceso(idProceso);
            if (dtoProceso.getIdProceso() == null) {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
                responseResultado.put("Proceso", "No existe ningun Proceso");
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_EXITO);
                responseResultado.put("Proceso", dtoProceso);
            }
        } catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado {}", jsonResultado);

        logger.info("Fin ProcesoController - obtenerProceso");
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }

    /**
     * Método que actualiza un responsable
     *
     * @return actualizarResponsable
     */
    @PutMapping(value = "/actualizarProceso", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para actualizar un Proceso", notes = "Actualiza un Proceso.")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> actualizarProceso(@RequestBody ProcesoBean procesoBean) {
        logger.info("Inicio ProcesoController - actualizarProceso");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonPeticion = gson.toJson(procesoBean);
        logger.info("Peticion : {}", jsonPeticion);

        Map<String, Object> responseResultado = new HashMap<>();
        DTOProceso dtoProceso;

        try {
            dtoProceso = iProcesoService.actualizarProceso(procesoBean);

            if (!dtoProceso.getDescripcionMensaje().trim().equals(SNConstantes.DE_RESULT_OK)) {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R_BD);
                responseResultado.put(SNConstantes.ID_PROCESO, 0L);
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_PROCESO + procesoBean.getDeProceso()
                        + SNConstantes.MENSAJE_EXITO_ACTUALIZAR);
                responseResultado.put(SNConstantes.ID_PROCESO, procesoBean.getIdProceso());
            }
        } catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            responseResultado.put(SNConstantes.ID_PROCESO, 0L);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado : {}", jsonResultado);

        logger.info("Fin ProcesoController - actualizarProceso");
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }


    @PostMapping(value = "/actualizarProcesos", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para registrar un Proceso", notes = "Registra un Proceso.")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> actualizarProcesos(@RequestBody DTOProceso procesoBean) {
        logger.info("Inicio de ProcesoController - actualizarProceso");


        Map<String, Object> responseResultado = new HashMap<>();
        DTOGenerico dtoGenerico;

        for (ProcesoBean proceso : procesoBean.getLstProcAct()) {
            if (proceso.getIdProceso() != null) {

                iProcesoService.actualizarProceso(proceso);
                logger.info("Fin de Actualizar proceso");

            }


            if (proceso.getLstProcAct() != null) {


                for (SubProcesoBean subproceso : proceso.getLstProcAct()) {
                    logger.info("Inicio de Actualizar subproceso");

                    if (subproceso.getIdSubProceso() != null) {

                        iSubProcesoService.actualizarSubProceso(subproceso);

                    }

                }

            }

            if (proceso.getLstProc() != null) {

                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String jsonPeticion = gson.toJson(proceso);
                logger.info("LST SUB PROC ACTUALIZAR: {}", jsonPeticion);

                for (SubProcesoBean subproceso : proceso.getLstProc()) {
                    logger.info("Inicio de Actualizar subproc");

                    if (subproceso.getIdSubProceso() != null) {

                        iSubProcesoService.actualizarSubProceso(subproceso);

                    }
                }
            }

            if (proceso.getLstProcAdd() != null) {

                for (SubProcesoBean subproceso : proceso.getLstProcAdd()) {

                    //     Gson gson1 = new GsonBuilder().setPrettyPrinting().create();
                    //      String jsonPeticion1 = gson.toJson(subproceso);
                    //     logger.info("LST SUB PROC ADD : {}", jsonPeticion1);
                    subproceso.setIdProceso(proceso.getIdProceso());

                    iSubProcesoService.registrarSubProceso(subproceso);

                }
            }

        }

        if (procesoBean.getLstProcAdd().size() > 0) {

            logger.info("Inicio de Registro Nuevo Proceso");

            iProcesoService.registrarProceso(procesoBean);

        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado: {}", jsonResultado);

        logger.info("Fin SubProcesoController - ");
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }

    /**
     * Método que anula un Proceso
     *
     * @return anularProceso
     */
    @PostMapping(value = "/anularProceso", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para anular un responsable", notes = "Anula un responsable.")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> anularProceso(@RequestBody ProcesoBean procesoBean) {
        logger.info("Inicio ProcesoController - anularProceso");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonPeticion = gson.toJson(procesoBean);
        logger.info("Peticion : {}", jsonPeticion);

        Map<String, Object> responseResultado = new HashMap<>();
        DTOProceso dtoProceso;

        try {
            dtoProceso = iProcesoService.obtenerProceso(procesoBean.getIdProceso());
            Byte indicadorAnularMatriz = iProcesoService.anularProceso(procesoBean);

            if (indicadorAnularMatriz != 1) {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_EXITO);
                responseResultado.put(SNConstantes.ID_PROCESO, 0L);
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_MATRIZ + SNConstantes.MENSAJE_EXITO_ANULAR);
                responseResultado.put(SNConstantes.ID_PROCESO, dtoProceso.getIdProceso());
            }
        } catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String jsonResultado = gson.toJson(dtoProceso);
        logger.info("Resultado : {}", jsonResultado);

        logger.info("Fin ProcesoController - anularProceso");
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }


}

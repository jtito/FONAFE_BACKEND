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
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOComentarioAuditoria;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOMatrizRiesgo;
import pe.gob.fonafe.sistemagestionriesgoapi.models.ComentarioAuditoriaBean;
import pe.gob.fonafe.sistemagestionriesgoapi.models.MatrizRiesgoBean;
import pe.gob.fonafe.sistemagestionriesgoapi.service.IMatrizRiesgoService;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTODetalleMatrizRiesgo;

@RestController
@RequestMapping("/gestionriesgo/matrizRiesgo")
public class MatrizRiesgoController {

    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    final IMatrizRiesgoService iMatrizRiesgoService;

    public MatrizRiesgoController(IMatrizRiesgoService iMatrizRiesgoService) {
        this.iMatrizRiesgoService = iMatrizRiesgoService;
    }

    @GetMapping(value = "/listarBandejaMatrizRiesgo/{idEmpresa}/{idPeriodo}/{idMatrizNivel}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para listar la bandeja de matriz riesgo", notes = "Registra una lista de matrices.")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> listarBandejaMatrizRiesgo(@PathVariable int idEmpresa, @PathVariable int idPeriodo, @PathVariable int idMatrizNivel) {
        logger.info("Inicio de MatrizRiesgoController - listarBandejaMatrizRiesgo");

        logger.info("Peticion : {} - {} - {}", idEmpresa, idPeriodo, idMatrizNivel);

        Map<String, Object> responseResultado = new HashMap<>();
        List<DTOMatrizRiesgo> listaBandejaMatrizRiesgo;
        try {
            listaBandejaMatrizRiesgo = iMatrizRiesgoService.listarBandejaMatrizRiesgo(idEmpresa, idPeriodo, idMatrizNivel);

            if (listaBandejaMatrizRiesgo.isEmpty()) {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R_BD);
                responseResultado.put("listaBandejaMatrizRiesgo", 0L);
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_EXITO);
                responseResultado.put("listaBandejaMatrizRiesgo", listaBandejaMatrizRiesgo);
            }

        } catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado : {}", jsonResultado);

        logger.info("Fin de MatrizRiesgoController - listarBandejaMatrizRiesgo");
        return new ResponseEntity<>(responseResultado, HttpStatus.CREATED);
    }

    /**
     * Método que registra una matriz riesgo y detalle
     *
     * @return registrarMatrizRiesgo
     */
    @PostMapping(value = "/registrarMatrizRiesgo", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para registrar una matriz riesgo", notes = "Registra una matriz riesgo.")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> registrarMatrizRiesgo(@RequestBody MatrizRiesgoBean matrizRiesgoBean) {
        logger.info("Inicio de MatrizRiesgoController - registrarMatrizRiesgo");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonPeticion = gson.toJson(matrizRiesgoBean);
        logger.info("Peticion : {}", jsonPeticion);

        Map<String, Object> responseResultado = new HashMap<>();
        DTOMatrizRiesgo dtoMatrizRiesgo;
        try {
            dtoMatrizRiesgo = iMatrizRiesgoService.registrarMatrizRiesgo(matrizRiesgoBean);

            /*if (dtoMatrizRiesgo.getCodigoResultado().compareTo(SNConstantes.CODIGO_ERROR) == 0) {
                responseResultado.put(SNConstantes.CODIGO, dtoMatrizRiesgo.getCodigoResultado());
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R_BD);
                responseResultado.put(SNConstantes.ID_MATRIZ_RIESGO, 0L);
            }
            else if(dtoMatrizRiesgo.getCodigoResultado().compareTo(SNConstantes.CODIGO_DUPLICATE) == 0) {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_DUPLICATE);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_DUPLICADO_PERIODO);
                responseResultado.put(SNConstantes.ID_MATRIZ_RIESGO, dtoMatrizRiesgo.getIdMatrizRiesgo());
            }
            else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_MATRIZ_RIESGO
                        + SNConstantes.MENSAJE_EXITO_REGISTRAR);
                responseResultado.put(SNConstantes.ID_MATRIZ_RIESGO, dtoMatrizRiesgo.getIdMatrizRiesgo());
            }*/

            if (dtoMatrizRiesgo.getCodigoResultado().compareTo(SNConstantes.CODIGO_EXITO) != 0) {
                responseResultado.put(SNConstantes.CODIGO, dtoMatrizRiesgo.getCodigoResultado());
                responseResultado.put(SNConstantes.MENSAJE, dtoMatrizRiesgo.getDescripcionResultado());
                responseResultado.put(SNConstantes.ID_MATRIZ_RIESGO, 0L);
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_MATRIZ_RIESGO
                        + SNConstantes.MENSAJE_EXITO_REGISTRAR);
                responseResultado.put(SNConstantes.ID_MATRIZ_RIESGO, dtoMatrizRiesgo.getIdMatrizRiesgo());
            }

        } catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            responseResultado.put(SNConstantes.ID_MATRIZ_RIESGO, 0L);

            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado : {}", jsonResultado);

        logger.info("Fin de MatrizRiesgoController - registrarMatrizRiesgo");
        return new ResponseEntity<>(responseResultado, HttpStatus.CREATED);
    }

    /**
     * Método que retorna una matriz riesgo
     *
     * @return obtenerMatrizRiesgo
     */
    @GetMapping(value = "/obtenerMatrizRiesgo/{idMatrizRiesgo}/{idUsuario}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para obtener la matriz de riesgo", notes = "Retorna una matriz de riesgo.")
    public ResponseEntity<?> obtenerMatrizRiesgo(@PathVariable Long idMatrizRiesgo, @PathVariable Long idUsuario) {
        logger.info("Inicio MatrizRiesgoController - obtenerMatrizRiesgo");

        Map<String, Object> responseResultado = new HashMap<>();
        DTOMatrizRiesgo dtoMatrizRiesgo;
        try {
            dtoMatrizRiesgo = iMatrizRiesgoService.obtenerMatrizRiesgo(idMatrizRiesgo, idUsuario);
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

        logger.info("Fin MatrizRiesgoController - obtenerMatrizRiesgo");
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }

    /**
     * Método que retorna comentario de detalle matriz riesgo
     *
     * @return obtenerComentarioMatrizRiesgo
     */
    @GetMapping(value = "/obtenerComentarioMatrizRiesgo/{idMatrizRiesgo}/{idDetaMatrizRiesgo}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para obtener el comentario de detalle matriz de riesgo", notes = "Retorna un comentario matriz de riesgo.")
    public ResponseEntity<?> obtenerComentarioMatrizRiesgo(@PathVariable Long idMatrizRiesgo,@PathVariable Long idDetaMatrizRiesgo) {
        logger.info("Inicio MatrizRiesgoController - obtenerComentarioMatrizRiesgo");

        Map<String, Object> responseResultado = new HashMap<>();
        DTOComentarioAuditoria dtoComentarioAuditoria;
        ComentarioAuditoriaBean comentarioAuditoriaBean;
        try {
            comentarioAuditoriaBean = new ComentarioAuditoriaBean();
            comentarioAuditoriaBean.setIdMatrizRiesgo(idMatrizRiesgo);
            comentarioAuditoriaBean.setIdDetaMatrizRiesgo(idDetaMatrizRiesgo);

            dtoComentarioAuditoria = iMatrizRiesgoService.obtenerComentarioAuditoriaRiesgo(comentarioAuditoriaBean);
            if (dtoComentarioAuditoria.getIdMatrizRiesgo() == null) {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
                responseResultado.put("MatrizRiesgo", "No existe ninguna matriz riesgo");
            }
            if (dtoComentarioAuditoria.getIdDetaMatrizRiesgo() == null) {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
                responseResultado.put("DetalleMatrizRiesgo", "No existe ningun detalle matriz riesgo");
            }
            else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_EXITO);
                responseResultado.put("ComentarioMatrizRiesgo", dtoComentarioAuditoria);
            }
        } catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado {}", jsonResultado);

        logger.info("Fin MatrizRiesgoController - obtenerComentarioMatrizRiesgo");
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }

    /**
     * Método que actualiza una matriz riesgo
     *
     * @return actualizarMatrizRiesgo
     */
    @PutMapping(value = "/actualizarMatrizRiesgo", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para actualizar una matriz de riesgo", notes = "Actualiza una matriz de riesgo.")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> actualizarMatrizRiesgo(@RequestBody MatrizRiesgoBean matrizRiesgoBean) {
        logger.info("Inicio MatrizRiesgoController - actualizarMatrizRiesgo");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonPeticion = gson.toJson(matrizRiesgoBean);
        logger.info("Peticion : {}", jsonPeticion);

        Map<String, Object> responseResultado = new HashMap<>();
        DTOMatrizRiesgo dtoMatrizRiesgo;

        try {
            dtoMatrizRiesgo = iMatrizRiesgoService.actualizarMatrizRiesgo(matrizRiesgoBean);
            if (dtoMatrizRiesgo.getCodigoResultado().compareTo(SNConstantes.CODIGO_EXITO) != 0) {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R_BD);
                responseResultado.put(SNConstantes.ID_MATRIZ_RIESGO, 0);
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_MATRIZ_RIESGO
                        + SNConstantes.MENSAJE_EXITO_ACTUALIZAR);
                responseResultado.put(SNConstantes.ID_MATRIZ_RIESGO, dtoMatrizRiesgo.getIdMatrizRiesgo());
            }
        } catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            responseResultado.put(SNConstantes.ID_MATRIZ_RIESGO, 0);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado : {}", jsonResultado);

        logger.info("Fin MatrizRiesgoController - actualizarMatrizRiesg");
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }

    @PutMapping(value = "/actualizarComentarioMatrizRiesgo", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para actualizar el comentario de matriz de riesgo", notes = "Actualiza comentario de matriz de riesgo.")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> actualizarComentarioMatrizRiesgo(@RequestBody ComentarioAuditoriaBean comentarioAuditoriaBean) {
        logger.info("Inicio MatrizRiesgoController - actualizarComentarioMatrizRiesgo");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonPeticion = gson.toJson(comentarioAuditoriaBean);
        logger.info("Peticion : {}", jsonPeticion);

        Map<String, Object> responseResultado = new HashMap<>();
        DTOComentarioAuditoria dtoComentarioAuditoria;

        try {
            dtoComentarioAuditoria = iMatrizRiesgoService.actualizarComentarioAuditoriaRiesgo(comentarioAuditoriaBean);
            if (dtoComentarioAuditoria.getCodigoResultado().compareTo(SNConstantes.CODIGO_EXITO) != 0) {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R_BD);
                //responseResultado.put(SNConstantes.ID_MATRIZ_RIESGO, 0);
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_COMENTARIO_MATRIZ_RIESGO
                        + SNConstantes.MENSAJE_EXITO_ACTUALIZAR);
                //responseResultado.put(SNConstantes.ID_MATRIZ_RIESGO, dtoMatrizRiesgo.getIdMatrizRiesgo());
            }
        } catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            //responseResultado.put(SNConstantes.ID_MATRIZ_RIESGO, 0);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        logger.info("Fin MatrizRiesgoController - actualizarComentarioMatrizRiesgo");
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }

    /**
     * Método que anula una matriz de riesgo
     *
     * @return anularMatrizRiesgo
     */
    @PostMapping(value = "/anularMatrizRiesgo", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para anular una matriz de riesgo", notes = "Anula una matriz de riesgo.")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> anularMatrizRiesgo(@RequestBody MatrizRiesgoBean matrizRiesgoBean) {
        logger.info("Inicio MatrizRiesgoController - anularMatrizRiesgo");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonPeticion = gson.toJson(matrizRiesgoBean);
        logger.info("Peticion : {}", jsonPeticion);

        Map<String, Object> responseResultado = new HashMap<>();
        DTOMatrizRiesgo dtoMatrizRiesgo;

        try {
//            dtoMatrizRiesgo = iMatrizRiesgoService.obtenerMatrizRiesgo(matrizRiesgoBean.getIdMatrizRiesgo());
            Byte indicadorAnularMatriz = iMatrizRiesgoService.anularMatrizRiesgo(matrizRiesgoBean);

            if (indicadorAnularMatriz != 1) {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_EXITO);
                responseResultado.put(SNConstantes.ID_MATRIZ_RIESGO, 0);
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_MATRIZ_RIESGO +
                        SNConstantes.MENSAJE_EXITO_ANULAR);
                responseResultado.put(SNConstantes.ID_MATRIZ_RIESGO, matrizRiesgoBean.getIdMatrizRiesgo());
            }
        } catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String jsonResultado = gson.toJson(matrizRiesgoBean);
        logger.info("Resultado : {}", jsonResultado);

        logger.info("Fin MatrizRiesgoController - anularMatrizRiesgo");
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }
    
    
    @GetMapping(value = "/obtenerDescripcion/{codRiesgo}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para obtener la descripcion de riesgo", notes = "Retorna la descripcion de un codigo de riesgo.")
    public ResponseEntity<?> obtenerDescripcion(@PathVariable String codRiesgo) {
        logger.info("Inicio MatrizRiesgoController - obtenerDescripcion");

        Map<String, Object> responseResultado = new HashMap<>();
        DTODetalleMatrizRiesgo dtoMatrizRiesgo;
        try {
            dtoMatrizRiesgo = iMatrizRiesgoService.obtenerDescripcion(codRiesgo);
            if (dtoMatrizRiesgo.getIdDetaMatrizRiesgo() == null) {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
                responseResultado.put("DetalleMatrizRiesgo", "No existe ninguna matriz riesgo");
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_EXITO);
                responseResultado.put("DetalleMatrizRiesgo", dtoMatrizRiesgo);
            }
        } catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado {}", jsonResultado);

        logger.info("Fin MatrizRiesgoController - obtenerDescripcion");
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }
    
    
    @GetMapping(value = "/obtenerMatrizPeriodo/{idEmpresa}/{idTipoMatriz}/{matrizNivel}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para obtener la matriz Periodo", notes = "Retorna la descripcion de un codigo de riesgo.")
    public ResponseEntity<?> obtenerMatrizPeriodo(@PathVariable Integer idEmpresa,@PathVariable Integer idTipoMatriz,@PathVariable Integer matrizNivel) {
        logger.info("Inicio MatrizRiesgoController - obtenerMatrizPeriodo");

        Map<String, Object> responseResultado = new HashMap<>();
        DTOGenerico listaDetaMatrizRiesgo;
        try {
            listaDetaMatrizRiesgo = iMatrizRiesgoService.obtenerMatrizPeriodo(idEmpresa,idTipoMatriz,matrizNivel);
            if (listaDetaMatrizRiesgo.getCodigoResultado().compareTo(SNConstantes.CODIGO_EXITO) != 0) {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
                responseResultado.put("DetalleMatrizRiesgo", listaDetaMatrizRiesgo.getListado());
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_EXITO);
                responseResultado.put("DetalleMatrizRiesgo", listaDetaMatrizRiesgo.getListado());
            }
        } catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado {}", jsonResultado);

        logger.info("Fin MatrizRiesgoController - obtenerMatrizPeriodo");
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }

    @GetMapping(value = "/obtenerSeveridad/{probabilidad}/{impacto}/{idTipoMatriz}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para obtener la descripcion de riesgo", notes = "Retorna la descripcion de un codigo de riesgo.")
    public ResponseEntity<?> obtenerSeveridad(@PathVariable Float probabilidad, @PathVariable Float impacto,
                                                @PathVariable Long idTipoMatriz) {
        logger.info("Inicio MatrizRiesgoController - obtenerSeveridad");

        Map<String, Object> responseResultado = new HashMap<>();
        DTOGenerico dtoGenerico;
        try {
            dtoGenerico = iMatrizRiesgoService.obtenerSeveridad(probabilidad,impacto,idTipoMatriz);

            if (dtoGenerico.getCodigoResultado().compareTo(SNConstantes.CODIGO_EXITO) != 0) {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
                responseResultado.put("severidad", "No se encontró la severidad");
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_EXITO);
                responseResultado.put("severidad", dtoGenerico.getDescripcionSeveridad());
            }
        } catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado {}", jsonResultado);

        logger.info("Fin MatrizRiesgoController - obtenerSeveridad");
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }

}

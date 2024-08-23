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
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOUsuario;
import pe.gob.fonafe.sistemagestionriesgoapi.models.UsuarioBean;
import pe.gob.fonafe.sistemagestionriesgoapi.service.IResponsableService;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/gestionriesgo/responsable")
public class ResponsableController {

    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    final IResponsableService iResponsableService;

    public ResponsableController(IResponsableService iResponsableService) {
        this.iResponsableService = iResponsableService;
    }

    /**
     * Método que registra un responsable
     *
     * @return registrarResponsable
     */
    @PostMapping(value = "/registrarResponsable", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para registrar un responsable", notes = "Registra un responsable.")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> registrarResponsable(@RequestBody UsuarioBean usuarioBean) {
        logger.info("Inicio de ResponsableController - registrarResponsable");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonPeticion = gson.toJson(usuarioBean);
        logger.info("Peticion : {}", jsonPeticion);

        Map<String, Object> responseResultado = new HashMap<>();
        DTOUsuario dtoUsuario;
        try {
            dtoUsuario = iResponsableService.registrarResponsable(usuarioBean);

            if (dtoUsuario.getCodigoResultado().compareTo(SNConstantes.CODIGO_EXITO) != 0) {
                responseResultado.put(SNConstantes.CODIGO, dtoUsuario.getCodigoResultado());
                responseResultado.put(SNConstantes.MENSAJE, dtoUsuario.getDescripcionResultado());
                responseResultado.put(SNConstantes.ID_RESPONSABLE, 0L);
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_RESPONSABLE
                        + SNConstantes.MENSAJE_EXITO_REGISTRAR);
                responseResultado.put(SNConstantes.ID_RESPONSABLE, dtoUsuario.getIdUsuario());
                responseResultado.put("pToken", dtoUsuario.getPassword());
            }

        } catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            responseResultado.put(SNConstantes.ID_RESPONSABLE, 0L);

            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado : {}", jsonResultado);

        logger.info("Fin de ResponsableController - registrarResponsable");
        return new ResponseEntity<>(responseResultado, HttpStatus.CREATED);
    }


    /**
     * Método que retorna la lista total de responsables
     *
     * @return listarResponsables
     */
    @GetMapping(value = "/listarResponsables/{idEmpresa}/{idSede}")
    @ApiOperation(value = "Endpoint para listar los responsables", notes = "Retorna la lista de responsables.")
    public ResponseEntity<?> listarResponsables(@PathVariable Long idEmpresa, @PathVariable Long idSede) {
        logger.info("Inicio ResponsableController - listarResponsable");

        Map<String, Object> responseResultado = new HashMap<>();

        DTOGenerico dtoGenerico = new DTOGenerico();
        try {
            dtoGenerico = iResponsableService.listarResponsables(idEmpresa, idSede);

            if (dtoGenerico.getCodigoResultado().compareTo(SNConstantes.CODIGO_EXITO) != 0) {
                responseResultado.put(SNConstantes.CODIGO, dtoGenerico.getCodigoResultado());
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R_BD);
                responseResultado.put("listaResponsables", dtoGenerico.getListado());
            } else {
                responseResultado.put(SNConstantes.CODIGO, dtoGenerico.getCodigoResultado());
                responseResultado.put(SNConstantes.MENSAJE, dtoGenerico.getDescripcionResultado());
                responseResultado.put("listaResponsables", dtoGenerico.getListado());
            }
        } catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado: {}", jsonResultado);

        logger.info("Fin ResponsableController - listarResponsable");
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }

    /**
     * Método que retorna un responsable
     *
     * @return obtenerResponsable
     */
    @GetMapping(value = "/obtenerResponsable/{idUsuario}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para obtener el responsable", notes = "Retorna un responsable.")
    public ResponseEntity<?> obtenerResponsable(@PathVariable Long idUsuario) {
        logger.info("Inicio ResponsableController - obtenerResponsable");

        Map<String, Object> responseResultado = new HashMap<>();
        DTOUsuario dtoUsuario;
        try {
            dtoUsuario = iResponsableService.obtenerResponsable(idUsuario);
            if (dtoUsuario.getIdUsuario() == null) {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
                responseResultado.put("Responsable", "No existe ningun responsable");
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_EXITO);
                responseResultado.put("Responsable", dtoUsuario);
            }
        } catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado {}", jsonResultado);

        logger.info("Fin ResponsableController - obtenerResponsable");
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }

    /**
     * Método que actualiza un responsable
     *
     * @return actualizarResponsable
     */
    @PutMapping(value = "/actualizarResponsable", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para actualizar un responsable", notes = "Actualiza un responsable.")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> actualizarResponsable(@RequestBody UsuarioBean usuarioBean) {
        logger.info("Inicio ResponsableController - actualizarResponsable");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonPeticion = gson.toJson(usuarioBean);
        logger.info("Peticion : {}", jsonPeticion);

        Map<String, Object> responseResultado = new HashMap<>();
        DTOUsuario dtoUsuario;

        try {
            dtoUsuario = iResponsableService.actualizarResponsable(usuarioBean);
            if (dtoUsuario.getCodigoResultado().compareTo(SNConstantes.CODIGO_EXITO) != 0) {
                responseResultado.put(SNConstantes.CODIGO, dtoUsuario.getCodigoResultado());
                responseResultado.put(SNConstantes.MENSAJE, dtoUsuario.getDescripcionResultado());
                responseResultado.put(SNConstantes.ID_RESPONSABLE, 0L);
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_RESPONSABLE
                        + SNConstantes.MENSAJE_EXITO_ACTUALIZAR);
                responseResultado.put(SNConstantes.ID_RESPONSABLE, usuarioBean.getIdUsuario());
            }
        } catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            responseResultado.put(SNConstantes.ID_RESPONSABLE, 0L);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado : {}", jsonResultado);

        logger.info("Fin ResponsableController - actualizarResponsable");
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }

    /**
     * Método que anula un responsable
     *
     * @return anularResponsable
     */
    @PostMapping(value = "/anularResponsable", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para anular un responsable", notes = "Anula un responsable.")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> anularResponsable(@RequestBody UsuarioBean usuarioBean) {
        logger.info("Inicio ResponsableController - anularResponsable");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonPeticion = gson.toJson(usuarioBean);
        logger.info("Peticion : {}", jsonPeticion);

        Map<String, Object> responseResultado = new HashMap<>();
        DTOUsuario dtoUsuario;

        try {
            dtoUsuario = iResponsableService.obtenerResponsable(usuarioBean.getIdUsuario());
            Byte indicadorAnularResponsable = iResponsableService.anularResponsable(usuarioBean);

            if (indicadorAnularResponsable != 1) {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_EXITO);
                responseResultado.put(SNConstantes.ID_RESPONSABLE, 0L);
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_RESPONSABLE + SNConstantes.MENSAJE_EXITO_ANULAR);
                responseResultado.put(SNConstantes.ID_RESPONSABLE, dtoUsuario.getIdUsuario());
            }
        } catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String jsonResultado = gson.toJson(dtoUsuario);
        logger.info("Resultado : {}", jsonResultado);

        logger.info("Fin ResponsableController - anularResponsable");
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }

    /**
     * Método que retorna la lista total de perfiles
     *
     * @return listarPerfiles
     */
    @GetMapping(value = "/listarPerfiles")
    @ApiOperation(value = "Endpoint para listar los perfiles", notes = "Retorna la lista de perfiles.")
    public ResponseEntity<?> listarPerfiles() {
        logger.info("Inicio ResponsableController - listarPerfiles");

        Map<String, Object> responseResultado = new HashMap<>();

        DTOGenerico dtoGenerico = new DTOGenerico();
        try {
            dtoGenerico = iResponsableService.listarPerfiles();

            if (dtoGenerico.getCodigoResultado().compareTo(SNConstantes.CODIGO_EXITO) != 0) {
                responseResultado.put(SNConstantes.CODIGO, dtoGenerico.getCodigoResultado());
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R_BD);
                responseResultado.put("listaPerfiles", dtoGenerico.getListado());
            } else {
                responseResultado.put(SNConstantes.CODIGO, dtoGenerico.getCodigoResultado());
                responseResultado.put(SNConstantes.MENSAJE, dtoGenerico.getDescripcionResultado());
                responseResultado.put("listaPerfiles", dtoGenerico.getListado());
            }
        } catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado: {}", jsonResultado);

        logger.info("Fin ResponsableController - listarResponsable");
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }

    /**
     * Método que actualiza contraseña
     *
     * @return actualizarPassword
     */
    @PutMapping(value = "/actualizarPassword", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para actualizar una contraseña", notes = "Actualiza una contraseña.")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> actualizarPassword(@RequestBody DTOUsuario dtoUsuario) {
        logger.info("Inicio ResponsableController - actualizarPassword");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonPeticion = gson.toJson(dtoUsuario);
        logger.info("Peticion : {}", jsonPeticion);

        Map<String, Object> responseResultado = new HashMap<>();
        DTOGenerico dtoGenerico;

        try {
            dtoGenerico = iResponsableService.actualizarPassword(dtoUsuario.getIdUsuario(),dtoUsuario.getPassword(),
                    dtoUsuario.getActPassword(),dtoUsuario.getNewPassword());
            if (dtoGenerico.getCodigoResultado().compareTo(SNConstantes.CODIGO_EXITO) != 0) {
                responseResultado.put(SNConstantes.CODIGO, dtoGenerico.getCodigoResultado());
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R_BD);
                responseResultado.put(SNConstantes.ID_RESPONSABLE, 0L);
            } else {
                responseResultado.put(SNConstantes.CODIGO, dtoGenerico.getCodigoResultado());
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_RESPONSABLE
                        + SNConstantes.MENSAJE_EXITO_ACTUALIZAR);
                responseResultado.put(SNConstantes.ID_RESPONSABLE, dtoUsuario.getIdUsuario());
            }
        } catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            responseResultado.put(SNConstantes.ID_RESPONSABLE, 0L);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado : {}", jsonResultado);

        logger.info("Fin ResponsableController - actualizarPassword");
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }


}

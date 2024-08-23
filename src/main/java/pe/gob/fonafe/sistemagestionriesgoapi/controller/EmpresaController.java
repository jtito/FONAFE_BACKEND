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
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOEmpresa;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.models.EmpresaBean;
import pe.gob.fonafe.sistemagestionriesgoapi.service.IEmpresaService;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/gestionriesgo/empresa")
public class EmpresaController {

    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    final IEmpresaService iEmpresaService;

    public EmpresaController(IEmpresaService iEmpresaService) {
        this.iEmpresaService = iEmpresaService;
    }

    /**
     * Método que registra una empresa
     *
     * @return registrarEmpresa
     */
    @PostMapping(value = "/registrarEmpresa", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para registrar una empresa", notes = "Registra una empresa.")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> registrarEmpresa(@RequestBody EmpresaBean empresaBean){
        logger.info("Inicio de EmpresaController - registrarEmpresa");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonPeticion = gson.toJson(empresaBean);
        logger.info("Peticion : {}", jsonPeticion);

        Map<String,Object> responseResultado = new HashMap<>();
        DTOEmpresa dtoEmpresa;
        try {
            dtoEmpresa = iEmpresaService.registrarEmpresa(empresaBean);

            if (dtoEmpresa.getCodigoResultado().compareTo(SNConstantes.CODIGO_EXITO) != 0){
                responseResultado.put(SNConstantes.CODIGO,dtoEmpresa.getCodigoResultado());
                responseResultado.put(SNConstantes.MENSAJE,SNConstantes.MENSAJE_ERR0R_BD);
                responseResultado.put(SNConstantes.ID_EMPRESA,0L);
            }else {
                responseResultado.put(SNConstantes.CODIGO,SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE,SNConstantes.MENSAJE_EMPRESA
                        + SNConstantes.MENSAJE_EXITO_REGISTRAR);
                responseResultado.put(SNConstantes.ID_EMPRESA,dtoEmpresa.getIdEmpresa());
            }

        }catch (Exception ex){
            responseResultado.put(SNConstantes.CODIGO,SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE,SNConstantes.MENSAJE_ERR0R);
            responseResultado.put(SNConstantes.ID_EMPRESA,0L);

            return new ResponseEntity<>(responseResultado,HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado : {}", jsonResultado);

        logger.info("Fin de EmpresaController - registrarEmpresa");
        return new ResponseEntity<>(responseResultado,HttpStatus.CREATED);
    }

    /**
     * Método que retorna la lista total de empresas
     *
     * @return listarEmpresas
     */
    @GetMapping(value = "/listarEmpresas")
    @ApiOperation(value = "Endpoint para listar las empresas", notes = "Retorna la lista de empresas.")
    //@RolesAllowed("ROLE_USER")
    public ResponseEntity<?> listarEmpresas(){
        logger.info("Inicio EmpresaController - listarEmpresas");

        Map<String,Object> responseResultado = new HashMap<>();

        DTOGenerico dtoGenerico = new DTOGenerico();
        try {
            dtoGenerico = iEmpresaService.listarEmpresas();

            if (dtoGenerico.getCodigoResultado().compareTo(SNConstantes.CODIGO_EXITO) != 0){
                responseResultado.put(SNConstantes.CODIGO,dtoGenerico.getCodigoResultado());
                responseResultado.put(SNConstantes.MENSAJE,SNConstantes.MENSAJE_ERR0R_BD);
                responseResultado.put("listaEmpresas",dtoGenerico.getListado());
            }else {
                responseResultado.put(SNConstantes.CODIGO, dtoGenerico.getCodigoResultado());
                responseResultado.put(SNConstantes.MENSAJE, dtoGenerico.getDescripcionResultado());
                responseResultado.put("listaEmpresas",dtoGenerico.getListado());
            }
        }catch (Exception ex){
            responseResultado.put(SNConstantes.CODIGO,SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE,SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado: {}",jsonResultado);

        logger.info("Fin EmpresaController - listarEmpresas");
        return new ResponseEntity<>(responseResultado,HttpStatus.ACCEPTED);
    }

    /**
     * Método que retorna una empresa
     *
     * @return obtenerEmpresa
     */
    @GetMapping(value = "/obtenerEmpresa/{idEmpresa}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para obtener la empresa", notes = "Retorna una empresa.")
    public ResponseEntity<?> obtenerEmpresa(@PathVariable Long idEmpresa){
        logger.info("Inicio EmpresaController - obtenerEmpresa");

        Map<String,Object> responseResultado = new HashMap<>();
        DTOEmpresa dtoEmpresa;
        try {
            dtoEmpresa = iEmpresaService.obtenerEmpresa(idEmpresa);
            if ( dtoEmpresa.getIdEmpresa() == null){
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
                responseResultado.put("Empresa", "No existe ninguna empresa");
            }else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_EXITO);
                responseResultado.put("Empresa", dtoEmpresa);
            }
        }catch (Exception ex){
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado,HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado {}", jsonResultado);

        logger.info("Fin EmpresaController - obtenerEmpresa");
        return new ResponseEntity<>(responseResultado,HttpStatus.ACCEPTED);
    }

    /**
     * Método que actualiza una empresa
     *
     * @return actualizarEmpresa
     */
    @PutMapping(value = "/actualizarEmpresa", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para actualizar una empresa", notes = "Actualiza una empresa.")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> actualizarEmpresa(@RequestBody EmpresaBean empresaBean){
        logger.info("Inicio EmpresaController - actualizarEmpresa");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonPeticion = gson.toJson(empresaBean);
        logger.info("Peticion : {}", jsonPeticion);

        Map<String,Object> responseResultado = new HashMap<>();
        DTOEmpresa dtoEmpresa;

        try {
            dtoEmpresa = iEmpresaService.actualizarEmpresa(empresaBean);
            if (dtoEmpresa.getCodigoResultado().compareTo(SNConstantes.CODIGO_EXITO) != 0){
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R_BD);
                responseResultado.put(SNConstantes.ID_EMPRESA, 0L);
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_EMPRESA
                        + SNConstantes.MENSAJE_EXITO_ACTUALIZAR);
                responseResultado.put(SNConstantes.ID_EMPRESA, empresaBean.getIdEmpresa());
            }
        }catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            responseResultado.put(SNConstantes.ID_EMPRESA, 0L);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado : {}",jsonResultado);

        logger.info("Fin EmpresaController - actualizarEmpresa");
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }

    /**
     * Método que anula una empresa
     *
     * @return anularEmpresa
     */
    @PostMapping(value = "/anularEmpresa", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para anular una empresa", notes = "Anula una empresa.")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> anularEmpresa(@RequestBody EmpresaBean empresaBean){
        logger.info("Inicio EmpresaController - anularEmpresa");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonPeticion = gson.toJson(empresaBean);
        logger.info("Peticion : {}", jsonPeticion);

        Map<String,Object> responseResultado = new HashMap<>();
        DTOEmpresa dtoEmpresa;

        try {
            dtoEmpresa = iEmpresaService.obtenerEmpresa(empresaBean.getIdEmpresa());
            Byte indicadorAnularEmpresa = iEmpresaService.anularEmpresa(empresaBean);

            if (indicadorAnularEmpresa != 1){
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_EXITO);
                responseResultado.put(SNConstantes.ID_EMPRESA, 0L);
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_EMPRESA + SNConstantes.MENSAJE_EXITO_ANULAR);
                responseResultado.put(SNConstantes.ID_EMPRESA, dtoEmpresa.getIdEmpresa());
            }
        }catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String jsonResultado = gson.toJson(dtoEmpresa);
        logger.info("Resultado : {}", jsonResultado);

        logger.info("Fin EmpresaController - anularEmpresa");
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }
}

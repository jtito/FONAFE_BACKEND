package pe.gob.fonafe.sistemagestionriesgoapi.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOMatrizEvidenciaControl;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOResponseApi;
import pe.gob.fonafe.sistemagestionriesgoapi.models.MatrizEvidenciaControlBean;
import pe.gob.fonafe.sistemagestionriesgoapi.service.IMatrizEvidenciaControlService;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import javax.servlet.http.HttpServletRequest;
import javax.swing.text.html.parser.Entity;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.util.Objects.isNull;

@Controller
@Slf4j
@RequestMapping("/gestionriesgo/MatrizEvidenciaControl")
public class MatrizEvidenciaControlController {

    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    final IMatrizEvidenciaControlService iMatrizEvidenciaControlService;

    public MatrizEvidenciaControlController(IMatrizEvidenciaControlService iMatrizEvidenciaControlService) {
        this.iMatrizEvidenciaControlService = iMatrizEvidenciaControlService;
    }

    @PostMapping(value = "/registrarMatriz/file", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para registrar matriz de evidencia control y subir archivos",
                notes = "subir archivo y Registra matriz de evidencia.")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> registrarMatrizEvidenciaControl(@RequestPart(value = "file", required = true) MultipartFile file,
                                                             @RequestParam("idMatrizRiesgo") Long idMatrizRiesgo,
                                                             @RequestParam("idDetaMatrizRiesgo") Long idDetaMatrizRiesgo,
                                                             @RequestParam("usuarioCreacion") String usuarioCreacion,
                                                             @RequestParam("ipCreacion") String ipCreacion,
                                                             @RequestParam("tipoEvidencia") Long tipoEvidencia){

        logger.info("Inicio de MatrizEvidenciaControlController - registrarMatrizEvidenciaControl");

        if (file == null || file.isEmpty() || Objects.requireNonNull(file.getOriginalFilename()).isEmpty()) {
            throw new MultipartException("File null");
        }

        try {
            MatrizEvidenciaControlBean matrizEvidenciaControlBean = new MatrizEvidenciaControlBean();
            matrizEvidenciaControlBean.setIdMatrizRiesgo(idMatrizRiesgo);
            matrizEvidenciaControlBean.setIdDetaMatrizRiesgo(idDetaMatrizRiesgo);
            matrizEvidenciaControlBean.setUsuarioCreacion(usuarioCreacion);
            matrizEvidenciaControlBean.setIpCreacion(ipCreacion);
            matrizEvidenciaControlBean.setTipoEvidencia(tipoEvidencia);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonPeticion = gson.toJson(matrizEvidenciaControlBean);
            logger.info("Peticion : {}", jsonPeticion);

            logger.info("Fin de MatrizEvidenciaControlController - registrarMatrizEvidenciaControl");

            return ResponseEntity.ok(DTOResponseApi.builder().code(200)
            .data(iMatrizEvidenciaControlService.registrarMatrizEvidenciaControl(file,matrizEvidenciaControlBean)).build());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(DTOResponseApi.builder().code(400).message(e.getMessage()).build());
        }

    }

    @GetMapping("/downloadMatrizEvidenciaControl/{fileName}")
    public ResponseEntity<Resource> downloadMatrizEvidenciaControl(@PathVariable("fileName") String fileName) throws IOException {
        if (isNull(fileName) || fileName.isEmpty()) {
            throw new RuntimeException("Parameters null");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=".concat(fileName));
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(iMatrizEvidenciaControlService.downloadMatrizEvidenciaControl(fileName));
    }

    @GetMapping("/listarMatrizEvidenciaControl/{idMatrizRiesgo}/{idDetaMatrizRiesgo}")
    @ApiOperation(value = "Endpoint para listar matriz evidencia", notes = "Retorna una lista.")
    public ResponseEntity<?> listarMatrizEvidenciaControl(@PathVariable Long idMatrizRiesgo,
                                                          @PathVariable Long idDetaMatrizRiesgo) {
        logger.info("Inicio de MatrizEvidenciaControlController - listarMatrizEvidenciaControl");
        Map<String, Object> responseResultado = new HashMap<>();

        DTOGenerico dtoGenerico = new DTOGenerico();
        MatrizEvidenciaControlBean matrizEvidenciaControlBean = new MatrizEvidenciaControlBean();

        try {
            matrizEvidenciaControlBean.setIdMatrizRiesgo(idMatrizRiesgo);
            matrizEvidenciaControlBean.setIdDetaMatrizRiesgo(idDetaMatrizRiesgo);

            dtoGenerico = iMatrizEvidenciaControlService.listarMatrizEvidenciaControl(matrizEvidenciaControlBean);

            if (dtoGenerico.getCodigoResultado().compareTo(SNConstantes.CODIGO_EXITO) != 0){
                responseResultado.put(SNConstantes.CODIGO,dtoGenerico.getCodigoResultado());
                responseResultado.put(SNConstantes.MENSAJE,SNConstantes.MENSAJE_ERR0R_BD);
                responseResultado.put("listaMatrizEvidenciaControl",dtoGenerico.getListadoEvidenciaControl());
                responseResultado.put("listaMatrizEvidenciaPlan",dtoGenerico.getListadoEvidenciaPlan());
            }else {
                responseResultado.put(SNConstantes.CODIGO, dtoGenerico.getCodigoResultado());
                responseResultado.put(SNConstantes.MENSAJE, dtoGenerico.getDescripcionResultado());
                responseResultado.put("listaMatrizEvidenciaControl",dtoGenerico.getListadoEvidenciaControl());
                responseResultado.put("listaMatrizEvidenciaPlan",dtoGenerico.getListadoEvidenciaPlan());
            }

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonResultado = gson.toJson(responseResultado);
            logger.info("Resultado: {}",jsonResultado);


        }catch (Exception ex){
            responseResultado.put(SNConstantes.CODIGO,SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE,SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        logger.info("Fin de MatrizEvidenciaControlController - listarArchivosMatrizEvidenciaControl");
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }

    @PostMapping("/deleteFileMatrizEvidenciaControl")
    public ResponseEntity<?> deleteFileMatrizEvidenciaControl(@RequestParam("fileName") String fileName,
                                                              @RequestParam("usuarioModificacion") String usuarioModificacion,
                                                              @RequestParam("ipModificacion") String ipModificacion) {
        logger.info("Inicio de MatrizEvidenciaControlController - deleteFileMatrizEvidenciaControl");

        if (isNull(fileName) || fileName.isEmpty()) {
            throw new RuntimeException("Parameters null");
        }
        try {

            MatrizEvidenciaControlBean matrizEvidenciaControlBean = new MatrizEvidenciaControlBean();
            matrizEvidenciaControlBean.setNombreArchivo(fileName);
            matrizEvidenciaControlBean.setUsuarioModificacion(usuarioModificacion);
            matrizEvidenciaControlBean.setIpModificacion(ipModificacion);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonPeticion = gson.toJson(matrizEvidenciaControlBean);
            logger.info("Peticion : {}", jsonPeticion);

            logger.info("Fin de MatrizEvidenciaControlController - deleteFileMatrizEvidenciaControl");
            return ResponseEntity.ok(DTOResponseApi.builder().code(200).data(iMatrizEvidenciaControlService.anularMatrizEvidenciaControl(matrizEvidenciaControlBean)).build());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(DTOResponseApi.builder().code(400).message(e.getMessage()).build());
        }
    }
}

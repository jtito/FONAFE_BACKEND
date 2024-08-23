package pe.gob.fonafe.sistemagestionriesgoapi.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOSubProceso;
import pe.gob.fonafe.sistemagestionriesgoapi.service.ISubProcesoService;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/gestionriesgo/subProceso")
public class SubProcesoController {

    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    final ISubProcesoService iSubProcesoService;

    public SubProcesoController(ISubProcesoService iSubProcesoService) {
        this.iSubProcesoService = iSubProcesoService;
    }

    /**
     * Método que retorna la lista total de subprocesos
     *
     * @return listarSubProcesos
     */
    @GetMapping(value = "/listarSubProceso/{idProceso}")
    @ApiOperation(value = "Endpoint para listar los subprocesos", notes = "Retorna la lista de subprocesos.")
    public ResponseEntity<?> listarSubProcesos(@PathVariable Long idProceso) {
        logger.info("Inicio SubProcesoController - listarSubProcesos");

        Map<String, Object> responseResultado = new HashMap<>();

        List<DTOSubProceso> listaSubProcesos;
        try {
            listaSubProcesos = iSubProcesoService.listarSubProceso(idProceso);

            if (listaSubProcesos.isEmpty()) {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
                responseResultado.put("listaSubProcesos", listaSubProcesos);
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_EXITO);
                responseResultado.put("listaSubProcesos", listaSubProcesos);
            }
        } catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado: {}", jsonResultado);

        logger.info("Fin SubProcesoController - listarSubProcesos");
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }
}

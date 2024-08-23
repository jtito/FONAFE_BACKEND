package pe.gob.fonafe.sistemagestionriesgoapi.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.swagger.annotations.ApiOperation;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOMatrizOportunidad;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOMatrizRiesgo;
import pe.gob.fonafe.sistemagestionriesgoapi.models.MatrizRiesgoBean;
import pe.gob.fonafe.sistemagestionriesgoapi.models.MatrizRiesgoOportunidadBean;
import pe.gob.fonafe.sistemagestionriesgoapi.service.IMatrizOportunidadService;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;



@RestController
@RequestMapping("/gestionriesgo/matrizOportunidad")
public class MatrizOportunidadController {

    @SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger("GESTION_OPORTUNIDAD_API");

    final IMatrizOportunidadService iMatrizOportunidadService;

    public MatrizOportunidadController(IMatrizOportunidadService iMatrizOportunidadService) {
        this.iMatrizOportunidadService = iMatrizOportunidadService;
    }
    
    /*
    **
    * Método que registra una matriz Oportunidad
    *
    * @return Registra Matriz de oportunidad
    */
   @PostMapping(value = "/registrarMatrizOportunidad", produces = {MediaType.APPLICATION_JSON_VALUE})
   @ApiOperation(value = "Endpoint para registrar una matriz  de oportunidad", notes = "Registra una matriz de oportunidad.")
   @ResponseStatus(HttpStatus.CREATED)
   public ResponseEntity<?> registrarMatrizOportunidad(@RequestBody MatrizRiesgoOportunidadBean matrizOportunidadBean) {
       logger.info("Inicio de MatrizOportunidadController - registrarMatrizOportunidad");

       Gson gson = new GsonBuilder().setPrettyPrinting().create();
       String jsonPeticion = gson.toJson(matrizOportunidadBean);
       logger.info("Peticion : {}", jsonPeticion);

       Map<String, Object> responseResultado = new HashMap<>();
       DTOMatrizOportunidad dtoMatrizoportunidad;
       try {
    	   dtoMatrizoportunidad = iMatrizOportunidadService.registrarMatrizOportunidad(matrizOportunidadBean);

           if (dtoMatrizoportunidad.getCodigoResultado().compareTo(SNConstantes.CODIGO_EXITO) != 0) {
               responseResultado.put(SNConstantes.CODIGO, dtoMatrizoportunidad.getCodigoResultado());
               responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R_BD);
               responseResultado.put(SNConstantes.ID_MATRIZ_RIESGO, 0L);
           } else {
               responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
               responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_MATRIZ_RIESGO
                       + SNConstantes.MENSAJE_EXITO_REGISTRAR);
               responseResultado.put(SNConstantes.ID_MATRIZ_RIESGO, dtoMatrizoportunidad.getIdMatrizRiesgo());
           }

       } catch (Exception ex) {
           responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
           responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
           responseResultado.put(SNConstantes.ID_MATRIZ_RIESGO, 0L);

           return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
       }

       String jsonResultado = gson.toJson(responseResultado);
       logger.info("Resultado : {}", jsonResultado);

       logger.info("Fin de MatrizOportunidadController - registrarMatrizOportunidad");
       return new ResponseEntity<>(responseResultado, HttpStatus.CREATED);
   }
/*
   * Método que actuaiza una matriz Oportunidad
   *
   * @return update  oportunidad
   */
   @PutMapping(value = "/UpdateMatrizOportunidad", produces = {MediaType.APPLICATION_JSON_VALUE})
  @ApiOperation(value = "Endpoint para Update una matriz  de oportunidad", notes = "Update una matriz de oportunidad.")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<?> updateMatrizOportunidad(@RequestBody MatrizRiesgoOportunidadBean matrizOportunidadBean) {
      logger.info("Inicio de MatrizOportunidadController - updateMatrizOportunidad");

      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      String jsonPeticion = gson.toJson(matrizOportunidadBean);
      logger.info("Peticion : {}", jsonPeticion);

      Map<String, Object> responseResultado = new HashMap<>();
      DTOMatrizOportunidad dtoMatrizoportunidad;
      try {
   	   dtoMatrizoportunidad = iMatrizOportunidadService.UpdateMatrizOportunidad(matrizOportunidadBean);

          if (dtoMatrizoportunidad.getCodigoResultado().compareTo(SNConstantes.CODIGO_EXITO) != 0) {
              responseResultado.put(SNConstantes.CODIGO, dtoMatrizoportunidad.getCodigoResultado());
              responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R_BD);
              responseResultado.put(SNConstantes.ID_MATRIZ_RIESGO, 0L);
          } else {
              responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
              responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_MATRIZ_OPORTUNIDAD
                      + SNConstantes.MENSAJE_EXITO_ACTUALIZAR);
              responseResultado.put(SNConstantes.ID_MATRIZ_RIESGO, dtoMatrizoportunidad.getIdMatrizRiesgo());
          }

      } catch (Exception ex) {
          responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
          responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
          responseResultado.put(SNConstantes.ID_MATRIZ_RIESGO, 0L);

          return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
      }

      String jsonResultado = gson.toJson(responseResultado);
      logger.info("Resultado : {}", jsonResultado);

      logger.info("Fin de MatrizOportunidadController - registrarMatrizOportunidad");
      return new ResponseEntity<>(responseResultado, HttpStatus.CREATED);
  }

  /**
   * Método que retorna una MatrizOportunidad
   *
   * @return obtenerMatrizOportunidad
   */
  @GetMapping(value = "/obtenerMatrizOportunidad/{idMatrizRiesgo}/{idUsuario}", produces = {MediaType.APPLICATION_JSON_VALUE})
  @ApiOperation(value = "Endpoint para obtener la matriz de Oportunidad", notes = "Retorna una matriz de Oportunidad.")
  public ResponseEntity<?> obtenerMatrizOportunidad(@PathVariable Long idMatrizRiesgo, @PathVariable Long idUsuario) {
      logger.info("Inicio MatrizOportunidadController - obtenerMatrizOportunidad");

      Map<String, Object> responseResultado = new HashMap<>();
      DTOMatrizOportunidad dtoMatrizoportunidad;
      try {
    	  dtoMatrizoportunidad = iMatrizOportunidadService.OptenerMatrizOportunidad(idMatrizRiesgo, idUsuario);
          if (dtoMatrizoportunidad.getIdMatrizRiesgo()  == null ) {
              responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
              responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
              responseResultado.put("MatrizOportunidad", "No existe ninguna  MatrizOportunidad");
          } else {
              responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
              responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_EXITO);
              responseResultado.put("MatrizOportunidad", dtoMatrizoportunidad);
          }
      } catch (Exception ex) {
          responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
          responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
          return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
      }

      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      String jsonResultado = gson.toJson(responseResultado);
      logger.info("Resultado {}", jsonResultado);

      logger.info("Fin MatrizOportunidadController - obtenerMatrizOportunidad");
      return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
  }

  @GetMapping(value = "/listarBandejaMatrizOportunidad/{idEmpresa}/{idPeriodo}/{idMatrizNivel}", produces = {MediaType.APPLICATION_JSON_VALUE})
  @ApiOperation(value = "Endpoint para listar la bandeja de matriz Oportunidad", notes = "Registra una lista de matrices.")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<?> listarBandejaMatrizOportunidad(@PathVariable int idEmpresa, @PathVariable int idPeriodo, @PathVariable int idMatrizNivel) {
      logger.info("Inicio de MatrizOportunidadController - listarBandejaMatrizOportunidad");

      logger.info("Peticion : {} - {} - {}", idEmpresa, idPeriodo, idMatrizNivel);

      Map<String, Object> responseResultado = new HashMap<>();
      List<DTOMatrizRiesgo> listaBandejaMatrizRiesgo;
      try {
          listaBandejaMatrizRiesgo = iMatrizOportunidadService.listarBandejaMatrizOportunidad(idEmpresa, idPeriodo, idMatrizNivel);

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

      logger.info("Fin de MatrizOportunidadController - listarBandejaMatrizOportunidad");
      return new ResponseEntity<>(responseResultado, HttpStatus.CREATED);
  }
  
  
  @PostMapping(value = "/anularMatrizOportunidad", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para anular una matriz de riesgo", notes = "Anula una matriz de oportunidad.")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> anularMatrizOportunidad(@RequestBody MatrizRiesgoOportunidadBean matrizRiesgoBean) {
        logger.info("Inicio MatrizOportunidadController - anularMatrizOportunidad");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonPeticion = gson.toJson(matrizRiesgoBean);
        logger.info("Peticion : {}", jsonPeticion);

        Map<String, Object> responseResultado = new HashMap<>();
        DTOMatrizOportunidad dtoMatrizOportunidad;

        try {
//            dtoMatrizOportunidad = iMatrizOportunidadService.OptenerMatrizOportunidad(matrizRiesgoBean.getIdMatrizRiesgo());
            Byte indicadorAnularMatriz = iMatrizOportunidadService.anularMatrizOportunidad(matrizRiesgoBean);

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

        logger.info("Fin MatrizOportunidadController - anularMatrizOportunidad");
        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }
  
}

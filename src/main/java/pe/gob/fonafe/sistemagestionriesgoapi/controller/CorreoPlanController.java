package pe.gob.fonafe.sistemagestionriesgoapi.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.*;
import pe.gob.fonafe.sistemagestionriesgoapi.models.CorreoPlanBean;
import pe.gob.fonafe.sistemagestionriesgoapi.models.ParametroBean;
import pe.gob.fonafe.sistemagestionriesgoapi.service.ICorreoPlanService;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/gestionriesgo/correoPlan")
public class CorreoPlanController {
    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    final ICorreoPlanService iCorreoPlanService;

    public CorreoPlanController(ICorreoPlanService iCorreoPlanService) {
        this.iCorreoPlanService = iCorreoPlanService;
    }

    @GetMapping(value = "/obtenerConfiguracionPlanAccion", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para obtener la configuracion de notificacion de plan de accion", notes = "obtiene numero de dias antes y despues del vencimiento.")
    public ResponseEntity<?> obtenerConfiguracionPlanAccion(){
        logger.info("Inicio de CorreoPlanController - obtenerConfiguracionPlanAccion");
        Map<String,Object> responseResultado = new HashMap<>();
        DTOCorreoPlan dtoCorreoPlan;

        try {
            dtoCorreoPlan = iCorreoPlanService.obtenerCorreoPlan();
            if ( dtoCorreoPlan.getIdConfigCorreo() == null){
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
                responseResultado.put("configuracionPlanAccion", "No existe ningun configuracion de notificacion de plan de acción");
            }else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_EXITO);
                responseResultado.put("configuracionPlanAccion", dtoCorreoPlan);
            }
        }catch (Exception ex){
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            return new ResponseEntity<>(responseResultado,HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado {}", jsonResultado);

        logger.info("Fin de CorreoPlanController - obtenerConfiguracionPlanAccion");
        return new ResponseEntity<>(responseResultado,HttpStatus.ACCEPTED);
    }

    @PutMapping(value = "/actualizarConfiguracionPlanAccion", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint para actualizar la configuracion de notificacion de plan de accion", notes = "Actualiza numero de dias antes y despues del vencimiento.")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> actualizarConfiguracionPlanAccion(@RequestBody CorreoPlanBean correoPlanBean){
        logger.info("Inicio de CorreoPlanController - actualizarConfiguracionPlanAccion");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonPeticion = gson.toJson(correoPlanBean);
        logger.info("Peticion : {}", jsonPeticion);

        Map<String,Object> responseResultado = new HashMap<>();
        DTOCorreoPlan dtoCorreoPlan;

        try {

            dtoCorreoPlan = iCorreoPlanService.actualizarNotificacionPlanAccion(correoPlanBean);
            if (dtoCorreoPlan.getCodigoResultado().compareTo(SNConstantes.CODIGO_EXITO) != 0){
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R_BD);
                responseResultado.put(SNConstantes.ID_CONFIG_CORREO, 0L);
            } else {
                responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_EXITO);
                responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_CONFIG_CORREO
                        + SNConstantes.MENSAJE_EXITO_ACTUALIZAR);
                responseResultado.put(SNConstantes.ID_CONFIG_CORREO, correoPlanBean.getIdConfigCorreo());
            }
        }catch (Exception ex) {
            responseResultado.put(SNConstantes.CODIGO, SNConstantes.CODIGO_ERROR);
            responseResultado.put(SNConstantes.MENSAJE, SNConstantes.MENSAJE_ERR0R);
            responseResultado.put(SNConstantes.ID_CONFIG_CORREO, 0L);
            return new ResponseEntity<>(responseResultado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String jsonResultado = gson.toJson(responseResultado);
        logger.info("Resultado : {}",jsonResultado);

        logger.info("Fin de CorreoPlanController - actualizarConfiguracionPlanAccion");

        return new ResponseEntity<>(responseResultado, HttpStatus.ACCEPTED);
    }

    @PostMapping("/enviarCorreoContrasenia")
    @ApiOperation(value = "Enviar un correo de prueba")
    public ResponseEntity<?> enviarCorreoPrueba(@RequestBody Map<String, String> correoRequest) {
        try {
        	logger.info("INGRESO AL ENDPOINT TEST");
            String destinatarios = correoRequest.get("destinatario");  
            String mensaje = correoRequest.get("mensaje").replace("\n", "<br>");
            String plantillaCorreo = "<b>Estimado(a),</b><p>A continuacion, haz click sobre el enlace para ser redirigido a la pagina.</p><p>" + mensaje + "</p><b>Gracias.</b>";
            String subject = correoRequest.get("asunto"); 
            
          
            DTOGenerico resultado = iCorreoPlanService.enviarCorreo(plantillaCorreo, destinatarios, "",subject);
            
            if (resultado.getCodigoResultado().equals(SNConstantes.CODIGO_EXITO)) {
                return ResponseEntity.ok("Correo enviado con éxito");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al enviar el correo");
            }
        } catch (Exception e) {
            logger.error("Error al enviar el correo de prueba: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al enviar el correo");
        }
    }

//    @Scheduled(fixedRate = 180000,zone = "America/Lima")
    @Scheduled(cron = "0 1 0 * * ?",zone = "America/Lima")
    public void envioCorreoAutomatico(){

        logger.info("Inicio de CorreoPlanController - envioCorreoAutomatico");

        Map<String, Object> responseResultado = new HashMap<>();
        DTOGenerico dtoGenerico = new DTOGenerico();
        DTOGenerico listaFechaVencimiento;
        DTOCorreoPlan dtoCorreoPlan;
        Integer diasAntesVencimiento=0;
        Integer diasDespuesVencimiento=0;
        String destinatarios="";
        String destinatarioJefeInmediato="";

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("America/Lima"));

        String plantillaCorreo="";

        Date fechaVencimiento;
        Date date = new Date();
        String fechaActualString = "";
        Date fechaActual;
        List<DTOFechaVencimiento> listaDestinatariosFechaProceso;
        List<DTOFechaVencimiento> listaDestinatariosFechaEntidad;
        List<DTOMailConfig> listaMailConfig;
        Integer diasDiferencia=0;


        try {
            fechaActualString = sdf.format(date);
            fechaActual = sdf.parse(fechaActualString);

            listaFechaVencimiento = iCorreoPlanService.listarFechaVencimiento();
            if (listaFechaVencimiento.getCodigoResultado().compareTo(SNConstantes.CODIGO_EXITO)!=0){
                throw new Exception(SNConstantes.CODIGO+" "+listaFechaVencimiento.getCodigoResultado());
            }

            listaDestinatariosFechaProceso=listaFechaVencimiento.getListado();
            listaDestinatariosFechaEntidad = listaFechaVencimiento.getListaCorreoEntidad();
            listaMailConfig = listaFechaVencimiento.getListaMailConfig();

            dtoCorreoPlan = iCorreoPlanService.obtenerCorreoPlan();
            if (dtoCorreoPlan.getIdConfigCorreo()==null){
                throw new Exception("No existen ninguna configuracion Correo Plan");
            }

            diasAntesVencimiento = dtoCorreoPlan.getNuDiasAntes().intValue();
            diasDespuesVencimiento = dtoCorreoPlan.getNuDiasDespues().intValue();

            logger.info("Dias antes del vencimiento ",diasAntesVencimiento.toString());
            logger.info("Dias despues del vencimiento ",diasDespuesVencimiento.toString());

            if(diasAntesVencimiento!=0 && diasDespuesVencimiento!=0){

                for (DTOFechaVencimiento item : listaDestinatariosFechaProceso){

                    fechaVencimiento = sdf.parse(item.getFechaVencimiento());
//                    fechaVencimiento = sdf.parse("30/12/2021"); //para prueba

                    if (fechaVencimiento!=null){

                        diasDiferencia = (int) ((fechaVencimiento.getTime()-fechaActual.getTime())/86400000);
                        destinatarios = item.getDestinatario();
//                        destinatarios = "rpumapillo@canvia.com,pruebacanvia@gmail.com";//para prueba

                        destinatarioJefeInmediato = item.getDestinatarioJefeInmediato();
//                        destinatarioJefeInmediato = "richardpumac@gmail.com"; //para prueba

                        logger.info("Destinatarios Proceso ",destinatarios);
                        logger.info("Destinatarios jefe Inmediato Proceso ",destinatarios);

                        if (destinatarios!=""||destinatarios!=null||item.getPlanAccion()!=""||item.getPlanAccion()!=null){
                            if((diasDiferencia>0)&&(diasDiferencia<=diasAntesVencimiento)){
                                //estas en el plazo
                                plantillaCorreo=String.format("<b>Estimado(a),</b> <p> El plan de acci&oacute;n "+item.getPlanAccion()+", est&aacute; pr&oacute;ximo a vencer debe ser segistrado antes del %s.</p>\n \n <b>Gracias.</b>",sdf.format(fechaVencimiento));
                                dtoGenerico = iCorreoPlanService.enviarCorreoPlan(plantillaCorreo,destinatarios,"",listaMailConfig);
                            }
                            if (diasDiferencia==0){
                                plantillaCorreo="<b>Estimado(a),</b> <p> El plan de acci&oacute;n "+item.getPlanAccion()+", venci&oacute; el d&iacute;a de hoy, debe ser registrado.</p>\n \n <b>Gracias.</b>";
                                dtoGenerico = iCorreoPlanService.enviarCorreoPlan(plantillaCorreo,destinatarios,destinatarioJefeInmediato,listaMailConfig);

                            }
                            if ((diasDiferencia<0)&&(diasDiferencia>=(diasDespuesVencimiento)*-1)){
                                //paso el plazo
                                plantillaCorreo=String.format("<b>Estimado(a),</b> <p> El plan de acci&oacute;n "+item.getPlanAccion()+", venci&oacute; hace %d d&iacute;as, debe ser registrado inmediatamente.</p>\n \n <b>Gracias.</b>",diasDiferencia*(-1));
                                dtoGenerico = iCorreoPlanService.enviarCorreoPlan(plantillaCorreo,destinatarios,destinatarioJefeInmediato,listaMailConfig);
                            }
                            logger.info("Resultado de envio correo ",dtoGenerico.getOderesult());
                        }
                    }

                }

                for (DTOFechaVencimiento item : listaDestinatariosFechaEntidad){

                    fechaVencimiento = sdf.parse(item.getFechaVencimiento());
//                    fechaVencimiento = sdf.parse("30/12/2021");
                    if (fechaVencimiento!=null){

                        diasDiferencia = (int) ((fechaVencimiento.getTime()-fechaActual.getTime())/86400000);
                        destinatarios = item.getDestinatario();
//                        destinatarios = "richardpumac@gmail.com";//para jose Felipa para prueba
                        logger.info("Destinatarios Entidad ",destinatarios);

                        if (destinatarios!=""||destinatarios!=null||item.getPlanAccion()!=""||item.getPlanAccion()!=null){
                            if((diasDiferencia>0)&&(diasDiferencia<=diasAntesVencimiento)){
                                //estas en el plazo
                                plantillaCorreo=String.format("<b>Estimado(a),</b> <p> El plan de acci&oacute;n "+item.getPlanAccion()+", est&aacute; pr&oacute;ximo a vencer debe ser segistrado antes del %s.</p>\n \n <b>Gracias.</b>",sdf.format(fechaVencimiento));
                                dtoGenerico = iCorreoPlanService.enviarCorreoPlan(plantillaCorreo,destinatarios,"",listaMailConfig);
                            }
                            if (diasDiferencia==0){
                                plantillaCorreo="<b>Estimado(a),</b> <p> El plan de acci&oacute;n "+item.getPlanAccion()+", venci&oacute; el d&iacute;a de hoy, debe ser registrado.</p>\n \n <b>Gracias.</b>";
                                dtoGenerico = iCorreoPlanService.enviarCorreoPlan(plantillaCorreo,destinatarios,"",listaMailConfig);

                            }
                            if ((diasDiferencia<0)&&(diasDiferencia>=(diasDespuesVencimiento)*-1)){
                                //paso el plazo
                                plantillaCorreo=String.format("<b>Estimado(a),</b> <p> El plan de acci&oacute;n "+item.getPlanAccion()+", venci&oacute; hace %d d&iacute;as, debe ser registrado inmediatamente.</p>\n \n <b>Gracias.</b>",diasDiferencia*(-1));
                                dtoGenerico = iCorreoPlanService.enviarCorreoPlan(plantillaCorreo,destinatarios,"",listaMailConfig);
                            }
                            logger.info("Resultado de envio correo ",dtoGenerico.getOderesult());
                        }
                    }
                }
            }

            logger.info("Fin de CorreoPlanController - envioCorreoAutomatico");

        }catch (Exception ex) {
            dtoGenerico = new DTOGenerico();
            logger.error(ex.getMessage());
        }

    }
}

package pe.gob.fonafe.sistemagestionriesgoapi.service;

import org.springframework.dao.DataAccessException;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOCorreoPlan;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOMailConfig;
import pe.gob.fonafe.sistemagestionriesgoapi.models.CorreoPlanBean;

import java.util.List;

public interface ICorreoPlanService {
    DTOGenerico enviarCorreoPlan(String mailHtml, String destinatarios, String destinatarioJefeInmediato, List<DTOMailConfig> listaMailConfig);
    DTOGenerico enviarCorreo(String mailHtml, String destinatarios, String destinatarioJefeInmediato, String subject);
    DTOCorreoPlan obtenerCorreoPlan();
    DTOCorreoPlan actualizarNotificacionPlanAccion(CorreoPlanBean correoPlanBean);
    DTOGenerico listarFechaVencimiento();
}

package pe.gob.fonafe.sistemagestionriesgoapi.dao;

import org.springframework.dao.DataAccessException;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOCorreoPlan;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.models.CorreoPlanBean;

public interface ICorreoPlanDao {

    DTOCorreoPlan obtenerCorreoPlan() throws DataAccessException;
    DTOCorreoPlan actualizarNotificacionPlanAccion(CorreoPlanBean correoPlanBean) throws DataAccessException;
    DTOGenerico listarFechaVencimiento() throws DataAccessException;
}

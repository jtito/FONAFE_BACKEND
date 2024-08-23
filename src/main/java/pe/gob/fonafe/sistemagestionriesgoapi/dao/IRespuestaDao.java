package pe.gob.fonafe.sistemagestionriesgoapi.dao;

import org.springframework.dao.DataAccessException;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTORespuesta;
import pe.gob.fonafe.sistemagestionriesgoapi.models.RespuestaBean;

public interface IRespuestaDao {
    DTORespuesta registrarRespuesta(RespuestaBean respuestaBean) throws DataAccessException;
    DTOGenerico listarRespuestas(Long idPregunta) throws DataAccessException;
    DTORespuesta actualizarRespuesta(RespuestaBean respuestaBean) throws DataAccessException;
}

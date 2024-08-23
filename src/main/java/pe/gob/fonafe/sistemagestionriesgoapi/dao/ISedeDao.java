package pe.gob.fonafe.sistemagestionriesgoapi.dao;

import org.springframework.dao.DataAccessException;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOSede;
import pe.gob.fonafe.sistemagestionriesgoapi.models.SedeBean;

public interface ISedeDao {
    DTOGenerico registrarSede(SedeBean sedeBean) throws DataAccessException;
    DTOSede obtenerSede(Long idSede) throws DataAccessException;
    DTOGenerico listarSedes(Long idSede) throws DataAccessException;
    DTOGenerico actualizarSede(SedeBean sedeBean) throws DataAccessException;
    Byte anularSede(SedeBean sedeBean) throws DataAccessException;
}

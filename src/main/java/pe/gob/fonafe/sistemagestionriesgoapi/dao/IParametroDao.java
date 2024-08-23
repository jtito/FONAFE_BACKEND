package pe.gob.fonafe.sistemagestionriesgoapi.dao;

import org.springframework.dao.DataAccessException;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOEmpresa;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOParametro;
import pe.gob.fonafe.sistemagestionriesgoapi.models.EmpresaBean;
import pe.gob.fonafe.sistemagestionriesgoapi.models.ParametroBean;

public interface IParametroDao {

    DTOParametro registrarParametro(ParametroBean parametroBean) throws DataAccessException;
    DTOParametro obtenerParametro(Long idParametro) throws DataAccessException;
    DTOGenerico listarParametro() throws DataAccessException;
    DTOParametro actualizarParametro(ParametroBean parametroBean) throws DataAccessException;
    Byte anularParametro(ParametroBean parametroBean) throws DataAccessException;
}

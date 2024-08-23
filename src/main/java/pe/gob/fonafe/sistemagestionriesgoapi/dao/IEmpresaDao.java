package pe.gob.fonafe.sistemagestionriesgoapi.dao;

import org.springframework.dao.DataAccessException;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOEmpresa;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.models.EmpresaBean;

public interface IEmpresaDao {

    DTOEmpresa registrarEmpresa(EmpresaBean empresaBean) throws DataAccessException;
    DTOEmpresa obtenerEmpresa(Long idEmpresa) throws DataAccessException;
    DTOGenerico listarEmpresas() throws DataAccessException;
    DTOEmpresa actualizarEmpresa(EmpresaBean empresaBean) throws DataAccessException;
    Byte anularEmpresa(EmpresaBean empresaBean) throws DataAccessException;
}

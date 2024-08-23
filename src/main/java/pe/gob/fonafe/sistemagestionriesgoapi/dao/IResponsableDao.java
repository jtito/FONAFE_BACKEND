package pe.gob.fonafe.sistemagestionriesgoapi.dao;

import org.springframework.dao.DataAccessException;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOUsuario;
import pe.gob.fonafe.sistemagestionriesgoapi.models.UsuarioBean;

import java.util.List;

public interface IResponsableDao {

    DTOUsuario registrarResponsable(UsuarioBean usuarioBean) throws DataAccessException;

    DTOUsuario obtenerResponsable(Long idUsuario) throws DataAccessException;

    DTOGenerico listarResponsables(Long idEmpresa, Long idSede) throws DataAccessException;

    DTOUsuario actualizarResponsable(UsuarioBean usuarioBean) throws DataAccessException;

    DTOUsuario actualizarArchivoResponsable(UsuarioBean usuarioBean) throws DataAccessException;

    Byte anularResponsable(UsuarioBean usuarioBean) throws DataAccessException;

    DTOGenerico listarPerfiles() throws DataAccessException;

    DTOUsuario registrarUsuarioProceso(Long idProceso, UsuarioBean usuarioBean,
                                       DTOUsuario dtoUsuario) throws DataAccessException;

    DTOUsuario obtenerUsuarioProceso(Long idUsuario, DTOUsuario dtoUsuario) throws DataAccessException;

    DTOGenerico actualizarPassword(Long idUsuario, String newPassword) throws DataAccessException;
}

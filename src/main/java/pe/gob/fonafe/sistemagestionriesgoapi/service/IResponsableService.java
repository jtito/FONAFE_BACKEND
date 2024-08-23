package pe.gob.fonafe.sistemagestionriesgoapi.service;

import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOUsuario;
import pe.gob.fonafe.sistemagestionriesgoapi.models.UsuarioBean;

import java.util.List;

public interface IResponsableService {
    DTOUsuario registrarResponsable(UsuarioBean usuarioBean);
    DTOUsuario obtenerResponsable(Long idUsuario);
    DTOGenerico listarResponsables(Long idEmpresa, Long idSede);
    DTOUsuario actualizarResponsable(UsuarioBean usuarioBean);
    Byte anularResponsable(UsuarioBean usuarioBean);

    DTOGenerico listarPerfiles();

    DTOGenerico actualizarPassword(Long idUsuario,String actPasswordEncript, String actualPassword,String newPassword);
}

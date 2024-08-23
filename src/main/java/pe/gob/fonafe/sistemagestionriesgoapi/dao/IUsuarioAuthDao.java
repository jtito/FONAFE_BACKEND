package pe.gob.fonafe.sistemagestionriesgoapi.dao;

import pe.gob.fonafe.sistemagestionriesgoapi.dto.*;

import java.util.List;

public interface IUsuarioAuthDao {

    List<DTORoles> obtenerRoles(String username);
    DTOUsuario obtenerUsuario(String username);
    List<DTOMenu> obtenerMenuUsuario(Long idUsuario);
    List<DTOSubmenu> obtenerSubmenuUsuario(Long idUsuario, Integer idOpcion);
    DTOUsuario obtenerDatosAdicionalesUsuario(String username);
    DTOEmpresa obtenerDatosEmpresaUsuario(Long idEmpresa, Long idUsuario);

}

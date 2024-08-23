package pe.gob.fonafe.sistemagestionriesgoapi.security.auth;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;
import pe.gob.fonafe.sistemagestionriesgoapi.dao.ISedeDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dao.IUsuarioAuthDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.*;
import pe.gob.fonafe.sistemagestionriesgoapi.models.UsuarioBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AdicionalInfoToken implements TokenEnhancer {

    private final IUsuarioAuthDao usuarioAuthDao;

    private final ISedeDao iSedeDao;

    public AdicionalInfoToken(IUsuarioAuthDao usuarioAuthDao, ISedeDao iSedeDao) {
        this.usuarioAuthDao = usuarioAuthDao;
        this.iSedeDao = iSedeDao;
    }

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Map<String, Object> info = new HashMap<>();
        DTOUsuario datosUsuario = usuarioAuthDao.obtenerDatosAdicionalesUsuario(authentication.getName());
        List<DTOMenu> listaMenu = usuarioAuthDao.obtenerMenuUsuario(datosUsuario.getIdUsuario());
        DTOEmpresa empresa = usuarioAuthDao.obtenerDatosEmpresaUsuario(datosUsuario.getIdEmpresa(), datosUsuario.getIdUsuario());
        DTOSede sede = iSedeDao.obtenerSede(datosUsuario.getIdSede());
        System.out.println(empresa.toString());
        List<DTOSubmenu> listaSubmenu;
        for (DTOMenu menu : listaMenu) {
            System.out.println(menu.toString());
            listaSubmenu = usuarioAuthDao.obtenerSubmenuUsuario(datosUsuario.getIdUsuario(), menu.getIdMenu());
            menu.setListaSubmenu(listaSubmenu);
        }

        UsuarioBean usuario = new UsuarioBean();
        usuario.setIdUsuario(datosUsuario.getIdUsuario());
        usuario.setUsername(datosUsuario.getUsername());
        usuario.setPassword(datosUsuario.getPassword());
        usuario.setNombre(datosUsuario.getNombre());
        usuario.setApellidoPaterno(datosUsuario.getApellidoPaterno());
        usuario.setApellidoMaterno(datosUsuario.getApellidoMaterno());
        usuario.setEstado(datosUsuario.getEstado());
        usuario.setCorreo(datosUsuario.getCorreo());
        usuario.setListaMenu(listaMenu);
        usuario.setDatosEmpresa(empresa);
        usuario.setDatosSede(sede);
        usuario.setIdPerfil(datosUsuario.getIdPerfil());

        info.put("data", usuario);

        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
        return accessToken;
    }
}

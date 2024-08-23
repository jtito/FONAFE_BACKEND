package pe.gob.fonafe.sistemagestionriesgoapi.security.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import pe.gob.fonafe.sistemagestionriesgoapi.dao.IUsuarioAuthDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTORoles;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOUsuario;
import pe.gob.fonafe.sistemagestionriesgoapi.models.UsuarioBean ;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UsuarioService implements UserDetailsService {

    private final IUsuarioAuthDao usuarioAuthDao;

    public UsuarioService(IUsuarioAuthDao usuarioAuthDao) {
        this.usuarioAuthDao = usuarioAuthDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<DTORoles> listaRoles = usuarioAuthDao.obtenerRoles(username);
        DTOUsuario usuario = usuarioAuthDao.obtenerUsuario(username);
        usuario.setRoles(listaRoles);
        boolean estado = true;
        if (usuario.getEstado().equals("0")) estado = false;
        if(ObjectUtils.isEmpty(usuario)){
            throw new UsernameNotFoundException("Error en el login: no existe el usuario '" + username + "' en el sistema ");
        }

        List<GrantedAuthority> authorities = usuario.getRoles()
                .stream().map(role -> new SimpleGrantedAuthority(role.getRolUsuario()))
                .collect(Collectors.toList());
        return new User(usuario.getUsername(), usuario.getPassword(), estado, true, true, true, authorities);
    }
}

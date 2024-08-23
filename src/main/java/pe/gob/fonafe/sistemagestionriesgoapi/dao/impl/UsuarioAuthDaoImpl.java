package pe.gob.fonafe.sistemagestionriesgoapi.dao.impl;

import oracle.jdbc.OracleTypes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import pe.gob.fonafe.sistemagestionriesgoapi.dao.IUsuarioAuthDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.*;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Repository
@SuppressWarnings("unchecked")
public class UsuarioAuthDaoImpl implements IUsuarioAuthDao {

    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    private final JdbcTemplate jdbcTemplate;

    public UsuarioAuthDaoImpl(@Qualifier("jdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<DTORoles> obtenerRoles(String username) {
        List<DTORoles> listaRoles;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_SEGURIDAD)
                .withProcedureName(SNConstantes.SP_LISTAR_ROLES_USUARIO)
                .declareParameters(new SqlParameter("p_USERNAME", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTORoles.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_USERNAME", username);

        Map<String, Object> out = call.execute(in);
        listaRoles = (List<DTORoles>) out.get(SNConstantes.O_CURSOR);
        return listaRoles;
    }

    @Override
    public DTOUsuario obtenerUsuario(String username) {
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_SEGURIDAD)
                .withProcedureName(SNConstantes.SP_OBTENER_USUARIO)
                .declareParameters(new SqlParameter("p_USERNAME", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTOUsuario.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_USERNAME", username);

        Map<String, Object> out = call.execute(in);
        ArrayList<DTOUsuario> usuarioArray = (ArrayList<DTOUsuario>) out.get(SNConstantes.O_CURSOR);
        Iterator<DTOUsuario> itrUsuario = usuarioArray.iterator();
        DTOUsuario usuario = new DTOUsuario();
        while (itrUsuario.hasNext()) {
            DTOUsuario itrUser = itrUsuario.next();
            usuario.setUsername(itrUser.getUsername());
            usuario.setPassword(itrUser.getPassword());
            usuario.setEstado(itrUser.getEstado());
        }
        return usuario;
    }

    @Override
    public List<DTOMenu> obtenerMenuUsuario(Long idUsuario) {
        List<DTOMenu> listaMenu;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_SEGURIDAD)
                .withProcedureName(SNConstantes.SP_LISTAR_MENU_USUARIO)
                .declareParameters(new SqlParameter("p_ID_USUARIO", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTOMenu.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_USUARIO", idUsuario);
        Map<String, Object> out = call.execute(in);
        listaMenu = (List<DTOMenu>) out.get(SNConstantes.O_CURSOR);
        return listaMenu;
    }

    @Override
    public List<DTOSubmenu> obtenerSubmenuUsuario(Long idUsuario, Integer idOpcion) {
        List<DTOSubmenu> listaSubmenu;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_SEGURIDAD)
                .withProcedureName(SNConstantes.SP_LISTAR_SUBMENU_USUARIO)
                .declareParameters(new SqlParameter("p_ID_USUARIO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_OPCION", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTOSubmenu.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_USUARIO", idUsuario)
                .addValue("p_ID_OPCION", idOpcion);
        Map<String, Object> out = call.execute(in);
        listaSubmenu = (List<DTOSubmenu>) out.get(SNConstantes.O_CURSOR);
        return listaSubmenu;
    }

    @Override
    public DTOUsuario obtenerDatosAdicionalesUsuario(String username) {
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_SEGURIDAD)
                .withProcedureName(SNConstantes.SP_OBTENER_AD_INFO_USUARIO)
                .declareParameters(new SqlParameter("p_USERNAME", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTOUsuario.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_USERNAME", username);

        Map<String, Object> out = call.execute(in);
        ArrayList<DTOUsuario> usuarioArray = (ArrayList<DTOUsuario>) out.get(SNConstantes.O_CURSOR);
        Iterator<DTOUsuario> itrUsuario = usuarioArray.iterator();
        DTOUsuario usuario = new DTOUsuario();
        while (itrUsuario.hasNext()) {
            DTOUsuario itrUser = itrUsuario.next();
            usuario.setIdUsuario(itrUser.getIdUsuario());
            usuario.setUsername(itrUser.getUsername());
            usuario.setPassword(itrUser.getPassword());
            usuario.setEstado(itrUser.getEstado());
            usuario.setNombre(itrUser.getNombre());
            usuario.setApellidoPaterno(itrUser.getApellidoPaterno());
            usuario.setApellidoMaterno(itrUser.getApellidoMaterno());
            usuario.setCorreo(itrUser.getCorreo());
            usuario.setIdEmpresa(itrUser.getIdEmpresa());
            usuario.setIdSede(itrUser.getIdSede());
            usuario.setIdPerfil(itrUser.getIdPerfil());
        }
        return usuario;
    }

    @Override
    public DTOEmpresa obtenerDatosEmpresaUsuario(Long idEmpresa, Long idUsuario) {
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_SEGURIDAD)
                .withProcedureName(SNConstantes.SP_OBTENER_DATOS_EMP_USU)
                .declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_USUARIO", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTOEmpresa.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_EMPRESA", idEmpresa)
                .addValue("p_ID_USUARIO", idUsuario);

        Map<String, Object> out = call.execute(in);
        ArrayList<DTOEmpresa> empresaArray = (ArrayList<DTOEmpresa>) out.get(SNConstantes.O_CURSOR);
        Iterator<DTOEmpresa> itrEmpresa = empresaArray.iterator();
        DTOEmpresa empresa = new DTOEmpresa();
        while (itrEmpresa.hasNext()) {
            DTOEmpresa itrEmpre = itrEmpresa.next();
            empresa.setIdEmpresa(itrEmpre.getIdEmpresa());
            empresa.setNombreCortoEmpresa(itrEmpre.getNombreCortoEmpresa());
            empresa.setIdCartera(itrEmpre.getIdCartera());
            empresa.setDireccion(itrEmpre.getDireccion());
            empresa.setRuc(itrEmpre.getRuc());
        }
        return empresa;
    }
}

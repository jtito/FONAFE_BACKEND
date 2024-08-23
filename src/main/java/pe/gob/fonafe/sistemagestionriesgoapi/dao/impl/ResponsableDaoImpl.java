package pe.gob.fonafe.sistemagestionriesgoapi.dao.impl;

import oracle.jdbc.OracleTypes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import pe.gob.fonafe.sistemagestionriesgoapi.dao.IResponsableDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.*;
import pe.gob.fonafe.sistemagestionriesgoapi.models.UsuarioBean;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import java.math.BigDecimal;
import java.util.*;

@Repository
public class ResponsableDaoImpl implements IResponsableDao {

    private final JdbcTemplate jdbcTemplate;

    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    public ResponsableDaoImpl(@Qualifier("jdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public DTOUsuario registrarResponsable(UsuarioBean usuarioBean) throws DataAccessException {
        logger.info("Inicio de ResponsableDaoImpl - registrarResponsable");

        DTOUsuario dtoUsuario = new DTOUsuario();

        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_SEGURIDAD)
                .withProcedureName(SNConstantes.SP_INSERT_RESPONSABLE)
                .declareParameters(new SqlParameter("p_DE_USUARIO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_CLAVE", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_CARTERA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_SEDE_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_NOMBRE_USUARIO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_APE_PAT_USUARIO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_APE_MAT_USUARIO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DNI_USUARIO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_CORREO_USUARIO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_PRE_TELEFONO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_NU_TELEFONO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ANEXO_USUARIO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_CEL_USUARIO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_CARGO_USUARIO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_RESP_USUARIO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_CORREO_JEFE", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_PERFIL", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_USUA_CREA", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_USUA_CREA_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter("p_ID_USUARIO_OUT", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_DE_USUARIO", usuarioBean.getUsername())
                .addValue("p_DE_CLAVE", usuarioBean.getPassword())
                .addValue("p_ID_CARTERA", usuarioBean.getIdCartera())
                .addValue("p_ID_EMPRESA", usuarioBean.getIdEmpresa())
                .addValue("p_ID_SEDE_EMPRESA", usuarioBean.getIdSede())
                .addValue("p_NOMBRE_USUARIO", usuarioBean.getNombre())
                .addValue("p_APE_PAT_USUARIO", usuarioBean.getApellidoPaterno())
                .addValue("p_APE_MAT_USUARIO", usuarioBean.getApellidoMaterno())
                .addValue("p_DNI_USUARIO", usuarioBean.getDni())
                .addValue("p_CORREO_USUARIO", usuarioBean.getCorreo())
                .addValue("p_PRE_TELEFONO", usuarioBean.getPrefijoTelefono())
                .addValue("p_NU_TELEFONO", usuarioBean.getNumeroTelefono())
                .addValue("p_ANEXO_USUARIO", usuarioBean.getAnexo())
                .addValue("p_CEL_USUARIO", usuarioBean.getCelular())
                .addValue("p_ID_CARGO_USUARIO", usuarioBean.getIdCargo())
                .addValue("p_ID_RESP_USUARIO", usuarioBean.getIdResponsabilidad())
                .addValue("p_CORREO_JEFE", usuarioBean.getCorreoJefe())
                .addValue("p_ID_PERFIL", usuarioBean.getIdPerfil())
                .addValue("p_ID_USUA_CREA", usuarioBean.getUsuarioCreacion())
                .addValue("p_DE_USUA_CREA_IP", usuarioBean.getIpCreacion());

        Map<String, Object> out = call.execute(in);
        BigDecimal idResponsableOut = (BigDecimal) out.get("p_ID_USUARIO_OUT");
        dtoUsuario.setIdUsuario(idResponsableOut.longValue());
        dtoUsuario.setPassword(usuarioBean.getPassword());
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        dtoUsuario.setCodigoResultado(numeroResultado);
        String descripcionResultado = out.get(SNConstantes.DE_ERROR).toString();
        dtoUsuario.setDescripcionResultado(descripcionResultado);

        logger.info("Fin de ResponsableDaoImpl - registrarResponsable");
        return dtoUsuario;
    }

    @Override
    public DTOUsuario obtenerResponsable(Long idUsuario) throws DataAccessException {
        logger.info("Inicio ResponsableDaoImpl - obtenerResponsable");

        DTOUsuario usuario;

        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONSULTAS)
                .withProcedureName(SNConstantes.SP_OBTENER_RESPONSABLE)
                .declareParameters(new SqlParameter("p_ID_USUARIO", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTOUsuario.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_USUARIO", idUsuario);

        Map<String, Object> out = call.execute(in);
        ArrayList<DTOUsuario> responsableArray = (ArrayList<DTOUsuario>) out.get(SNConstantes.O_CURSOR);
        Iterator<DTOUsuario> iteradorResponsable = responsableArray.iterator();

        usuario = new DTOUsuario();

        while (iteradorResponsable.hasNext()) {
            DTOUsuario itrResp = iteradorResponsable.next();
            usuario.setIdUsuario(itrResp.getIdUsuario());
            usuario.setUsername(itrResp.getUsername());
            usuario.setPassword(itrResp.getPassword());
            usuario.setNombre(itrResp.getNombre());
            usuario.setApellidoPaterno(itrResp.getApellidoPaterno());
            usuario.setApellidoMaterno(itrResp.getApellidoMaterno());
            usuario.setDni(itrResp.getDni());
            usuario.setCorreo(itrResp.getCorreo());
            usuario.setPrefijoTelefono(itrResp.getPrefijoTelefono());
            usuario.setNumeroTelefono(itrResp.getNumeroTelefono());
            usuario.setAnexo(itrResp.getAnexo());
            usuario.setCelular(itrResp.getCelular());
            usuario.setIdCartera(itrResp.getIdCartera());
            usuario.setIdEmpresa(itrResp.getIdEmpresa());
            usuario.setIdSede(itrResp.getIdSede());
            usuario.setIdCargo(itrResp.getIdCargo());
            usuario.setIdResponsabilidad(itrResp.getIdResponsabilidad());
            usuario.setCorreoJefe(itrResp.getCorreoJefe());
            usuario.setIdPerfil(itrResp.getIdPerfil());
            usuario.setIndicadorBaja(itrResp.getIndicadorBaja());
            usuario.setFechaCreacion(itrResp.getFechaCreacion());
            if (itrResp.getFileName() != null) {
                List<String> fileNameList = Arrays.asList(itrResp.getFileName().split("\\."));
                DTOFile dtoFile = DTOFile.builder()
                        .status("OK")
                        .idUser(idUsuario)
                        .size("4011")
                        .name(itrResp.getFileName())
                        .type(fileNameList.get(fileNameList.size() - 1))
                        .build();
                usuario.setDtoFile(dtoFile);
            }
        }

        logger.info("Fin ResponsableDaoImpl - obtenerResponsable");
        return usuario;
    }

    @Override
    public DTOGenerico listarResponsables(Long idEmpresa, Long idSede) throws DataAccessException {
        logger.info("Inicio ResponsableDaoImpl - listarResponsables");

        List<DTOUsuario> listaResponsables;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONSULTAS)
                .withProcedureName(SNConstantes.SP_LISTAR_RESPONSABLE)
                .declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_SEDE_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTOUsuario.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_EMPRESA", idEmpresa)
                .addValue("p_ID_SEDE_EMPRESA", idSede);

        Map<String, Object> out = call.execute(in);
        listaResponsables = (List<DTOUsuario>) out.get(SNConstantes.O_CURSOR);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionResultado = (String) out.get(SNConstantes.DE_ERROR);
        logger.info("numero resultado:" + numeroResultado);
        logger.info("descripcion error:" + descripcionResultado);

        DTOGenerico dtoGenerico = new DTOGenerico();
        dtoGenerico.setCodigoResultado(numeroResultado);
        dtoGenerico.setDescripcionResultado(descripcionResultado);
        dtoGenerico.setListado(listaResponsables);

        logger.info("Fin ResponsableDaoImpl - listarResponsables");
        return dtoGenerico;
    }

    @Override
    public DTOUsuario actualizarResponsable(UsuarioBean usuarioBean) throws DataAccessException {
        logger.info("Inicio ResponsableDaoImpl - actualizarResponsable");

        DTOUsuario dtoUsuario;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_SEGURIDAD)
                .withProcedureName(SNConstantes.SP_UPDATE_RESPONSABLE)
                .declareParameters(new SqlParameter("p_ID_USUARIO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_DE_USUARIO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_CLAVE", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_CARTERA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_SEDE_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_NOMBRE_USUARIO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_APE_PAT_USUARIO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_APE_MAT_USUARIO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DNI_USUARIO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_CORREO_USUARIO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_PRE_TELEFONO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_NU_TELEFONO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ANEXO_USUARIO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_CEL_USUARIO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_CARGO_USUARIO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_RESP_USUARIO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_CORREO_JEFE", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_PERFIL", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_IN_BAJA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_USUA_MODI", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_USUA_MODI_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_USUARIO", usuarioBean.getIdUsuario())
                .addValue("p_DE_USUARIO", usuarioBean.getUsername())
                .addValue("p_DE_CLAVE", usuarioBean.getPassword())
                .addValue("p_ID_CARTERA", usuarioBean.getIdCartera())
                .addValue("p_ID_EMPRESA", usuarioBean.getIdEmpresa())
                .addValue("p_ID_SEDE_EMPRESA", usuarioBean.getIdSede())
                .addValue("p_NOMBRE_USUARIO", usuarioBean.getNombre())
                .addValue("p_APE_PAT_USUARIO", usuarioBean.getApellidoPaterno())
                .addValue("p_APE_MAT_USUARIO", usuarioBean.getApellidoMaterno())
                .addValue("p_DNI_USUARIO", usuarioBean.getDni())
                .addValue("p_CORREO_USUARIO", usuarioBean.getCorreo())
                .addValue("p_PRE_TELEFONO", usuarioBean.getPrefijoTelefono())
                .addValue("p_NU_TELEFONO", usuarioBean.getNumeroTelefono())
                .addValue("p_ANEXO_USUARIO", usuarioBean.getAnexo())
                .addValue("p_CEL_USUARIO", usuarioBean.getCelular())
                .addValue("p_ID_CARGO_USUARIO", usuarioBean.getIdCargo())
                .addValue("p_ID_RESP_USUARIO", usuarioBean.getIdResponsabilidad())
                .addValue("p_CORREO_JEFE", usuarioBean.getCorreoJefe())
                .addValue("p_ID_PERFIL", usuarioBean.getIdPerfil())
                .addValue("p_IN_BAJA", usuarioBean.getIndicadorBaja())
                .addValue("p_ID_USUA_MODI", usuarioBean.getUsuarioModificacion())
                .addValue("p_DE_USUA_MODI_IP", usuarioBean.getIpModificacion())
                .addValue("p_DE_USUA_MODI_IP", usuarioBean.getIpModificacion());

        Map<String, Object> out = call.execute(in);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionResultado = out.get(SNConstantes.DE_ERROR).toString();

        dtoUsuario = new DTOUsuario();
        dtoUsuario.setIdUsuario(usuarioBean.getIdUsuario());
        dtoUsuario.setCodigoResultado(numeroResultado);
        dtoUsuario.setDescripcionResultado(descripcionResultado);
        logger.info(" Número de error actualizar Responsable : {}", numeroResultado);

        logger.info("Fin de ResponsableDaoImpl - actualizarResponsable");
        return dtoUsuario;
    }

    @Override
    public DTOUsuario actualizarArchivoResponsable(UsuarioBean usuarioBean) throws DataAccessException {
        logger.info("Inicio ResponsableDaoImpl - actualizarResponsable");

        DTOUsuario dtoUsuario;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_SEGURIDAD)
                .withProcedureName(SNConstantes.SP_UPDATE_FILE_RESPONSABLE)
                .declareParameters(new SqlParameter("p_ID_USER", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_FILE_NAME", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_USER", usuarioBean.getIdUsuario())
                .addValue("p_FILE_NAME", usuarioBean.getFileName());

        Map<String, Object> out = call.execute(in);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionResultado = out.get(SNConstantes.DE_ERROR).toString();

        dtoUsuario = new DTOUsuario();
        dtoUsuario.setIdUsuario(usuarioBean.getIdUsuario());
        dtoUsuario.setCodigoResultado(numeroResultado);
        dtoUsuario.setDescripcionResultado(descripcionResultado);
        logger.info(" Número de error actualizar Responsable : {}", numeroResultado);

        logger.info("Fin de ResponsableDaoImpl - actualizarResponsable");
        return dtoUsuario;
    }

    @Override
    public Byte anularResponsable(UsuarioBean usuarioBean) throws DataAccessException {

        logger.info("Inicio ResponsableDaoImpl - anularResponsable");
        Byte resultadoAnularResponsable;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_SEGURIDAD)
                .withProcedureName(SNConstantes.SP_ANULAR_RESPONSABLE)
                .declareParameters(new SqlParameter("p_ID_USUARIO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_USUA_MODI", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_USUA_MODI_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_USUARIO", usuarioBean.getIdUsuario())
                .addValue("p_ID_USUA_MODI", usuarioBean.getUsuarioModificacion())
                .addValue("p_DE_USUA_MODI_IP", usuarioBean.getIpModificacion());

        Map<String, Object> out = call.execute(in);
        BigDecimal result = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        resultadoAnularResponsable = result.byteValueExact();
        String descripcionError = (String) out.get(SNConstantes.DE_ERROR);
        logger.info("Número de error anular Responsable : {}", result);
        logger.info("Descripcion de error anular Responsable : {}", descripcionError);

        logger.info("Fin ResponsableDaoImpl - anularResponsable");
        return resultadoAnularResponsable;
    }

    @Override
    public DTOGenerico listarPerfiles() throws DataAccessException {
        logger.info("Inicio ResponsableDaoImpl - listarPerfiles");

        List<DTOPerfil> listaPerfiles;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONSULTAS)
                .withProcedureName(SNConstantes.SP_LISTAR_PERFIL)
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTOPerfil.class));

        Map<String, Object> out = call.execute();
        listaPerfiles = (List<DTOPerfil>) out.get(SNConstantes.O_CURSOR);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionResultado = (String) out.get(SNConstantes.DE_ERROR);
        logger.info("numero resultado:" + numeroResultado);
        logger.info("descripcion error:" + descripcionResultado);

        DTOGenerico dtoGenerico = new DTOGenerico();
        dtoGenerico.setCodigoResultado(numeroResultado);
        dtoGenerico.setDescripcionResultado(descripcionResultado);
        dtoGenerico.setListado(listaPerfiles);

        logger.info("Fin ResponsableDaoImpl - listarPerfiles");
        return dtoGenerico;
    }

    @Override
    public DTOUsuario registrarUsuarioProceso(Long idProceso, UsuarioBean usuarioBean, DTOUsuario dtoUsuario) throws DataAccessException {
        logger.info("Inicio de ResponsableDaoImpl - registrarUsuarioProceso");

        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONFIGURACION)
                .withProcedureName(SNConstantes.SP_INSERT_USUARIOPROCESO)
                .declareParameters(new SqlParameter("p_ID_USUARIO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_PROCESO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_USUA_CREA", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_USUA_CREA_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_USUARIO", dtoUsuario.getIdUsuario())
                .addValue("p_ID_PROCESO", idProceso)
                .addValue("p_ID_USUA_CREA", usuarioBean.getUsuarioCreacion())
                .addValue("p_DE_USUA_CREA_IP", usuarioBean.getIpCreacion());

        Map<String, Object> out = call.execute(in);

        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        dtoUsuario.setCodigoResultado(numeroResultado);
        String descripcionResultado = out.get(SNConstantes.DE_ERROR).toString();
        dtoUsuario.setDescripcionResultado(descripcionResultado);

        logger.info("Fin de ResponsableDaoImpl - registrarUsuarioProceso");
        return dtoUsuario;
    }

    @Override
    public DTOUsuario obtenerUsuarioProceso(Long idUsuario, DTOUsuario dtoUsuario) throws DataAccessException {
        logger.info("Inicio ResponsableDaoImpl - obtenerUsuarioProceso");

        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONSULTAS)
                .withProcedureName(SNConstantes.SP_OBTENER_USUARIOPROCESO)
                .declareParameters(new SqlParameter("p_ID_USUARIO", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTOProceso.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_USUARIO", idUsuario);

        Map<String, Object> out = call.execute(in);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionResultado = (String) out.get(SNConstantes.DE_ERROR);
        logger.info("numero resultado:" + numeroResultado);
        logger.info("descripcion error:" + descripcionResultado);

        ArrayList<DTOProceso> procesoArray = (ArrayList<DTOProceso>) out.get(SNConstantes.O_CURSOR);
        Iterator<DTOProceso> iteradorProceso = procesoArray.iterator();

        List<Long> listaProcesoUsuario = new ArrayList<>();

        while (iteradorProceso.hasNext()){
            DTOProceso itrProceso = iteradorProceso.next();
            listaProcesoUsuario.add(itrProceso.getIdProceso());
        }

        dtoUsuario.setProcesos(listaProcesoUsuario);

        logger.info("Fin ResponsableDaoImpl - obtenerUsuarioProceso");
        return dtoUsuario;
    }

    @Override
    public DTOGenerico actualizarPassword(Long idUsuario, String newPassword) throws DataAccessException {
        logger.info("Inicio ResponsableDaoImpl - actualizarPassword");

        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_SEGURIDAD)
                .withProcedureName(SNConstantes.SP_UPDATE_PASSWORD)
                .declareParameters(new SqlParameter("p_ID_USUARIO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_DE_NUEVA_CLAVE", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_USUA_MODI", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_USUA_MODI_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_USUARIO", idUsuario)
                .addValue("p_DE_NUEVA_CLAVE", newPassword);

        Map<String, Object> out = call.execute(in);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionResultado = out.get(SNConstantes.DE_ERROR).toString();
        logger.info("numero resultado:" + numeroResultado);
        logger.info("descripcion error:" + descripcionResultado);

        DTOGenerico dtoGenerico = new DTOGenerico();
        dtoGenerico.setCodigoResultado(numeroResultado);
        dtoGenerico.setDescripcionResultado(descripcionResultado);

        logger.info("Fin de ResponsableDaoImpl - actualizarPassword");
        return dtoGenerico;
    }
}

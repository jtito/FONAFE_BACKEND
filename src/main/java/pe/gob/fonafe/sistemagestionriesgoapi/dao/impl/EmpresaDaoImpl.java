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
import pe.gob.fonafe.sistemagestionriesgoapi.dao.IEmpresaDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOEmpresa;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.models.EmpresaBean;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Repository
public class EmpresaDaoImpl implements IEmpresaDao {

    private final JdbcTemplate jdbcTemplate;

    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    public EmpresaDaoImpl(@Qualifier("jdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public DTOEmpresa registrarEmpresa(EmpresaBean empresaBean) throws DataAccessException {
        logger.info("Inicio de EmpresaDaoImpl - registrarEmpresa");

        DTOEmpresa dtoEmpresa = new DTOEmpresa();

        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONFIGURACION)
                .withProcedureName(SNConstantes.SP_INSERT_EMPRESA)
                .declareParameters(new SqlParameter("p_ID_CARTERA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_DE_RAZONSOCIAL", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_NOMBRE_CORTO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_NU_RUC", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DIRECCION", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_IND_SEDE", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_USUA_CREA", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_USUA_CREA_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter("p_ID_EMPRESA_OUT", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_CARTERA", empresaBean.getIdCartera())
                .addValue("p_DE_RAZONSOCIAL", empresaBean.getRazonSocial())
                .addValue("p_NOMBRE_CORTO", empresaBean.getNombreCortoEmpresa())
                .addValue("p_NU_RUC", empresaBean.getRuc())
                .addValue("p_DIRECCION", empresaBean.getDireccion())
                .addValue("p_IND_SEDE", empresaBean.getIndicadorSede())
                .addValue("p_ID_USUA_CREA", empresaBean.getUsuarioCreacion())
                .addValue("p_DE_USUA_CREA_IP", empresaBean.getIpCreacion());

        Map<String, Object> out = call.execute(in);
        BigDecimal idEmpresaOut = (BigDecimal) out.get("p_ID_EMPRESA_OUT");
        dtoEmpresa.setIdEmpresa(idEmpresaOut.longValue());
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        dtoEmpresa.setCodigoResultado(numeroResultado);
        String descripcionResultado = out.get(SNConstantes.DE_ERROR).toString();
        dtoEmpresa.setDescripcionResultado(descripcionResultado);

        logger.info("Fin de EmpresaDaoImpl - registrarEmpresa");
        return dtoEmpresa;
    }

    @Override
    public DTOEmpresa obtenerEmpresa(Long idEmpresa) throws DataAccessException {
        logger.info("Inicio EmpresaDaoImpl - obtenerEmpresa");

        DTOEmpresa empresa;

        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONSULTAS)
                .withProcedureName(SNConstantes.SP_OBTENER_EMPRESA)
                .declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTOEmpresa.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_EMPRESA", idEmpresa);

        Map<String, Object> out = call.execute(in);
        ArrayList<DTOEmpresa> empresaArray = (ArrayList<DTOEmpresa>) out.get(SNConstantes.O_CURSOR);
        Iterator<DTOEmpresa> iteradorEmpresa = empresaArray.iterator();

        empresa = new DTOEmpresa();

        while (iteradorEmpresa.hasNext()) {
            DTOEmpresa itrEmp = iteradorEmpresa.next();
            empresa.setIdEmpresa(itrEmp.getIdEmpresa());
            empresa.setIdCartera(itrEmp.getIdCartera());
            empresa.setDescripcionCartera(itrEmp.getDescripcionCartera());
            empresa.setNombreCortoEmpresa(itrEmp.getNombreCortoEmpresa());
            empresa.setRazonSocial(itrEmp.getRazonSocial());
            empresa.setRuc(itrEmp.getRuc());
            empresa.setDireccion(itrEmp.getDireccion());
            empresa.setIndicadorSede(itrEmp.getIndicadorSede());
            empresa.setIndicadorBaja(itrEmp.getIndicadorBaja());
            empresa.setFechaCreacion(itrEmp.getFechaCreacion());
        }

        logger.info("Fin de EmpresaDaoImpl - obtenerEmpresa");
        return empresa;
    }

    @Override
    public DTOGenerico listarEmpresas() throws DataAccessException {
        logger.info("Inicio EmpresaDaoImpl - listarEmpresas");

        List<DTOEmpresa> listaEmpresas;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONSULTAS)
                .withProcedureName(SNConstantes.SP_LISTAR_EMPRESA)
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTOEmpresa.class));

        Map<String, Object> out = call.execute();
        listaEmpresas = (List<DTOEmpresa>) out.get(SNConstantes.O_CURSOR);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionResultado = (String) out.get(SNConstantes.DE_ERROR);
        logger.info("numero resultado:" + numeroResultado);
        logger.info("descripcion error:" + descripcionResultado);

        DTOGenerico dtoGenerico = new DTOGenerico();
        dtoGenerico.setCodigoResultado(numeroResultado);
        dtoGenerico.setDescripcionResultado(descripcionResultado);
        dtoGenerico.setListado(listaEmpresas);

        logger.info("Fin EmpresaDaoImpl - listarEmpresas");
        return dtoGenerico;
    }

    @Override
    public DTOEmpresa actualizarEmpresa(EmpresaBean empresaBean) throws DataAccessException {
        logger.info("Inicio EmpresaDaoImpl - actualizarEmpresa");

        DTOEmpresa dtoEmpresa;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONFIGURACION)
                .withProcedureName(SNConstantes.SP_UPDATE_EMPRESA)
                .declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_CARTERA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_DE_RAZONSOCIAL", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_NOMBRE_CORTO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_NU_RUC", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DIRECCION", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_IND_SEDE", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_IN_BAJA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_USUA_MODI", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_USUA_MODI_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_EMPRESA", empresaBean.getIdEmpresa())
                .addValue("p_ID_CARTERA", empresaBean.getIdCartera())
                .addValue("p_DE_RAZONSOCIAL", empresaBean.getRazonSocial())
                .addValue("p_NOMBRE_CORTO", empresaBean.getNombreCortoEmpresa())
                .addValue("p_NU_RUC", empresaBean.getRuc())
                .addValue("p_DIRECCION", empresaBean.getDireccion())
                .addValue("p_IND_SEDE", empresaBean.getIndicadorSede())
                .addValue("p_IN_BAJA", empresaBean.getIndicadorBaja())
                .addValue("p_ID_USUA_MODI", empresaBean.getUsuarioModificacion())
                .addValue("p_DE_USUA_MODI_IP", empresaBean.getIpModificacion());

        Map<String, Object> out = call.execute(in);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionResultado = out.get(SNConstantes.DE_ERROR).toString();

        dtoEmpresa = new DTOEmpresa();
        dtoEmpresa.setIdEmpresa(empresaBean.getIdEmpresa());
        dtoEmpresa.setCodigoResultado(numeroResultado);
        dtoEmpresa.setDescripcionResultado(descripcionResultado);
        logger.info(" Número de error actualizar Empresa : {}", numeroResultado);

        logger.info("Fin de EmpresaDaoImpl - actualizarEmpresa");
        return dtoEmpresa;
    }

    @Override
    public Byte anularEmpresa(EmpresaBean empresaBean) throws DataAccessException {
        logger.info("Inicio EmpresaDaoImpl - anularEmpresa");

        Byte resultadoAnularEmpresa;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONFIGURACION)
                .withProcedureName(SNConstantes.SP_ANULAR_EMPRESA)
                .declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_USUA_MODI", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_USUA_MODI_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_EMPRESA", empresaBean.getIdEmpresa())
                .addValue("p_ID_USUA_MODI", empresaBean.getUsuarioModificacion())
                .addValue("p_DE_USUA_MODI_IP", empresaBean.getIpModificacion());

        Map<String, Object> out = call.execute(in);
        BigDecimal result = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        resultadoAnularEmpresa = result.byteValueExact();
        String descripcionError = (String) out.get(SNConstantes.DE_ERROR);
        logger.info("Número de error anular Empresa : {}", result);
        logger.info("Descripcion de error anular Empresa : {}", descripcionError);

        logger.info("Fin EmpresaDaoImpl - anularEmpresa");
        return resultadoAnularEmpresa;
    }
}

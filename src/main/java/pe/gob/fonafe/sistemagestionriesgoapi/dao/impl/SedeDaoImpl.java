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
import pe.gob.fonafe.sistemagestionriesgoapi.dao.ISedeDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOSede;
import pe.gob.fonafe.sistemagestionriesgoapi.models.SedeBean;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Repository
public class SedeDaoImpl implements ISedeDao {

    private final JdbcTemplate jdbcTemplate;

    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    public SedeDaoImpl(@Qualifier("jdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public DTOGenerico registrarSede(SedeBean sedeBean) throws DataAccessException {
        logger.info("Inicio de SedeDaoImpl - registrarSede");

        DTOGenerico dtoGenerico = new DTOGenerico();

        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONFIGURACION)
                .withProcedureName(SNConstantes.SP_INSERT_SEDE)
                .declareParameters(new SqlParameter("p_DE_SEDE", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_USUA_CREA", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_USUA_CREA_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter("p_ID_SEDE_OUT", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_DE_SEDE", sedeBean.getDescripcionSede())
                .addValue("p_ID_EMPRESA", sedeBean.getIdEmpresa())
                .addValue("p_ID_USUA_CREA", sedeBean.getUsuarioCreacion())
                .addValue("p_DE_USUA_CREA_IP", sedeBean.getIpCreacion());

        Map<String, Object> out = call.execute(in);
        BigDecimal idSedeOut = (BigDecimal) out.get("p_ID_SEDE_OUT");
        dtoGenerico.setIdGenerico(idSedeOut.longValue());
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        dtoGenerico.setCodigoResultado(numeroResultado);
        String descripcionResultado = out.get(SNConstantes.DE_ERROR).toString();
        dtoGenerico.setDescripcionResultado(descripcionResultado);

        logger.info("Fin de SedeDaoImpl - registrarSede");
        return dtoGenerico;
    }

    @Override
    public DTOSede obtenerSede(Long idSede) throws DataAccessException {
        logger.info("Inicio SedeDaoImpl - obtenerSede");

        DTOSede sede;

        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONSULTAS)
                .withProcedureName(SNConstantes.SP_OBTENER_SEDE)
                .declareParameters(new SqlParameter("p_ID_SEDE_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTOSede.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_SEDE_EMPRESA", idSede);

        Map<String, Object> out = call.execute(in);
        ArrayList<DTOSede> sedeArray = (ArrayList<DTOSede>) out.get(SNConstantes.O_CURSOR);
        Iterator<DTOSede> iteradorSede = sedeArray.iterator();

        sede = new DTOSede();

        while (iteradorSede.hasNext()) {
            DTOSede itrSede = iteradorSede.next();
            sede.setIdSede(itrSede.getIdSede());
            sede.setDescripcionSede(itrSede.getDescripcionSede());
            sede.setIdEmpresa(itrSede.getIdEmpresa());
            sede.setIndicadorBaja(itrSede.getIndicadorBaja());
            sede.setFechaCreacion(itrSede.getFechaCreacion());
        }

        logger.info("Fin SedeDaoImpl - obtenerSede");
        return sede;
    }

    @Override
    public DTOGenerico listarSedes(Long idEmpresa) throws DataAccessException {
        logger.info("Inicio SedeDaoImpl - listarSede");

        List<DTOSede> listaSedes;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONSULTAS)
                .withProcedureName(SNConstantes.SP_LISTAR_SEDE)
                .declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTOSede.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_EMPRESA", idEmpresa);

        Map<String, Object> out = call.execute(in);
        listaSedes = (List<DTOSede>) out.get(SNConstantes.O_CURSOR);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionResultado = (String) out.get(SNConstantes.DE_ERROR);
        logger.info("numero resultado:" + numeroResultado);
        logger.info("descripcion error:" + descripcionResultado);

        DTOGenerico dtoGenerico = new DTOGenerico();
        dtoGenerico.setCodigoResultado(numeroResultado);
        dtoGenerico.setDescripcionResultado(descripcionResultado);
        dtoGenerico.setListado(listaSedes);

        logger.info("Fin SedeDaoImpl - listarSedes");
        return dtoGenerico;
    }

    @Override
    public DTOGenerico actualizarSede(SedeBean sedeBean) throws DataAccessException {
        logger.info("Inicio SedeDaoImpl - actualizarSede");

        DTOGenerico dtoSede;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONFIGURACION)
                .withProcedureName(SNConstantes.SP_UPDATE_SEDE)
                .declareParameters(new SqlParameter("p_ID_SEDE", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_DE_SEDE", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_IN_BAJA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_USUA_MODI", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_USUA_MODI_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_SEDE", sedeBean.getIdSede())
                .addValue("p_DE_SEDE", sedeBean.getDescripcionSede())
                .addValue("p_ID_EMPRESA", sedeBean.getIdEmpresa())
                .addValue("p_IN_BAJA", sedeBean.getIndicadorBaja())
                .addValue("p_ID_USUA_MODI", sedeBean.getUsuarioModificacion())
                .addValue("p_DE_USUA_MODI_IP", sedeBean.getIpModificacion());

        Map<String, Object> out = call.execute(in);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionResultado = out.get(SNConstantes.DE_ERROR).toString();
        logger.error(" Error al actualizar en BD : {}", descripcionResultado);

        dtoSede = new DTOGenerico();
        dtoSede.setIdGenerico(sedeBean.getIdSede());
        dtoSede.setCodigoResultado(numeroResultado);
        dtoSede.setDescripcionResultado(descripcionResultado);
        logger.info(" Número de error actualizar Sede : {}", numeroResultado);

        logger.info("Fin de SedeDaoImpl - actualizarSede");
        return dtoSede;
    }

    @Override
    public Byte anularSede(SedeBean sedeBean) throws DataAccessException {
        logger.info("Inicio SedeDaoImpl - anularSede");
        Byte resultadoAnularSede;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONFIGURACION)
                .withProcedureName(SNConstantes.SP_ANULAR_SEDE)
                .declareParameters(new SqlParameter("p_ID_SEDE", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_USUA_MODI", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_USUA_MODI_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_SEDE", sedeBean.getIdSede())
                .addValue("p_ID_USUA_MODI", sedeBean.getUsuarioModificacion())
                .addValue("p_DE_USUA_MODI_IP", sedeBean.getIpModificacion());

        Map<String, Object> out = call.execute(in);
        BigDecimal result = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        resultadoAnularSede = result.byteValueExact();
        String descripcionError = (String) out.get(SNConstantes.DE_ERROR);
        logger.info("Número de error anular Sede : {}", result);
        logger.info("Descripcion de error anular Sede : {}", descripcionError);

        logger.info("Fin SedeDaoImpl - anularSede");
        return resultadoAnularSede;
    }
}

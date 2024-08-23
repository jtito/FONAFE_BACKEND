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
import pe.gob.fonafe.sistemagestionriesgoapi.dao.IParametroDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOParametro;
import pe.gob.fonafe.sistemagestionriesgoapi.models.ParametroBean;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Repository
public class ParametroDaoImpl implements IParametroDao {

    private final JdbcTemplate jdbcTemplate;

    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    public ParametroDaoImpl(@Qualifier("jdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public DTOParametro registrarParametro(ParametroBean parametroBean) throws DataAccessException {
        logger.info("Inicio de ParametroDaoImpl - registrarParametro");

        DTOParametro dtoParametro = new DTOParametro();
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_PARAMETRO)
                .withProcedureName(SNConstantes.SP_INSERT_PARAMETRO)
                .declareParameters(new SqlParameter("p_DE_PARAMETRO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_CO_PARAMETRO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_USUA_CREA", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_USUA_CREA_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter("p_ID_PARAMETRO_OUT", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_DE_PARAMETRO", parametroBean.getDeParametro())
                .addValue("p_CO_PARAMETRO", parametroBean.getCoParametro())
                .addValue("p_ID_USUA_CREA", parametroBean.getUsuarioCreacion())
                .addValue("p_DE_USUA_CREA_IP", parametroBean.getIpCreacion());

        Map<String, Object> out = call.execute(in);
        BigDecimal idParametroOut = (BigDecimal) out.get("p_ID_PARAMETRO_OUT");
        dtoParametro.setIdParametro(idParametroOut.longValue());
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        dtoParametro.setCodigoResultado(numeroResultado);
        String descripcionResultado = out.get(SNConstantes.DE_ERROR).toString();
        dtoParametro.setDescripcionResultado(descripcionResultado);

        logger.info("Fin de ParametroDaoImpl - registrarParametro");

        return dtoParametro;
    }

    @Override
    public DTOParametro obtenerParametro(Long idParametro) throws DataAccessException {
        logger.info("Inicio ParametroDaoImpl - obtenerParametro");

        DTOParametro parametro;

        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_PARAMETRO)
                .withProcedureName(SNConstantes.SP_OBTENER_PARAMETRO)
                .declareParameters(new SqlParameter("p_ID_PARAMETRO", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTOParametro.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_PARAMETRO", idParametro);

        Map<String, Object> out = call.execute(in);
        ArrayList<DTOParametro> parametroArray = (ArrayList<DTOParametro>) out.get(SNConstantes.O_CURSOR);
        Iterator<DTOParametro> iteradorParametro = parametroArray.iterator();

        parametro = new DTOParametro();

        while (iteradorParametro.hasNext()){
            DTOParametro itrParam = iteradorParametro.next();
            parametro.setIdParametro(itrParam.getIdParametro());
            parametro.setDeParametro(itrParam.getDeParametro());
            parametro.setCoParametro(itrParam.getCoParametro());
            parametro.setIndicadorBaja(itrParam.getIndicadorBaja());
            parametro.setFechaCreacion(itrParam.getFechaCreacion());
        }

        logger.info("Fin ParametroDaoImpl - obtenerParametro");
        return parametro;
    }

    @Override
    public DTOGenerico listarParametro() throws DataAccessException {
        logger.info("Inicio ParametroDaoImpl - listarParametro");

        List<DTOParametro> listaParametros;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_PARAMETRO)
                .withProcedureName(SNConstantes.SP_LISTAR_PARAMETRO)
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTOParametro.class));

        Map<String, Object> out = call.execute();
        listaParametros = (List<DTOParametro>) out.get(SNConstantes.O_CURSOR);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionResultado = (String) out.get(SNConstantes.DE_ERROR);
        logger.info("numero resultado:" + numeroResultado);
        logger.info("descripcion error:" + descripcionResultado);

        DTOGenerico dtoGenerico = new DTOGenerico();
        dtoGenerico.setCodigoResultado(numeroResultado);
        dtoGenerico.setDescripcionResultado(descripcionResultado);
        dtoGenerico.setListado(listaParametros);

        logger.info("Fin ParametroDaoImpl - listarParametro");
        return dtoGenerico;
    }

    @Override
    public DTOParametro actualizarParametro(ParametroBean parametroBean) throws DataAccessException {
        logger.info("Inicio ParametroDaoImpl - actualizarParametro");

        DTOParametro dtoParametro;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_PARAMETRO)
                .withProcedureName(SNConstantes.SP_UPDATE_PARAMETRO)
                .declareParameters(new SqlParameter("p_ID_PARAMETRO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_DE_PARAMETRO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_CO_PARAMETRO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_IN_BAJA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_USUA_MODI", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_USUA_MODI_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_PARAMETRO", parametroBean.getIdParametro())
                .addValue("p_DE_PARAMETRO", parametroBean.getDeParametro())
                .addValue("p_CO_PARAMETRO", parametroBean.getCoParametro())
                .addValue("p_IN_BAJA", parametroBean.getIndicadorBaja())
                .addValue("p_ID_USUA_MODI", parametroBean.getUsuarioModificacion())
                .addValue("p_DE_USUA_MODI_IP", parametroBean.getIpModificacion());

        Map<String, Object> out = call.execute(in);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionResultado = out.get(SNConstantes.DE_ERROR).toString();

        dtoParametro = new DTOParametro();
        dtoParametro.setIdParametro(parametroBean.getIdParametro());
        dtoParametro.setCodigoResultado(numeroResultado);
        dtoParametro.setDescripcionResultado(descripcionResultado);
        logger.info(" Número de error actualizar Parametro : {}", numeroResultado);

        logger.info("Fin ParametroDaoImpl - actualizarParametro");
        return dtoParametro;
    }

    @Override
    public Byte anularParametro(ParametroBean parametroBean) throws DataAccessException {

        logger.info("Inicio ParametroDaoImpl - anularParametro");

        Byte resultadoAnularParametro;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_PARAMETRO)
                .withProcedureName(SNConstantes.SP_ANULAR_PARAMETRO)
                .declareParameters(new SqlParameter("p_ID_PARAMETRO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_USUA_MODI", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_USUA_MODI_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_PARAMETRO", parametroBean.getIdParametro())
                .addValue("p_ID_USUA_MODI", parametroBean.getUsuarioModificacion())
                .addValue("p_DE_USUA_MODI_IP", parametroBean.getIpModificacion());

        Map<String, Object> out = call.execute(in);
        BigDecimal result = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        resultadoAnularParametro = result.byteValueExact();
        String descripcionError = (String) out.get(SNConstantes.DE_ERROR);
        logger.info("Número de error anular Parametro : {}", result);
        logger.info("Descripcion de error anular Parametro : {}", descripcionError);

        logger.info("Fin ParametroDaoImpl - anularParametro");
        return resultadoAnularParametro;
    }
}

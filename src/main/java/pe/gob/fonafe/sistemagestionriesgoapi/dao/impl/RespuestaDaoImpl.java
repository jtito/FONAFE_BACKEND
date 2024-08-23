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
import pe.gob.fonafe.sistemagestionriesgoapi.dao.IRespuestaDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTORespuesta;
import pe.gob.fonafe.sistemagestionriesgoapi.models.RespuestaBean;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Repository
public class RespuestaDaoImpl implements IRespuestaDao {

    private final JdbcTemplate jdbcTemplate;

        private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    public RespuestaDaoImpl(@Qualifier("jdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public DTORespuesta registrarRespuesta(RespuestaBean respuestaBean) throws DataAccessException {
        logger.info("Inicio de RespuestaDaoImpl - registrarRespuesta");

        DTORespuesta dtoRespuesta = new DTORespuesta();

        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONFIGURACION)
                .withProcedureName(SNConstantes.SP_INSERT_RESPUESTA)
                .declareParameters(new SqlParameter("p_DE_RESPUESTA", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_IN_ALTERNATIVA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_PREGUNTA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_USUA_CREA", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_USUA_CREA_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter("p_ID_RESPUESTA_OUT", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_DE_RESPUESTA", respuestaBean.getDeRespuesta())
                .addValue("p_IN_ALTERNATIVA", respuestaBean.getIndicadorAlternativa())
                .addValue("p_ID_PREGUNTA", respuestaBean.getIdPregunta())
                .addValue("p_ID_USUA_CREA", respuestaBean.getUsuarioCreacion())
                .addValue("p_DE_USUA_CREA_IP", respuestaBean.getIpCreacion());

        Map<String,Object> out = call.execute(in);
        BigDecimal idRespuestaOut = (BigDecimal)out.get("p_ID_RESPUESTA_OUT");
        dtoRespuesta.setIdRespuesta(idRespuestaOut.longValue());
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        dtoRespuesta.setCodigoResultado(numeroResultado);
        String descripcionResultado = out.get(SNConstantes.DE_ERROR).toString();
        dtoRespuesta.setDescripcionResultado(descripcionResultado);

        logger.info("Fin de RespuestaDaoImpl - registrarRespuesta");
        return dtoRespuesta;
    }

    @Override
    public DTOGenerico listarRespuestas(Long idPregunta) throws DataAccessException {
        logger.info("Inicio RespuestaDaoImpl - listarRespuestas");

        List<DTORespuesta> listaRespuestas;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONSULTAS)
                .withProcedureName(SNConstantes.SP_LISTAR_RESPUESTA)
                .declareParameters(new SqlParameter("p_ID_PREGUNTA", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTORespuesta.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_PREGUNTA", idPregunta);

        Map<String,Object> out = call.execute(in);
        listaRespuestas = (List<DTORespuesta>) out.get(SNConstantes.O_CURSOR);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionResultado = (String) out.get(SNConstantes.DE_ERROR);
        logger.info("numero resultado:" + numeroResultado);
        logger.info("descripcion error:" + descripcionResultado);

        DTOGenerico dtoGenerico = new DTOGenerico();
        dtoGenerico.setCodigoResultado(numeroResultado);
        dtoGenerico.setDescripcionResultado(descripcionResultado);
        dtoGenerico.setListado(listaRespuestas);

        logger.info("Fin RespuestaDaoImpl - listarRespuestas");
        return dtoGenerico;
    }

    @Override
    public DTORespuesta actualizarRespuesta(RespuestaBean respuestaBean) throws DataAccessException {
        logger.info("Inicio RespuestaDaoImpl - actualizarRespuesta");

        DTORespuesta dtoRespuesta;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONFIGURACION)
                .withProcedureName(SNConstantes.SP_UPDATE_RESPUESTA)
                .declareParameters(new SqlParameter("p_ID_RESPUESTA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_DE_RESPUESTA", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_IN_ALTERNATIVA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_PREGUNTA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_IN_BAJA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_USUA_MODI", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_USUA_MODI_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_RESPUESTA", respuestaBean.getIdRespuesta())
                .addValue("p_DE_RESPUESTA", respuestaBean.getDeRespuesta())
                .addValue("p_IN_ALTERNATIVA", respuestaBean.getIndicadorAlternativa())
                .addValue("p_ID_PREGUNTA", respuestaBean.getIdPregunta())
                .addValue("p_IN_BAJA", respuestaBean.getIndicadorBaja())
                .addValue("p_ID_USUA_MODI", respuestaBean.getUsuarioModificacion())
                .addValue("p_DE_USUA_MODI_IP", respuestaBean.getIpModificacion());

        Map<String,Object> out = call.execute(in);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionResultado = out.get(SNConstantes.DE_ERROR).toString();

        dtoRespuesta = new DTORespuesta();
        dtoRespuesta.setIdRespuesta(respuestaBean.getIdRespuesta());
        dtoRespuesta.setCodigoResultado(numeroResultado);
        dtoRespuesta.setDescripcionResultado(descripcionResultado);
        logger.info(" Número de error actualizar Respuesta : {}", numeroResultado);

        logger.info("Fin de RespuestaDaoImpl - actualizarRespuesta");
        return dtoRespuesta;
    }
}

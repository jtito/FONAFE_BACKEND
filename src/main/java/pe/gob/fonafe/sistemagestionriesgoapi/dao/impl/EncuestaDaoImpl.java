/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.fonafe.sistemagestionriesgoapi.dao.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import pe.gob.fonafe.sistemagestionriesgoapi.dao.IEncuestaDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.*;
import pe.gob.fonafe.sistemagestionriesgoapi.models.EncuestaBean;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class EncuestaDaoImpl implements IEncuestaDao {

    private final JdbcTemplate jdbcTemplate;

    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    public EncuestaDaoImpl(@Qualifier("jdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public DTOEncuesta registrarEncuesta(EncuestaBean encuestaBean) throws DataAccessException {

        logger.info("Inicio de EncuestaDaoImpl - registrarEncuesta");

        DTOEncuesta dtoEncuesta = new DTOEncuesta();

        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONFIGURACION)
                .withProcedureName(SNConstantes.SP_INSERT_ENCUESTA)
                .declareParameters(new SqlParameter("p_TITULO_ENCUESTA", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_FE_ENCUESTA", OracleTypes.DATE))
                .declareParameters(new SqlParameter("p_FE_FIN_ENCUESTA", OracleTypes.DATE))
                .declareParameters(new SqlParameter("p_SUBTITULO_ENCUESTA", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_PERIODO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_TEXTO_ENCUESTA", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_CONT_TEXTO_ENCUESTA", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_USUA_CREA", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_USUA_CREA_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter("p_ID_ENCUESTA_OUT", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));

        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaEncuesta = null;
        Date fechaFinEncuesta = null;
        try {
            fechaEncuesta = formatoFecha.parse(encuestaBean.getFeEncuesta());
            fechaFinEncuesta = formatoFecha.parse(encuestaBean.getFeFinEncuesta());
        } catch (ParseException e) {
            fechaEncuesta = null;
            fechaFinEncuesta = null;
        }

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_TITULO_ENCUESTA", encuestaBean.getTituloEncuesta())
                .addValue("p_FE_ENCUESTA", fechaEncuesta)
                .addValue("p_FE_FIN_ENCUESTA", fechaFinEncuesta)
                .addValue("p_SUBTITULO_ENCUESTA", encuestaBean.getSubtituloEncuesta())
                .addValue("p_ID_EMPRESA", encuestaBean.getIdEmpresa())
                .addValue("p_ID_PERIODO", encuestaBean.getIdPeriodo())
                .addValue("p_TEXTO_ENCUESTA", encuestaBean.getTextoEncuesta())
                .addValue("p_CONT_TEXTO_ENCUESTA", encuestaBean.getContTextoEncuesta())
                .addValue("p_ID_USUA_CREA", encuestaBean.getUsuarioCreacion())
                .addValue("p_DE_USUA_CREA_IP", encuestaBean.getIpCreacion());

        Map<String, Object> out = call.execute(in);
        String descripcionResultado = out.get(SNConstantes.DE_ERROR).toString();
        BigDecimal idEncuesta = (BigDecimal) out.get("p_ID_ENCUESTA_OUT");

        dtoEncuesta.setIdEncuesta(idEncuesta.longValue());
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        dtoEncuesta.setCodigoResultado(numeroResultado);
        dtoEncuesta.setDescripcionResultado(descripcionResultado);

        logger.info("Fin de EncuestaDaoImpl - registrarEncuesta");
        return dtoEncuesta;
    }

    @Override
    public DTOEncuesta obtenerEncuesta(Long idEncuesta) throws DataAccessException {

        logger.info("Inicio EncuestaDaoImpl - obtenerEncuesta");

        DTOEncuesta encuesta;

        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONSULTAS)
                .withProcedureName(SNConstantes.SP_OBTENER_ENCUESTA)
                .declareParameters(new SqlParameter("p_ID_ENCUESTA", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTOEncuesta.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_ENCUESTA", idEncuesta);

        Map<String, Object> out = call.execute(in);
        ArrayList<DTOEncuesta> encuestaArray = (ArrayList<DTOEncuesta>) out.get(SNConstantes.O_CURSOR);
        Iterator<DTOEncuesta> iteradorEncuesta = encuestaArray.iterator();

        encuesta = new DTOEncuesta();

        while (iteradorEncuesta.hasNext()) {
            DTOEncuesta itrEnc = iteradorEncuesta.next();
            encuesta.setIdEncuesta(itrEnc.getIdEncuesta());
            encuesta.setContTextoEncuesta(itrEnc.getContTextoEncuesta());
            encuesta.setDescripcionResultado(itrEnc.getDescripcionResultado());
            encuesta.setTextoEncuesta(itrEnc.getTextoEncuesta());
            encuesta.setIdEmpresa(itrEnc.getIdEmpresa());
            encuesta.setIdPeriodo(itrEnc.getIdPeriodo());
            encuesta.setIndicadorBaja(itrEnc.getIndicadorBaja());
            encuesta.setFeEncuesta(itrEnc.getFeEncuesta());
            encuesta.setFeFinEncuesta(itrEnc.getFeFinEncuesta());
            encuesta.setTituloEncuesta(itrEnc.getTituloEncuesta());
            encuesta.setSubtituloEncuesta(itrEnc.getSubtituloEncuesta());

        }

        logger.info("Fin EncuestaDaoImpl - obtenerEncuesta");
        return encuesta;

    }

    @Override
    public DTOGenerico listarEncuestas(Long idEmpresa, Long idPeriodo) throws DataAccessException {

        logger.info("Inicio EncuestaDaoImpl - listarEncuestas");

        List<DTOEncuesta> listaEncuesta;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONSULTAS)
                .withProcedureName(SNConstantes.SP_LISTAR_ENCUESTA)
                .declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_PERIODO", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTOEncuesta.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_EMPRESA", idEmpresa)
                .addValue("p_ID_PERIODO", idPeriodo);

        Map<String, Object> out = call.execute(in);
        listaEncuesta = (List<DTOEncuesta>) out.get(SNConstantes.O_CURSOR);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionResultado = (String) out.get(SNConstantes.DE_ERROR);
        logger.info("numero resultado:" + numeroResultado);
        logger.info("descripcion error:" + descripcionResultado);

        DTOGenerico dtoGenerico = new DTOGenerico();
        dtoGenerico.setCodigoResultado(numeroResultado);
        dtoGenerico.setDescripcionResultado(descripcionResultado);
        dtoGenerico.setListado(listaEncuesta);

        logger.info("Fin EncuestasDaoImpl - listarEncuestas");
        return dtoGenerico;

    }

    @Override
    public DTOEncuesta actualizarEncuesta(EncuestaBean encuestaBean) throws DataAccessException {

        logger.info("Inicio EncuestaDaoImpl - actualizarEncuesta");

        DTOEncuesta dtoEncuesta;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONFIGURACION)
                .withProcedureName(SNConstantes.SP_UPDATE_ENCUESTA)
                .declareParameters(new SqlParameter("p_ID_ENCUESTA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_TITULO_ENCUESTA", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_FE_ENCUESTA", OracleTypes.DATE))
                .declareParameters(new SqlParameter("p_FE_FIN_ENCUESTA", OracleTypes.DATE))
                .declareParameters(new SqlParameter("p_SUBTITULO_ENCUESTA", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_PERIODO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_TEXTO_ENCUESTA", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_CONT_TEXTO_ENCUESTA", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_IN_BAJA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_USUA_MODI", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_USUA_MODI_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));

        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaEncuesta = null;
        Date fechaFinEncuesta = null;
        try {
            fechaEncuesta = formatoFecha.parse(encuestaBean.getFeEncuesta());
            fechaFinEncuesta = formatoFecha.parse(encuestaBean.getFeFinEncuesta());
        } catch (ParseException e) {
            fechaEncuesta = null;
            fechaFinEncuesta = null;
        }

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_ENCUESTA", encuestaBean.getIdEncuesta())
                .addValue("p_TITULO_ENCUESTA", encuestaBean.getTituloEncuesta())
                .addValue("p_FE_ENCUESTA", fechaEncuesta)
                .addValue("p_FE_FIN_ENCUESTA", fechaFinEncuesta)
                .addValue("p_SUBTITULO_ENCUESTA", encuestaBean.getSubtituloEncuesta())
                .addValue("p_ID_EMPRESA", encuestaBean.getIdEmpresa())
                .addValue("p_ID_PERIODO", encuestaBean.getIdPeriodo())
                .addValue("p_TEXTO_ENCUESTA", encuestaBean.getTextoEncuesta())
                .addValue("p_CONT_TEXTO_ENCUESTA", encuestaBean.getContTextoEncuesta())
                .addValue("p_IN_BAJA", encuestaBean.getIndicadorBaja())
                .addValue("p_ID_USUA_MODI", encuestaBean.getUsuarioModificacion())
                .addValue("p_DE_USUA_MODI_IP", encuestaBean.getIpModificacion());

        Map<String, Object> out = call.execute(in);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionResultado = out.get(SNConstantes.DE_ERROR).toString();

        dtoEncuesta = new DTOEncuesta();
        dtoEncuesta.setIdEncuesta(encuestaBean.getIdEncuesta());
        dtoEncuesta.setCodigoResultado(numeroResultado);
        dtoEncuesta.setDescripcionResultado(descripcionResultado);
        logger.info(" Número de error actualizar Encuesta : {}", numeroResultado);

        logger.info("Fin de EncuestaDaoImpl - actualizarEncuesta");
        return dtoEncuesta;


    }

    @Override
    public Byte anularEncuesta(EncuestaBean encuestaBean) throws DataAccessException {

        logger.info("Inicio EncuestaDaoImpl - anularEncuesta");
        Byte resultadoAnularEncuesta;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONFIGURACION)
                .withProcedureName(SNConstantes.SP_ANULAR_ENCUESTA)
                .declareParameters(new SqlParameter("p_ID_ENCUESTA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_USUA_MODI", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_USUA_MODI_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_ENCUESTA", encuestaBean.getIdEncuesta())
                .addValue("p_ID_USUA_MODI", encuestaBean.getUsuarioModificacion())
                .addValue("p_DE_USUA_MODI_IP", encuestaBean.getIpModificacion());

        Map<String, Object> out = call.execute(in);
        BigDecimal result = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        resultadoAnularEncuesta = result.byteValueExact();
        String descripcionError = (String) out.get(SNConstantes.DE_ERROR);
        logger.info("Número de error anular Encuesta : {}", result);
        logger.info("Descripcion de error anular Encuesta : {}", descripcionError);

        logger.info("Fin EncuestaDaoImpl - anularEncuesta");
        return resultadoAnularEncuesta;

    }

    @Override
    public Map<String, List<?>> getReportData(Long idSurvey) {
        Map<String, List<?>> listMap = new HashMap<>(1);

        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONSULTAS)
                .withProcedureName(SNConstantes.SP_OBTENER_REPORTE)
                .declareParameters(new SqlParameter("p_ID_ENCUESTA", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter("oCURSOR1", OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter("oCURSOR2", OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR))
                .returningResultSet("oCURSOR1", BeanPropertyRowMapper.newInstance(DTORespuestaSql.class))
                .returningResultSet("oCURSOR2", BeanPropertyRowMapper.newInstance(DTODetalleRespuesta.class))
                .returningResultSet("oCURSOR3", BeanPropertyRowMapper.newInstance(DTOReportHeaderSql.class));

        SqlParameterSource in = new MapSqlParameterSource().addValue("p_ID_ENCUESTA", idSurvey);

        Map<String, Object> out = call.execute(in);
        BigDecimal result = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String error = (String) out.get(SNConstantes.DE_ERROR);


        List<DTORespuestaSql> userInfo = (List<DTORespuestaSql>) out.get("oCURSOR1");
        List<DTODetalleRespuesta> respInfo = (List<DTODetalleRespuesta>) out.get("oCURSOR2");
        List<DTOReportHeaderSql> respHeader = (List<DTOReportHeaderSql>) out.get("oCURSOR3");
        List<DTODetalleRespuesta> listAnswers = respInfo.stream().map(dtoDetalleRespuesta -> {
            if (dtoDetalleRespuesta.getInAlternativa() != null) {
                if (dtoDetalleRespuesta.getInAlternativa().intValue() == 1) {
                    dtoDetalleRespuesta.setInAlternativa(dtoDetalleRespuesta.getScore());
                }
            }
            return dtoDetalleRespuesta;
        }).collect(Collectors.toList());
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonResultado = gson.toJson(respInfo);
        System.out.println("JSON RESP INFO "+jsonResultado);
        
        System.out.println(userInfo);
        listMap.put("userInfo", userInfo);
        listMap.put("respInfo", listAnswers);
        listMap.put("respHeaders", respHeader);
        return listMap;
    }

    @Override
    public List<DTOPuntaje> listaPuntaje(long idEncuesta) throws DataAccessException {
    
        System.out.println("1401 IDENC "+idEncuesta);
        List<DTOPuntaje> listaPuntaje;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONSULTAS)
                .withProcedureName(SNConstantes.SP_OBTENER_PUNTAJES)
                .declareParameters(new SqlParameter("p_ID_ENCUESTA", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter("oCURSOR1", OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR))
                .returningResultSet("oCURSOR1", BeanPropertyRowMapper.newInstance(DTOPuntaje.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_ENCUESTA", idEncuesta);

        Map<String, Object> out = call.execute(in);
        listaPuntaje = (List<DTOPuntaje>) out.get("oCURSOR1");
        
        return listaPuntaje;
    }

}

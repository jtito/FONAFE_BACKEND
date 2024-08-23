/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import pe.gob.fonafe.sistemagestionriesgoapi.dao.IPreguntaDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOPregunta;
import pe.gob.fonafe.sistemagestionriesgoapi.models.PreguntaBean;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Repository
public class PreguntaDaoImpl implements IPreguntaDao {

    private final JdbcTemplate jdbcTemplate;

    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    public PreguntaDaoImpl(@Qualifier("jdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public DTOPregunta registrarPregunta(PreguntaBean preguntaBean) throws DataAccessException {

        logger.info("Inicio de PreguntaDaoImpl - registrarPregunta");

        DTOPregunta dtoPregunta = new DTOPregunta();


        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONFIGURACION)
                .withProcedureName(SNConstantes.SP_INSERT_PREGUNTA)
                .declareParameters(new SqlParameter("p_DE_PREGUNTA", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_TI_PREGUNTA", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_GRUPO_PREGUNTA", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_ENCUESTA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_PUNTAJE", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_USUA_CREA", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_USUA_CREA_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_NOTA_PREGUNTA", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter("p_ID_PREGUNTA_OUT", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));


        SqlParameterSource in = new MapSqlParameterSource()

                .addValue("p_DE_PREGUNTA", preguntaBean.getDePregunta())
                .addValue("p_TI_PREGUNTA", preguntaBean.getTiPregunta())
                .addValue("p_GRUPO_PREGUNTA", preguntaBean.getGrupoPregunta())
                .addValue("p_PUNTAJE", preguntaBean.getPuntaje())
                .addValue("p_ID_ENCUESTA", preguntaBean.getIdEncuesta())
                .addValue("p_ID_EMPRESA", preguntaBean.getIdEmpresa())
                .addValue("p_ID_USUA_CREA", preguntaBean.getUsuarioCreacion())
                .addValue("p_DE_USUA_CREA_IP", preguntaBean.getIpCreacion())
                .addValue("p_NOTA_PREGUNTA", preguntaBean.getNotaPregunta());

        Map<String, Object> out = call.execute(in);
        String descripcionResultado = out.get(SNConstantes.DE_ERROR).toString();
        logger.info("ERROR REG ENC " + descripcionResultado);
        BigDecimal idPregunta = (BigDecimal) out.get("p_ID_PREGUNTA_OUT");
        logger.info("ERROR REG ENC1 " + idPregunta);
        dtoPregunta.setIdPregunta(idPregunta.longValue());
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        dtoPregunta.setCodigoResultado(numeroResultado);

        dtoPregunta.setDescripcionResultado(descripcionResultado);

        logger.info("Fin de PreguntaDaoImpl - registrarPregunta");
        return dtoPregunta;

    }

    @Override
    public DTOPregunta obtenerPregunta(Long idPregunta) throws DataAccessException {


        logger.info("Inicio PreguntaDaoImpl - obtenerPregunta");

        DTOPregunta pregunta;

        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONSULTAS)
                .withProcedureName(SNConstantes.SP_OBTENER_PREGUNTA)
                .declareParameters(new SqlParameter("p_ID_PREGUNTA", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTOPregunta.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_PREGUNTA", idPregunta);

        Map<String, Object> out = call.execute(in);
        ArrayList<DTOPregunta> preguntaArray = (ArrayList<DTOPregunta>) out.get(SNConstantes.O_CURSOR);
        logger.info("Fin PreguntaDaoImpl - obtenerPregunta " + preguntaArray.size());
        Iterator<DTOPregunta> iteradorPregunta = preguntaArray.iterator();

        pregunta = new DTOPregunta();

        while (iteradorPregunta.hasNext()) {
            DTOPregunta itrEnc = iteradorPregunta.next();
            pregunta.setIdPregunta(itrEnc.getIdPregunta());
            pregunta.setIdEncuesta(itrEnc.getIdEncuesta());
            pregunta.setDePregunta(itrEnc.getDePregunta());
            pregunta.setTiPregunta(itrEnc.getTiPregunta());
            pregunta.setIndicadorBaja(itrEnc.getIndicadorBaja());
            pregunta.setGrupoPregunta(itrEnc.getGrupoPregunta());
            pregunta.setPuntaje(itrEnc.getPuntaje());
            pregunta.setNotaPregunta(itrEnc.getNotaPregunta());

        }

        logger.info("Fin PreguntaDaoImpl - obtenerPregunta");
        return pregunta;

    }

    @Override
    public DTOGenerico listarPreguntas(Long idEncuesta) throws DataAccessException {

        logger.info("Inicio PreguntaDaoImpl - listarPreguntas");

        List<DTOPregunta> listaPreguntas;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONSULTAS)
                .withProcedureName(SNConstantes.SP_LISTAR_PREGUNTA)
                .declareParameters(new SqlParameter("p_ID_ENCUESTA", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTOPregunta.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_ENCUESTA", idEncuesta);

        Map<String, Object> out = call.execute(in);
        listaPreguntas = (List<DTOPregunta>) out.get(SNConstantes.O_CURSOR);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionResultado = (String) out.get(SNConstantes.DE_ERROR);
        logger.info("numero resultado:" + numeroResultado);
        logger.info("descripcion error:" + descripcionResultado);

        DTOGenerico dtoGenerico = new DTOGenerico();
        dtoGenerico.setCodigoResultado(numeroResultado);
        dtoGenerico.setDescripcionResultado(descripcionResultado);
        dtoGenerico.setListado(listaPreguntas);

        logger.info("Fin PreguntasDaoImpl - listarPreguntas");
        return dtoGenerico;
    }

    @Override
    public DTOPregunta actualizarPregunta(PreguntaBean preguntaBean) throws DataAccessException {

        logger.info("Inicio PreguntaDaoImpl - actualizarPregunta");


        DTOPregunta dtoPregunta;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONFIGURACION)
                .withProcedureName(SNConstantes.SP_UPDATE_PREGUNTA)
                .declareParameters(new SqlParameter("p_ID_PREGUNTA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_DE_PREGUNTA", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_TI_PREGUNTA", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_GRUPO_PREGUNTA", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_ENCUESTA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_USUA_MODI", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_USUA_MODI_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("P_IN_BAJA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("P_PUNTAJE", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_NOTA_PREGUNTA", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter("p_ID_ENCUESTA_OUT", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));


        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_PREGUNTA", preguntaBean.getIdPregunta())
                .addValue("p_DE_PREGUNTA", preguntaBean.getDePregunta())
                .addValue("p_TI_PREGUNTA", preguntaBean.getTiPregunta())
                .addValue("p_GRUPO_PREGUNTA", preguntaBean.getGrupoPregunta())
                .addValue("p_ID_ENCUESTA", preguntaBean.getIdEncuesta())
                .addValue("p_ID_EMPRESA", preguntaBean.getIdEmpresa())
                .addValue("p_IN_BAJA", preguntaBean.getIndicadorBaja())
                .addValue("p_PUNTAJE", preguntaBean.getPuntaje())
                .addValue("p_ID_USUA_MODI", preguntaBean.getUsuarioCreacion())
                .addValue("p_DE_USUA_MODI_IP", preguntaBean.getIpCreacion())
                .addValue("p_NOTA_PREGUNTA", preguntaBean.getNotaPregunta());


        Map<String, Object> out = call.execute(in);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionResultado = out.get(SNConstantes.DE_ERROR).toString();

        dtoPregunta = new DTOPregunta();
        dtoPregunta.setIdPregunta(preguntaBean.getIdPregunta());
        dtoPregunta.setCodigoResultado(numeroResultado);
        dtoPregunta.setDescripcionResultado(descripcionResultado);
        logger.info(" Número de error actualizar Encuesta : {}", numeroResultado);

        logger.info("Fin de PreguntaDaoImpl - actualizarPregunta");
        return dtoPregunta;

    }

    @Override
    public Byte anularPregunta(PreguntaBean preguntaBean) throws DataAccessException {

        logger.info("Inicio PreguntaDaoImpl - anularPregunta");
        Byte resultadoAnularPregunta;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONFIGURACION)
                .withProcedureName(SNConstantes.SP_ANULAR_PREGUNTA)
                .declareParameters(new SqlParameter("p_ID_PREGUNTA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_USUA_MODI", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_USUA_MODI_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_PREGUNTA", preguntaBean.getIdPregunta())
                .addValue("p_ID_USUA_MODI", preguntaBean.getUsuarioModificacion())
                .addValue("p_DE_USUA_MODI_IP", preguntaBean.getIpModificacion());

        Map<String, Object> out = call.execute(in);
        BigDecimal result = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        resultadoAnularPregunta = result.byteValueExact();
        String descripcionError = (String) out.get(SNConstantes.DE_ERROR);
        logger.info("Número de error anular Pregunta : {}", result);
        logger.info("Descripcion de error anular Pregunta : {}", descripcionError);

        logger.info("Fin EncuestaDaoImpl - anularEncuesta");
        return resultadoAnularPregunta;
    }


}

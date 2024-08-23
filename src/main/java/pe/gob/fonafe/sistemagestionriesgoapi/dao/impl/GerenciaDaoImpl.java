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
import pe.gob.fonafe.sistemagestionriesgoapi.dao.IGerenciaDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGerencia;
import pe.gob.fonafe.sistemagestionriesgoapi.models.GerenciaBean;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Repository
public class GerenciaDaoImpl implements IGerenciaDao {

    private final JdbcTemplate jdbcTemplate;

    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    public GerenciaDaoImpl(@Qualifier("jdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public DTOGerencia registrarGerencia(GerenciaBean gerenciaBean) throws DataAccessException {
        logger.info("Inicio de GerenciaDaoImpl - registrarGerencia");

        DTOGerencia dtoGerencia = new DTOGerencia();

        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONFIGURACION)
                .withProcedureName(SNConstantes.SP_INSERT_GERENCIA)
                .declareParameters(new SqlParameter("p_DE_GERENCIA", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_SEDE", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_USUA_CREA", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_USUA_CREA_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter("p_ID_GERENCIA_OUT", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_DE_GERENCIA", gerenciaBean.getDescripcionGerencia())
                .addValue("p_ID_EMPRESA", gerenciaBean.getIdEmpresa())
                .addValue("p_ID_SEDE", gerenciaBean.getIdSede())
                .addValue("p_ID_USUA_CREA", gerenciaBean.getUsuarioCreacion())
                .addValue("p_DE_USUA_CREA_IP", gerenciaBean.getIpCreacion());

        Map<String, Object> out = call.execute(in);
        BigDecimal idGerenciaOut = (BigDecimal) out.get("p_ID_GERENCIA_OUT");
        dtoGerencia.setIdGerencia(idGerenciaOut.longValue());
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        dtoGerencia.setCodigoResultado(numeroResultado);
        String descripcionResultado = out.get(SNConstantes.DE_ERROR).toString();
        dtoGerencia.setDescripcionResultado(descripcionResultado);

        logger.info("Fin de GerenciaDaoImpl - registrarGerencia");
        return dtoGerencia;
    }

    @Override
    public DTOGerencia obtenerGerencia(Long idGerencia) throws DataAccessException {
        logger.info("Inicio GerenciaDaoImpl - obtenerGerencia");

        DTOGerencia gerencia;

        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONSULTAS)
                .withProcedureName(SNConstantes.SP_OBTENER_GERENCIA)
                .declareParameters(new SqlParameter("p_ID_GERENCIA", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTOGerencia.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_GERENCIA", idGerencia);

        Map<String, Object> out = call.execute(in);
        ArrayList<DTOGerencia> gerenciaArray = (ArrayList<DTOGerencia>) out.get(SNConstantes.O_CURSOR);
        Iterator<DTOGerencia> iteradorGerencia = gerenciaArray.iterator();

        gerencia = new DTOGerencia();

        while (iteradorGerencia.hasNext()) {
            DTOGerencia itrGer = iteradorGerencia.next();
            gerencia.setIdGerencia(itrGer.getIdGerencia());
            gerencia.setDescripcionGerencia(itrGer.getDescripcionGerencia());
            gerencia.setIdEmpresa(itrGer.getIdEmpresa());
            gerencia.setIdSede(itrGer.getIdSede());
            gerencia.setIndicadorBaja(itrGer.getIndicadorBaja());
            gerencia.setFechaCreacion(itrGer.getFechaCreacion());
        }

        logger.info("Fin GerenciaDaoImpl - obtenerGerencia");
        return gerencia;
    }

    @Override
    public DTOGenerico listarGerencias(Long idEmpresa, Long idSede) throws DataAccessException {
        logger.info("Inicio GerenciaDaoImpl - listarGerencia");

        List<DTOGerencia> listaGerencias;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONSULTAS)
                .withProcedureName(SNConstantes.SP_LISTAR_GERENCIA)
                .declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_SEDE_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTOGerencia.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_EMPRESA", idEmpresa)
                .addValue("p_ID_SEDE_EMPRESA", idSede);

        Map<String, Object> out = call.execute(in);
        listaGerencias = (List<DTOGerencia>) out.get(SNConstantes.O_CURSOR);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionResultado = (String) out.get(SNConstantes.DE_ERROR);
        logger.info("numero resultado:" + numeroResultado);
        logger.info("descripcion error:" + descripcionResultado);

        DTOGenerico dtoGenerico = new DTOGenerico();
        dtoGenerico.setCodigoResultado(numeroResultado);
        dtoGenerico.setDescripcionResultado(descripcionResultado);
        dtoGenerico.setListado(listaGerencias);

        logger.info("Fin GerenciaDaoImpl - listarGerencias");
        return dtoGenerico;
    }

    @Override
    public DTOGerencia actualizarGerencia(GerenciaBean gerenciaBean) throws DataAccessException {
        logger.info("Inicio GerenciaDaoImpl - actualizarGerencia");

        DTOGerencia dtoGerencia;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONFIGURACION)
                .withProcedureName(SNConstantes.SP_UPDATE_GERENCIA)
                .declareParameters(new SqlParameter("p_ID_GERENCIA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_DE_GERENCIA", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_SEDE", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_IN_BAJA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_USUA_MODI", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_USUA_MODI_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_GERENCIA", gerenciaBean.getIdGerencia())
                .addValue("p_DE_GERENCIA", gerenciaBean.getDescripcionGerencia())
                .addValue("p_ID_EMPRESA", gerenciaBean.getIdEmpresa())
                .addValue("p_ID_SEDE", gerenciaBean.getIdSede())
                .addValue("p_IN_BAJA", gerenciaBean.getIndicadorBaja())
                .addValue("p_ID_USUA_MODI", gerenciaBean.getUsuarioModificacion())
                .addValue("p_DE_USUA_MODI_IP", gerenciaBean.getIpModificacion());

        Map<String, Object> out = call.execute(in);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionResultado = out.get(SNConstantes.DE_ERROR).toString();

        dtoGerencia = new DTOGerencia();
        dtoGerencia.setIdGerencia(gerenciaBean.getIdGerencia());
        dtoGerencia.setCodigoResultado(numeroResultado);
        dtoGerencia.setDescripcionResultado(descripcionResultado);
        logger.info(" Número de error actualizar Gerencia : {}", numeroResultado);

        logger.info("Fin de GerenciaDaoImpl - actualizarGerencia");
        return dtoGerencia;
    }

    @Override
    public Byte anularGerencia(GerenciaBean gerenciaBean) throws DataAccessException {
        logger.info("Inicio GerenciaDaoImpl - anularGerencia");
        Byte resultadoAnularGerencia;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONFIGURACION)
                .withProcedureName(SNConstantes.SP_ANULAR_GERENCIA)
                .declareParameters(new SqlParameter("p_ID_GERENCIA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_USUA_MODI", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_USUA_MODI_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_GERENCIA", gerenciaBean.getIdGerencia())
                .addValue("p_ID_USUA_MODI", gerenciaBean.getUsuarioModificacion())
                .addValue("p_DE_USUA_MODI_IP", gerenciaBean.getIpModificacion());

        Map<String, Object> out = call.execute(in);
        BigDecimal result = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        resultadoAnularGerencia = result.byteValueExact();
        String descripcionError = (String) out.get(SNConstantes.DE_ERROR);
        logger.info("Número de error anular Gerencia : {}", result);
        logger.info("Descripcion de error anular Gerencia : {}", descripcionError);

        logger.info("Fin GerenciaDaoImpl - anularGerencia");
        return resultadoAnularGerencia;
    }
}

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
import pe.gob.fonafe.sistemagestionriesgoapi.dao.IProcesoMatrizDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOProcesoMatriz;
import pe.gob.fonafe.sistemagestionriesgoapi.models.ProcesoMatrizBean;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Repository
public class ProcesoMatrizDaoImpl implements IProcesoMatrizDao {

    private final JdbcTemplate jdbcTemplate;

    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    public ProcesoMatrizDaoImpl(@Qualifier("jdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public DTOProcesoMatriz registrarProcesoMatriz(ProcesoMatrizBean procesoMatrizBean) throws DataAccessException {
        logger.info("Inicio de ProcesoMatrizDaoImpl - registrarProcesoMatriz");

        DTOProcesoMatriz dtoProcesoMatriz = new DTOProcesoMatriz();

        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONFIGURACION)
                .withProcedureName(SNConstantes.SP_INSERT_PROCESO_MATRIZ)
                .declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_USUA_CREA", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_USUA_CREA_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter("p_ID_PROC_MATRIZ_OUT", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_EMPRESA", procesoMatrizBean.getIdEmpresa())
                .addValue("p_ID_USUA_CREA", procesoMatrizBean.getUsuarioCreacion())
                .addValue("p_DE_USUA_CREA_IP", procesoMatrizBean.getIpCreacion());

        Map<String, Object> out = call.execute(in);
        BigDecimal idProcesoMatrizOut = (BigDecimal) out.get("p_ID_PROC_MATRIZ_OUT");
        dtoProcesoMatriz.setIdProcesoMatriz(idProcesoMatrizOut.longValue());
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        dtoProcesoMatriz.setCodigoResultado(numeroResultado);
        String descripcionResultado = out.get(SNConstantes.DE_ERROR).toString();
        dtoProcesoMatriz.setDescripcionResultado(descripcionResultado);

        logger.info("Fin de ProcesoMatrizDaoImpl - registrarProcesoMatriz");
        return dtoProcesoMatriz;
    }

    @Override
    public DTOProcesoMatriz obtenerProcesoMatriz(Long idProcesoMatriz) throws DataAccessException {
        logger.info("Inicio ProcesoMatrizDaoImpl - obtenerProcesoMatriz");

        DTOProcesoMatriz procesoMatriz;

        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONSULTAS)
                .withProcedureName(SNConstantes.SP_OBTENER_PROCESO_MATRIZ)
                .declareParameters(new SqlParameter("p_ID_PROC_MATRIZ", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTOProcesoMatriz.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_PROC_MATRIZ", idProcesoMatriz);

        Map<String, Object> out = call.execute(in);
        ArrayList<DTOProcesoMatriz> procesoMatrizArray = (ArrayList<DTOProcesoMatriz>) out.get(SNConstantes.O_CURSOR);
        Iterator<DTOProcesoMatriz> iteradorProcesoMatriz = procesoMatrizArray.iterator();

        procesoMatriz = new DTOProcesoMatriz();

        while (iteradorProcesoMatriz.hasNext()) {
            DTOProcesoMatriz itrProcMatriz = iteradorProcesoMatriz.next();
            procesoMatriz.setIdEmpresa(itrProcMatriz.getIdEmpresa());
            procesoMatriz.setIdProcesoMatriz(itrProcMatriz.getIdProcesoMatriz());
            procesoMatriz.setIndicadorBaja(itrProcMatriz.getIndicadorBaja());
            procesoMatriz.setFechaCreacion(itrProcMatriz.getFechaCreacion());
        }

        logger.info("Fin de ProcesoMatrizDaoImpl - obtenerProcesoMatriz");
        return procesoMatriz;
    }

    @Override
    public DTOGenerico listarProcesoMatriz(Long idEmpresa) throws DataAccessException {
        logger.info("Inicio de ProcesoMatrizDaoImpl - listarProcesoMatriz");

        List<DTOProcesoMatriz> listaProcesoMatriz;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONSULTAS)
                .withProcedureName(SNConstantes.SP_LISTAR_PROCESO_MATRIZ)
                .declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTOProcesoMatriz.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_EMPRESA", idEmpresa);

        Map<String, Object> out = call.execute(in);
        listaProcesoMatriz = (List<DTOProcesoMatriz>) out.get(SNConstantes.O_CURSOR);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionResultado = (String) out.get(SNConstantes.DE_ERROR);
        logger.info("numero resultado:" + numeroResultado);
        logger.info("descripcion error:" + descripcionResultado);

        DTOGenerico dtoGenerico = new DTOGenerico();
        dtoGenerico.setCodigoResultado(numeroResultado);
        dtoGenerico.setDescripcionResultado(descripcionResultado);
        dtoGenerico.setListado(listaProcesoMatriz);

        logger.info("Fin de ProcesoMatrizDaoImpl - listarProcesoMatriz");
        return dtoGenerico;
    }

    @Override
    public DTOProcesoMatriz actualizarProcesoMatriz(ProcesoMatrizBean procesoMatrizBean) throws DataAccessException {
        logger.info("Inicio ProcesoMatrizDaoImpl - actualizarProcesoMatriz");

        DTOProcesoMatriz dtoProcesoMatriz;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONFIGURACION)
                .withProcedureName(SNConstantes.SP_UPDATE_PROCESO_MATRIZ)
                .declareParameters(new SqlParameter("p_ID_PROC_MATRIZ", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_IN_BAJA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_USUA_MODI", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_USUA_MODI_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_PROC_MATRIZ", procesoMatrizBean.getIdProcesoMatriz())
                .addValue("p_ID_EMPRESA", procesoMatrizBean.getIdEmpresa())
                .addValue("p_IN_BAJA", procesoMatrizBean.getIndicadorBaja())
                .addValue("p_ID_USUA_MODI", procesoMatrizBean.getUsuarioModificacion())
                .addValue("p_DE_USUA_MODI_IP", procesoMatrizBean.getIpModificacion());

        Map<String, Object> out = call.execute(in);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionResultado = out.get(SNConstantes.DE_ERROR).toString();

        dtoProcesoMatriz = new DTOProcesoMatriz();
        dtoProcesoMatriz.setIdProcesoMatriz(procesoMatrizBean.getIdProcesoMatriz());
        dtoProcesoMatriz.setCodigoResultado(numeroResultado);
        dtoProcesoMatriz.setDescripcionResultado(descripcionResultado);
        logger.info(" Número de error actualizar Proceso Matriz : {}", numeroResultado);

        logger.info("Fin ProcesoMatrizDaoImpl - actualizarProcesoMatriz");
        return dtoProcesoMatriz;
    }

    @Override
    public Byte anularProcesoMatriz(ProcesoMatrizBean procesoMatrizBean) throws DataAccessException {
        logger.info("Inicio ProcesoMatrizDaoImpl - anularProcesoMatriz");

        Byte resultadoAnularProcesoMatriz;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONFIGURACION)
                .withProcedureName(SNConstantes.SP_ANULAR_PROCESO_MATRIZ)
                .declareParameters(new SqlParameter("p_ID_PROC_MATRIZ", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_USUA_MODI", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_USUA_MODI_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_PROC_MATRIZ", procesoMatrizBean.getIdProcesoMatriz())
                .addValue("p_ID_USUA_MODI", procesoMatrizBean.getUsuarioModificacion())
                .addValue("p_DE_USUA_MODI_IP", procesoMatrizBean.getIpModificacion());

        Map<String, Object> out = call.execute(in);
        BigDecimal result = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        resultadoAnularProcesoMatriz = result.byteValueExact();
        String descripcionError = (String) out.get(SNConstantes.DE_ERROR);
        logger.info("Número de error anular Proceso Matriz : {}", result);
        logger.info("Descripcion de error anular Proceso Matriz : {}", descripcionError);

        logger.info("Fin ProcesoMatrizDaoImpl - anularProcesoMatriz");
        return resultadoAnularProcesoMatriz;
    }
}

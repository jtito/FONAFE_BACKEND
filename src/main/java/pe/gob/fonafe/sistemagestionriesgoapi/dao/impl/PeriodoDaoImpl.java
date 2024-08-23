package pe.gob.fonafe.sistemagestionriesgoapi.dao.impl;

import oracle.jdbc.OracleTypes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import pe.gob.fonafe.sistemagestionriesgoapi.dao.IPeriodoDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOPeriodo;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class PeriodoDaoImpl implements IPeriodoDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    @Override
    public DTOGenerico listarPeriodos(Long empresa, Long anio) throws DataAccessException {
        // TODO Auto-generated method stub
        logger.info("Inicio de PeriodoDaoImpl - listarPeriodos");
        DTOGenerico dTOGenerico = new DTOGenerico();
        List<DTOPeriodo> listDtoPeriodo;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONSULTAS)
                .withProcedureName(SNConstantes.SP_LISTAR_PERIODO)
                .declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ANIO", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter("oCURSOR", OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter("p_NU_RESULT", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter("p_DE_ERROR", OracleTypes.VARCHAR))
                .returningResultSet("oCURSOR", BeanPropertyRowMapper.newInstance(DTOPeriodo.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_EMPRESA", empresa)
                .addValue("p_ANIO", anio);

        Map<String, Object> out = call.execute(in);
        listDtoPeriodo = (List<DTOPeriodo>) out.get("oCURSOR");
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionResultado = (String) out.get(SNConstantes.DE_ERROR);
        dTOGenerico.setCodigoResultado(numeroResultado);
        dTOGenerico.setDescripcionResultado(descripcionResultado);
        dTOGenerico.setListado(listDtoPeriodo);
        logger.info("Fin ListarPeriodo");
        return dTOGenerico;
    }

    @Override
    public DTOGenerico buscarPeriodo(Long p_id_periodo) throws DataAccessException {
        // TODO Auto-generated method stub
        DTOGenerico dTOGenerico = new DTOGenerico();
        DTOPeriodo dtoPeriodo;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONSULTAS)
                .withProcedureName(SNConstantes.SP_OBTENER_PERIODO)
                .declareParameters(new SqlParameter("p_id_periodo", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter("oCURSOR", OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter("p_NU_RESULT", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter("p_DE_ERROR", OracleTypes.VARCHAR))
                .returningResultSet("oCURSOR", BeanPropertyRowMapper.newInstance(DTOPeriodo.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_id_periodo", p_id_periodo);
        Map<String, Object> out = call.execute(in);
        List<DTOPeriodo> listDtoPeriodo = (List<DTOPeriodo>) out.get("oCURSOR");
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionResultado = (String) out.get(SNConstantes.DE_ERROR);

        dTOGenerico.setCodigoResultado(numeroResultado);
        dTOGenerico.setDescripcionResultado(descripcionResultado);
        dTOGenerico.setListado(listDtoPeriodo);
        return dTOGenerico;
    }

    @Override
    public DTOPeriodo registrarPeriodo(DTOPeriodo dTOPeriodo) throws DataAccessException {
        // TODO Auto-generated method stub
        DTOPeriodo dtoPeriodo = new DTOPeriodo();
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONFIGURACION)
                .withProcedureName(SNConstantes.SP_INSERT_PERIODO)
                .declareParameters(new SqlParameter("p_ANIO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_DE_PERIODO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_FE_INI", OracleTypes.DATE))
                .declareParameters(new SqlParameter("p_FE_FIN", OracleTypes.DATE))
                .declareParameters(new SqlParameter("p_FRECUENCIA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_USUA_CREA", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter("p_ID_PERIODO_OUT", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter("p_NU_RESULT", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter("p_DE_ERROR", OracleTypes.VARCHAR));

        SimpleDateFormat parse = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaIni = null;
        Date fechaFin = null;
        try {
            fechaIni = parse.parse(dTOPeriodo.getFeIni());
            fechaFin = parse.parse(dTOPeriodo.getFeFin());
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error de parseo");
        }
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ANIO", dTOPeriodo.getAnio())
                .addValue("p_DE_PERIODO", dTOPeriodo.getDePeriodo())
                .addValue("p_ID_EMPRESA", dTOPeriodo.getIdEmpresa())
                .addValue("p_FE_INI", fechaIni)
                .addValue("p_FE_FIN", fechaFin)
                .addValue("p_FRECUENCIA", dTOPeriodo.getIdFrecuencia())
                .addValue("p_ID_USUA_CREA", dTOPeriodo.getIdUsuaCrea());

        Map<String, Object> out = call.execute(in);
        BigDecimal idPeriodoOut = (BigDecimal) out.get("p_ID_PERIODO_OUT");
        dtoPeriodo.setIdPeriodo(idPeriodoOut.longValue());
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        dtoPeriodo.setCodigoResultado(numeroResultado);
        String descripcionResultado = out.get(SNConstantes.DE_ERROR).toString();
        dtoPeriodo.setDescripcionResultado(descripcionResultado);

        return dtoPeriodo;
    }

    @Override
    public DTOPeriodo actualizarPeriodo(DTOPeriodo dTOPeriodo) throws DataAccessException {
        // TODO Auto-generated method stub
        DTOPeriodo dtoPeriodo = new DTOPeriodo();
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONFIGURACION)
                .withProcedureName(SNConstantes.SP_UPDATE_PERIODO)
                .declareParameters(new SqlParameter("p_ID_PERIODO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_DE_PERIODO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_FE_INI", OracleTypes.DATE))
                .declareParameters(new SqlParameter("p_FE_FIN", OracleTypes.DATE))
                .declareParameters(new SqlParameter("p_FRECUENCIA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ANIO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_IN_BAJA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_USUA_MODI", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_USUA_MODI_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter("p_NU_RESULT", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter("p_DE_ERROR", OracleTypes.VARCHAR));

        SimpleDateFormat parse = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaIni = null;
        Date fechaFin = null;
        try {
            fechaIni = parse.parse(dTOPeriodo.getFeIni());
            fechaFin = parse.parse(dTOPeriodo.getFeFin());
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error de parseo");
        }
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_PERIODO", dTOPeriodo.getIdPeriodo())
                .addValue("p_ID_EMPRESA", dTOPeriodo.getIdEmpresa())
                .addValue("p_DE_PERIODO", dTOPeriodo.getDePeriodo())
                .addValue("p_FE_INI", fechaIni)
                .addValue("p_FE_FIN", fechaFin)
                .addValue("p_FRECUENCIA", dTOPeriodo.getIdFrecuencia())
                .addValue("p_ANIO", dTOPeriodo.getAnio())
                .addValue("p_IN_BAJA", dTOPeriodo.getInBaja())
                .addValue("p_ID_USUA_MODI", dTOPeriodo.getIdUsuaModi())
                .addValue("p_DE_USUA_MODI_IP", dTOPeriodo.getDeUsuaModiIp());

        Map<String, Object> out = call.execute(in);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        dtoPeriodo.setCodigoResultado(numeroResultado);
        String descripcionResultado = out.get(SNConstantes.DE_ERROR).toString();
        dtoPeriodo.setDescripcionResultado(descripcionResultado);
        logger.info("actualizarPeriodo mensaje: ", descripcionResultado);
        logger.info("actualizarPeriodo numeroc: ", numeroResultado);
        return dtoPeriodo;
    }

    @Override
    public DTOPeriodo anularPeriodo(DTOPeriodo dTOPeriodo) throws DataAccessException {
        // TODO Auto-generated method stub
        DTOPeriodo dtoPeriodo = new DTOPeriodo();
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONFIGURACION)
                .withProcedureName(SNConstantes.SP_ANULAR_PERIODO)
                .declareParameters(new SqlParameter("p_ID_PERIODO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_IN_BAJA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_USUA_MODI", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_USUA_MODI_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter("p_NU_RESULT", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter("p_DE_ERROR", OracleTypes.VARCHAR))
                .returningResultSet("oCURSOR", BeanPropertyRowMapper.newInstance(DTOPeriodo.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_PERIODO", dTOPeriodo.getIdPeriodo())
                .addValue("p_IN_BAJA", dTOPeriodo.getInBaja())
                .addValue("p_ID_USUA_MODI", dTOPeriodo.getIdUsuaModi())
                .addValue("p_DE_USUA_MODI_IP", dTOPeriodo.getDeUsuaModiIp());

        Map<String, Object> out = call.execute(in);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        dtoPeriodo.setCodigoResultado(numeroResultado);
        String descripcionResultado = out.get(SNConstantes.DE_ERROR).toString();
        dtoPeriodo.setDescripcionResultado(descripcionResultado);

        return dtoPeriodo;
    }

    @Override
    public DTOGenerico generarDePeriodo(Long idEmpresa, Long anio, Long idFrecuencia) throws DataAccessException {
        logger.info("Inicio de PeriodoDaoImpl - generarDePeriodo");

        DTOGenerico dtoResultado = new DTOGenerico();
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONFIGURACION)
                .withProcedureName(SNConstantes.SP_CODIGO_PERIODO)
                .declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_ANIO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_TIPO_FRECUENCIA", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter("oDE_PERIODO", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter("p_NU_RESULT", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter("p_DE_ERROR", OracleTypes.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_EMPRESA", idEmpresa)
                .addValue("p_ID_ANIO", anio)
                .addValue("p_ID_TIPO_FRECUENCIA", idFrecuencia);

        Map<String, Object> out = call.execute(in);

        String dePeriodo = (String) out.get("oDE_PERIODO");
        BigDecimal onuresult = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String oderesult = (String) out.get(SNConstantes.DE_ERROR);

        logger.info("Fin de PeriodoDaoImpl - generarDePeriodo");

        dtoResultado.setDescripcionPeriodo(dePeriodo);
        dtoResultado.setCodigoResultado(onuresult);
        dtoResultado.setDescripcionResultado(oderesult);

        return dtoResultado;
    }


}

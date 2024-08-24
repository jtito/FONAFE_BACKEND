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
import pe.gob.fonafe.sistemagestionriesgoapi.dao.IMatrizEventoDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.*;
import pe.gob.fonafe.sistemagestionriesgoapi.models.DetalleMatrizEventoBean;
import pe.gob.fonafe.sistemagestionriesgoapi.models.MatrizEventoBean;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import pe.gob.fonafe.sistemagestionriesgoapi.dao.IDetalleGraficoDao;

@Repository
public class DetalleGraficoDaoImpl implements IDetalleGraficoDao {


    private final JdbcTemplate jdbcTemplate;


    private static final Logger logger = LogManager.getLogger("GESTION_EVENTO_API");

    public DetalleGraficoDaoImpl(@Qualifier("jdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<DTODetalleGrafico> listarCantidadRiesgoInhe(int idEmpresa, int idPeriodo, int idMatrizNivel, int idTipoMatriz) throws DataAccessException {
    
        logger.info("Inicio DetalleGraficoDaoImpl - listarCantidadRiesgoInhe idemp ");

        List<DTODetalleGrafico> listadetallegrafico;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_REPORTES)
                .withProcedureName(SNConstantes.SP_REPORTE_MATRIZ_OPER_INHE)
                .declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_PERIODO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_MATRIZ_NIVEL", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_TIPO_MATRIZ", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTODetalleGrafico.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_EMPRESA", idEmpresa)
                .addValue("p_ID_PERIODO", idPeriodo)
                .addValue("p_ID_MATRIZ_NIVEL", idMatrizNivel)
                .addValue("p_ID_TIPO_MATRIZ", idTipoMatriz);

        Map<String, Object> out = call.execute(in);
        listadetallegrafico = (List<DTODetalleGrafico>) out.get(SNConstantes.O_CURSOR);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionError = (String) out.get(SNConstantes.DE_ERROR);
        logger.info("numero resultado: {} ", numeroResultado);
        logger.info("descripcion error: {} ", descripcionError);

        logger.info("Fin DetalleGraficoDaoImpl - listarCantidadRiesgoInhe");
        return listadetallegrafico;
    
    }

    @Override
    public List<DTODetalleGrafico> listarCantidadRiesgoGer(int idEmpresa, int idPeriodo, int idMatrizNivel) throws DataAccessException {
    
        logger.info("Inicio DetalleGraficoDaoImpl - listarCantidadRiesgoGer ");

        List<DTODetalleGrafico> listadetallegrafico;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_REPORTES)
                .withProcedureName(SNConstantes.SP_REPORTE_MATRIZ_OPER_GER)
                .declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_PERIODO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_MATRIZ_NIVEL", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTODetalleGrafico.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_EMPRESA", idEmpresa)
                .addValue("p_ID_PERIODO", idPeriodo)
                .addValue("p_ID_MATRIZ_NIVEL", idMatrizNivel);

        Map<String, Object> out = call.execute(in);
        listadetallegrafico = (List<DTODetalleGrafico>) out.get(SNConstantes.O_CURSOR);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionError = (String) out.get(SNConstantes.DE_ERROR);
        logger.info("numero resultado: {} ", numeroResultado);
        logger.info("descripcion error: {} ", descripcionError);

        logger.info("Fin DetalleGraficoDaoImpl - listarCantidadRiesgoGer");
        return listadetallegrafico;
    }

    @Override
    public DTOGenerico listarCantidadRiesgoKri(int idEmpresa, int idPeriodo, int idMatrizNivel, int idTipoMatriz) throws DataAccessException {
        logger.info("Inicio DetalleGraficoDaoImpl - listarCantidadRiesgoKri ");

        List<DTODetalleGrafico> listadetallegrafico;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_REPORTES)
                .withProcedureName(SNConstantes.SP_REPORTE_MATRIZ_OPER_KRI)
                .declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_PERIODO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_MATRIZ_NIVEL", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_TIPO_MATRIZ", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter("p_CUMPLIMIENTO", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTODetalleGrafico.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_EMPRESA", idEmpresa)
                .addValue("p_ID_PERIODO", idPeriodo)
                .addValue("p_ID_MATRIZ_NIVEL", idMatrizNivel)
                .addValue("p_ID_TIPO_MATRIZ", idTipoMatriz);

        Map<String, Object> out = call.execute(in);
        listadetallegrafico = (List<DTODetalleGrafico>) out.get(SNConstantes.O_CURSOR);
        String porcentajeCumplimiento = (String) out.get("p_CUMPLIMIENTO");
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionError = (String) out.get(SNConstantes.DE_ERROR);

        logger.info("numero resultado: {} ", numeroResultado);
        logger.info("descripcion error: {} ", descripcionError);
        DTOGenerico dtoGenerico = new DTOGenerico();
        dtoGenerico.setCodigoResultado(numeroResultado);
        dtoGenerico.setDescripcionResultado(descripcionError);
        dtoGenerico.setListaDetalleGrafico(listadetallegrafico);
        dtoGenerico.setPorcentajeCumplimiento(porcentajeCumplimiento);

        logger.info("Fin DetalleGraficoDaoImpl - listarCantidadRiesgoKri");
        return dtoGenerico;
    }

    @Override
    public DTOGenerico reporteMatrizEventos(Long idEmpresa, Long idPeriodo) throws DataAccessException {
        logger.info("Inicio DetalleGraficoDaoImpl - reporteMatrizEventos ");

        List<DTODetalleGrafico> listaGraficoEvento;
        List<DTODetalleGrafico> listaGraficoCump;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_REPORTES)
                .withProcedureName(SNConstantes.SP_REPORTE_MATRIZ_EVENTO)
                .declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_PERIODO", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter("p_NU_PERDIDA", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter("p_CUMPLIMIENTO", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR_EVENTO, OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR_CUMP, OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR))
                .returningResultSet(SNConstantes.O_CURSOR_EVENTO, BeanPropertyRowMapper.newInstance(DTODetalleGrafico.class))
                .returningResultSet(SNConstantes.O_CURSOR_CUMP, BeanPropertyRowMapper.newInstance(DTODetalleGrafico.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_EMPRESA", idEmpresa)
                .addValue("p_ID_PERIODO", idPeriodo);

        Map<String, Object> out = call.execute(in);
        listaGraficoEvento = (List<DTODetalleGrafico>) out.get(SNConstantes.O_CURSOR_EVENTO);
        listaGraficoCump = (List<DTODetalleGrafico>) out.get(SNConstantes.O_CURSOR_CUMP);
        BigDecimal nuPerdida = (BigDecimal) out.get("p_NU_PERDIDA");
        String porcentajeCumplimiento = (String) out.get("p_CUMPLIMIENTO");
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionError = (String) out.get(SNConstantes.DE_ERROR);
        logger.info("numero resultado: {} ", numeroResultado);
        logger.info("descripcion error: {} ", descripcionError);

        DTOGenerico dtoGenerico = new DTOGenerico();
        dtoGenerico.setCodigoResultado(numeroResultado);
        dtoGenerico.setDescripcionResultado(descripcionError);
        dtoGenerico.setListaGraficoEvento(listaGraficoEvento);
        dtoGenerico.setListaGraficoCump(listaGraficoCump);
        dtoGenerico.setNuPerdida(nuPerdida);
        dtoGenerico.setPorcentajeCumplimiento(porcentajeCumplimiento);

        logger.info("Fin DetalleGraficoDaoImpl - reporteMatrizEventos");
        return dtoGenerico;
    }
}

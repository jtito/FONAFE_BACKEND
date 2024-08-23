/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.fonafe.sistemagestionriesgoapi.dao.impl;

/**
 *
 * @author CANVIA
 */

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

@Repository
public class MatrizEventoDaoImpl implements IMatrizEventoDao {

    private final JdbcTemplate jdbcTemplate;

    private static final Logger logger = LogManager.getLogger("GESTION_EVENTO_API");

    public MatrizEventoDaoImpl(@Qualifier("jdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<DTOMatrizEvento> listarBandejaMatrizEvento(int idEmpresa, int idPeriodo, int idMatrizNivel) throws DataAccessException {
       
        logger.info("Inicio MatrizEventoDaoImpl - listarBandejaMatrizEvento");

        List<DTOMatrizEvento> listaBandejaMatrizEvento;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_TRANSACCION)
                .withProcedureName(SNConstantes.SP_LISTAR_BANDEJA_MATRIZ_EVENTO)
                .declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_PERIODO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_MATRIZ_NIVEL", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTOMatrizEvento.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_EMPRESA", idEmpresa)
                .addValue("p_ID_PERIODO", idPeriodo)
                .addValue("p_ID_MATRIZ_NIVEL", idMatrizNivel);

        Map<String, Object> out = call.execute(in);
        listaBandejaMatrizEvento = (List<DTOMatrizEvento>) out.get(SNConstantes.O_CURSOR);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionError = (String) out.get(SNConstantes.DE_ERROR);
        logger.info("numero resultado: {} ", numeroResultado);
        logger.info("descripcion error: {} ", descripcionError);

        logger.info("Fin MatrizEventoDaoImpl - listarBandejaMatrizEvento");
        return listaBandejaMatrizEvento;
    
    }

    @Override
    public DTOMatrizEvento registrarMatrizEvento(MatrizEventoBean matrizEventoBean) throws DataAccessException {
       
        logger.info("Inicio de MatrizEventoDaoImpl - registrarMatrizEvento");

        DTOMatrizEvento dtoMatrizEvento = new DTOMatrizEvento();

        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_TRANSACCION)
                .withProcedureName(SNConstantes.SP_INSERT_MATRIZ_EVENTO)
                .declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_SEDE_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_GERENCIA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_PERIODO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_TIPO_MATRIZ", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_MATRIZ_NIVEL", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_MATRIZ_NIVEL", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_USUA_CREA", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_USUA_CREA_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter("p_ID_MATRIZ_RIESGO_OUT", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_EMPRESA", matrizEventoBean.getIdEmpresa())
                .addValue("p_ID_SEDE_EMPRESA", matrizEventoBean.getIdSede())
                .addValue("p_ID_GERENCIA", matrizEventoBean.getIdGerencia())
                .addValue("p_ID_PERIODO", matrizEventoBean.getIdPeriodo())
                .addValue("p_ID_TIPO_MATRIZ", matrizEventoBean.getIdTipoMatriz())
                .addValue("p_ID_MATRIZ_NIVEL", matrizEventoBean.getIdMatrizNivel())
                .addValue("p_MATRIZ_NIVEL", matrizEventoBean.getMatrizNivel())
                .addValue("p_ID_USUA_CREA", matrizEventoBean.getUsuarioCreacion())
                .addValue("p_DE_USUA_CREA_IP", matrizEventoBean.getIpCreacion());

        Map<String, Object> out = call.execute(in);
        BigDecimal idMatrizOut = (BigDecimal) out.get("p_ID_MATRIZ_RIESGO_OUT");
        dtoMatrizEvento.setIdMatrizRiesgo(idMatrizOut.intValue());
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        dtoMatrizEvento.setCodigoResultado(numeroResultado);
        String descripcionResultado = out.get(SNConstantes.DE_ERROR).toString();
        dtoMatrizEvento.setDescripcionResultado(descripcionResultado);

        logger.info("Fin de MatrizEventoDaoImpl - registrarMatrizEvento");
        return dtoMatrizEvento;
    
    }

    @Override
    public DTOGenerico registrarDetalleMatrizEvento(DetalleMatrizEventoBean detalleMatrizEvento) throws DataAccessException {
      
        logger.info("Inicio de MatrizEventoDaoImpl - registrarDetalleMatrizEvento");

        DTOGenerico dtoGenerico = new DTOGenerico();
        
//        SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd");
//        Date fechaOcurrencia = null;
//        Date fechaMaterial = null;
//
//        try {
//            fechaOcurrencia = parse.parse(detalleMatrizEvento.getFeOcurrencia());
//
//        } catch (Exception e) {
//            // TODO: handle exception
//            System.out.println("Error de parseo");
//        }
//
//        try {
//            fechaMaterial = parse.parse(detalleMatrizEvento.getFeMaterial());
//
//        } catch (Exception e) {
//            // TODO: handle exception
//            System.out.println("Error de parseo");
//        }
        


        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_TRANSACCION)
                .withProcedureName(SNConstantes.SP_INSERT_DETALLE_MATRIZEVENTO)
                .declareParameters(new SqlParameter("p_ID_MATRIZ_RIESGO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_SEDE_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_GERENCIA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_PERIODO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_MATRIZ_NIVEL", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_TRIMESTRE", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_TI_EVENTO2", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_TI_EVENTO1", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_PERDIDA_BRUTA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_INTERES", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_IN_PLAN_ACCION", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_IN_BAJA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_IMPORTE_PERDIDA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_USUA_CREA", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_ESTADO_EVENTO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_FE_OCURRENCIA", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_FE_MATERIAL", OracleTypes.VARCHAR))
                
                .declareParameters(new SqlParameter("p_ESSALUD", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_DE_USUA_CREA_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_PLAN_ACCION", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_EVENTO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_CUENTA_CONTABLE", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_COMENTARIO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_COD_EVENTO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_CAUSA_EVENTO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_AREA_EVENTO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ANIO", OracleTypes.NUMBER))


                
                .declareParameters(new SqlOutParameter("p_ID_DETA_MATRIZEVENTO_OUT", OracleTypes.NUMBER))  //110821
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))  //110821
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_MATRIZ_RIESGO", detalleMatrizEvento.getIdMatrizRiesgo())
                .addValue("p_ID_EMPRESA", detalleMatrizEvento.getIdEmpresa())
                .addValue("p_ID_SEDE_EMPRESA", detalleMatrizEvento.getIdSede())
                .addValue("p_ID_GERENCIA", detalleMatrizEvento.getIdGerencia())
                .addValue("p_ID_PERIODO", detalleMatrizEvento.getIdPeriodo())
                .addValue("p_ID_MATRIZ_NIVEL", detalleMatrizEvento.getIdMatrizNivel())
                .addValue("p_TRIMESTRE", detalleMatrizEvento.getTrimestre())
                .addValue("p_TI_EVENTO2", detalleMatrizEvento.getTievento2())
                .addValue("p_TI_EVENTO1", detalleMatrizEvento.getTievento1())
                .addValue("p_PERDIDA_BRUTA", detalleMatrizEvento.getPerdidaBruta())
                
                .addValue("p_INTERES", detalleMatrizEvento.getInteres())
                .addValue("p_IN_PLAN_ACCION", detalleMatrizEvento.getInPlanAccion())
                .addValue("p_IN_BAJA", detalleMatrizEvento.getIndicadorBaja())
                .addValue("p_IMPORTE_PERDIDA", detalleMatrizEvento.getImportePerdida())
                
                .addValue("p_ID_USUA_CREA", detalleMatrizEvento.getUsuarioCreacion())
                .addValue("p_ID_ESTADO_EVENTO", detalleMatrizEvento.getIdEstadoEvento())
                .addValue("p_FE_OCURRENCIA", detalleMatrizEvento.getFeOcurrencia())
                .addValue("p_FE_MATERIAL", detalleMatrizEvento.getFeMaterial())
                
                .addValue("p_ESSALUD", detalleMatrizEvento.getEssalud())
                .addValue("p_DE_USUA_CREA_IP", detalleMatrizEvento.getIpCreacion())
                .addValue("p_DE_PLAN_ACCION", detalleMatrizEvento.getDePlanAccion())
                .addValue("p_DE_EVENTO", detalleMatrizEvento.getDeEvento())
                
                .addValue("p_CUENTA_CONTABLE", detalleMatrizEvento.getCuentaContable())
                .addValue("p_COMENTARIO", detalleMatrizEvento.getComentario())
                .addValue("p_COD_EVENTO", detalleMatrizEvento.getCodEvento())
                .addValue("p_CAUSA_EVENTO", detalleMatrizEvento.getCausaEvento())
                .addValue("p_AREA_EVENTO", detalleMatrizEvento.getAreaEvento())
                .addValue("p_ANIO", detalleMatrizEvento.getAnio());

        
        Map<String, Object> out = call.execute(in);
        BigDecimal idDetaMatrizOut = (BigDecimal) out.get("p_ID_DETA_MATRIZEVENTO_OUT");
        dtoGenerico.setIdGenerico(idDetaMatrizOut.longValue());
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        dtoGenerico.setCodigoResultado(numeroResultado);
        String descripcionResultado = out.get(SNConstantes.DE_ERROR).toString();
        dtoGenerico.setDescripcionResultado(descripcionResultado);

        logger.info("Fin de MatrizRiesgoDaoImpl - registrarDetalleMatrizRiesgo");
        return dtoGenerico;
    }

    @Override
    public DTOMatrizEvento registrarMatrizRiesgoEvento(MatrizEventoBean matrizFraudeBean) throws DataAccessException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DTOMatrizEvento obtenerMatrizEvento(Integer idMatrizEvento) throws DataAccessException {
    
         logger.info("Inicio MatrizEventoDaoImpl - obtenerMatrizEvento");

        DTOMatrizEvento matrizEvento;

        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_TRANSACCION)
                .withProcedureName(SNConstantes.SP_OBTENER_MATRIZ_RIESGO)
                .declareParameters(new SqlParameter("p_ID_MATRIZ_RIESGO", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTOMatrizEvento.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_MATRIZ_RIESGO", idMatrizEvento);

        Map<String, Object> out = call.execute(in);
        ArrayList<DTOMatrizEvento> matrizRiesgoArray = (ArrayList<DTOMatrizEvento>) out.get(SNConstantes.O_CURSOR);
        Iterator<DTOMatrizEvento> iteradorMatrizRiesgo = matrizRiesgoArray.iterator();

        matrizEvento = new DTOMatrizEvento();

        while (iteradorMatrizRiesgo.hasNext()) {
            DTOMatrizEvento itrMatrizEvento = iteradorMatrizRiesgo.next();
            matrizEvento.setIdMatrizRiesgo(itrMatrizEvento.getIdMatrizRiesgo());
            matrizEvento.setIdEmpresa(itrMatrizEvento.getIdEmpresa());
            matrizEvento.setIdSede(itrMatrizEvento.getIdSede());
            matrizEvento.setIdGerencia(itrMatrizEvento.getIdGerencia());
            matrizEvento.setIdPeriodo(itrMatrizEvento.getIdPeriodo());
            matrizEvento.setIdTipoMatriz(itrMatrizEvento.getIdTipoMatriz());
            matrizEvento.setIdMatrizNivel(itrMatrizEvento.getIdMatrizNivel());
            matrizEvento.setIndicadorBaja(itrMatrizEvento.getIndicadorBaja());
            matrizEvento.setFechaCreacion(itrMatrizEvento.getFechaCreacion());
            matrizEvento.setMatrizNivel(itrMatrizEvento.getMatrizNivel());
        }

        logger.info("Fin MatrizEventoDaoImpl - obtenerMatrizEvento");
        return matrizEvento;
    }

    @Override
    public DTOGenerico listarDetalleMatrizEvento(Integer idMatrizEvento, Long idUsuario) throws DataAccessException {
       
        logger.info("Inicio MatrizEventoDaoImpl - listarDetalleMatrizEvento");

        List<DTODetalleMatrizEvento> listaDetaMatrizRiesgo;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_TRANSACCION)
                .withProcedureName(SNConstantes.SP_LISTAR_DETALLE_MATRIZEVENTO)
                .declareParameters(new SqlParameter("p_ID_MATRIZ_RIESGO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_USUARIO", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTODetalleMatrizEvento.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_MATRIZ_RIESGO", idMatrizEvento)
                .addValue("p_ID_USUARIO", idUsuario);

        Map<String, Object> out = call.execute(in);
        listaDetaMatrizRiesgo = (List<DTODetalleMatrizEvento>) out.get(SNConstantes.O_CURSOR);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionResultado = (String) out.get(SNConstantes.DE_ERROR);
        logger.info("numero resultado:" + numeroResultado);
        logger.info("descripcion error:" + descripcionResultado);

        DTOGenerico dtoGenerico = new DTOGenerico();
        dtoGenerico.setCodigoResultado(numeroResultado);
        dtoGenerico.setDescripcionResultado(descripcionResultado);
        dtoGenerico.setListado(listaDetaMatrizRiesgo);

        logger.info("Fin MatrizEventoDaoImpl - listarDetalleMatrizEvento");
        return dtoGenerico;
    
    }

    @Override
    public DTOMatrizEvento actualizarMatrizEvento(MatrizEventoBean matrizEventobean) throws DataAccessException {
     
        logger.info("Inicio MatrizEventoDaoImpl - actualizarMatrizEvento");

        DTOMatrizEvento dtoMatrizEvento;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_TRANSACCION)
                .withProcedureName(SNConstantes.SP_UPDATE_MATRIZ_RIESGO)
                .declareParameters(new SqlParameter("p_ID_MATRIZ_RIESGO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_SEDE_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_GERENCIA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_PERIODO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_TIPO_MATRIZ", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_MATRIZ_NIVEL", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_IN_BAJA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_USUA_MODI", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_USUA_MODI_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_MATRIZ_RIESGO", matrizEventobean.getIdMatrizRiesgo())
                .addValue("p_ID_EMPRESA", matrizEventobean.getIdEmpresa())
                .addValue("p_ID_SEDE_EMPRESA", matrizEventobean.getIdSede())
                .addValue("p_ID_GERENCIA", matrizEventobean.getIdGerencia())
                .addValue("p_ID_PERIODO", matrizEventobean.getIdPeriodo())
                .addValue("p_ID_TIPO_MATRIZ", matrizEventobean.getIdTipoMatriz())
                .addValue("p_ID_MATRIZ_NIVEL", matrizEventobean.getIdMatrizNivel())
                .addValue("p_IN_BAJA", matrizEventobean.getIndicadorBaja())
                .addValue("p_ID_USUA_MODI", matrizEventobean.getUsuarioModificacion())
                .addValue("p_DE_USUA_MODI_IP", matrizEventobean.getIpModificacion());

        Map<String, Object> out = call.execute(in);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionResultado = out.get(SNConstantes.DE_ERROR).toString();

        dtoMatrizEvento = new DTOMatrizEvento();
        dtoMatrizEvento.setIdMatrizRiesgo(matrizEventobean.getIdMatrizRiesgo());
        dtoMatrizEvento.setCodigoResultado(numeroResultado);
        dtoMatrizEvento.setDescripcionResultado(descripcionResultado);
        logger.info(" Número de error actualizar Matriz evento : {}", numeroResultado);

        logger.info("Fin de MatrizEventoDaoImpl - actualizarMatrizEvento");
        return dtoMatrizEvento;
    
    }

    @Override
    public DTODetalleMatrizEvento actualizarDetaMatrizEvento(DetalleMatrizEventoBean detalleMatrizEvento) throws DataAccessException {
        
       logger.info("Inicio MatrizEventoDaoImpl - actualizarDetaMatrizEvento");
        
//        SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd");
//        Date fechaMaterial = null;
//        Date fechaOcurrencia = null;
//
//        try {
//            fechaMaterial = parse.parse(detalleMatrizEvento.getFeMaterial());
//
//        } catch (Exception e) {
//            // TODO: handle exception
//            System.out.println("Error de parseo");
//        }
//
//        try {
//            fechaOcurrencia = parse.parse(detalleMatrizEvento.getFeOcurrencia());
//
//        } catch (Exception e) {
//            // TODO: handle exception
//            System.out.println("Error de parseo");
//        }
//
    

        DTODetalleMatrizEvento dTODetalleMatrizEvento;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_TRANSACCION)
                .withProcedureName(SNConstantes.SP_UPDATE_DETALLE_MATRIZEVENTO)
                .declareParameters(new SqlParameter("P_ID_DETA_MATRIZEVENTO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_MATRIZ_RIESGO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_SEDE_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_GERENCIA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_PERIODO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_MATRIZ_NIVEL", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_TRIMESTRE", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_TI_EVENTO2", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_TI_EVENTO1", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_PERDIDA_BRUTA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_INTERES", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_IN_PLAN_ACCION", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_IN_BAJA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_IMPORTE_PERDIDA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_USUA_CREA", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_ESTADO_EVENTO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_FE_OCURRENCIA", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_FE_MATERIAL", OracleTypes.VARCHAR))
                
                .declareParameters(new SqlParameter("p_ESSALUD", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_DE_USUA_CREA_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_PLAN_ACCION", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_EVENTO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_CUENTA_CONTABLE", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_COMENTARIO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_COD_EVENTO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_CAUSA_EVENTO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_AREA_EVENTO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ANIO", OracleTypes.NUMBER))


                
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))  //110821
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("P_ID_DETA_MATRIZEVENTO", detalleMatrizEvento.getIdDetaMatrizEvento())
                .addValue("p_ID_MATRIZ_RIESGO", detalleMatrizEvento.getIdMatrizRiesgo())
                .addValue("p_ID_EMPRESA", detalleMatrizEvento.getIdEmpresa())
                .addValue("p_ID_SEDE_EMPRESA", detalleMatrizEvento.getIdSede())
                .addValue("p_ID_GERENCIA", detalleMatrizEvento.getIdGerencia())
                .addValue("p_ID_PERIODO", detalleMatrizEvento.getIdPeriodo())
                .addValue("p_ID_MATRIZ_NIVEL", detalleMatrizEvento.getIdMatrizNivel())
                .addValue("p_TRIMESTRE", detalleMatrizEvento.getTrimestre())
                .addValue("p_TI_EVENTO2", detalleMatrizEvento.getTievento2())
                .addValue("p_TI_EVENTO1", detalleMatrizEvento.getTievento1())
                .addValue("p_PERDIDA_BRUTA", detalleMatrizEvento.getPerdidaBruta())
                
                .addValue("p_INTERES", detalleMatrizEvento.getInteres())
                .addValue("p_IN_PLAN_ACCION", detalleMatrizEvento.getInPlanAccion())
                .addValue("p_IN_BAJA", detalleMatrizEvento.getIndicadorBaja())
                .addValue("p_IMPORTE_PERDIDA", detalleMatrizEvento.getImportePerdida())
                
                .addValue("p_ID_USUA_CREA", detalleMatrizEvento.getUsuarioCreacion())
                .addValue("p_ID_ESTADO_EVENTO", detalleMatrizEvento.getIdEstadoEvento())
                .addValue("p_FE_OCURRENCIA", detalleMatrizEvento.getFeOcurrencia())
                .addValue("p_FE_MATERIAL", detalleMatrizEvento.getFeMaterial())
                
                .addValue("p_ESSALUD", detalleMatrizEvento.getEssalud())
                .addValue("p_DE_USUA_CREA_IP", detalleMatrizEvento.getIpCreacion())
                .addValue("p_DE_PLAN_ACCION", detalleMatrizEvento.getDePlanAccion())
                .addValue("p_DE_EVENTO", detalleMatrizEvento.getDeEvento())
                
                .addValue("p_CUENTA_CONTABLE", detalleMatrizEvento.getCuentaContable())
                .addValue("p_COMENTARIO", detalleMatrizEvento.getComentario())
                .addValue("p_COD_EVENTO", detalleMatrizEvento.getCodEvento())
                .addValue("p_CAUSA_EVENTO", detalleMatrizEvento.getCausaEvento())
                .addValue("p_AREA_EVENTO", detalleMatrizEvento.getAreaEvento())
                .addValue("p_ANIO", detalleMatrizEvento.getAnio())

                
                .addValue("p_ID_USUA_MODI", detalleMatrizEvento.getUsuarioModificacion())
                .addValue("p_DE_USUA_MODI_IP", detalleMatrizEvento.getIpModificacion());

        Map<String, Object> out = call.execute(in);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionResultado = out.get(SNConstantes.DE_ERROR).toString();

        dTODetalleMatrizEvento = new DTODetalleMatrizEvento();
        dTODetalleMatrizEvento.setIdDetaMatrizEvento(detalleMatrizEvento.getIdDetaMatrizEvento());
        dTODetalleMatrizEvento.setCodigoResultado(numeroResultado);
        dTODetalleMatrizEvento.setDescripcionResultado(descripcionResultado);
        logger.info(" Número de error actualizar detalle de matriz EVENTO : {}", descripcionResultado);

        logger.info("Fin de MatrizEventoDaoImpl - actualizarDetaMatrizEvento");
        return dTODetalleMatrizEvento;
    }

    @Override
    public Byte anularMatrizEvento(MatrizEventoBean matrizEventoBean) throws DataAccessException {
      
       logger.info("Inicio MatrizEventoDaoImpl - anularMatrizEvento");

        Byte resultadoAnularMatriz;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_TRANSACCION)
                .withProcedureName(SNConstantes.SP_ANULAR_MATRIZ_EVENTO)
                .declareParameters(new SqlParameter("p_ID_MATRIZ_RIESGO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_USUA_MODI", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_USUA_MODI_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_MATRIZ_RIESGO", matrizEventoBean.getIdMatrizRiesgo())
                .addValue("p_ID_USUA_MODI", matrizEventoBean.getUsuarioModificacion())
                .addValue("p_DE_USUA_MODI_IP", matrizEventoBean.getIpModificacion());

        Map<String, Object> out = call.execute(in);
        BigDecimal result = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        resultadoAnularMatriz = result.byteValueExact();
        String descripcionError = (String) out.get(SNConstantes.DE_ERROR);
        logger.info("Número de error anular Matriz de Evento : {}", result);
        logger.info("Descripcion de error anular Matriz de Evento : {}", descripcionError);

        logger.info("Fin MatrizEventoDaoImpl - anularMatrizEvento");
        return resultadoAnularMatriz;
    
    }
}

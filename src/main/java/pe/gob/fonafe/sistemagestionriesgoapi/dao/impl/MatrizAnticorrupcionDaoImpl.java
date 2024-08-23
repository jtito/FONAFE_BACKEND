/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.fonafe.sistemagestionriesgoapi.dao.impl;

/**
 *
 * @author J. Lorayco
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
import pe.gob.fonafe.sistemagestionriesgoapi.dao.IMatrizAnticorrupcionDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTODetalleMatrizAnticorrupcion;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOMatrizAnticorrupcion;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOMatrizRiesgo;
import pe.gob.fonafe.sistemagestionriesgoapi.models.DetalleMatrizAnticorrupcionBean;
import pe.gob.fonafe.sistemagestionriesgoapi.models.MatrizAnticorrupcionBean;

@Repository
public class MatrizAnticorrupcionDaoImpl implements IMatrizAnticorrupcionDao{

    private final JdbcTemplate jdbcTemplate;

    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    public MatrizAnticorrupcionDaoImpl(@Qualifier("jdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @Override
    public List<DTOMatrizAnticorrupcion> listarBandejaMatrizAntic(int idEmpresa, int idPeriodo, int idMatrizNivel) throws DataAccessException {
   logger.info("Inicio MatrizAnticDaoImpl - listarBandejaMatrizAntic");

        List<DTOMatrizAnticorrupcion> listaBandejaMatrizAntic;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_TRANSACCION)
                .withProcedureName(SNConstantes.SP_LISTAR_BNDJA_MATRIZ_ANTIC)
                .declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_PERIODO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_MATRIZ_NIVEL", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTOMatrizAnticorrupcion.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_EMPRESA", idEmpresa)
                .addValue("p_ID_PERIODO", idPeriodo)
                .addValue("p_ID_MATRIZ_NIVEL", idMatrizNivel);

        Map<String, Object> out = call.execute(in);
        listaBandejaMatrizAntic = (List<DTOMatrizAnticorrupcion>) out.get(SNConstantes.O_CURSOR);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionError = (String) out.get(SNConstantes.DE_ERROR);
        logger.info("numero resultado: {} ", numeroResultado);
        logger.info("descripcion error: {} ", descripcionError);

        logger.info("Fin MatrizAnticDaoImpl - listarBandejaMatrizAntic");
        return listaBandejaMatrizAntic;
    
    }

    @Override
    public DTOMatrizAnticorrupcion registrarMatrizRiesgo(MatrizAnticorrupcionBean matrizRiesgoBean) throws DataAccessException {
   
        logger.info("Inicio de MatrizAnticDaoImpl - registrarMatrizAntic");

        DTOMatrizAnticorrupcion dtoMatrizAntic = new DTOMatrizAnticorrupcion();

        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_TRANSACCION)
                .withProcedureName(SNConstantes.SP_INSERT_MATRIZ_RIESGO)
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
                .addValue("p_ID_EMPRESA", matrizRiesgoBean.getIdEmpresa())
                .addValue("p_ID_SEDE_EMPRESA", matrizRiesgoBean.getIdSede())
                .addValue("p_ID_GERENCIA", matrizRiesgoBean.getIdGerencia())
                .addValue("p_ID_PERIODO", matrizRiesgoBean.getIdPeriodo())
                .addValue("p_ID_TIPO_MATRIZ", matrizRiesgoBean.getIdTipoMatriz())
                .addValue("p_ID_MATRIZ_NIVEL", matrizRiesgoBean.getIdMatrizNivel())
                .addValue("p_MATRIZ_NIVEL", matrizRiesgoBean.getMatrizNivel())
                .addValue("p_ID_USUA_CREA", matrizRiesgoBean.getUsuarioCreacion())
                .addValue("p_DE_USUA_CREA_IP", matrizRiesgoBean.getIpCreacion());

        Map<String, Object> out = call.execute(in);
        BigDecimal idMatrizOut = (BigDecimal) out.get("p_ID_MATRIZ_RIESGO_OUT");
        dtoMatrizAntic.setIdMatrizRiesgo(idMatrizOut.intValue());
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        dtoMatrizAntic.setCodigoResultado(numeroResultado);
        String descripcionResultado = out.get(SNConstantes.DE_ERROR).toString();
        dtoMatrizAntic.setDescripcionResultado(descripcionResultado);

        logger.info("Fin de MatrizAnticDaoImpl - registrarMatrizAntic id "+idMatrizOut);
        return dtoMatrizAntic;
    
    }

    @Override
    public DTOGenerico registrarDetalleMatrizAnticorrupcion(DetalleMatrizAnticorrupcionBean detalleMatrizAnticBean) throws DataAccessException {
   
        logger.info("Inicio de MatrizAnticDaoImpl - registrarDetalleMatrizAntic");

        DTOGenerico dtoGenerico = new DTOGenerico();

        SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaFinPlanAccion = null;

        try {
            fechaFinPlanAccion = parse.parse(detalleMatrizAnticBean.getFeFinPlanAccion());

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error de parseo");
        }


        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_TRANSACCION)
                .withProcedureName(SNConstantes.SP_INSERT_DETALLE_MATRIZANTICORRUPCION)
                .declareParameters(new SqlParameter("p_ID_MATRIZ_RIESGO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_SEDE_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_GERENCIA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_PERIODO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_COD_MATRIZ", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_MATRIZ_NIVEL", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_PROCESO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_SUBPROCESO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_DE_CARGO_REL_RIESGO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_RIESGO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_SOCIO_REL_RIESGO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_ACCION", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_TIPORIESGO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_NU_IMPA_INHE", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_NU_PUNTA_INHE", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_DE_SEVERIDAD_INHE", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_CONTROL", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_NU_FRECU_INHE", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_NU_FRECU_RES", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_NU_PUNTA_RES", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_NU_IMPA_RES", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_DE_SEVERIDAD_RES", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_USUA_CREA", OracleTypes.VARCHAR))
                
                .declareParameters(new SqlParameter("p_DE_USUA_CREA_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_COD_RIESGO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_COD_CONTROL", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_ESTRATEGIA_RESP", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_COD_PLAN_ACCION", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_PLAN_ACCION", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_EFICACIA_CONTROL", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_RESP_PLAN_ACCION", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_ESTADO_PLAN_ACCION", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_FE_FIN_PLAN_ACCION", OracleTypes.DATE))
                .declareParameters(new SqlParameter("p_IN_EFICAZ", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_FE_VERIFICACION", OracleTypes.DATE))
                .declareParameters(new SqlParameter("p_ID_TIPODELITO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_IN_DEBIDA_DILIG", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_IND_FIN_PLAN_ACCION", OracleTypes.NUMBER))		
                .declareParameters(new SqlOutParameter("p_ID_DETA_MATRIZANTICORRUP_OUT", OracleTypes.NUMBER))  //110821
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))  //110821
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_MATRIZ_RIESGO", detalleMatrizAnticBean.getIdMatrizRiesgo())
                .addValue("p_ID_EMPRESA", detalleMatrizAnticBean.getIdEmpresa())
                .addValue("p_ID_SEDE_EMPRESA", detalleMatrizAnticBean.getIdSede())
                .addValue("p_ID_GERENCIA", detalleMatrizAnticBean.getIdGerencia())
                .addValue("p_ID_PERIODO", detalleMatrizAnticBean.getIdPeriodo())
                .addValue("p_COD_MATRIZ", detalleMatrizAnticBean.getCodMatriz())
                .addValue("p_ID_MATRIZ_NIVEL", detalleMatrizAnticBean.getIdMatrizNivel())
                .addValue("p_ID_PROCESO", detalleMatrizAnticBean.getIdProceso())
                .addValue("p_ID_SUBPROCESO", detalleMatrizAnticBean.getIdSubProceso())
                .addValue("p_DE_CARGO_REL_RIESGO", detalleMatrizAnticBean.getDecargoRiesgo())
                .addValue("p_DE_RIESGO", detalleMatrizAnticBean.getDeRiesgo())
                .addValue("p_DE_SOCIO_REL_RIESGO", detalleMatrizAnticBean.getDeSociodelRiesgo())
                .addValue("p_DE_ACCION", detalleMatrizAnticBean.getDeAccion())
                .addValue("p_ID_TIPORIESGO", detalleMatrizAnticBean.getIdTipoRiesgo())
                .addValue("p_NU_IMPA_INHE", detalleMatrizAnticBean.getNuImpaInhe())
                .addValue("p_NU_PUNTA_INHE", detalleMatrizAnticBean.getNuPuntaInhe())
                .addValue("p_DE_SEVERIDAD_INHE", detalleMatrizAnticBean.getDePuntaInhe())
                .addValue("p_DE_CONTROL", detalleMatrizAnticBean.getDeControl())
                
                .addValue("p_NU_FRECU_INHE", detalleMatrizAnticBean.getNuFrecuInhe())
                .addValue("p_NU_FRECU_RES", detalleMatrizAnticBean.getNuFrecuRes())
                .addValue("p_NU_PUNTA_RES", detalleMatrizAnticBean.getNuPuntaRes())
                .addValue("p_NU_IMPA_RES", detalleMatrizAnticBean.getNuImpaRes())
                .addValue("p_DE_SEVERIDAD_RES", detalleMatrizAnticBean.getDeSeveridadRes())
                .addValue("p_ID_USUA_CREA", detalleMatrizAnticBean.getUsuarioCreacion())
                .addValue("p_DE_USUA_CREA_IP", detalleMatrizAnticBean.getIpCreacion())
                
                .addValue("p_COD_RIESGO", detalleMatrizAnticBean.getCodRiesgo())
                .addValue("p_COD_CONTROL", detalleMatrizAnticBean.getCodControl())
                .addValue("p_ID_ESTRATEGIA_RESP", detalleMatrizAnticBean.getIdRespEstrategia())
                .addValue("p_COD_PLAN_ACCION", detalleMatrizAnticBean.getCodPlanAccion())
                
                .addValue("p_DE_PLAN_ACCION", detalleMatrizAnticBean.getDePlanAccion())
                .addValue("p_ID_EFICACIA_CONTROL", detalleMatrizAnticBean.getIdEficaciaControl())
                .addValue("p_ID_RESP_PLAN_ACCION", detalleMatrizAnticBean.getIdRespPlanAccion())
                .addValue("p_ID_ESTADO_PLAN_ACCION", detalleMatrizAnticBean.getIdEstadoPlanAccion())
                .addValue("p_FE_FIN_PLAN_ACCION", fechaFinPlanAccion)
                .addValue("p_IN_EFICAZ", detalleMatrizAnticBean.getInFicaz())
                .addValue("p_FE_VERIFICACION", fechaFinPlanAccion)
                .addValue("p_ID_TIPODELITO", detalleMatrizAnticBean.getIdTipoDelito())
                .addValue("p_IN_DEBIDA_DILIG", detalleMatrizAnticBean.getIdDebidaDilig())
                .addValue("p_IND_FIN_PLAN_ACCION", detalleMatrizAnticBean.getIndFinPlanAccion());

        logger.info("Inicio de MatrizAnticDaoImpl - registrarDetalleMatrizAntic 2");

        Map<String, Object> out = call.execute(in);
        logger.info("Fin de MatrizRiesgoDaoImpl - registrarDetalleMatrizRiesgo id ");
        BigDecimal idDetaMatrizOut = (BigDecimal) out.get("p_ID_DETA_MATRIZANTICORRUP_OUT");
        logger.info("Fin de MatrizRiesgoDaoImpl - registrarDetalleMatrizRiesgo idout "+idDetaMatrizOut);
        dtoGenerico.setIdGenerico(idDetaMatrizOut.longValue());
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        dtoGenerico.setCodigoResultado(numeroResultado);
        String descripcionResultado = out.get(SNConstantes.DE_ERROR).toString();
        dtoGenerico.setDescripcionResultado(descripcionResultado);

        logger.info("Fin de MatrizRiesgoDaoImpl - registrarDetalleMatrizRiesgo");
        return dtoGenerico;
    
    }

    @Override
    public DTOMatrizAnticorrupcion obtenerMatrizAntic(Integer idMatrizRiesgo) throws DataAccessException {
        
       logger.info("Inicio MatrizAnticDaoImpl - obtenerMatrizAntic");

        DTOMatrizAnticorrupcion matrizRiesgo;

        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_TRANSACCION)
                .withProcedureName(SNConstantes.SP_OBTENER_MATRIZ_RIESGO)
                .declareParameters(new SqlParameter("p_ID_MATRIZ_RIESGO", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTOMatrizAnticorrupcion.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_MATRIZ_RIESGO", idMatrizRiesgo);

        Map<String, Object> out = call.execute(in);
        ArrayList<DTOMatrizAnticorrupcion> matrizRiesgoArray = (ArrayList<DTOMatrizAnticorrupcion>) out.get(SNConstantes.O_CURSOR);
        Iterator<DTOMatrizAnticorrupcion> iteradorMatrizRiesgo = matrizRiesgoArray.iterator();

        matrizRiesgo = new DTOMatrizAnticorrupcion();

        while (iteradorMatrizRiesgo.hasNext()) {
            DTOMatrizAnticorrupcion itrMatrizRiesgo = iteradorMatrizRiesgo.next();
            matrizRiesgo.setIdMatrizRiesgo(itrMatrizRiesgo.getIdMatrizRiesgo());
            matrizRiesgo.setIdEmpresa(itrMatrizRiesgo.getIdEmpresa());
            matrizRiesgo.setIdSede(itrMatrizRiesgo.getIdSede());
            matrizRiesgo.setIdGerencia(itrMatrizRiesgo.getIdGerencia());
            matrizRiesgo.setIdPeriodo(itrMatrizRiesgo.getIdPeriodo());
            matrizRiesgo.setIdTipoMatriz(itrMatrizRiesgo.getIdTipoMatriz());
            matrizRiesgo.setIdMatrizNivel(itrMatrizRiesgo.getIdMatrizNivel());
            matrizRiesgo.setIndicadorBaja(itrMatrizRiesgo.getIndicadorBaja());
            matrizRiesgo.setFechaCreacion(itrMatrizRiesgo.getFechaCreacion());
            matrizRiesgo.setMatrizNivel(itrMatrizRiesgo.getMatrizNivel());
        }

        logger.info("Fin MatrizRiesgoDaoImpl - obtenerMatrizRiesgo");
        return matrizRiesgo;
    }

    @Override
    public DTOGenerico listarDetalleMatrizAnticorrupcion(Integer idMatrizRiesgo, Long idUsuario) throws DataAccessException {
   
       logger.info("Inicio MatrizRiesgoDaoImpl - listarDetalleMatrizRiesgo");

        List<DTODetalleMatrizAnticorrupcion> listaDetaMatrizRiesgo;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_TRANSACCION)
                .withProcedureName(SNConstantes.SP_LISTAR_DETALLE_MATRIZANTIC)
                .declareParameters(new SqlParameter("p_ID_MATRIZ_RIESGO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_USUARIO", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTODetalleMatrizAnticorrupcion.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_MATRIZ_RIESGO", idMatrizRiesgo)
                .addValue("p_ID_USUARIO", idUsuario);

        Map<String, Object> out = call.execute(in);
        listaDetaMatrizRiesgo = (List<DTODetalleMatrizAnticorrupcion>) out.get(SNConstantes.O_CURSOR);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionResultado = (String) out.get(SNConstantes.DE_ERROR);
        logger.info("numero resultado:" + numeroResultado);
        logger.info("descripcion error:" + descripcionResultado);

        DTOGenerico dtoGenerico = new DTOGenerico();
        dtoGenerico.setCodigoResultado(numeroResultado);
        dtoGenerico.setDescripcionResultado(descripcionResultado);
        dtoGenerico.setListado(listaDetaMatrizRiesgo);

        logger.info("Fin MatrizRiesgoDaoImpl - listarDetalleMatrizRiesgo");
        return dtoGenerico;
    
    }

    @Override
    public DTOMatrizAnticorrupcion actualizarMatrizRiesgo(MatrizAnticorrupcionBean matrizRiesgoBean) throws DataAccessException {
      
         logger.info("Inicio MatrizRiesgoDaoImpl - actualizarMatrizRiesgo");

        DTOMatrizAnticorrupcion dtoMatrizAntic;
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
                .addValue("p_ID_MATRIZ_RIESGO", matrizRiesgoBean.getIdMatrizRiesgo())
                .addValue("p_ID_EMPRESA", matrizRiesgoBean.getIdEmpresa())
                .addValue("p_ID_SEDE_EMPRESA", matrizRiesgoBean.getIdSede())
                .addValue("p_ID_GERENCIA", matrizRiesgoBean.getIdGerencia())
                .addValue("p_ID_PERIODO", matrizRiesgoBean.getIdPeriodo())
                .addValue("p_ID_TIPO_MATRIZ", matrizRiesgoBean.getIdTipoMatriz())
                .addValue("p_ID_MATRIZ_NIVEL", matrizRiesgoBean.getIdMatrizNivel())
                .addValue("p_IN_BAJA", matrizRiesgoBean.getIndicadorBaja())
                .addValue("p_ID_USUA_MODI", matrizRiesgoBean.getUsuarioModificacion())
                .addValue("p_DE_USUA_MODI_IP", matrizRiesgoBean.getIpModificacion());

        Map<String, Object> out = call.execute(in);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionResultado = out.get(SNConstantes.DE_ERROR).toString();

        dtoMatrizAntic = new DTOMatrizAnticorrupcion();
        dtoMatrizAntic.setIdMatrizRiesgo(matrizRiesgoBean.getIdMatrizRiesgo());
        dtoMatrizAntic.setCodigoResultado(numeroResultado);
        dtoMatrizAntic.setDescripcionResultado(descripcionResultado);
        logger.info(" Número de error actualizar Matriz riesgo : {}", numeroResultado);

        logger.info("Fin de MatrizAnticDaoImpl - actualizarMatrizAntic");
        return dtoMatrizAntic;
    
    }

    @Override
    public DTODetalleMatrizAnticorrupcion actualizarDetaMatrizAnticorrupcion(DetalleMatrizAnticorrupcionBean DetalleMatrizAnticorrupcionBean) throws DataAccessException {
      
        logger.info("Inicio MatrizRiesgoDaoImpl - actualizarDetaMatrizRiesgo");
        
        SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaFinPlanAccion = null;

        try {
            fechaFinPlanAccion = parse.parse(DetalleMatrizAnticorrupcionBean.getFeFinPlanAccion());
            
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error de parseo");
        }

        DTODetalleMatrizAnticorrupcion dtoDetalleMatrizRiesgo;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_TRANSACCION)
                .withProcedureName(SNConstantes.SP_UPDATE_DETALLE_MATRIZANTIC)
                .declareParameters(new SqlParameter("P_ID_DETA_MATRIZANTICORRUP", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_MATRIZ_RIESGO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_SEDE_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_GERENCIA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_PERIODO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_COD_MATRIZ", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_MATRIZ_NIVEL", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_PROCESO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_SUBPROCESO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_DE_CARGO_REL_RIESGO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_RIESGO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_SOCIO_REL_RIESGO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_ACCION", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_TIPORIESGO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_NU_IMPA_INHE", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_NU_PUNTA_INHE", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_DE_SEVERIDAD_INHE", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_CONTROL", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_NU_FRECU_INHE", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_NU_FRECU_RES", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_NU_PUNTA_RES", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_DE_SEVERIDAD_RES", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_USUA_CREA", OracleTypes.VARCHAR))
                
                .declareParameters(new SqlParameter("p_DE_USUA_CREA_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_COD_RIESGO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_COD_CONTROL", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_ESTRATEGIA_RESP", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_COD_PLAN_ACCION", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_PLAN_ACCION", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_EFICACIA_CONTROL", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_RESP_PLAN_ACCION", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_ESTADO_PLAN_ACCION", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_FE_FIN_PLAN_ACCION", OracleTypes.DATE))
                .declareParameters(new SqlParameter("p_IN_EFICAZ", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_FE_VERIFICACION", OracleTypes.DATE))
                .declareParameters(new SqlParameter("p_ID_TIPODELITO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_IN_DEBIDA_DILIG", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_IN_BAJA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_IND_FIN_PLAN_ACCION", OracleTypes.NUMBER))		
                .declareParameters(new SqlParameter("p_ID_USUA_MODI", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_USUA_MODI_IP", OracleTypes.VARCHAR)) //110821
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))  //110821
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_DETA_MATRIZANTICORRUP", DetalleMatrizAnticorrupcionBean.getIdDetaMatrizAntic())
                .addValue("p_ID_MATRIZ_RIESGO", DetalleMatrizAnticorrupcionBean.getIdMatrizRiesgo())
                .addValue("p_ID_EMPRESA", DetalleMatrizAnticorrupcionBean.getIdEmpresa())
                .addValue("p_ID_SEDE_EMPRESA", DetalleMatrizAnticorrupcionBean.getIdSede())
                .addValue("p_ID_GERENCIA", DetalleMatrizAnticorrupcionBean.getIdGerencia())
                .addValue("p_ID_PERIODO", DetalleMatrizAnticorrupcionBean.getIdPeriodo())
                .addValue("p_COD_MATRIZ", DetalleMatrizAnticorrupcionBean.getCodMatriz())
                .addValue("p_ID_MATRIZ_NIVEL", DetalleMatrizAnticorrupcionBean.getIdMatrizNivel())
                .addValue("p_ID_PROCESO", DetalleMatrizAnticorrupcionBean.getIdProceso())
                .addValue("p_ID_SUBPROCESO", DetalleMatrizAnticorrupcionBean.getIdSubProceso())
                .addValue("p_DE_CARGO_REL_RIESGO", DetalleMatrizAnticorrupcionBean.getDecargoRiesgo())
                .addValue("p_DE_RIESGO", DetalleMatrizAnticorrupcionBean.getDeRiesgo())
                .addValue("p_DE_SOCIO_REL_RIESGO", DetalleMatrizAnticorrupcionBean.getDeSociodelRiesgo())
                .addValue("p_DE_ACCION", DetalleMatrizAnticorrupcionBean.getDeAccion())
                .addValue("p_ID_TIPORIESGO", DetalleMatrizAnticorrupcionBean.getIdTipoRiesgo())
                .addValue("p_NU_IMPA_INHE", DetalleMatrizAnticorrupcionBean.getNuImpaInhe())
                .addValue("p_NU_PUNTA_INHE", DetalleMatrizAnticorrupcionBean.getNuPuntaInhe())
                .addValue("p_DE_SEVERIDAD_INHE", DetalleMatrizAnticorrupcionBean.getDePuntaInhe())
                .addValue("p_DE_CONTROL", DetalleMatrizAnticorrupcionBean.getDeControl())
                
                .addValue("p_NU_FRECU_INHE", DetalleMatrizAnticorrupcionBean.getNuFrecuInhe())
                .addValue("p_NU_FRECU_RES", DetalleMatrizAnticorrupcionBean.getNuFrecuRes())
                .addValue("p_NU_PUNTA_RES", DetalleMatrizAnticorrupcionBean.getNuPuntaRes())
                .addValue("p_NU_IMPA_RES", DetalleMatrizAnticorrupcionBean.getNuImpaRes())
                .addValue("p_DE_SEVERIDAD_RES", DetalleMatrizAnticorrupcionBean.getDeSeveridadRes())
                .addValue("p_ID_USUA_CREA", DetalleMatrizAnticorrupcionBean.getUsuarioCreacion())
                .addValue("p_DE_USUA_CREA_IP", DetalleMatrizAnticorrupcionBean.getIpCreacion())
                
                .addValue("p_COD_RIESGO", DetalleMatrizAnticorrupcionBean.getCodRiesgo())
                .addValue("p_COD_CONTROL", DetalleMatrizAnticorrupcionBean.getCodControl())
                .addValue("p_ID_ESTRATEGIA_RESP", DetalleMatrizAnticorrupcionBean.getIdRespEstrategia())
                .addValue("p_COD_PLAN_ACCION", DetalleMatrizAnticorrupcionBean.getCodPlanAccion())
                
                .addValue("p_DE_PLAN_ACCION", DetalleMatrizAnticorrupcionBean.getDePlanAccion())
                .addValue("p_ID_EFICACIA_CONTROL", DetalleMatrizAnticorrupcionBean.getIdEficaciaControl())
                .addValue("p_ID_RESP_PLAN_ACCION", DetalleMatrizAnticorrupcionBean.getIdRespPlanAccion())
                .addValue("p_ID_ESTADO_PLAN_ACCION", DetalleMatrizAnticorrupcionBean.getIdEstadoPlanAccion())
                .addValue("p_FE_FIN_PLAN_ACCION", fechaFinPlanAccion)
                .addValue("p_FE_VERIFICACION", fechaFinPlanAccion)
                .addValue("p_IN_EFICAZ", DetalleMatrizAnticorrupcionBean.getInFicaz())
                .addValue("p_ID_TIPODELITO", DetalleMatrizAnticorrupcionBean.getIdTipoDelito())
                .addValue("p_IN_DEBIDA_DILIG", DetalleMatrizAnticorrupcionBean.getIdDebidaDilig())
                .addValue("p_IN_BAJA", DetalleMatrizAnticorrupcionBean.getIndicadorBaja())
                .addValue("p_ID_USUA_MODI", DetalleMatrizAnticorrupcionBean.getUsuarioModificacion())
                .addValue("p_DE_USUA_MODI_IP", DetalleMatrizAnticorrupcionBean.getIpModificacion())
                .addValue("p_IND_FIN_PLAN_ACCION", DetalleMatrizAnticorrupcionBean.getIndFinPlanAccion());

        Map<String, Object> out = call.execute(in);
        String descripcionResultado = out.get(SNConstantes.DE_ERROR).toString();
                logger.info(" Número de error actualizar detalle de matriz riesgo : {}", descripcionResultado);

        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        

        dtoDetalleMatrizRiesgo = new DTODetalleMatrizAnticorrupcion();
        dtoDetalleMatrizRiesgo.setIdDetaMatrizAntic(DetalleMatrizAnticorrupcionBean.getIdDetaMatrizAntic());
        dtoDetalleMatrizRiesgo.setCodigoResultado(numeroResultado);
        dtoDetalleMatrizRiesgo.setDescripcionResultado(descripcionResultado);

        logger.info("Fin de MatrizRiesgoDaoImpl - actualizarDetaMatrizRiesgo");
        return dtoDetalleMatrizRiesgo;
    
    }

    @Override
    public Byte anularMatrizRiesgo(MatrizAnticorrupcionBean matrizRiesgoBean) throws DataAccessException {
      
      logger.info("Inicio MatrizAnticorrupcionDaoImpl - anularMatrizAnticorrupcion");

        Byte resultadoAnularMatriz;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_TRANSACCION)
                .withProcedureName(SNConstantes.SP_ANULAR_MATRIZ_ANTIC)
                .declareParameters(new SqlParameter("p_ID_MATRIZ_RIESGO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_USUA_MODI", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_USUA_MODI_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_MATRIZ_RIESGO", matrizRiesgoBean.getIdMatrizRiesgo())
                .addValue("p_ID_USUA_MODI", matrizRiesgoBean.getUsuarioModificacion())
                .addValue("p_DE_USUA_MODI_IP", matrizRiesgoBean.getIpModificacion());

        Map<String, Object> out = call.execute(in);
        BigDecimal result = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        resultadoAnularMatriz = result.byteValueExact();
        String descripcionError = (String) out.get(SNConstantes.DE_ERROR);
        logger.info("Número de error anular Matriz de Anticorrupcion : {}", result);
        logger.info("Descripcion de error anular Matriz de Anticorrupcion : {}", descripcionError);

        logger.info("Fin MatrizAnticDaoImpl - anularMatrizAntic");
        return resultadoAnularMatriz;
    
    }
    
    
}

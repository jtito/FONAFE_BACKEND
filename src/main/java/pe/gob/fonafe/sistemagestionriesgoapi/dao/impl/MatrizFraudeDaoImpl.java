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
import pe.gob.fonafe.sistemagestionriesgoapi.dao.IMatrizFraudeDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.*;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import pe.gob.fonafe.sistemagestionriesgoapi.models.DetalleMatrizFraudeBean;
import pe.gob.fonafe.sistemagestionriesgoapi.models.MatrizFraudeBean;

@Repository
public class MatrizFraudeDaoImpl implements IMatrizFraudeDao{

    private final JdbcTemplate jdbcTemplate;

    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    public MatrizFraudeDaoImpl(@Qualifier("jdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<DTOMatrizFraude> listarBandejaMatrizFraude(int idEmpresa, int idPeriodo, int idMatrizNivel) throws DataAccessException {
       
        logger.info("Inicio MatrizFraudeDaoImpl - listarBandejaMatrizFraude");

        List<DTOMatrizFraude> listaBandejaMatrizFraude;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_TRANSACCION)
                .withProcedureName(SNConstantes.SP_LISTAR_BANDEJA_MATRIZ_FRAUDE)
                .declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_PERIODO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_MATRIZ_NIVEL", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTOMatrizFraude.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_EMPRESA", idEmpresa)
                .addValue("p_ID_PERIODO", idPeriodo)
                .addValue("p_ID_MATRIZ_NIVEL", idMatrizNivel);

        Map<String, Object> out = call.execute(in);
        listaBandejaMatrizFraude = (List<DTOMatrizFraude>) out.get(SNConstantes.O_CURSOR);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionError = (String) out.get(SNConstantes.DE_ERROR);
        logger.info("numero resultado: {} ", numeroResultado);
        logger.info("descripcion error: {} ", descripcionError);

        logger.info("Fin MatrizFraudeDaoImpl - listarBandejaMatrizFraude");
        return listaBandejaMatrizFraude;
    
    }

    @Override
    public DTOMatrizFraude registrarMatrizFraude(MatrizFraudeBean matrizFraudeBean) throws DataAccessException {
        logger.info("Inicio de MatrizFraudeDaoImpl - registrarMatrizFraude");

        DTOMatrizFraude dtoMatrizFraude = new DTOMatrizFraude();

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
                .addValue("p_ID_EMPRESA", matrizFraudeBean.getIdEmpresa())
                .addValue("p_ID_SEDE_EMPRESA", matrizFraudeBean.getIdSede())
                .addValue("p_ID_GERENCIA", matrizFraudeBean.getIdGerencia())
                .addValue("p_ID_PERIODO", matrizFraudeBean.getIdPeriodo())
                .addValue("p_ID_TIPO_MATRIZ", matrizFraudeBean.getIdTipoMatriz())
                .addValue("p_ID_MATRIZ_NIVEL", matrizFraudeBean.getIdMatrizNivel())
                .addValue("p_MATRIZ_NIVEL", matrizFraudeBean.getMatrizNivel())
                .addValue("p_ID_USUA_CREA", matrizFraudeBean.getUsuarioCreacion())
                .addValue("p_DE_USUA_CREA_IP", matrizFraudeBean.getIpCreacion());

        Map<String, Object> out = call.execute(in);
        BigDecimal idMatrizOut = (BigDecimal) out.get("p_ID_MATRIZ_RIESGO_OUT");
        dtoMatrizFraude.setIdMatrizRiesgo(idMatrizOut.intValue());
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        dtoMatrizFraude.setCodigoResultado(numeroResultado);
        String descripcionResultado = out.get(SNConstantes.DE_ERROR).toString();
        dtoMatrizFraude.setDescripcionResultado(descripcionResultado);

        logger.info("FFFin de MatrizRiesgoDaoImpl - registrarMatrizRiesgo ID "+idMatrizOut);
        return dtoMatrizFraude;
    
    }

    @Override
    public DTOGenerico registrarDetalleMatrizFraude(DetalleMatrizFraudeBean detalleMatrizFraude) throws DataAccessException {
      
       logger.info("Inicio de MatrizFraudeDaoImpl - registrarDetalleMatrizFraude");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonResultado = gson.toJson(detalleMatrizFraude);
        
         logger.info("registrarDetalleMatrizFraude "+jsonResultado);
        
        DTOGenerico dtoGenerico = new DTOGenerico();
        
        
        SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaIniPlanAcion = null;
        Date fechaFinPlanAccion = null;
        Date fechaPrevista = null;
        Date fechaVerificacion = null;
        try {
            fechaIniPlanAcion = parse.parse(detalleMatrizFraude.getFechaInicioPlanAccion());
            
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error de parseo");
        }
        
        try {
            fechaFinPlanAccion = parse.parse(detalleMatrizFraude.getFechaFinPlanAccion());
            
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error de parseo");
        }
        
        try {
            fechaPrevista = parse.parse(detalleMatrizFraude.getFechaPrevista());
            
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error de parseo");
        }
        
        try {
            fechaVerificacion = parse.parse(detalleMatrizFraude.getFechaVerificacion());
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error de parseo");
        }

        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_TRANSACCION)
                .withProcedureName(SNConstantes.SP_INSERT_DETALLE_MATRIZFRAUDE)
                .declareParameters(new SqlParameter("p_ID_MATRIZ_RIESGO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_SEDE_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_GERENCIA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_PERIODO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_COD_MATRIZ", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_MATRIZ_NIVEL", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_PROCESO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_SUBPROCESO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_COD_RIESGO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_RIESGO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_RIESGO_ASOCIADO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_ORIGENRIESGO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_AGENTE_FRAUDE", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_FRECUENCIARIESGO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_TIPORIESGO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_NU_PROBA_INHE", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_NU_IMPA_ECO_INHE", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_NU_IMPA_LEG_INHE", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_NU_IMPA_REP_INHE", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_NU_IMPA_CONT_INHE", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_NU_IMPA_INHE", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_NU_PUNTA_INHE", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_DE_SEVERIDAD_INHE", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_COD_CONTROL", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_CONTROL", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_AREA_CONTROL", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_RESP_CONTROL", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_LI_OPOR_CONTROL", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_MOT_ACTO_CONTROL", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ACT_POT_CONTROL", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_FRECU_CONTROL", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_OPOR_CONTROL", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_AUTO_CONTROL", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_DE_EVIDE_CONTROL", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_NU_PROBA_RES", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_NU_IMPA_ECO_RES", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_NU_IMPA_LEG_RES", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_NU_IMPA_REP_RES", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_NU_IMPA_CONT_RES", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_NU_IMPA_RES", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_NU_PUNTA_RES", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_DE_SEVERIDAD_RES", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_ESTRATEGIA_RESP", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_COD_PLAN_ACCION", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_PLAN_ACCION", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_RESP_PLAN_ACCION", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_FE_INI_PLAN_ACCION", OracleTypes.DATE))
                .declareParameters(new SqlParameter("p_ID_ESTADO_PLAN_ACCION", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_FE_FIN_PLAN_ACCION", OracleTypes.DATE))
                .declareParameters(new SqlParameter("p_FE_PREVISTA", OracleTypes.DATE))
                .declareParameters(new SqlParameter("p_IN_EFICAZ", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_FE_VERIFICACION", OracleTypes.DATE))
                .declareParameters(new SqlParameter("p_ID_VERIFICADOR", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_EVIDENCIA", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_OBSERVACION", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_COD_INDICADOR", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_INDICADOR", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_FRECUENCIA_INDICADOR", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_META_KRI", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_KRI_ACTUAL", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_RESP_INDICADOR", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_USUA_CREA", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_USUA_CREA_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter("p_ID_DETA_MATRIZFRAUDE_OUT", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_MATRIZ_RIESGO", detalleMatrizFraude.getIdMatrizRiesgo())
                .addValue("p_ID_EMPRESA", detalleMatrizFraude.getIdEmpresa())
                .addValue("p_ID_SEDE_EMPRESA", detalleMatrizFraude.getIdSede())
                .addValue("p_ID_GERENCIA", detalleMatrizFraude.getIdGerencia())
                .addValue("p_ID_PERIODO", detalleMatrizFraude.getIdPeriodo())
                .addValue("p_COD_MATRIZ", detalleMatrizFraude.getCodMatriz())
                .addValue("p_ID_MATRIZ_NIVEL", detalleMatrizFraude.getIdMatrizNivel())
                .addValue("p_ID_PROCESO", detalleMatrizFraude.getIdProceso())
                .addValue("p_ID_SUBPROCESO", detalleMatrizFraude.getIdSubProceso())
                .addValue("p_COD_RIESGO", detalleMatrizFraude.getCodRiesgo())
                .addValue("p_DE_RIESGO", detalleMatrizFraude.getDeRiesgo())
                .addValue("p_RIESGO_ASOCIADO", detalleMatrizFraude.getRiesgoAsociado())
                .addValue("p_ID_ORIGENRIESGO", detalleMatrizFraude.getIdOrigenRiesgo())
                .addValue("p_AGENTE_FRAUDE", detalleMatrizFraude.getAgenteFraude())
                .addValue("p_ID_FRECUENCIARIESGO", detalleMatrizFraude.getIdFrecuenciaRiesgo())
                .addValue("p_ID_TIPORIESGO", detalleMatrizFraude.getIdTipoRiesgo())
                .addValue("p_NU_PROBA_INHE", detalleMatrizFraude.getNuProbabilidadInherente())
                .addValue("p_NU_IMPA_ECO_INHE", detalleMatrizFraude.getNuImpactoInherenteE())
                .addValue("p_NU_IMPA_LEG_INHE", detalleMatrizFraude.getNuImpactoInherenteL())
                .addValue("p_NU_IMPA_REP_INHE", detalleMatrizFraude.getNuImpactoInherenteR())
                .addValue("p_NU_IMPA_CONT_INHE", detalleMatrizFraude.getNuImpactoInherenteC())
                .addValue("p_NU_IMPA_INHE", detalleMatrizFraude.getNuImpactoInherenteG())
                .addValue("p_NU_PUNTA_INHE", detalleMatrizFraude.getNuPuntajeInherente())
                .addValue("p_DE_SEVERIDAD_INHE", detalleMatrizFraude.getDeSeveridadInherente())
                .addValue("p_COD_CONTROL", detalleMatrizFraude.getCodControl())
                .addValue("p_DE_CONTROL", detalleMatrizFraude.getDeControl())
                .addValue("p_ID_AREA_CONTROL", detalleMatrizFraude.getIdAreaControl())
                .addValue("p_ID_RESP_CONTROL", detalleMatrizFraude.getIdResponsableControl())
                .addValue("p_LI_OPOR_CONTROL", detalleMatrizFraude.getLimitaOportunidaControl())
                .addValue("p_MOT_ACTO_CONTROL", detalleMatrizFraude.getMotivaActoControl())
                .addValue("p_ACT_POT_CONTROL", detalleMatrizFraude.getActitudPotencialControl())
                .addValue("p_ID_FRECU_CONTROL", detalleMatrizFraude.getIdFrecuenciaControl())
                .addValue("p_ID_OPOR_CONTROL", detalleMatrizFraude.getIdOportunidadControl())
                .addValue("p_ID_AUTO_CONTROL", detalleMatrizFraude.getIdAutomatizacionControl())
                .addValue("p_DE_EVIDE_CONTROL", detalleMatrizFraude.getDeEvidenciaControl())
                .addValue("p_NU_PROBA_RES", detalleMatrizFraude.getNuProbabilidadResidual())
                .addValue("p_NU_IMPA_ECO_RES", detalleMatrizFraude.getNuImpactoResidualE())
                .addValue("p_NU_IMPA_LEG_RES", detalleMatrizFraude.getNuImpactoResidualL())
                .addValue("p_NU_IMPA_REP_RES", detalleMatrizFraude.getNuImpactoResidualR())
                .addValue("p_NU_IMPA_CONT_RES", detalleMatrizFraude.getNuImpactoResidualC())
                .addValue("p_NU_IMPA_RES", detalleMatrizFraude.getNuImpactoResidualG())
                .addValue("p_NU_PUNTA_RES", detalleMatrizFraude.getNuPuntajeResidual())
                .addValue("p_DE_SEVERIDAD_RES", detalleMatrizFraude.getDeSeveridadResidual())
                .addValue("p_ID_ESTRATEGIA_RESP", detalleMatrizFraude.getIdEstrategiaResp())
                .addValue("p_COD_PLAN_ACCION", detalleMatrizFraude.getCodPlanAccion())
                .addValue("p_DE_PLAN_ACCION", detalleMatrizFraude.getDesPlanAccion())
                .addValue("p_ID_RESP_PLAN_ACCION", detalleMatrizFraude.getIdResponsablePlanAccion())
                .addValue("p_FE_INI_PLAN_ACCION", fechaIniPlanAcion)
                .addValue("p_ID_ESTADO_PLAN_ACCION", detalleMatrizFraude.getEstadoPlanAccion())
                .addValue("p_FE_FIN_PLAN_ACCION", fechaFinPlanAccion)
                .addValue("p_FE_PREVISTA", fechaPrevista)
                .addValue("p_IN_EFICAZ", detalleMatrizFraude.getFueEficaz())
                .addValue("p_FE_VERIFICACION", fechaVerificacion)
                .addValue("p_ID_VERIFICADOR", detalleMatrizFraude.getVerificadoPor())
                .addValue("p_DE_EVIDENCIA", detalleMatrizFraude.getEvidenciaEficacia())
                .addValue("p_DE_OBSERVACION", detalleMatrizFraude.getObservaciones())
                .addValue("p_COD_INDICADOR", detalleMatrizFraude.getCodkri())
                .addValue("p_DE_INDICADOR", detalleMatrizFraude.getDefkri())
                .addValue("p_FRECUENCIA_INDICADOR", detalleMatrizFraude.getFrecuencia())
                .addValue("p_META_KRI", detalleMatrizFraude.getMetkri())
                .addValue("p_KRI_ACTUAL", detalleMatrizFraude.getKriActual())
                .addValue("p_ID_RESP_INDICADOR", detalleMatrizFraude.getKriResponsable())
                .addValue("p_ID_USUA_CREA", detalleMatrizFraude.getUsuarioCreacion())
                .addValue("p_DE_USUA_CREA_IP", detalleMatrizFraude.getIpCreacion());

        Map<String, Object> out = call.execute(in);
        String descripcionResultado = out.get(SNConstantes.DE_ERROR).toString();
        BigDecimal idDetaMatrizOut = (BigDecimal) out.get("p_ID_DETA_MATRIZFRAUDE_OUT");
        dtoGenerico.setIdGenerico(idDetaMatrizOut.longValue());
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        dtoGenerico.setCodigoResultado(numeroResultado);
        
        dtoGenerico.setDescripcionResultado(descripcionResultado);

        logger.info("Fin de MatrizFraudeDaoImpl - registrarDetalleMatrizFraude");
        return dtoGenerico;
    
    }

    @Override
    public DTOMatrizFraude registrarMatrizRiesgoFraude(MatrizFraudeBean matrizFraudeBean) throws DataAccessException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DTOMatrizFraude obtenerMatrizFraude(Integer idMatrizFraude) throws DataAccessException {
        
        
        logger.info("Inicio MatrizFraudeDaoImpl - obtenerMatrizFraude");

        DTOMatrizFraude matrizFraude;

        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_TRANSACCION)
                .withProcedureName(SNConstantes.SP_OBTENER_MATRIZ_RIESGO)
                .declareParameters(new SqlParameter("p_ID_MATRIZ_RIESGO", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTOMatrizFraude.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_MATRIZ_RIESGO", idMatrizFraude);

        Map<String, Object> out = call.execute(in);
        ArrayList<DTOMatrizFraude> matrizFraudeArray = (ArrayList<DTOMatrizFraude>) out.get(SNConstantes.O_CURSOR);
        Iterator<DTOMatrizFraude> iteradorMatrizFraude = matrizFraudeArray.iterator();

        matrizFraude = new DTOMatrizFraude();

        while (iteradorMatrizFraude.hasNext()) {
            DTOMatrizFraude itrMatrizFraude = iteradorMatrizFraude.next();
            matrizFraude.setIdMatrizRiesgo(itrMatrizFraude.getIdMatrizRiesgo());
            matrizFraude.setIdEmpresa(itrMatrizFraude.getIdEmpresa());
            matrizFraude.setIdSede(itrMatrizFraude.getIdSede());
            matrizFraude.setIdGerencia(itrMatrizFraude.getIdGerencia());
            matrizFraude.setIdPeriodo(itrMatrizFraude.getIdPeriodo());
            matrizFraude.setIdTipoMatriz(itrMatrizFraude.getIdTipoMatriz());
            matrizFraude.setIdMatrizNivel(itrMatrizFraude.getIdMatrizNivel());
            matrizFraude.setIndicadorBaja(itrMatrizFraude.getIndicadorBaja());
            matrizFraude.setFechaCreacion(itrMatrizFraude.getFechaCreacion());
            matrizFraude.setMatrizNivel(itrMatrizFraude.getMatrizNivel());
        }

        logger.info("Fin MatrizFraudeDaoImpl - obtenerMatrizFraude");
        return matrizFraude;
    
    }

    @Override
    public DTOGenerico listarDetalleMatrizFraude(Integer idMatrizFraude, Long idUsuario) throws DataAccessException {
        
       logger.info("Inicio MatrizFraudeDaoImpl - listarDetalleMatrizFraude");

        List<DTODetalleMatrizFraude> listaDetaMatrizFraude;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_TRANSACCION)
                .withProcedureName(SNConstantes.SP_LISTAR_DETALLE_MATRIZFRAUDE)
                .declareParameters(new SqlParameter("p_ID_MATRIZ_RIESGO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_USUARIO", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTODetalleMatrizFraude.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_MATRIZ_RIESGO", idMatrizFraude)
                .addValue("p_ID_USUARIO", idUsuario);

        Map<String, Object> out = call.execute(in);
        listaDetaMatrizFraude = (List<DTODetalleMatrizFraude>) out.get(SNConstantes.O_CURSOR);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionResultado = (String) out.get(SNConstantes.DE_ERROR);
        logger.info("numero resultado:" + numeroResultado);
        logger.info("descripcion error:" + descripcionResultado);

        DTOGenerico dtoGenerico = new DTOGenerico();
        dtoGenerico.setCodigoResultado(numeroResultado);
        dtoGenerico.setDescripcionResultado(descripcionResultado);
        dtoGenerico.setListado(listaDetaMatrizFraude);

        logger.info("Fin MatrizRiesgoDaoImpl - listarDetalleMatrizRiesgo");
        return dtoGenerico;
    
    }

    @Override
    public DTOMatrizFraude actualizarMatrizFraude(MatrizFraudeBean matrizFraudebean) throws DataAccessException {
        logger.info("Inicio MatrizFraudeDaoImpl - actualizarMatrizFraude");

        DTOMatrizFraude dtoMatrizFraude;
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
                .addValue("p_ID_MATRIZ_RIESGO", matrizFraudebean.getIdMatrizRiesgo())
                .addValue("p_ID_EMPRESA", matrizFraudebean.getIdEmpresa())
                .addValue("p_ID_SEDE_EMPRESA", matrizFraudebean.getIdSede())
                .addValue("p_ID_GERENCIA", matrizFraudebean.getIdGerencia())
                .addValue("p_ID_PERIODO", matrizFraudebean.getIdPeriodo())
                .addValue("p_ID_TIPO_MATRIZ", matrizFraudebean.getIdTipoMatriz())
                .addValue("p_ID_MATRIZ_NIVEL", matrizFraudebean.getIdMatrizNivel())
                .addValue("p_IN_BAJA", matrizFraudebean.getIndicadorBaja())
                .addValue("p_ID_USUA_MODI", matrizFraudebean.getUsuarioModificacion())
                .addValue("p_DE_USUA_MODI_IP", matrizFraudebean.getIpModificacion());

        Map<String, Object> out = call.execute(in);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionResultado = out.get(SNConstantes.DE_ERROR).toString();

        dtoMatrizFraude = new DTOMatrizFraude();
        dtoMatrizFraude.setIdMatrizRiesgo(matrizFraudebean.getIdMatrizRiesgo());
        dtoMatrizFraude.setCodigoResultado(numeroResultado);
        dtoMatrizFraude.setDescripcionResultado(descripcionResultado);
        logger.info(" Número de error actualizar Matriz fraude : {}", numeroResultado);

        logger.info("Fin de MatrizFraudeDaoImpl - actualizarMatrizFraude");
        return dtoMatrizFraude;
    }

    @Override
    public DTODetalleMatrizFraude actualizarDetaMatrizRiesgo(DetalleMatrizFraudeBean detalleMatrizFraude) throws DataAccessException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Byte anularMatrizFraude(MatrizFraudeBean matrizFraudeBean) throws DataAccessException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DTODetalleMatrizFraude actualizarDetaMatrizFraude(DetalleMatrizFraudeBean detalleMatrizFraude) throws DataAccessException {
       logger.info("Inicio MatrizFraudeDaoImpl - actualizarDetaMatrizFraude");

        SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaIniPlanAcion = null;
        Date fechaFinPlanAccion = null;
        Date fechaPrevista = null;
        Date fechaVerificacion = null;
        try {
            fechaIniPlanAcion = parse.parse(detalleMatrizFraude.getFechaInicioPlanAccion());
            
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error de parseo");
        }
        
        try {
            fechaFinPlanAccion = parse.parse(detalleMatrizFraude.getFechaFinPlanAccion());
            
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error de parseo");
        }
        
        try {
            fechaPrevista = parse.parse(detalleMatrizFraude.getFechaPrevista());
            
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error de parseo");
        }
        
        try {
            fechaVerificacion = parse.parse(detalleMatrizFraude.getFechaVerificacion());
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error de parseo");
        }
       
        DTODetalleMatrizFraude dtoDetalleMatrizFraude;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_TRANSACCION)
                .withProcedureName(SNConstantes.SP_UPDATE_DETALLE_MATRIZFRAUDE)
                .declareParameters(new SqlParameter("p_ID_DETA_MATRIZFRAUDE", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_MATRIZ_RIESGO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_SEDE_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_GERENCIA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_PERIODO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_COD_MATRIZ", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_MATRIZ_NIVEL", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_PROCESO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_SUBPROCESO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_COD_RIESGO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_RIESGO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_RIESGO_ASOCIADO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_ORIGENRIESGO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_AGENTE_FRAUDE", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_FRECUENCIARIESGO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_TIPORIESGO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_NU_PROBA_INHE", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_NU_IMPA_ECO_INHE", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_NU_IMPA_LEG_INHE", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_NU_IMPA_REP_INHE", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_NU_IMPA_CONT_INHE", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_NU_IMPA_INHE", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_NU_PUNTA_INHE", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_DE_SEVERIDAD_INHE", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_COD_CONTROL", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_CONTROL", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_AREA_CONTROL", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_RESP_CONTROL", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_LI_OPOR_CONTROL", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_MOT_ACTO_CONTROL", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ACT_POT_CONTROL", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_FRECU_CONTROL", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_OPOR_CONTROL", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_AUTO_CONTROL", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_DE_EVIDE_CONTROL", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_NU_PROBA_RES", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_NU_IMPA_ECO_RES", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_NU_IMPA_LEG_RES", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_NU_IMPA_REP_RES", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_NU_IMPA_CONT_RES", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_NU_IMPA_RES", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_NU_PUNTA_RES", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_DE_SEVERIDAD_RES", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_ESTRATEGIA_RESP", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_COD_PLAN_ACCION", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_PLAN_ACCION", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_RESP_PLAN_ACCION", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_FE_INI_PLAN_ACCION", OracleTypes.DATE))
                .declareParameters(new SqlParameter("p_ID_ESTADO_PLAN_ACCION", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_FE_FIN_PLAN_ACCION", OracleTypes.DATE))
                .declareParameters(new SqlParameter("p_FE_PREVISTA", OracleTypes.DATE))
                .declareParameters(new SqlParameter("p_IN_EFICAZ", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_FE_VERIFICACION", OracleTypes.DATE))
                .declareParameters(new SqlParameter("p_ID_VERIFICADOR", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_EVIDENCIA", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_OBSERVACION", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_COD_INDICADOR", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_INDICADOR", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_FRECUENCIA_INDICADOR", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_META_KRI", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_KRI_ACTUAL", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_RESP_INDICADOR", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_IN_BAJA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_USUA_MODI", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_USUA_MODI_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_DETA_MATRIZFRAUDE", detalleMatrizFraude.getIdDetaMatrizFraude())
                .addValue("p_ID_MATRIZ_RIESGO", detalleMatrizFraude.getIdMatrizRiesgo())
                .addValue("p_ID_EMPRESA", detalleMatrizFraude.getIdEmpresa())
                .addValue("p_ID_SEDE_EMPRESA", detalleMatrizFraude.getIdSede())
                .addValue("p_ID_GERENCIA", detalleMatrizFraude.getIdGerencia())
                .addValue("p_ID_PERIODO", detalleMatrizFraude.getIdPeriodo())
                .addValue("p_COD_MATRIZ", detalleMatrizFraude.getCodMatriz())
                .addValue("p_ID_MATRIZ_NIVEL", detalleMatrizFraude.getIdMatrizNivel())
                .addValue("p_ID_PROCESO", detalleMatrizFraude.getIdProceso())
                .addValue("p_ID_SUBPROCESO", detalleMatrizFraude.getIdSubProceso())
                .addValue("p_COD_RIESGO", detalleMatrizFraude.getCodRiesgo())
                .addValue("p_DE_RIESGO", detalleMatrizFraude.getDeRiesgo())
                .addValue("p_RIESGO_ASOCIADO", detalleMatrizFraude.getRiesgoAsociado())
                .addValue("p_ID_ORIGENRIESGO", detalleMatrizFraude.getIdOrigenRiesgo())
                .addValue("p_AGENTE_FRAUDE", detalleMatrizFraude.getAgenteFraude())
                .addValue("p_ID_FRECUENCIARIESGO", detalleMatrizFraude.getIdFrecuenciaRiesgo())
                .addValue("p_ID_TIPORIESGO", detalleMatrizFraude.getIdTipoRiesgo())
                .addValue("p_NU_PROBA_INHE", detalleMatrizFraude.getNuProbabilidadInherente())
                .addValue("p_NU_IMPA_ECO_INHE", detalleMatrizFraude.getNuImpactoInherenteE())
                .addValue("p_NU_IMPA_LEG_INHE", detalleMatrizFraude.getNuImpactoInherenteL())
                .addValue("p_NU_IMPA_REP_INHE", detalleMatrizFraude.getNuImpactoInherenteR())
                .addValue("p_NU_IMPA_CONT_INHE", detalleMatrizFraude.getNuImpactoInherenteC())
                .addValue("p_NU_IMPA_INHE", detalleMatrizFraude.getNuImpactoInherenteG())
                .addValue("p_NU_PUNTA_INHE", detalleMatrizFraude.getNuPuntajeInherente())
                .addValue("p_DE_SEVERIDAD_INHE", detalleMatrizFraude.getDeSeveridadInherente())
                .addValue("p_COD_CONTROL", detalleMatrizFraude.getCodControl())
                .addValue("p_DE_CONTROL", detalleMatrizFraude.getDeControl())
                .addValue("p_ID_AREA_CONTROL", detalleMatrizFraude.getIdAreaControl())
                .addValue("p_ID_RESP_CONTROL", detalleMatrizFraude.getIdResponsableControl())
                .addValue("p_LI_OPOR_CONTROL", detalleMatrizFraude.getLimitaOportunidaControl())
                .addValue("p_MOT_ACTO_CONTROL", detalleMatrizFraude.getMotivaActoControl())
                .addValue("p_ACT_POT_CONTROL", detalleMatrizFraude.getActitudPotencialControl())
                .addValue("p_ID_FRECU_CONTROL", detalleMatrizFraude.getIdFrecuenciaControl())
                .addValue("p_ID_OPOR_CONTROL", detalleMatrizFraude.getIdOportunidadControl())
                .addValue("p_ID_AUTO_CONTROL", detalleMatrizFraude.getIdAutomatizacionControl())
                .addValue("p_DE_EVIDE_CONTROL", detalleMatrizFraude.getDeEvidenciaControl())
                .addValue("p_NU_PROBA_RES", detalleMatrizFraude.getNuProbabilidadResidual())
                .addValue("p_NU_IMPA_ECO_RES", detalleMatrizFraude.getNuImpactoResidualE())
                .addValue("p_NU_IMPA_LEG_RES", detalleMatrizFraude.getNuImpactoResidualL())
                .addValue("p_NU_IMPA_REP_RES", detalleMatrizFraude.getNuImpactoResidualR())
                .addValue("p_NU_IMPA_CONT_RES", detalleMatrizFraude.getNuImpactoResidualC())
                .addValue("p_NU_IMPA_RES", detalleMatrizFraude.getNuImpactoResidualG())
                .addValue("p_NU_PUNTA_RES", detalleMatrizFraude.getNuPuntajeResidual())
                .addValue("p_DE_SEVERIDAD_RES", detalleMatrizFraude.getDeSeveridadResidual())
                .addValue("p_ID_ESTRATEGIA_RESP", detalleMatrizFraude.getIdEstrategiaResp())
                .addValue("p_COD_PLAN_ACCION", detalleMatrizFraude.getCodPlanAccion())
                .addValue("p_DE_PLAN_ACCION", detalleMatrizFraude.getDesPlanAccion())
                .addValue("p_ID_RESP_PLAN_ACCION", detalleMatrizFraude.getIdResponsablePlanAccion())
                .addValue("p_FE_INI_PLAN_ACCION", fechaIniPlanAcion)
                .addValue("p_ID_ESTADO_PLAN_ACCION", detalleMatrizFraude.getEstadoPlanAccion())
                .addValue("p_FE_FIN_PLAN_ACCION", fechaFinPlanAccion)
                .addValue("p_FE_PREVISTA", fechaPrevista)
                .addValue("p_IN_EFICAZ", detalleMatrizFraude.getFueEficaz())
                .addValue("p_FE_VERIFICACION", fechaVerificacion)
                .addValue("p_ID_VERIFICADOR", detalleMatrizFraude.getVerificadoPor())
                .addValue("p_DE_EVIDENCIA", detalleMatrizFraude.getEvidenciaEficacia())
                .addValue("p_DE_OBSERVACION", detalleMatrizFraude.getObservaciones())
                .addValue("p_COD_INDICADOR", detalleMatrizFraude.getCodkri())
                .addValue("p_DE_INDICADOR", detalleMatrizFraude.getDefkri())
                .addValue("p_FRECUENCIA_INDICADOR", detalleMatrizFraude.getFrecuencia())
                .addValue("p_META_KRI", detalleMatrizFraude.getMetkri())
                .addValue("p_KRI_ACTUAL", detalleMatrizFraude.getKriActual())
                .addValue("p_ID_RESP_INDICADOR", detalleMatrizFraude.getKriResponsable())
                .addValue("P_IN_BAJA", detalleMatrizFraude.getIndicadorBaja()) //170821
                .addValue("p_ID_USUA_MODI", detalleMatrizFraude.getUsuarioModificacion()) //170821
                .addValue("p_DE_USUA_MODI_IP", detalleMatrizFraude.getIpModificacion());

        Map<String, Object> out = call.execute(in);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionResultado = out.get(SNConstantes.DE_ERROR).toString();

        dtoDetalleMatrizFraude = new DTODetalleMatrizFraude();
        dtoDetalleMatrizFraude.setIdDetaMatrizFraude(detalleMatrizFraude.getIdDetaMatrizFraude());
        dtoDetalleMatrizFraude.setCodigoResultado(numeroResultado);
        dtoDetalleMatrizFraude.setDescripcionResultado(descripcionResultado);
        logger.info(" Número de error actualizar detalle de matriz fraude : {}", numeroResultado);

        logger.info("Fin de MatrizRiesgoDaoImpl - actualizarDetaMatrizRiesgo");
        return dtoDetalleMatrizFraude;
    }

    
 
    
}

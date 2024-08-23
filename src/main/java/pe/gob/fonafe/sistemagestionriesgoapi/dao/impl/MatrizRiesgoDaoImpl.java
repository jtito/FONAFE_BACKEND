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
import pe.gob.fonafe.sistemagestionriesgoapi.dao.IMatrizRiesgoDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.*;
import pe.gob.fonafe.sistemagestionriesgoapi.models.ComentarioAuditoriaBean;
import pe.gob.fonafe.sistemagestionriesgoapi.models.DetalleMatrizRiesgoBean;
import pe.gob.fonafe.sistemagestionriesgoapi.models.MatrizRiesgoBean;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Repository
public class MatrizRiesgoDaoImpl implements IMatrizRiesgoDao {

    private final JdbcTemplate jdbcTemplate;

    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    public MatrizRiesgoDaoImpl(@Qualifier("jdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<DTOMatrizRiesgo> listarBandejaMatrizRiesgo(int idEmpresa, int idPeriodo, int idMatrizNivel) throws DataAccessException {
        logger.info("Inicio MatrizRiesgoDaoImpl - listarBandejaMatrizRiesgo");

        List<DTOMatrizRiesgo> listaBandejaMatrizRiesgo;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_TRANSACCION)
                .withProcedureName(SNConstantes.SP_LISTAR_BANDEJA_MATRIZ_RIESGO)
                .declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_PERIODO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_MATRIZ_NIVEL", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTOMatrizRiesgo.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_EMPRESA", idEmpresa)
                .addValue("p_ID_PERIODO", idPeriodo)
                .addValue("p_ID_MATRIZ_NIVEL", idMatrizNivel);

        Map<String, Object> out = call.execute(in);
        listaBandejaMatrizRiesgo = (List<DTOMatrizRiesgo>) out.get(SNConstantes.O_CURSOR);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionError = (String) out.get(SNConstantes.DE_ERROR);
        logger.info("numero resultado: {} ", numeroResultado);
        logger.info("descripcion error: {} ", descripcionError);

        logger.info("Fin MatrizRiesgoDaoImpl - listarBandejaMatrizRiesgo");
        return listaBandejaMatrizRiesgo;
    }

    @Override
    public DTOMatrizRiesgo registrarMatrizRiesgo(MatrizRiesgoBean matrizRiesgoBean) throws DataAccessException {
        logger.info("Inicio de MatrizRiesgoDaoImpl - registrarMatrizRiesgo");

        DTOMatrizRiesgo dtoMatrizRiesgo = new DTOMatrizRiesgo();

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
        dtoMatrizRiesgo.setIdMatrizRiesgo(idMatrizOut.longValue());
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        dtoMatrizRiesgo.setCodigoResultado(numeroResultado);
        String descripcionResultado = out.get(SNConstantes.DE_ERROR).toString();
        dtoMatrizRiesgo.setDescripcionResultado(descripcionResultado);

        logger.info("Fin de MatrizRiesgoDaoImpl - registrarMatrizRiesgo");
        return dtoMatrizRiesgo;
    }

    @Override
    public DTOGenerico registrarDetalleMatrizRiesgo(DetalleMatrizRiesgoBean detalleMatrizRiesgo) throws DataAccessException {
        logger.info("Inicio de MatrizRiesgoDaoImpl - registrarDetalleMatrizRiesgo");

        DTOGenerico dtoGenerico = new DTOGenerico();
        
        SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaIniPlanAcion = null;
        Date fechaFinPlanAccion = null;
        Date fechaPrevista = null;
        Date fechaVerificacion = null;
        try {
            fechaIniPlanAcion = parse.parse(detalleMatrizRiesgo.getFechaInicioPlanAccion());
            
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error de parseo");
        }
        
        try {
            fechaFinPlanAccion = parse.parse(detalleMatrizRiesgo.getFechaFinPlanAccion());
            
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error de parseo");
        }
        
        try {
            fechaPrevista = parse.parse(detalleMatrizRiesgo.getFechaPrevista());
            
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error de parseo");
        }
        
        try {
            fechaVerificacion = parse.parse(detalleMatrizRiesgo.getFechaVerificacion());
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error de parseo");
        }

        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_TRANSACCION)
                .withProcedureName(SNConstantes.SP_INSERT_DETALLE_MATRIZRIESGO)
                .declareParameters(new SqlParameter("p_ID_MATRIZ_RIESGO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_SEDE_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_GERENCIA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_PERIODO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_COD_MATRIZ", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_MATRIZ_NIVEL", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_PROCESO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_SUBPROCESO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_TITULO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_RIESGO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_PROCESO_IMPACTADO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_FODA", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_GRUPO_INTERES", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_ORIGENRIESGO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_FRECUENCIARIESGO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_TIPORIESGO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_NU_PROBA_INHE", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_NU_IMPA_INHE", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_NU_PUNTA_INHE", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_DE_SEVERIDAD_INHE", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_CONTROL_RIESGO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_CONTROL", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_AREA_CONTROL", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_RESP_CONTROL", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_FRECU_CONTROL", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_OPOR_CONTROL", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_AUTO_CONTROL", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_EVIDE_CONTROL", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_NU_PROBA_RES", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_NU_IMPA_RES", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_NU_PUNTA_RES", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_DE_SEVERIDAD_RES", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_USUA_CREA", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_USUA_CREA_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_COD_RIESGO", OracleTypes.VARCHAR))  //090821
                .declareParameters(new SqlParameter("p_COD_CONTROL", OracleTypes.VARCHAR)) //090821
                .declareParameters(new SqlParameter("p_COD_PLAN_ACCION", OracleTypes.VARCHAR))  //110821
                .declareParameters(new SqlParameter("p_DE_PLAN_ACCION", OracleTypes.VARCHAR))  //110821
                .declareParameters(new SqlParameter("p_ID_AREA_PLAN_ACCION", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_RESP_PLAN_ACCION", OracleTypes.VARCHAR))  //110821
                .declareParameters(new SqlParameter("p_FE_INI_PLAN_ACCION", OracleTypes.DATE))  //110821
                .declareParameters(new SqlParameter("p_ID_ESTADO_PLAN_ACCION", OracleTypes.VARCHAR))  //110821
                .declareParameters(new SqlParameter("p_FE_FIN_PLAN_ACCION", OracleTypes.DATE))  //110821
                .declareParameters(new SqlParameter("p_FE_PREVISTA", OracleTypes.DATE))  //110821
                .declareParameters(new SqlParameter("p_IN_EFICAZ", OracleTypes.VARCHAR))  //110821//110821
                .declareParameters(new SqlParameter("p_FE_VERIFICACION", OracleTypes.DATE))   //110821
                .declareParameters(new SqlParameter("p_ID_VERIFICADOR", OracleTypes.VARCHAR))  //110821
                .declareParameters(new SqlParameter("p_DE_EVIDENCIA", OracleTypes.VARCHAR))  //110821
                .declareParameters(new SqlParameter("p_DE_OBSERVACION", OracleTypes.VARCHAR))  //110821
                .declareParameters(new SqlParameter("p_ID_ESTRATEGIA_RESP", OracleTypes.VARCHAR))  //170821
                .declareParameters(new SqlParameter("p_COD_INDICADOR", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_INDICADOR", OracleTypes.VARCHAR))  //170821
                .declareParameters(new SqlParameter("p_FRECUENCIA_INDICADOR", OracleTypes.VARCHAR))  //170821
                .declareParameters(new SqlParameter("p_META_KRI", OracleTypes.VARCHAR))  //170821
                .declareParameters(new SqlParameter("p_KRI_ACTUAL", OracleTypes.VARCHAR))  //170821
                .declareParameters(new SqlParameter("p_ID_RESP_INDICADOR", OracleTypes.VARCHAR))  //170821

                .declareParameters(new SqlParameter("p_DE_USUA_CREA_IP", OracleTypes.VARCHAR))  //110821
                .declareParameters(new SqlOutParameter("p_ID_DETA_MATRIZRIESGO_OUT", OracleTypes.NUMBER))  //110821
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))  //110821
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_MATRIZ_RIESGO", detalleMatrizRiesgo.getIdMatrizRiesgo())
                .addValue("p_ID_EMPRESA", detalleMatrizRiesgo.getIdEmpresa())
                .addValue("p_ID_SEDE_EMPRESA", detalleMatrizRiesgo.getIdSede())
                .addValue("p_ID_GERENCIA", detalleMatrizRiesgo.getIdGerencia())
                .addValue("p_ID_PERIODO", detalleMatrizRiesgo.getIdPeriodo())
                .addValue("p_COD_MATRIZ", detalleMatrizRiesgo.getCodMatriz())
                .addValue("p_ID_MATRIZ_NIVEL", detalleMatrizRiesgo.getIdMatrizNivel())
                .addValue("p_ID_PROCESO", detalleMatrizRiesgo.getIdProceso())
                .addValue("p_ID_SUBPROCESO", detalleMatrizRiesgo.getIdSubProceso())
                .addValue("p_DE_TITULO", detalleMatrizRiesgo.getDeTitulo())
                .addValue("p_DE_RIESGO", detalleMatrizRiesgo.getDeRiesgo())
                .addValue("p_DE_PROCESO_IMPACTADO", detalleMatrizRiesgo.getDeProcesoImpactado())
                .addValue("p_DE_FODA", detalleMatrizRiesgo.getDeFoda())
                .addValue("p_DE_GRUPO_INTERES", detalleMatrizRiesgo.getDeGrupoInteres())
                .addValue("p_ID_ORIGENRIESGO", detalleMatrizRiesgo.getIdOrigenRiesgo())
                .addValue("p_ID_FRECUENCIARIESGO", detalleMatrizRiesgo.getIdFrecuenciaRiesgo())
                .addValue("p_ID_TIPORIESGO", detalleMatrizRiesgo.getIdTipoRiesgo())
                .addValue("p_NU_PROBA_INHE", detalleMatrizRiesgo.getNuProbabilidadInherente())
                .addValue("p_NU_IMPA_INHE", detalleMatrizRiesgo.getNuImpactoInherente())
                .addValue("p_NU_PUNTA_INHE", detalleMatrizRiesgo.getNuPuntajeInherente())
                .addValue("p_DE_SEVERIDAD_INHE", detalleMatrizRiesgo.getDeSeveridadInherente())
                .addValue("p_ID_CONTROL_RIESGO", detalleMatrizRiesgo.getIdControl())
                .addValue("p_DE_CONTROL", detalleMatrizRiesgo.getDeControl())
                .addValue("p_ID_AREA_CONTROL", detalleMatrizRiesgo.getIdAreaControl())
                .addValue("p_ID_RESP_CONTROL", detalleMatrizRiesgo.getIdResponsableControl())
                .addValue("p_ID_FRECU_CONTROL", detalleMatrizRiesgo.getIdFrecuenciaControl())
                .addValue("p_ID_OPOR_CONTROL", detalleMatrizRiesgo.getIdOportunidadControl())
                .addValue("p_ID_AUTO_CONTROL", detalleMatrizRiesgo.getIdAutomatizacionControl())
                .addValue("p_DE_EVIDE_CONTROL", detalleMatrizRiesgo.getDeEvidenciaControl())
                .addValue("p_NU_PROBA_RES", detalleMatrizRiesgo.getNuProbabilidadResidual())
                .addValue("p_NU_IMPA_RES", detalleMatrizRiesgo.getNuImpactoResidual())
                .addValue("p_NU_PUNTA_RES", detalleMatrizRiesgo.getNuPuntajeResidual())
                .addValue("p_DE_SEVERIDAD_RES", detalleMatrizRiesgo.getDeSeveridadResidual())
                .addValue("p_ID_USUA_CREA", detalleMatrizRiesgo.getUsuarioCreacion())
                .addValue("p_COD_RIESGO", detalleMatrizRiesgo.getCodRiesgo())  //090821
                .addValue("p_COD_CONTROL", detalleMatrizRiesgo.getCodControl()) //090821
                
                .addValue("p_COD_PLAN_ACCION", detalleMatrizRiesgo.getCodPlanAccion())  //110821
                .addValue("p_DE_PLAN_ACCION", detalleMatrizRiesgo.getDesPlanAccion()) //110821
                .addValue("p_ID_AREA_PLAN_ACCION", detalleMatrizRiesgo.getIdAreaPlanAccion())
                .addValue("p_ID_RESP_PLAN_ACCION", detalleMatrizRiesgo.getIdResponsablePlanAccion()) //110821
                .addValue("p_FE_INI_PLAN_ACCION", fechaIniPlanAcion) //110821
                .addValue("p_ID_ESTADO_PLAN_ACCION", detalleMatrizRiesgo.getEstadoPlanAccion()) //110821
                .addValue("p_FE_FIN_PLAN_ACCION", fechaFinPlanAccion) //110821
                .addValue("p_FE_PREVISTA", fechaPrevista) //110821
                .addValue("p_IN_EFICAZ", detalleMatrizRiesgo.getFueEficaz()) //110821
                .addValue("p_FE_VERIFICACION", fechaVerificacion) //110821
                .addValue("p_ID_VERIFICADOR", detalleMatrizRiesgo.getVerificadoPor()) //110821
                .addValue("p_DE_EVIDENCIA", detalleMatrizRiesgo.getEvidenciaEficacia()) //110821
                .addValue("p_DE_OBSERVACION", detalleMatrizRiesgo.getObservaciones()) //110821
                .addValue("p_ID_ESTRATEGIA_RESP", detalleMatrizRiesgo.getIdEstrategiaResp()) //170821
                
                .addValue("p_COD_INDICADOR",detalleMatrizRiesgo.getCodkri())
                .addValue("p_DE_INDICADOR",detalleMatrizRiesgo.getDefKri())
                .addValue("p_FRECUENCIA_INDICADOR", detalleMatrizRiesgo.getFrecuencia()) //170821
                .addValue("p_META_KRI", detalleMatrizRiesgo.getMetkri()) //170821
                .addValue("p_KRI_ACTUAL", detalleMatrizRiesgo.getKriActual()) //170821
                .addValue("p_ID_RESP_INDICADOR", detalleMatrizRiesgo.getKriResponsable()) //170821

                .addValue("p_DE_USUA_CREA_IP", detalleMatrizRiesgo.getIpCreacion());

        
        Map<String, Object> out = call.execute(in);
        BigDecimal idDetaMatrizOut = (BigDecimal) out.get("p_ID_DETA_MATRIZRIESGO_OUT");
        dtoGenerico.setIdGenerico(idDetaMatrizOut.longValue());
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        dtoGenerico.setCodigoResultado(numeroResultado);
        String descripcionResultado = out.get(SNConstantes.DE_ERROR).toString();
        dtoGenerico.setDescripcionResultado(descripcionResultado);

        logger.info("Fin de MatrizRiesgoDaoImpl - registrarDetalleMatrizRiesgo");
        return dtoGenerico;
    }

    @Override
    public DTOMatrizRiesgo obtenerMatrizRiesgo(Long idMatrizRiesgo) throws DataAccessException {
        logger.info("Inicio MatrizRiesgoDaoImpl - obtenerMatrizRiesgo");

        DTOMatrizRiesgo matrizRiesgo;

        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_TRANSACCION)
                .withProcedureName(SNConstantes.SP_OBTENER_MATRIZ_RIESGO)
                .declareParameters(new SqlParameter("p_ID_MATRIZ_RIESGO", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTOMatrizRiesgo.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_MATRIZ_RIESGO", idMatrizRiesgo);

        Map<String, Object> out = call.execute(in);
        ArrayList<DTOMatrizRiesgo> matrizRiesgoArray = (ArrayList<DTOMatrizRiesgo>) out.get(SNConstantes.O_CURSOR);
        Iterator<DTOMatrizRiesgo> iteradorMatrizRiesgo = matrizRiesgoArray.iterator();

        matrizRiesgo = new DTOMatrizRiesgo();

        while (iteradorMatrizRiesgo.hasNext()) {
            DTOMatrizRiesgo itrMatrizRiesgo = iteradorMatrizRiesgo.next();
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
    public DTOComentarioAuditoria obtenerComentarioAuditoriaRiesgo(ComentarioAuditoriaBean comentarioAuditoriaBean) throws DataAccessException {
        logger.info("Inicio MatrizRiesgoDaoImpl - obtenerComentarioAuditoriaRiesgo");

        DTOComentarioAuditoria dtoComentarioAuditoria;

        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_TRANSACCION)
                .withProcedureName(SNConstantes.SP_OBTENER_COMENT_DETA_RIESGO)
                .declareParameters(new SqlParameter("p_ID_MATRIZ_RIESGO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_DETA_MATRIZRIESGO", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTOComentarioAuditoria.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_DETA_MATRIZRIESGO",comentarioAuditoriaBean.getIdDetaMatrizRiesgo())
                .addValue("p_ID_MATRIZ_RIESGO", comentarioAuditoriaBean.getIdMatrizRiesgo());

        Map<String, Object> out = call.execute(in);
        ArrayList<DTOComentarioAuditoria> comentarioRiesgoArray = (ArrayList<DTOComentarioAuditoria>) out.get(SNConstantes.O_CURSOR);
        Iterator<DTOComentarioAuditoria> iteradorComentarioRiesgo = comentarioRiesgoArray.iterator();

        dtoComentarioAuditoria = new DTOComentarioAuditoria();
        String comentario;

        while (iteradorComentarioRiesgo.hasNext()){
            DTOComentarioAuditoria itrComentarioAuditoria = iteradorComentarioRiesgo.next();
            dtoComentarioAuditoria.setIdMatrizRiesgo(itrComentarioAuditoria.getIdMatrizRiesgo());
            dtoComentarioAuditoria.setIdDetaMatrizRiesgo(itrComentarioAuditoria.getIdDetaMatrizRiesgo());
            comentario = (itrComentarioAuditoria.getComentarioAuditoria() == null) ? "" : itrComentarioAuditoria.getComentarioAuditoria();
            dtoComentarioAuditoria.setComentarioAuditoria(comentario);
        }

        logger.info("Fin MatrizRiesgoDaoImpl - obtenerComentarioAuditoriaRiesgo");
        return dtoComentarioAuditoria;
    }

    @Override
    public DTOGenerico listarDetalleMatrizRiesgo(Long idMatrizRiesgo, Long idUsuario) throws DataAccessException {
        logger.info("Inicio MatrizRiesgoDaoImpl - listarDetalleMatrizRiesgo");

        List<DTODetalleMatrizRiesgo> listaDetaMatrizRiesgo;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_TRANSACCION)
                .withProcedureName(SNConstantes.SP_LISTAR_DETALLE_MATRIZRIESGO)
                .declareParameters(new SqlParameter("p_ID_MATRIZ_RIESGO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_USUARIO", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTODetalleMatrizRiesgo.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_MATRIZ_RIESGO", idMatrizRiesgo)
                .addValue("p_ID_USUARIO", idUsuario);

        Map<String, Object> out = call.execute(in);
        listaDetaMatrizRiesgo = (List<DTODetalleMatrizRiesgo>) out.get(SNConstantes.O_CURSOR);
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
    public DTOMatrizRiesgo actualizarMatrizRiesgo(MatrizRiesgoBean matrizRiesgoBean) throws DataAccessException {
        logger.info("Inicio MatrizRiesgoDaoImpl - actualizarMatrizRiesgo");

        DTOMatrizRiesgo dtoMatrizRiesgo;
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

        dtoMatrizRiesgo = new DTOMatrizRiesgo();
        dtoMatrizRiesgo.setIdMatrizRiesgo(matrizRiesgoBean.getIdMatrizRiesgo());
        dtoMatrizRiesgo.setCodigoResultado(numeroResultado);
        dtoMatrizRiesgo.setDescripcionResultado(descripcionResultado);
        logger.info(" Número de error actualizar Matriz riesgo : {}", numeroResultado);

        logger.info("Fin de MatrizRiesgoDaoImpl - actualizarMatrizRiesgo");
        return dtoMatrizRiesgo;
    }



    @Override
    public DTODetalleMatrizRiesgo actualizarDetaMatrizRiesgo(DetalleMatrizRiesgoBean detalleMatrizRiesgo) throws DataAccessException {
        logger.info("Inicio MatrizRiesgoDaoImpl - actualizarDetaMatrizRiesgo");
        
         SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaIniPlanAcion = null;
        Date fechaFinPlanAccion = null;
        Date fechaPrevista = null;
        Date fechaVerificacion = null;
        try {
            fechaIniPlanAcion = parse.parse(detalleMatrizRiesgo.getFechaInicioPlanAccion());
            
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error de parseo");
        }
        
        try {
            fechaFinPlanAccion = parse.parse(detalleMatrizRiesgo.getFechaFinPlanAccion());
            
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error de parseo");
        }
        
        try {
            fechaPrevista = parse.parse(detalleMatrizRiesgo.getFechaPrevista());
            
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error de parseo");
        }
        
        try {
            fechaVerificacion = parse.parse(detalleMatrizRiesgo.getFechaVerificacion());
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error de parseo");
        }

        DTODetalleMatrizRiesgo dtoDetalleMatrizRiesgo;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_TRANSACCION)
                .withProcedureName(SNConstantes.SP_UPDATE_DETALLE_MATRIZRIESGO)
                .declareParameters(new SqlParameter("p_ID_DETA_MATRIZRIESGO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_MATRIZ_RIESGO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_SEDE_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_GERENCIA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_PERIODO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_COD_MATRIZ", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_MATRIZ_NIVEL", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_PROCESO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_SUBPROCESO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_DE_TITULO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_COD_RIESGO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_RIESGO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_PROCESO_IMPACTADO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_FODA", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_GRUPO_INTERES", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_ORIGENRIESGO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_FRECUENCIARIESGO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_TIPORIESGO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_NU_PROBA_INHE", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_NU_IMPA_INHE", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_NU_PUNTA_INHE", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_DE_SEVERIDAD_INHE", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_CONTROL_RIESGO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_DE_CONTROL", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_AREA_CONTROL", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_RESP_CONTROL", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_FRECU_CONTROL", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_OPOR_CONTROL", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_AUTO_CONTROL", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_DE_EVIDE_CONTROL", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_NU_PROBA_RES", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_NU_IMPA_RES", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_NU_PUNTA_RES", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_DE_SEVERIDAD_RES", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_IN_BAJA", OracleTypes.NUMBER))
                
                .declareParameters(new SqlParameter("p_COD_PLAN_ACCION", OracleTypes.VARCHAR))  //110821
                .declareParameters(new SqlParameter("p_DE_PLAN_ACCION", OracleTypes.VARCHAR))  //110821
                .declareParameters(new SqlParameter("p_ID_AREA_PLAN_ACCION", OracleTypes.VARCHAR))  //110821
                .declareParameters(new SqlParameter("p_ID_RESP_PLAN_ACCION", OracleTypes.VARCHAR))  //110821
                .declareParameters(new SqlParameter("p_FE_INI_PLAN_ACCION", OracleTypes.DATE))  //110821
                .declareParameters(new SqlParameter("p_ID_ESTADO_PLAN_ACCION", OracleTypes.NUMBER))  //110821
                .declareParameters(new SqlParameter("p_FE_FIN_PLAN_ACCION", OracleTypes.DATE))  //110821
                .declareParameters(new SqlParameter("p_FE_PREVISTA", OracleTypes.DATE))  //110821
                .declareParameters(new SqlParameter("p_IN_EFICAZ", OracleTypes.NUMBER))  //110821//110821
                .declareParameters(new SqlParameter("p_FE_VERIFICACION", OracleTypes.DATE))   //110821
                .declareParameters(new SqlParameter("p_ID_VERIFICADOR", OracleTypes.VARCHAR))  //110821
                .declareParameters(new SqlParameter("p_DE_EVIDENCIA", OracleTypes.VARCHAR))  //110821
                .declareParameters(new SqlParameter("p_DE_OBSERVACION", OracleTypes.VARCHAR))  //110821
                .declareParameters(new SqlParameter("p_ID_ESTRATEGIA_RESP", OracleTypes.NUMBER))  //170821
                .declareParameters(new SqlParameter("p_COD_INDICADOR", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_INDICADOR", OracleTypes.VARCHAR))  //170821
                .declareParameters(new SqlParameter("p_FRECUENCIA_INDICADOR", OracleTypes.VARCHAR))  //170821
                .declareParameters(new SqlParameter("p_META_KRI", OracleTypes.VARCHAR))  //170821
                .declareParameters(new SqlParameter("p_KRI_ACTUAL", OracleTypes.VARCHAR))  //170821
                .declareParameters(new SqlParameter("p_ID_USUA_MODI", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_USUA_MODI_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_DETA_MATRIZRIESGO", detalleMatrizRiesgo.getIdDetaMatrizRiesgo())
                .addValue("p_ID_MATRIZ_RIESGO", detalleMatrizRiesgo.getIdMatrizRiesgo())
                .addValue("p_ID_EMPRESA", detalleMatrizRiesgo.getIdEmpresa())
                .addValue("p_ID_SEDE_EMPRESA", detalleMatrizRiesgo.getIdSede())
                .addValue("p_ID_GERENCIA", detalleMatrizRiesgo.getIdGerencia())
                .addValue("p_ID_PERIODO", detalleMatrizRiesgo.getIdPeriodo())
                .addValue("p_COD_MATRIZ", detalleMatrizRiesgo.getCodMatriz())
                .addValue("p_ID_MATRIZ_NIVEL", detalleMatrizRiesgo.getIdMatrizNivel())
                .addValue("p_ID_PROCESO", detalleMatrizRiesgo.getIdProceso())
                .addValue("p_ID_SUBPROCESO", detalleMatrizRiesgo.getIdSubProceso())
                .addValue("p_DE_TITULO", detalleMatrizRiesgo.getDeTitulo())
                .addValue("p_COD_RIESGO", detalleMatrizRiesgo.getCodRiesgo())
                .addValue("p_DE_RIESGO", detalleMatrizRiesgo.getDeRiesgo())
                .addValue("p_DE_PROCESO_IMPACTADO", detalleMatrizRiesgo.getDeProcesoImpactado())
                .addValue("p_DE_FODA", detalleMatrizRiesgo.getDeFoda())
                .addValue("p_DE_GRUPO_INTERES", detalleMatrizRiesgo.getDeGrupoInteres())
                .addValue("p_ID_ORIGENRIESGO", detalleMatrizRiesgo.getIdOrigenRiesgo())
                .addValue("p_ID_FRECUENCIARIESGO", detalleMatrizRiesgo.getIdFrecuenciaRiesgo())
                .addValue("p_ID_TIPORIESGO", detalleMatrizRiesgo.getIdTipoRiesgo())
                .addValue("p_NU_PROBA_INHE", detalleMatrizRiesgo.getNuProbabilidadInherente())
                .addValue("p_NU_IMPA_INHE", detalleMatrizRiesgo.getNuImpactoInherente())
                .addValue("p_NU_PUNTA_INHE", detalleMatrizRiesgo.getNuPuntajeInherente())
                .addValue("p_DE_SEVERIDAD_INHE", detalleMatrizRiesgo.getDeSeveridadInherente())
                .addValue("p_ID_CONTROL_RIESGO", detalleMatrizRiesgo.getIdControl())
                .addValue("p_DE_CONTROL", detalleMatrizRiesgo.getDeControl())
                .addValue("p_ID_AREA_CONTROL", detalleMatrizRiesgo.getIdAreaControl())
                .addValue("p_ID_RESP_CONTROL", detalleMatrizRiesgo.getIdResponsableControl())
                .addValue("p_ID_FRECU_CONTROL", detalleMatrizRiesgo.getIdFrecuenciaControl())
                .addValue("p_ID_OPOR_CONTROL", detalleMatrizRiesgo.getIdOportunidadControl())
                .addValue("p_ID_AUTO_CONTROL", detalleMatrizRiesgo.getIdAutomatizacionControl())
                .addValue("p_DE_EVIDE_CONTROL", detalleMatrizRiesgo.getDeEvidenciaControl())
                .addValue("p_NU_PROBA_RES", detalleMatrizRiesgo.getNuProbabilidadResidual())
                .addValue("p_NU_IMPA_RES", detalleMatrizRiesgo.getNuImpactoResidual())
                .addValue("p_NU_PUNTA_RES", detalleMatrizRiesgo.getNuPuntajeResidual())
                .addValue("p_DE_SEVERIDAD_RES", detalleMatrizRiesgo.getDeSeveridadResidual())
                .addValue("p_IN_BAJA", detalleMatrizRiesgo.getIndicadorBaja())
                .addValue("p_COD_PLAN_ACCION", detalleMatrizRiesgo.getCodPlanAccion())  //110821
                .addValue("p_DE_PLAN_ACCION", detalleMatrizRiesgo.getDesPlanAccion()) //110821
                .addValue("p_ID_AREA_PLAN_ACCION", detalleMatrizRiesgo.getIdAreaPlanAccion()) //110821
                .addValue("p_ID_RESP_PLAN_ACCION", detalleMatrizRiesgo.getIdResponsablePlanAccion()) //110821
                .addValue("p_FE_INI_PLAN_ACCION", fechaIniPlanAcion) //110821
                .addValue("p_ID_ESTADO_PLAN_ACCION", detalleMatrizRiesgo.getEstadoPlanAccion()) //110821
                .addValue("p_FE_FIN_PLAN_ACCION", fechaFinPlanAccion) //110821
                .addValue("p_FE_PREVISTA", fechaPrevista) //110821
                .addValue("p_IN_EFICAZ", detalleMatrizRiesgo.getFueEficaz()) //110821
                .addValue("p_FE_VERIFICACION", fechaVerificacion) //110821
                .addValue("p_ID_VERIFICADOR", detalleMatrizRiesgo.getVerificadoPor()) //110821
                .addValue("p_DE_EVIDENCIA", detalleMatrizRiesgo.getEvidenciaEficacia()) //110821
                .addValue("p_DE_OBSERVACION", detalleMatrizRiesgo.getObservaciones()) //110821

                .addValue("p_ID_ESTRATEGIA_RESP", detalleMatrizRiesgo.getIdEstrategiaResp()) //170821
                .addValue("p_COD_INDICADOR",detalleMatrizRiesgo.getCodkri())
                .addValue("p_DE_INDICADOR",detalleMatrizRiesgo.getDefKri())
                .addValue("p_FRECUENCIA_INDICADOR", detalleMatrizRiesgo.getFrecuencia()) //170821
                .addValue("p_META_KRI", detalleMatrizRiesgo.getMetkri()) //170821
                .addValue("p_KRI_ACTUAL", detalleMatrizRiesgo.getKriActual()) //170821
                .addValue("p_ID_RESP_INDICADOR", detalleMatrizRiesgo.getKriResponsable()) //170821
                
                .addValue("p_ID_USUA_MODI", detalleMatrizRiesgo.getUsuarioModificacion())
                .addValue("p_DE_USUA_MODI_IP", detalleMatrizRiesgo.getIpModificacion());

        Map<String, Object> out = call.execute(in);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionResultado = out.get(SNConstantes.DE_ERROR).toString();

        dtoDetalleMatrizRiesgo = new DTODetalleMatrizRiesgo();
        dtoDetalleMatrizRiesgo.setIdDetaMatrizRiesgo(detalleMatrizRiesgo.getIdDetaMatrizRiesgo());
        dtoDetalleMatrizRiesgo.setCodigoResultado(numeroResultado);
        dtoDetalleMatrizRiesgo.setDescripcionResultado(descripcionResultado);
        logger.info(" Número de error actualizar detalle de matriz riesgo : {}", numeroResultado);

        logger.info("Fin de MatrizRiesgoDaoImpl - actualizarDetaMatrizRiesgo");
        return dtoDetalleMatrizRiesgo;
    }

    @Override
    public DTOComentarioAuditoria actualizarComentarioAuditoriaRiesgo(ComentarioAuditoriaBean comentarioAuditoriaBean) throws DataAccessException {
        logger.info("Inicio MatrizRiesgoDaoImpl - actualizarComentarioAuditoriaRiesgo");

        DTOComentarioAuditoria dtoComentarioAuditoria;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_TRANSACCION)
                .withProcedureName(SNConstantes.SP_UPDATE_COMENT_DETA_RIESGO)
                .declareParameters(new SqlParameter("p_ID_DETA_MATRIZRIESGO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_MATRIZ_RIESGO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_COMENTARIO_AUDITORIA", OracleTypes.VARCHAR))

                .declareParameters(new SqlParameter("p_ID_USUA_MODI", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_USUA_MODI_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_DETA_MATRIZRIESGO",comentarioAuditoriaBean.getIdDetaMatrizRiesgo())
                .addValue("p_ID_MATRIZ_RIESGO", comentarioAuditoriaBean.getIdMatrizRiesgo())
                .addValue("p_COMENTARIO_AUDITORIA", comentarioAuditoriaBean.getComentarioAuditoria())

                .addValue("p_ID_USUA_MODI", comentarioAuditoriaBean.getUsuarioModificacion())
                .addValue("p_DE_USUA_MODI_IP", comentarioAuditoriaBean.getIpModificacion());

        Map<String, Object> out = call.execute(in);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionResultado = out.get(SNConstantes.DE_ERROR).toString();

        dtoComentarioAuditoria = new DTOComentarioAuditoria();
        dtoComentarioAuditoria.setCodigoResultado(numeroResultado);
        dtoComentarioAuditoria.setDescripcionResultado(descripcionResultado);

        logger.info("Fin MatrizRiesgoDaoImpl - actualizarComentarioAuditoriaRiesgo");
        return dtoComentarioAuditoria;
    }

    @Override
    public Byte anularMatrizRiesgo(MatrizRiesgoBean matrizRiesgoBean) throws DataAccessException {
        logger.info("Inicio MatrizRiesgoDaoImpl - anularMatrizRiesgo");

        Byte resultadoAnularMatriz;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_TRANSACCION)
                .withProcedureName(SNConstantes.SP_ANULAR_MATRIZ_RIESGO)
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
        logger.info("Número de error anular Matriz de riesgo : {}", result);
        logger.info("Descripcion de error anular Matriz de riesgo : {}", descripcionError);

        logger.info("Fin MatrizRiesgoDaoImpl - anularMatrizRiesgo");
        return resultadoAnularMatriz;
    }

    @Override
    public DTOGenerico ObtenerSeveridad(Float probabilidad, Float impacto, Long idTipoMatriz) throws DataAccessException {
        logger.info("Inicio de MatrizRiesgoDaoImpl - ObtenerSeveridad");

        DTOGenerico dtoGenerico = new DTOGenerico();

        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_TRANSACCION)
                .withProcedureName(SNConstantes.SP_OBTENER_SEVERIDAD)
                .declareParameters(new SqlParameter("p_PROBABILIDAD", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_IMPACTO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_TIPO_MATRIZ", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter("p_SEVERIDAD", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_PROBABILIDAD", probabilidad)
                .addValue("p_IMPACTO", impacto)
                .addValue("p_TIPO_MATRIZ", idTipoMatriz);

        Map<String, Object> out = call.execute(in);
        String severidad = out.get("p_SEVERIDAD").toString();
        dtoGenerico.setDescripcionSeveridad(severidad);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        dtoGenerico.setCodigoResultado(numeroResultado);
        String descripcionResultado = out.get(SNConstantes.DE_ERROR).toString();
        dtoGenerico.setDescripcionResultado(descripcionResultado);

        logger.info("Fin de MatrizRiesgoDaoImpl - ObtenerSeveridad");
        return dtoGenerico;
    }

    @Override
    public DTODetalleMatrizRiesgo obtenerDescripcion(String codRiesgo) throws DataAccessException {
        
        logger.info("Inicio MatrizRiesgoDaoImpl - Obtenerdescripcion "+codRiesgo);

        DTODetalleMatrizRiesgo DetaMatrizRiesgo;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_TRANSACCION)
                .withProcedureName(SNConstantes.SP_OBTENER_MATRIZ_RIESGO_DESCRIPCION)
                .declareParameters(new SqlParameter("p_COD_RIESGO", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTODetalleMatrizRiesgo.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_COD_RIESGO", codRiesgo);

        Map<String, Object> out = call.execute(in);
        ArrayList<DTODetalleMatrizRiesgo> matrizRiesgoArray = (ArrayList<DTODetalleMatrizRiesgo>) out.get(SNConstantes.O_CURSOR);
        Iterator<DTODetalleMatrizRiesgo> iteradorMatrizRiesgo = matrizRiesgoArray.iterator();

        DetaMatrizRiesgo = new DTODetalleMatrizRiesgo();

        int limite=0;
        while (iteradorMatrizRiesgo.hasNext()) {
            if(limite==0){
                DTODetalleMatrizRiesgo itrMatrizRiesgo = iteradorMatrizRiesgo.next();
            
                DetaMatrizRiesgo.setIdDetaMatrizRiesgo(itrMatrizRiesgo.getIdDetaMatrizRiesgo());
                DetaMatrizRiesgo.setDeRiesgo(itrMatrizRiesgo.getDeRiesgo());
            }
            
            limite++;
        }

        logger.info("Fin MatrizRiesgoDaoImpl - obtenerMatrizRiesgo");
        return DetaMatrizRiesgo;
    
    }

    @Override
    public DTOGenerico obtenerMatrizPeriodo(int idEmpresa, int idTipoMatriz, int matrizNivel) throws DataAccessException {

        logger.info("Inicio MatrizRiesgoDaoImpl - ObtenerMatrizPeriodo ");

        List<DTODetalleMatrizRiesgo> listaDetaMatrizRiesgo;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_TRANSACCION)
                .withProcedureName(SNConstantes.SP_OBTENER_MATRIZ_RIESGO_PERIODO)
                .declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_TIPO_MATRIZ", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_MATRIZ_NIVEL", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTODetalleMatrizRiesgo.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_EMPRESA", idEmpresa)
                .addValue("p_ID_TIPO_MATRIZ",idTipoMatriz)
                .addValue("p_MATRIZ_NIVEL", matrizNivel);

        Map<String, Object> out = call.execute(in);

        listaDetaMatrizRiesgo = (List<DTODetalleMatrizRiesgo>) out.get(SNConstantes.O_CURSOR);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionResultado = (String) out.get(SNConstantes.DE_ERROR);
        logger.info("numero resultado:" + numeroResultado);
        logger.info("descripcion error:" + descripcionResultado);

        DTOGenerico dtoGenerico = new DTOGenerico();
        dtoGenerico.setCodigoResultado(numeroResultado);
        dtoGenerico.setDescripcionResultado(descripcionResultado);
        dtoGenerico.setListado(listaDetaMatrizRiesgo);

        logger.info("Fin MatrizRiesgoDaoImpl - obtenerMatrizRiesgo");

        return dtoGenerico;
    }
}

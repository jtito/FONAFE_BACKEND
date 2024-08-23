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
import pe.gob.fonafe.sistemagestionriesgoapi.dao.IMatrizOportunidadDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.*;
import pe.gob.fonafe.sistemagestionriesgoapi.models.DetalleMatrizOportunidadBean;
import pe.gob.fonafe.sistemagestionriesgoapi.models.MatrizRiesgoOportunidadBean;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Repository
public class MatrizOportunidadDaoImpl implements IMatrizOportunidadDao {

    private final JdbcTemplate jdbcTemplate;

    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    public MatrizOportunidadDaoImpl(@Qualifier("jdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
  
    @Override
    public DTOMatrizOportunidad registrarMatrizOportunidad(MatrizRiesgoOportunidadBean matrizRiesgoBean) throws DataAccessException {
        logger.info("Inicio de MatrizOportunidadDaoImpl - registrarMatrizOportunidad");

        DTOMatrizOportunidad dtoMatrizContinuidad = new DTOMatrizOportunidad();

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
        dtoMatrizContinuidad.setIdMatrizRiesgo(idMatrizOut.longValue());
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        dtoMatrizContinuidad.setCodigoResultado(numeroResultado);
        String descripcionResultado = out.get(SNConstantes.DE_ERROR).toString();
        dtoMatrizContinuidad.setDescripcionResultado(descripcionResultado);

        logger.info("Fin de MatrizOportunidadDaoImpl - registrarMatrizOportunidad");
        return dtoMatrizContinuidad;
    }

	@Override
    public DTOGenerico registrarDetalleMatrizOportunidad(DetalleMatrizOportunidadBean matrizOportunidadBean) throws DataAccessException {
        logger.info("Inicio de MatrizOportunidadDaoImpl - registrarDetalleMatrizOportunidad");

        SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaIniPlanAcion = null;
        Date fechaFinPlanAccion = null;
        Date fechaPrevista = null;
        Date fechaVerificacion = null;
        try {
            fechaIniPlanAcion = parse.parse(matrizOportunidadBean.getFeIniPlanAccion());
            
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error de parseo");
        }
        
        try {
            fechaFinPlanAccion = parse.parse(matrizOportunidadBean.getFeFinPlanAccion());
            
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error de parseo");
        }
        
        try {
            fechaPrevista = parse.parse(matrizOportunidadBean.getFePrevista());
            
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error de parseo");
        }
        
        try {
            fechaVerificacion = parse.parse(matrizOportunidadBean.getFeVerificacion());
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error de parseo");
        }

		DTOGenerico dtoGenerico = new DTOGenerico();
        
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_TRANSACCION)
                .withProcedureName(SNConstantes.SP_INSERT_DETALLE_MATRIZOPORTUNIDAD)
                .declareParameters(new SqlParameter("p_ID_MATRIZ_RIESGO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_PERIODO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_MATRIZ_NIVEL", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_GERENCIA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_SEDE_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_DE_ORIGEN", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_GRUPO_INTERES", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_OBJETIVO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_PROCESO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_SUBPROCESO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_COD_OPOR", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_OPORTUNIDAD", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_NIVEL_COMPLEJIDAD", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_NIVEL_COSTO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_NU_VIABILIDAD", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_TIPO_BENEFICIO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_NU_BENEFICIO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_NU_NIVEL_PRIORI", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_DE_NIVEL_PRIORI", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_ESTRATEGIA_PLAN", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_COD_SAM", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_COD_PLAN_ACCION", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_PLAN_ACCION", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_RESP_PLAN_ACCION", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_RECURSO_FINA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_RECURSO_OPER", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_RECURSO_TECNO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_RECURSO_HUMA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_REQ_NEGO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_FE_INI_PLAN_ACCION", OracleTypes.DATE))
                .declareParameters(new SqlParameter("p_FE_FIN_PLAN_ACCION", OracleTypes.DATE))
                .declareParameters(new SqlParameter("p_DE_ENTREGABLE", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_ESTADO_PLAN_ACCION", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_FE_PREVISTA", OracleTypes.DATE))
                .declareParameters(new SqlParameter("p_IN_EFICAZ", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_FE_VERIFICACION", OracleTypes.DATE))
                .declareParameters(new SqlParameter("p_ID_VERIFICADOR", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_EVIDENCIA", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_COMENTARIO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_USUA_CREA", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_USUA_CREA_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter("p_ID_DETA_MATRIZOP_OUT", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));;

        SqlParameterSource in = new MapSqlParameterSource()
        		   .addValue("p_ID_MATRIZ_RIESGO",matrizOportunidadBean.getIdMatrizRiesgo())
        			.addValue("p_ID_PERIODO",matrizOportunidadBean.getIdPeriodo())
        			.addValue("p_ID_MATRIZ_NIVEL",matrizOportunidadBean.getIdMatrizNivel())
        			.addValue("p_ID_GERENCIA",matrizOportunidadBean.getIdGerencia())
        			.addValue("p_ID_EMPRESA",matrizOportunidadBean.getIdEmpresa())
        			.addValue("p_ID_SEDE_EMPRESA",matrizOportunidadBean.getIdSedeEmpresa())
        			.addValue("p_DE_ORIGEN",matrizOportunidadBean.getDeOrigen())
        			.addValue("p_DE_GRUPO_INTERES",matrizOportunidadBean.getDeGrupoInteres())
        			.addValue("p_DE_OBJETIVO",matrizOportunidadBean.getDeObjetivo())
        			.addValue("p_ID_PROCESO",matrizOportunidadBean.getIdProceso())
        			.addValue("p_ID_SUBPROCESO",matrizOportunidadBean.getIdSubproceso())
        			.addValue("p_COD_OPOR",matrizOportunidadBean.getCodOpor())
        			.addValue("p_DE_OPORTUNIDAD",matrizOportunidadBean.getDeOportunidad())
        			.addValue("p_NIVEL_COMPLEJIDAD",matrizOportunidadBean.getNivelComplejidad())
        			.addValue("p_NIVEL_COSTO",matrizOportunidadBean.getNivelCosto())
        			.addValue("p_NU_VIABILIDAD",matrizOportunidadBean.getNuViabilidad())
        			.addValue("p_ID_TIPO_BENEFICIO",matrizOportunidadBean.getIdTipoBeneficio())
        			.addValue("p_NU_BENEFICIO",matrizOportunidadBean.getNuBeneficio())
        			.addValue("p_NU_NIVEL_PRIORI",matrizOportunidadBean.getNuNivelPriori())
        			.addValue("p_DE_NIVEL_PRIORI",matrizOportunidadBean.getDeNivelPriori())
        			.addValue("p_ID_ESTRATEGIA_PLAN",matrizOportunidadBean.getIdEstrategiaPlan())
        			.addValue("p_COD_SAM",matrizOportunidadBean.getCodSam())
        			.addValue("p_COD_PLAN_ACCION",matrizOportunidadBean.getCodPlanAccion())
        			.addValue("p_DE_PLAN_ACCION",matrizOportunidadBean.getDePlanAccion())
        			.addValue("p_ID_RESP_PLAN_ACCION",matrizOportunidadBean.getIdRespPlanAccion())
        			.addValue("p_RECURSO_FINA",matrizOportunidadBean.getRecursoFina())
        			.addValue("p_RECURSO_OPER",matrizOportunidadBean.getRecursoOper())
        			.addValue("p_RECURSO_TECNO",matrizOportunidadBean.getRecursoTecno())
        			.addValue("p_RECURSO_HUMA",matrizOportunidadBean.getRecursoHuma())
        			.addValue("p_REQ_NEGO",matrizOportunidadBean.getReqNego())
        			.addValue("p_FE_INI_PLAN_ACCION",fechaIniPlanAcion)
        			.addValue("p_FE_FIN_PLAN_ACCION",fechaFinPlanAccion)
        			.addValue("p_DE_ENTREGABLE",matrizOportunidadBean.getDeEntregable())
        			.addValue("p_ID_ESTADO_PLAN_ACCION",matrizOportunidadBean.getIdEstadoPlanAccion())
        			.addValue("p_FE_PREVISTA",fechaPrevista)
        			.addValue("p_IN_EFICAZ",matrizOportunidadBean.getInEficaz())
        			.addValue("p_FE_VERIFICACION",fechaVerificacion)
        			.addValue("p_ID_VERIFICADOR",matrizOportunidadBean.getIdVerificador())
        			.addValue("p_DE_EVIDENCIA",matrizOportunidadBean.getDeEvidencia())
        			.addValue("p_DE_COMENTARIO",matrizOportunidadBean.getDeComentario())
        			.addValue("p_ID_USUA_CREA",matrizOportunidadBean.getIdUsuaCrea())
        			.addValue("p_DE_USUA_CREA_IP",matrizOportunidadBean.getDeUsuaCreaIp());

        Map<String, Object> out = call.execute(in);
        BigDecimal idDetaMatrizOut = (BigDecimal) out.get("p_ID_DETA_MATRIZOP_OUT");
        dtoGenerico.setIdGenerico(idDetaMatrizOut.longValue());
		BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
		dtoGenerico.setCodigoResultado(numeroResultado);
		String descripcionResultado = out.get(SNConstantes.DE_ERROR).toString();
		dtoGenerico.setDescripcionResultado(descripcionResultado);

        logger.info("Fin de MatrizOportunidadDaoImpl - registrarDetalleMatrizOportunidad");
        return dtoGenerico;

    }
    
    
    @Override
	public DTOGenerico listarDetalleMatrizOportunidad(Long idMatrizRiesgo, Long idUsuario) throws DataAccessException {
	    logger.info("Inicio MatrizOportunidadDaoImpl - listarDetalleMatrizOportunidad");

        List<DTODetalleMatrizOportunidad> listaDetaMatrizOportunidad;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_TRANSACCION)
                .withProcedureName(SNConstantes.SP_OBTENER_DETALLE_MATRIZOPOR)
                .declareParameters(new SqlParameter("p_ID_MATRIZ_RIESGO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_USUARIO", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTODetalleMatrizOportunidad.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_MATRIZ_RIESGO", idMatrizRiesgo)
                .addValue("p_ID_USUARIO", idUsuario);

        Map<String, Object> out = call.execute(in);
        listaDetaMatrizOportunidad = (List<DTODetalleMatrizOportunidad>) out.get(SNConstantes.O_CURSOR);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionResultado = (String) out.get(SNConstantes.DE_ERROR);
        logger.info("numero resultado:" + numeroResultado);
        logger.info("descripcion error:" + descripcionResultado);

        DTOGenerico dtoGenerico = new DTOGenerico();
        dtoGenerico.setCodigoResultado(numeroResultado);
        dtoGenerico.setDescripcionResultado(descripcionResultado);
        dtoGenerico.setListado(listaDetaMatrizOportunidad);

        logger.info("Fin MatrizOportunidadDaoImpl - listarDetalleMatrizOportunidad");
        return dtoGenerico;
	}

	@Override
	public DTOMatrizOportunidad actualizarMatrizOportunidad(MatrizRiesgoOportunidadBean matrizRiesgoOportunidadBean) throws DataAccessException {
		logger.info("Inicio de MatrizOportunidadDaoImpl - actualizarMatrizOportunidad");

		DTOMatrizOportunidad dtoMatrizOportunidad;
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
				.addValue("p_ID_MATRIZ_RIESGO", matrizRiesgoOportunidadBean.getIdMatrizRiesgo())
				.addValue("p_ID_EMPRESA", matrizRiesgoOportunidadBean.getIdEmpresa())
				.addValue("p_ID_SEDE_EMPRESA", matrizRiesgoOportunidadBean.getIdSede())
				.addValue("p_ID_GERENCIA", matrizRiesgoOportunidadBean.getIdGerencia())
				.addValue("p_ID_PERIODO", matrizRiesgoOportunidadBean.getIdPeriodo())
				.addValue("p_ID_TIPO_MATRIZ", matrizRiesgoOportunidadBean.getIdTipoMatriz())
				.addValue("p_ID_MATRIZ_NIVEL", matrizRiesgoOportunidadBean.getIdMatrizNivel())
				.addValue("p_IN_BAJA", matrizRiesgoOportunidadBean.getIndicadorBaja())
				.addValue("p_ID_USUA_MODI", matrizRiesgoOportunidadBean.getUsuarioModificacion())
				.addValue("p_DE_USUA_MODI_IP", matrizRiesgoOportunidadBean.getIpModificacion());

		Map<String, Object> out = call.execute(in);
		BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
		String descripcionResultado = out.get(SNConstantes.DE_ERROR).toString();

		dtoMatrizOportunidad = new DTOMatrizOportunidad();
		dtoMatrizOportunidad.setIdMatrizRiesgo(matrizRiesgoOportunidadBean.getIdMatrizRiesgo());
		dtoMatrizOportunidad.setCodigoResultado(numeroResultado);
		dtoMatrizOportunidad.setDescripcionResultado(descripcionResultado);
		logger.info(" Número de error actualizar Matriz fraude : {}", numeroResultado);

		logger.info("Fin de MatrizOportunidadDaoImpl - actualizarMatrizOportunidad");
		return dtoMatrizOportunidad;
	}

	@Override
	public DTOGenerico actualizarDetalleMatrizOportunidad(DetalleMatrizOportunidadBean matrizOportunidadBean)
			throws DataAccessException {
		 DTOGenerico dtoGenerico = new DTOGenerico();
                 
                 
        SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaIniPlanAcion = null;
        Date fechaFinPlanAccion = null;
        Date fechaPrevista = null;
        Date fechaVerificacion = null;
        try {
            fechaIniPlanAcion = parse.parse(matrizOportunidadBean.getFeIniPlanAccion());
            
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error de parseo");
        }
        
        try {
            fechaFinPlanAccion = parse.parse(matrizOportunidadBean.getFeFinPlanAccion());
            
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error de parseo");
        }
        
        try {
            fechaPrevista = parse.parse(matrizOportunidadBean.getFePrevista());
            
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error de parseo");
        }
        
        try {
            fechaVerificacion = parse.parse(matrizOportunidadBean.getFeVerificacion());
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error de parseo");
        }

	        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
	                .withCatalogName(SNConstantes.PKG_TRANSACCION)
	                .withProcedureName(SNConstantes.SP_UPDATE_DETALLE_MATRIZOPORAD)
	                .declareParameters(new SqlParameter("p_ID_DETA_MATRIZOPORTUNIDAD", OracleTypes.NUMBER))
	                .declareParameters(new SqlParameter("p_ID_MATRIZ_RIESGO", OracleTypes.NUMBER))
	                .declareParameters(new SqlParameter("p_ID_PERIODO", OracleTypes.NUMBER))
	                .declareParameters(new SqlParameter("p_ID_MATRIZ_NIVEL", OracleTypes.NUMBER))
	                .declareParameters(new SqlParameter("p_ID_GERENCIA", OracleTypes.NUMBER))
	                .declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.NUMBER))
	                .declareParameters(new SqlParameter("p_ID_SEDE_EMPRESA", OracleTypes.NUMBER))
	                .declareParameters(new SqlParameter("p_DE_ORIGEN", OracleTypes.VARCHAR))
	                .declareParameters(new SqlParameter("p_DE_GRUPO_INTERES", OracleTypes.VARCHAR))
	                .declareParameters(new SqlParameter("p_DE_OBJETIVO", OracleTypes.VARCHAR))
	                .declareParameters(new SqlParameter("p_ID_PROCESO", OracleTypes.NUMBER))
	                .declareParameters(new SqlParameter("p_ID_SUBPROCESO", OracleTypes.NUMBER))
	                .declareParameters(new SqlParameter("p_COD_OPOR", OracleTypes.VARCHAR))
	                .declareParameters(new SqlParameter("p_DE_OPORTUNIDAD", OracleTypes.VARCHAR))
	                .declareParameters(new SqlParameter("p_NIVEL_COMPLEJIDAD", OracleTypes.NUMBER))
	                .declareParameters(new SqlParameter("p_NIVEL_COSTO", OracleTypes.NUMBER))
	                .declareParameters(new SqlParameter("p_NU_VIABILIDAD", OracleTypes.NUMBER))
	                .declareParameters(new SqlParameter("p_ID_TIPO_BENEFICIO", OracleTypes.NUMBER))
	                .declareParameters(new SqlParameter("p_NU_BENEFICIO", OracleTypes.NUMBER))
	                .declareParameters(new SqlParameter("p_NU_NIVEL_PRIORI", OracleTypes.NUMBER))
	                .declareParameters(new SqlParameter("p_DE_NIVEL_PRIORI", OracleTypes.VARCHAR))
	                .declareParameters(new SqlParameter("p_ID_ESTRATEGIA_PLAN", OracleTypes.NUMBER))
	                .declareParameters(new SqlParameter("p_COD_SAM", OracleTypes.VARCHAR))
	                .declareParameters(new SqlParameter("p_COD_PLAN_ACCION", OracleTypes.VARCHAR))
	                .declareParameters(new SqlParameter("p_DE_PLAN_ACCION", OracleTypes.VARCHAR))
	                .declareParameters(new SqlParameter("p_ID_RESP_PLAN_ACCION", OracleTypes.VARCHAR))
	                .declareParameters(new SqlParameter("p_RECURSO_FINA", OracleTypes.NUMBER))
	                .declareParameters(new SqlParameter("p_RECURSO_OPER", OracleTypes.NUMBER))
	                .declareParameters(new SqlParameter("p_RECURSO_TECNO", OracleTypes.NUMBER))
	                .declareParameters(new SqlParameter("p_RECURSO_HUMA", OracleTypes.NUMBER))
	                .declareParameters(new SqlParameter("p_REQ_NEGO", OracleTypes.NUMBER))
	                .declareParameters(new SqlParameter("p_FE_INI_PLAN_ACCION", OracleTypes.DATE))
	                .declareParameters(new SqlParameter("p_FE_FIN_PLAN_ACCION", OracleTypes.DATE))
	                .declareParameters(new SqlParameter("p_DE_ENTREGABLE", OracleTypes.VARCHAR))
	                .declareParameters(new SqlParameter("p_ID_ESTADO_PLAN_ACCION", OracleTypes.NUMBER))
	                .declareParameters(new SqlParameter("p_FE_PREVISTA", OracleTypes.DATE))
	                .declareParameters(new SqlParameter("p_IN_EFICAZ", OracleTypes.NUMBER))
	                .declareParameters(new SqlParameter("p_FE_VERIFICACION", OracleTypes.DATE))
	                .declareParameters(new SqlParameter("p_ID_VERIFICADOR", OracleTypes.VARCHAR))
	                .declareParameters(new SqlParameter("p_DE_EVIDENCIA", OracleTypes.VARCHAR))
	                .declareParameters(new SqlParameter("p_DE_COMENTARIO", OracleTypes.VARCHAR))
	                .declareParameters(new SqlParameter("p_ID_USUA_MODI", OracleTypes.VARCHAR))
	                .declareParameters(new SqlParameter("p_DE_USUA_MODI_IP", OracleTypes.VARCHAR))
	                .declareParameters(new SqlParameter("p_IN_BAJA", OracleTypes.NUMBER))
	                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER)) 
	                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));

	        SqlParameterSource in = new MapSqlParameterSource()
	        	    .addValue("p_ID_DETA_MATRIZOPORTUNIDAD",matrizOportunidadBean.getIdDetalleMOportunidad())
	      			.addValue("p_ID_MATRIZ_RIESGO",matrizOportunidadBean.getIdMatrizRiesgo())
	      			.addValue("p_ID_PERIODO",matrizOportunidadBean.getIdPeriodo())
	      			.addValue("p_ID_MATRIZ_NIVEL",matrizOportunidadBean.getIdMatrizNivel())
	      			.addValue("p_ID_GERENCIA",matrizOportunidadBean.getIdGerencia())
	      			.addValue("p_ID_EMPRESA",matrizOportunidadBean.getIdEmpresa())
	      			.addValue("p_ID_SEDE_EMPRESA",matrizOportunidadBean.getIdSedeEmpresa())
	      			.addValue("p_DE_ORIGEN",matrizOportunidadBean.getDeOrigen())
	      			.addValue("p_DE_GRUPO_INTERES",matrizOportunidadBean.getDeGrupoInteres())
	      			.addValue("p_DE_OBJETIVO",matrizOportunidadBean.getDeObjetivo())
	      			.addValue("p_ID_PROCESO",matrizOportunidadBean.getIdProceso())
	      			.addValue("p_ID_SUBPROCESO",matrizOportunidadBean.getIdSubproceso())
	      			.addValue("p_COD_OPOR",matrizOportunidadBean.getCodOpor())
	      			.addValue("p_DE_OPORTUNIDAD",matrizOportunidadBean.getDeOportunidad())
	      			.addValue("p_NIVEL_COMPLEJIDAD",matrizOportunidadBean.getNivelComplejidad())
	      			.addValue("p_NIVEL_COSTO",matrizOportunidadBean.getNivelCosto())
	      			.addValue("p_NU_VIABILIDAD",matrizOportunidadBean.getNuViabilidad())
	      			.addValue("p_ID_TIPO_BENEFICIO",matrizOportunidadBean.getIdTipoBeneficio())
	      			.addValue("p_NU_BENEFICIO",matrizOportunidadBean.getNuBeneficio())
	      			.addValue("p_NU_NIVEL_PRIORI",matrizOportunidadBean.getNuNivelPriori())
	      			.addValue("p_DE_NIVEL_PRIORI",matrizOportunidadBean.getDeNivelPriori())
	      			.addValue("p_ID_ESTRATEGIA_PLAN",matrizOportunidadBean.getIdEstrategiaPlan())
	      			.addValue("p_COD_SAM",matrizOportunidadBean.getCodSam())
	      			.addValue("p_COD_PLAN_ACCION",matrizOportunidadBean.getCodPlanAccion())
	      			.addValue("p_DE_PLAN_ACCION",matrizOportunidadBean.getDePlanAccion())
	      			.addValue("p_ID_RESP_PLAN_ACCION",matrizOportunidadBean.getIdRespPlanAccion())
	      			.addValue("p_RECURSO_FINA",matrizOportunidadBean.getRecursoFina())
	      			.addValue("p_RECURSO_OPER",matrizOportunidadBean.getRecursoOper())
	      			.addValue("p_RECURSO_TECNO",matrizOportunidadBean.getRecursoTecno())
	      			.addValue("p_RECURSO_HUMA",matrizOportunidadBean.getRecursoHuma())
	      			.addValue("p_REQ_NEGO",matrizOportunidadBean.getReqNego())
	      			.addValue("p_FE_INI_PLAN_ACCION",fechaIniPlanAcion)
	      			.addValue("p_FE_FIN_PLAN_ACCION",fechaFinPlanAccion)
	      			.addValue("p_DE_ENTREGABLE",matrizOportunidadBean.getDeEntregable())
	      			.addValue("p_ID_ESTADO_PLAN_ACCION",matrizOportunidadBean.getIdEstadoPlanAccion())
	      			.addValue("p_FE_PREVISTA",fechaPrevista)
	      			.addValue("p_IN_EFICAZ",matrizOportunidadBean.getInEficaz())
	      			.addValue("p_FE_VERIFICACION",fechaVerificacion)
	      			.addValue("p_ID_VERIFICADOR",matrizOportunidadBean.getIdVerificador())
	      			.addValue("p_DE_EVIDENCIA",matrizOportunidadBean.getDeEvidencia())
	      			.addValue("p_DE_COMENTARIO",matrizOportunidadBean.getDeComentario())
	      			.addValue("p_ID_USUA_MODI",matrizOportunidadBean.getIdUsuaModi())
	      			.addValue("p_DE_USUA_MODI_IP",matrizOportunidadBean.getDeUsuaModiIp())
	      			.addValue("p_IN_BAJA",matrizOportunidadBean.getInBaja());

	        Map<String, Object> out = call.execute(in);
	        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
	        dtoGenerico.setCodigoResultado(numeroResultado);
	        String descripcionResultado = out.get(SNConstantes.DE_ERROR).toString();
	        dtoGenerico.setDescripcionResultado(descripcionResultado);
	        logger.info(" Número de error actualizar detalle de matriz Oportunidad : {}", numeroResultado);
	        logger.info("Fin de MatrizOportunidadDaoImpl - actualizarDetaMatrizOportunidad");
	        return dtoGenerico; 
	}

    @Override
    public DTOMatrizOportunidad obtenerMatrizOportunidad(Integer idMatrizRiesgo) throws DataAccessException {
        logger.info("Inicio MatrizRiesgoDaoImpl - obtenerMatrizRiesgo");

        DTOMatrizOportunidad matrizopor;

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
        ArrayList<DTOMatrizOportunidad> matrizRiesgoArray = (ArrayList<DTOMatrizOportunidad>) out.get(SNConstantes.O_CURSOR);
        Iterator<DTOMatrizOportunidad> iteradorMatrizRiesgo = matrizRiesgoArray.iterator();

        matrizopor = new DTOMatrizOportunidad();

        while (iteradorMatrizRiesgo.hasNext()) {
        	DTOMatrizOportunidad itrMatrizRiesgo = iteradorMatrizRiesgo.next();
        	matrizopor.setIdMatrizRiesgo(itrMatrizRiesgo.getIdMatrizRiesgo());
        	matrizopor.setIdEmpresa(itrMatrizRiesgo.getIdEmpresa());
        	matrizopor.setIdSede(itrMatrizRiesgo.getIdSede());
            matrizopor.setIdGerencia(itrMatrizRiesgo.getIdGerencia());
            matrizopor.setIdPeriodo(itrMatrizRiesgo.getIdPeriodo());
            matrizopor.setIdTipoMatriz(itrMatrizRiesgo.getIdTipoMatriz());
            matrizopor.setIdMatrizNivel(itrMatrizRiesgo.getIdMatrizNivel());
            matrizopor.setIndicadorBaja(itrMatrizRiesgo.getIndicadorBaja());
            matrizopor.setFechaCreacion(itrMatrizRiesgo.getFechaCreacion());
            matrizopor.setMatrizNivel(itrMatrizRiesgo.getMatrizNivel());
        }

        logger.info("Fin MatrizRiesgoDaoImpl - obtenerMatrizRiesgo");
        return matrizopor;
    }

	@SuppressWarnings({ "unchecked" })
	@Override
	public List<DTOMatrizRiesgo> listarBandejaMatrizOportunidad(int idEmpresa, int idPeriodo, int idMatrizNivel)
			throws DataAccessException {
		  logger.info("Inicio MatrizOportunidadDaoImpl - listarBandejaMatrizOportunidad");

	        List<DTOMatrizRiesgo> listaBandejaMatrizRiesgo = new  ArrayList<>();
	        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
	                .withCatalogName(SNConstantes.PKG_TRANSACCION)
	                .withProcedureName(SNConstantes.SP_LISTAR_BNDJA_MATRIZ_OPOR)
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

	        logger.info("Fin MatrizOportunidadDaoImpl - listarBandejaMatrizOportunidad");
	        return listaBandejaMatrizRiesgo;
	}

    @Override
    public Byte anularMatrizRiesgo(MatrizRiesgoOportunidadBean matrizRiesgoBean) throws DataAccessException {
        
        
        logger.info("Inicio MatrizOportunidadDaoImpl - anularMatrizOportunidad");

        Byte resultadoAnularMatriz;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_TRANSACCION)
                .withProcedureName(SNConstantes.SP_ANULAR_MATRIZ_OPOR)
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
}
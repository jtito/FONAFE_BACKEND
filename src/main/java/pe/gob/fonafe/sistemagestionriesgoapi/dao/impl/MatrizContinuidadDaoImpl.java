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
import pe.gob.fonafe.sistemagestionriesgoapi.dao.IMatrizContinuidadDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.*;
import pe.gob.fonafe.sistemagestionriesgoapi.models.DetalleMatrizContinuidadBean;
import pe.gob.fonafe.sistemagestionriesgoapi.models.MatrizRiesgoContinuidadBean;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Repository
public class MatrizContinuidadDaoImpl implements IMatrizContinuidadDao {

    private final JdbcTemplate jdbcTemplate;

    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    public MatrizContinuidadDaoImpl(@Qualifier("jdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<DTOMatrizContinuidad> listarBandejaMatrizContinuidad(int idEmpresa, int idPeriodo, int idMatrizNivel) throws DataAccessException {
        logger.info("Inicio MatrizContinuidadDaoImpl - listarBandejaMatrizContinuidad");

        List<DTOMatrizContinuidad> listaBandejaMatrizContinuidad;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_TRANSACCION)
                .withProcedureName(SNConstantes.SP_LISTAR_BANDEJA_MATRIZ_CONTINUIDAD)
                .declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_PERIODO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_MATRIZ_NIVEL", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTOMatrizContinuidad.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_EMPRESA", idEmpresa)
                .addValue("p_ID_PERIODO", idPeriodo)
                .addValue("p_ID_MATRIZ_NIVEL", idMatrizNivel);

        Map<String, Object> out = call.execute(in);
        listaBandejaMatrizContinuidad = (List<DTOMatrizContinuidad>) out.get(SNConstantes.O_CURSOR);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionError = (String) out.get(SNConstantes.DE_ERROR);
        logger.info("numero resultado: {} ", numeroResultado);
        logger.info("descripcion error: {} ", descripcionError);

        logger.info("Fin MatrizContinuidadDaoImpl - listarBandejaMatrizContinuidad");
        return listaBandejaMatrizContinuidad;
    }

    @Override
    public DTOMatrizContinuidad registrarMatrizRiesgo(MatrizRiesgoContinuidadBean matrizRiesgoBean) throws DataAccessException {
        logger.info("Inicio de MatrizContinuidadDaoImpl - registrarMatrizRiesgo");

        DTOMatrizContinuidad dtoMatrizContinuidad = new DTOMatrizContinuidad();

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

        logger.info("Fin de MatrizContinuidadDaoImpl - registrarMatrizRiesgo");
        return dtoMatrizContinuidad;
    }

    @Override
    public DTOGenerico registrarDetalleMatrizContinuidad(DetalleMatrizContinuidadBean detalleMatrizContinuidad) throws DataAccessException {
        logger.info("Inicio de MatrizContinuidadDaoImpl - registrarDetalleMatrizContinuidad");

        SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaIniPlanAcion = null;
        Date fechaFinPlanAccion = null;
        try {
            fechaIniPlanAcion = parse.parse(detalleMatrizContinuidad.getFeIniPlanAccion());

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error de parseo");
        }

        try {
            fechaFinPlanAccion = parse.parse(detalleMatrizContinuidad.getFeFinPlanAccion());

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error de parseo");
        }

        DTOGenerico dtoGenerico = new DTOGenerico();

        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_TRANSACCION)
                .withProcedureName(SNConstantes.SP_INSERT_DETALLE_MATRIZCONTINUIDAD)
                .declareParameters(new SqlParameter("ID_MATRIZ_RIESGO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("ID_PERIODO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("ID_PERIODO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_COD_MATRIZ", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("ID_MATRIZ_NIVEL", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("ID_GERENCIA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_SEDE_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_PROCESO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_SUBPROCESO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("COD_RIESGO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("DE_RIESGO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("NU_PROBA_INHE", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("NU_IMPA_INHE", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("NU_PUNTA_INHE", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("DE_SEVERIDAD_INHE", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("COD_CONTROL", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("DE_CONTROL", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("ID_EFEC_CONTROL", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("ADIC_CONTROL", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("INFRA_CONTROL", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("RE_HU_CONTROL", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("RE_TI_CONTROL", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("REG_VITAL_CONTROL", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("PROVE_CONTROL", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("OTROS_CONTROL", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("NU_PROBA_RES", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("NU_IMPA_RES", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("NU_PUNTA_RES", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("DE_SEVERIDAD_RES", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("ID_ESTRATEGIA_PLAN", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("COD_PLAN_ACCION", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("DE_PLAN_ACCION", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("ID_AREA_PLAN_ACCION", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("ID_RESP_PLAN_ACCION", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("FE_INI_PLAN_ACCION", OracleTypes.DATE))
                .declareParameters(new SqlParameter("ID_ESTADO_PLAN_ACCION", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("FE_FIN_PLAN_ACCION", OracleTypes.DATE))
                .declareParameters(new SqlParameter("COME_PLAN_ACCION", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("ID_USUA_CREA", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("DE_USUA_CREA_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter("p_ID_DETA_MATRIZCONT_OUT", OracleTypes.NUMBER))  //110821
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))  //110821
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));;

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_MATRIZ_RIESGO", detalleMatrizContinuidad.getIdMatrizRiesgo())
                .addValue("p_ID_PERIODO", detalleMatrizContinuidad.getIdPeriodo())
                .addValue("p_COD_MATRIZ", detalleMatrizContinuidad.getCodMatriz())
                .addValue("p_ID_MATRIZ_NIVEL", detalleMatrizContinuidad.getIdMatrizNivel())
                .addValue("p_ID_EMPRESA", detalleMatrizContinuidad.getIdEmpresa())
                .addValue("p_ID_GERENCIA", detalleMatrizContinuidad.getIdGerencia())
                .addValue("p_ID_EMPRESA", detalleMatrizContinuidad.getIdEmpresa())
                .addValue("p_ID_SEDE_EMPRESA", detalleMatrizContinuidad.getIdSede())
                .addValue("p_ID_PROCESO", detalleMatrizContinuidad.getIdProceso())
                .addValue("p_ID_SUBPROCESO", detalleMatrizContinuidad.getIdSubProceso())
                .addValue("p_COD_RIESGO", detalleMatrizContinuidad.getCodRiesgo())  //090821
                .addValue("p_DE_RIESGO", detalleMatrizContinuidad.getDeRiesgo())
                .addValue("p_NU_PROBA_INHE", detalleMatrizContinuidad.getNuProbabilidadInherente())
                .addValue("p_NU_IMPA_INHE", detalleMatrizContinuidad.getNuImpactoInherente())
                .addValue("p_NU_PUNTA_INHE", detalleMatrizContinuidad.getNuPuntajeInherente())
                .addValue("p_DE_SEVERIDAD_INHE", detalleMatrizContinuidad.getDeSeveridadInherente())
                .addValue("p_COD_CONTROL", detalleMatrizContinuidad.getCodControl())
                .addValue("p_DE_CONTROL", detalleMatrizContinuidad.getDeControl())
                .addValue("p_ID_EFEC_CONTROL", detalleMatrizContinuidad.getIdEfecControl())
                .addValue("p_ADIC_CONTROL", detalleMatrizContinuidad.getAdicControl())
                .addValue("p_INFRA_CONTROL", detalleMatrizContinuidad.getInfraControl())
                .addValue("p_RE_HU_CONTROL", detalleMatrizContinuidad.getReHuControl())
                .addValue("p_RE_TI_CONTROL", detalleMatrizContinuidad.getReTiControl())
                .addValue("p_REG_VITAL_CONTROL", detalleMatrizContinuidad.getRegVitalControl())
                .addValue("p_PROVE_CONTROL", detalleMatrizContinuidad.getProveControl())
                .addValue("p_OTROS_CONTROL", detalleMatrizContinuidad.getOtrosControl())
                .addValue("p_NU_PROBA_RES", detalleMatrizContinuidad.getNuProbabilidadResidual())
                .addValue("p_NU_IMPA_RES", detalleMatrizContinuidad.getNuImpactoResidual())
                .addValue("p_NU_PUNTA_RES", detalleMatrizContinuidad.getNuPuntajeResidual())
                .addValue("p_DE_SEVERIDAD_RES", detalleMatrizContinuidad.getDeSeveridadResidual())
                .addValue("p_ID_ESTRATEGIA_PLAN", detalleMatrizContinuidad.getIdEstrategiaPlan())
                .addValue("p_COD_PLAN_ACCION", detalleMatrizContinuidad.getCodPlanAccion())
                .addValue("p_DE_PLAN_ACCION", detalleMatrizContinuidad.getDePlanAccion())
                .addValue("p_ID_AREA_PLAN_ACCION", detalleMatrizContinuidad.getIdAreaPlanAccion())
                .addValue("p_ID_RESP_PLAN_ACCION", detalleMatrizContinuidad.getIdResPlanAccion())
                .addValue("p_FE_INI_PLAN_ACCION", fechaIniPlanAcion)
                .addValue("p_ID_ESTADO_PLAN_ACCION", detalleMatrizContinuidad.getIdEstadoPlanAccion())
                .addValue("p_FE_FIN_PLAN_ACCION", fechaFinPlanAccion)
                .addValue("p_COME_PLAN_ACCION", detalleMatrizContinuidad.getComePlanAccion())
                .addValue("p_ID_USUA_CREA", detalleMatrizContinuidad.getUsuarioCreacion())
                .addValue("p_DE_USUA_CREA_IP", detalleMatrizContinuidad.getIpCreacion());

        Map<String, Object> out = call.execute(in);
        BigDecimal idDetaMatrizOut = (BigDecimal) out.get("p_ID_DETA_MATRIZCONT_OUT");
        dtoGenerico.setIdGenerico(idDetaMatrizOut.longValue());
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        dtoGenerico.setCodigoResultado(numeroResultado);
        String descripcionResultado = out.get(SNConstantes.DE_ERROR).toString();
        dtoGenerico.setDescripcionResultado(descripcionResultado);

        logger.info("Fin de MatrizContinuidadDaoImpl - registrarDetalleMatrizContinuidad");
        return dtoGenerico;
    }


    @Override
    public DTOMatrizContinuidad obtenerMatrizRiesgo(Integer idMatrizRiesgo) throws DataAccessException {
        logger.info("Inicio MatrizRiesgoDaoImpl - obtenerMatrizRiesgo");

        DTOMatrizContinuidad matrizRiesgo;

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

        matrizRiesgo = new DTOMatrizContinuidad();

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
    public DTOGenerico listarDetalleMatrizContinuidad(Integer idMatrizRiesgo, Long idUsuario) throws DataAccessException {
        logger.info("Inicio MatrizContinuidadDaoImpl - listarDetalleMatrizContinuidad");

        List<DTODetalleMatrizContinuidad> listaDetaMatrizContinuidad;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_TRANSACCION)
                .withProcedureName(SNConstantes.SP_LISTAR_DETALLE_MATRIZCONTINUIDAD)
                .declareParameters(new SqlParameter("p_ID_MATRIZ_RIESGO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_USUARIO", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTODetalleMatrizContinuidad.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_MATRIZ_RIESGO", idMatrizRiesgo)
                .addValue("p_ID_USUARIO", idUsuario);

        Map<String, Object> out = call.execute(in);
        listaDetaMatrizContinuidad = (List<DTODetalleMatrizContinuidad>) out.get(SNConstantes.O_CURSOR);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionResultado = (String) out.get(SNConstantes.DE_ERROR);
        logger.info("numero resultado:" + numeroResultado);
        logger.info("descripcion error:" + descripcionResultado);

        DTOGenerico dtoGenerico = new DTOGenerico();
        dtoGenerico.setCodigoResultado(numeroResultado);
        dtoGenerico.setDescripcionResultado(descripcionResultado);
        dtoGenerico.setListado(listaDetaMatrizContinuidad);

        logger.info("Fin MatrizContinuidadDaoImpl - listarDetalleMatrizContinuidad");
        return dtoGenerico;
    }

    @Override
    public DTOMatrizContinuidad actualizarMatrizRiesgo(MatrizRiesgoContinuidadBean matrizContinuidadBean) throws DataAccessException {
        logger.info("Inicio MatrizContinuidadDaoImpl - actualizarMatrizContinuidad");

        DTOMatrizContinuidad dtoMatrizContinuidad;
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
                .addValue("p_ID_MATRIZ_RIESGO", matrizContinuidadBean.getIdMatrizRiesgo())
                .addValue("p_ID_EMPRESA", matrizContinuidadBean.getIdEmpresa())
                .addValue("p_ID_SEDE_EMPRESA", matrizContinuidadBean.getIdSede())
                .addValue("p_ID_GERENCIA", matrizContinuidadBean.getIdGerencia())
                .addValue("p_ID_PERIODO", matrizContinuidadBean.getIdPeriodo())
                .addValue("p_ID_TIPO_MATRIZ", matrizContinuidadBean.getIdTipoMatriz())
                .addValue("p_ID_MATRIZ_NIVEL", matrizContinuidadBean.getIdMatrizNivel())
                .addValue("p_IN_BAJA", matrizContinuidadBean.getIndicadorBaja())
                .addValue("p_ID_USUA_MODI", matrizContinuidadBean.getUsuarioModificacion())
                .addValue("p_DE_USUA_MODI_IP", matrizContinuidadBean.getIpModificacion());

        Map<String, Object> out = call.execute(in);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionResultado = out.get(SNConstantes.DE_ERROR).toString();

        dtoMatrizContinuidad = new DTOMatrizContinuidad();
        dtoMatrizContinuidad.setIdMatrizRiesgo(matrizContinuidadBean.getIdMatrizRiesgo());
        dtoMatrizContinuidad.setCodigoResultado(numeroResultado);
        dtoMatrizContinuidad.setDescripcionResultado(descripcionResultado);
        logger.info(" Número de error actualizar Matriz Continuidad : {}", numeroResultado);

        logger.info("Fin de MatrizContinuidadDaoImpl - actualizarMatrizContinuidad");
        return dtoMatrizContinuidad;
    }

    @Override
    public DTODetalleMatrizContinuidad actualizarDetaMatrizContinuidad(DetalleMatrizContinuidadBean detalleMatrizContinuidad) throws DataAccessException {
        logger.info("Inicio MatrizContinuidadDaoImpl - actualizarDetaMatrizContinuidad");

        SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaIniPlanAcion = null;
        Date fechaFinPlanAccion = null;
        try {
            fechaIniPlanAcion = parse.parse(detalleMatrizContinuidad.getFeIniPlanAccion());

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error de parseo");
        }

        try {
            fechaFinPlanAccion = parse.parse(detalleMatrizContinuidad.getFeFinPlanAccion());

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error de parseo");
        }

        DTODetalleMatrizContinuidad dtoDetalleMatrizContinuidad;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_TRANSACCION)
                .withProcedureName(SNConstantes.SP_UPDATE_DETALLE_MATRIZCONTINUIDAD)
                .declareParameters(new SqlParameter("ID_DETA_MATRIZCONTINUIDAD", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("ID_MATRIZ_RIESGO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("ID_PERIODO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_COD_MATRIZ", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("ID_MATRIZ_NIVEL", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("ID_GERENCIA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_SEDE_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_PROCESO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_SUBPROCESO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("COD_RIESGO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("DE_RIESGO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("NU_PROBA_INHE", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("NU_IMPA_INHE", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("NU_PUNTA_INHE", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("DE_SEVERIDAD_INHE", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("COD_CONTROL", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("DE_CONTROL", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("ID_EFEC_CONTROL", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("ADIC_CONTROL", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("INFRA_CONTROL", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("RE_HU_CONTROL", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("RE_TI_CONTROL", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("REG_VITAL_CONTROL", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("PROVE_CONTROL", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("OTROS_CONTROL", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("NU_PROBA_RES", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("NU_IMPA_RES", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("NU_PUNTA_RES", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("DE_SEVERIDAD_RES", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("ID_ESTRATEGIA_PLAN", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("COD_PLAN_ACCION", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("DE_PLAN_ACCION", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("ID_AREA_PLAN_ACCION", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("ID_RESP_PLAN_ACCION", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("FE_INI_PLAN_ACCION", OracleTypes.DATE))
                .declareParameters(new SqlParameter("ID_ESTADO_PLAN_ACCION", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("FE_FIN_PLAN_ACCION", OracleTypes.DATE))
                .declareParameters(new SqlParameter("COME_PLAN_ACCION", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("ID_USUA_MODI", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("DE_USUA_MODI_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("IN_BAJA", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));;

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_DETA_MATRIZCONTINUIDAD", detalleMatrizContinuidad.getIdDetaMatrizContinuidad())
                .addValue("p_ID_MATRIZ_RIESGO", detalleMatrizContinuidad.getIdMatrizRiesgo())
                .addValue("p_ID_PERIODO", detalleMatrizContinuidad.getIdPeriodo())
                .addValue("p_COD_MATRIZ", detalleMatrizContinuidad.getCodMatriz())
                .addValue("p_ID_MATRIZ_NIVEL", detalleMatrizContinuidad.getIdMatrizNivel())
                .addValue("p_ID_GERENCIA", detalleMatrizContinuidad.getIdGerencia())
                .addValue("p_ID_EMPRESA", detalleMatrizContinuidad.getIdEmpresa())
                .addValue("p_ID_SEDE_EMPRESA", detalleMatrizContinuidad.getIdSede())
                .addValue("p_ID_PROCESO", detalleMatrizContinuidad.getIdProceso())
                .addValue("p_ID_SUBPROCESO", detalleMatrizContinuidad.getIdSubProceso())
                .addValue("p_COD_RIESGO", detalleMatrizContinuidad.getCodRiesgo())  //090821
                .addValue("p_DE_RIESGO", detalleMatrizContinuidad.getDeRiesgo())
                .addValue("p_NU_PROBA_INHE", detalleMatrizContinuidad.getNuProbabilidadInherente())
                .addValue("p_NU_IMPA_INHE", detalleMatrizContinuidad.getNuImpactoInherente())
                .addValue("p_NU_PUNTA_INHE", detalleMatrizContinuidad.getNuPuntajeInherente())
                .addValue("p_DE_SEVERIDAD_INHE", detalleMatrizContinuidad.getDeSeveridadInherente())
                .addValue("p_COD_CONTROL", detalleMatrizContinuidad.getCodControl())
                .addValue("p_DE_CONTROL", detalleMatrizContinuidad.getDeControl())
                .addValue("p_ID_EFEC_CONTROL", detalleMatrizContinuidad.getIdEfecControl())
                .addValue("p_ADIC_CONTROL", detalleMatrizContinuidad.getAdicControl())
                .addValue("p_INFRA_CONTROL", detalleMatrizContinuidad.getInfraControl())
                .addValue("p_RE_HU_CONTROL", detalleMatrizContinuidad.getReHuControl())
                .addValue("p_RE_TI_CONTROL", detalleMatrizContinuidad.getReTiControl())
                .addValue("p_REG_VITAL_CONTROL", detalleMatrizContinuidad.getRegVitalControl())
                .addValue("p_PROVE_CONTROL", detalleMatrizContinuidad.getProveControl())
                .addValue("p_OTROS_CONTROL", detalleMatrizContinuidad.getOtrosControl())
                .addValue("p_NU_PROBA_RES", detalleMatrizContinuidad.getNuProbabilidadResidual())
                .addValue("p_NU_IMPA_RES", detalleMatrizContinuidad.getNuImpactoResidual())
                .addValue("p_NU_PUNTA_RES", detalleMatrizContinuidad.getNuPuntajeResidual())
                .addValue("p_DE_SEVERIDAD_RES", detalleMatrizContinuidad.getDeSeveridadResidual())
                .addValue("p_ID_ESTRATEGIA_PLAN", detalleMatrizContinuidad.getIdEstrategiaPlan())
                .addValue("p_COD_PLAN_ACCION", detalleMatrizContinuidad.getCodPlanAccion())
                .addValue("p_DE_PLAN_ACCION", detalleMatrizContinuidad.getDePlanAccion())
                .addValue("p_ID_AREA_PLAN_ACCION", detalleMatrizContinuidad.getIdAreaPlanAccion())
                .addValue("p_ID_RESP_PLAN_ACCION", detalleMatrizContinuidad.getIdResPlanAccion())
                .addValue("p_FE_INI_PLAN_ACCION", fechaIniPlanAcion)
                .addValue("p_ID_ESTADO_PLAN_ACCION", detalleMatrizContinuidad.getIdEstadoPlanAccion())
                .addValue("p_FE_FIN_PLAN_ACCION", fechaFinPlanAccion)
                .addValue("p_COME_PLAN_ACCION", detalleMatrizContinuidad.getComePlanAccion())
                .addValue("p_ID_USUA_MODI", detalleMatrizContinuidad.getUsuarioModificacion())
                .addValue("p_DE_USUA_MODI_IP", detalleMatrizContinuidad.getIpModificacion())
                .addValue("p_IN_BAJA", detalleMatrizContinuidad.getIndicadorBaja());

        Map<String, Object> out = call.execute(in);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionResultado = out.get(SNConstantes.DE_ERROR).toString();

        dtoDetalleMatrizContinuidad = new DTODetalleMatrizContinuidad();
        dtoDetalleMatrizContinuidad.setIdDetaMatrizContinuidad(detalleMatrizContinuidad.getIdDetaMatrizContinuidad());
        dtoDetalleMatrizContinuidad.setCodigoResultado(numeroResultado);
        dtoDetalleMatrizContinuidad.setDescripcionResultado(descripcionResultado);
        logger.info(" Número de error actualizar detalle de matriz continuidad : {}", numeroResultado);

        logger.info("Fin de MatrizContinuidadDaoImpl - actualizarDetaMatrizContinuidad");
        return dtoDetalleMatrizContinuidad;
    }
}
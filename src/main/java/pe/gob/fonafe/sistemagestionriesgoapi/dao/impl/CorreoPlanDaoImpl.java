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
import pe.gob.fonafe.sistemagestionriesgoapi.dao.ICorreoPlanDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.*;
import pe.gob.fonafe.sistemagestionriesgoapi.models.CorreoPlanBean;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Repository
public class CorreoPlanDaoImpl implements ICorreoPlanDao {

    private final JdbcTemplate jdbcTemplate;

    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    public CorreoPlanDaoImpl(@Qualifier("jdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public DTOCorreoPlan obtenerCorreoPlan() throws DataAccessException {
        logger.info("Inicio CorreoPlanDaoImpl - obtenerCorreoPlan");
        DTOCorreoPlan correoPlan;

        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONSULTAS)
                .withProcedureName(SNConstantes.SP_OBTENER_CONF_CORREO)
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTOCorreoPlan.class));

        Map<String, Object> out = call.execute();
        ArrayList<DTOCorreoPlan> correoArray = (ArrayList<DTOCorreoPlan>) out.get(SNConstantes.O_CURSOR);
        Iterator<DTOCorreoPlan> iteradorCorreo = correoArray.iterator();

        correoPlan = new DTOCorreoPlan();

        while (iteradorCorreo.hasNext()){
            DTOCorreoPlan itrCorreo = iteradorCorreo.next();
            correoPlan.setIdConfigCorreo(itrCorreo.getIdConfigCorreo());
            correoPlan.setNuDiasAntes(itrCorreo.getNuDiasAntes());
            correoPlan.setNuDiasDespues(itrCorreo.getNuDiasDespues());
            correoPlan.setFechaCreacion(itrCorreo.getFechaCreacion());
        }


        logger.info("Fin CorreoPlanDaoImpl - obtenerCorreoPlan");
        return correoPlan;
    }

    @Override
    public DTOCorreoPlan actualizarNotificacionPlanAccion(CorreoPlanBean correoPlanBean) throws DataAccessException {
        logger.info("Inicio CorreoPlanDaoImpl - actualizarNotificacionPlanAccion");

        DTOCorreoPlan dtoCorreoPlan;

        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONFIGURACION)
                .withProcedureName(SNConstantes.SP_UPDATE_NOTIF_PLAN_ACC)
                .declareParameters(new SqlParameter("p_NU_DIAS_ANTES", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_NU_DIAS_DESPUES", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_USUA_MODI", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_USUA_MODI_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_NU_DIAS_ANTES", correoPlanBean.getNuDiasAntes())
                .addValue("p_NU_DIAS_DESPUES", correoPlanBean.getNuDiasDespues())
                .addValue("p_ID_USUA_MODI", correoPlanBean.getUsuarioModificacion())
                .addValue("p_DE_USUA_MODI_IP", correoPlanBean.getIpModificacion());

        Map<String, Object> out = call.execute(in);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionResultado = out.get(SNConstantes.DE_ERROR).toString();

        dtoCorreoPlan = new DTOCorreoPlan();
        dtoCorreoPlan.setIdConfigCorreo(correoPlanBean.getIdConfigCorreo());
        dtoCorreoPlan.setCodigoResultado(numeroResultado);
        dtoCorreoPlan.setDescripcionResultado(descripcionResultado);
        logger.info(" Número de error actualizar Notificacion Plan Acción : {}", numeroResultado);


        logger.info("Fin CorreoPlanDaoImpl - actualizarNotificacionPlanAccion");
        return dtoCorreoPlan;
    }

    @Override
    public DTOGenerico listarFechaVencimiento() throws DataAccessException {
        logger.info("Inicio CorreoPlanDaoImpl - listarFechaVencimiento");

        List<DTOFechaVencimiento> listaFechaVencimientoProceso;
        List<DTOFechaVencimiento> listaFechaVencimientoEntidad;
        List<DTOMailConfig> listaMailConfig;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONSULTAS)
                .withProcedureName(SNConstantes.SP_LISTAR_FECH_VENCIMIENTO)
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSORENTIDAD, OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSORMAILCONFIG, OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTOFechaVencimiento.class))
                .returningResultSet(SNConstantes.O_CURSORENTIDAD, BeanPropertyRowMapper.newInstance(DTOFechaVencimiento.class))
                .returningResultSet(SNConstantes.O_CURSORMAILCONFIG, BeanPropertyRowMapper.newInstance(DTOMailConfig.class));

        Map<String, Object> out = call.execute();
        listaFechaVencimientoProceso = (List<DTOFechaVencimiento>) out.get(SNConstantes.O_CURSOR);
        listaFechaVencimientoEntidad = (List<DTOFechaVencimiento>) out.get(SNConstantes.O_CURSORENTIDAD);
        listaMailConfig = (List<DTOMailConfig>) out.get(SNConstantes.O_CURSORMAILCONFIG);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionResultado = (String) out.get(SNConstantes.DE_ERROR);
        logger.info("numero resultado:" + numeroResultado);
        logger.info("descripcion error:" + descripcionResultado);

        DTOGenerico dtoGenerico = new DTOGenerico();
        dtoGenerico.setCodigoResultado(numeroResultado);
        dtoGenerico.setDescripcionResultado(descripcionResultado);
        dtoGenerico.setListado(listaFechaVencimientoProceso);
        dtoGenerico.setListaCorreoEntidad(listaFechaVencimientoEntidad);
        dtoGenerico.setListaMailConfig(listaMailConfig);

        logger.info("Fin CorreoPlanDaoImpl - listarFechaVencimiento");
        return dtoGenerico;
    }
}

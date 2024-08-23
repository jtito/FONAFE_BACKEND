
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
import pe.gob.fonafe.sistemagestionriesgoapi.dao.ISubProcesoDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOSubProceso;
import pe.gob.fonafe.sistemagestionriesgoapi.models.SubProcesoBean;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Repository
public class SubProcesoDaoImpl implements ISubProcesoDao{
    
    private final JdbcTemplate jdbcTemplate;

    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    public SubProcesoDaoImpl(@Qualifier("jdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    

    @Override
    public DTOGenerico registrarSubProceso(SubProcesoBean procesoBean) throws DataAccessException {

        logger.info("Inicio de SubProcesoImpl - registrarSubProceso");

        DTOGenerico dtoGenerico = new DTOGenerico();

        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONFIGURACION)
                .withProcedureName(SNConstantes.SP_INSERT_SUBPROCESO)
                .declareParameters(new SqlParameter("p_ID_PROCESO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_DE_SUBPROCESO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_USUA_CREA", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_USUA_CREA_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter("p_ID_PROCESO_OUT", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_PROCESO", procesoBean.getIdProceso())
                .addValue("p_DE_SUBPROCESO", procesoBean.getDeSubProceso())
                .addValue("p_ID_USUA_CREA", procesoBean.getUsuarioCreacion())
                .addValue("p_DE_USUA_CREA_IP", procesoBean.getIpCreacion());

        Map<String, Object> out = call.execute(in);
        BigDecimal idSubProcesoOut = (BigDecimal) out.get("p_ID_SUBPROCESO_OUT");
        
        dtoGenerico.setCodigoResultado(idSubProcesoOut);
        String numeroResult = (String) out.get(SNConstantes.NUM_RESULT);
        dtoGenerico.setDescripcionResultado(numeroResult);
        String descripcionError = out.get(SNConstantes.DE_ERROR).toString();
      
        logger.info("Fin de SubProcesoImpl - registrarSubProceso");
        return dtoGenerico;
    
    }

    @Override
    public List<DTOSubProceso> listarSubProcesos(Long idProceso) throws DataAccessException {
        
        logger.info("Inicio SubProcesoDaoImpl - listarSubProceso");

        List<DTOSubProceso> listaSubProcesos;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONSULTAS)
                .withProcedureName(SNConstantes.SP_LISTAR_SUBPROCESO)
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .declareParameters(new SqlParameter("p_ID_PROCESO", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTOSubProceso.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_PROCESO",idProceso );

        Map<String, Object> out = call.execute(in);
        listaSubProcesos = (List<DTOSubProceso>) out.get(SNConstantes.O_CURSOR);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionError = (String) out.get(SNConstantes.DE_ERROR);
        logger.info("numero resultado:" + numeroResultado);
        logger.info("descripcion error:" + descripcionError);

        logger.info("Fin SubProcesoDaoImpl - listarSubProceso");
        return listaSubProcesos;
    }

    @Override
    public DTOSubProceso actualizarSubProceso(SubProcesoBean subprocesoBean) throws DataAccessException {
        
        logger.info("Inicio SubProcesoDaoImpl - actualizarSubProceso");

        DTOSubProceso dtoSubProceso;

        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONFIGURACION)
                .withProcedureName(SNConstantes.SP_UPDATE_SUBPROCESO)
                .declareParameters(new SqlParameter("p_ID_SUBPROCESO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_DE_SUBPROCESO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_USUA_MODI", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_USUA_MODI_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_IN_BAJA", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_SUBPROCESO", subprocesoBean.getIdSubProceso())
                .addValue("p_DE_SUBPROCESO", subprocesoBean.getDeSubProceso())
                .addValue("p_ID_USUA_MODI", subprocesoBean.getUsuarioModificacion())
                .addValue("p_DE_USUA_MODI_IP", subprocesoBean.getIpModificacion())
                .addValue("p_IN_BAJA", subprocesoBean.getIndicadorBaja());

        Map<String, Object> out = call.execute(in);
        String numeroResultado = (String) out.get(SNConstantes.NUM_RESULT);
        String descripcionError = out.get(SNConstantes.DE_ERROR).toString();

        dtoSubProceso = new DTOSubProceso();
        dtoSubProceso.setIdSubProceso(subprocesoBean.getIdSubProceso());
        dtoSubProceso.setCodigoMensaje(numeroResultado);
        dtoSubProceso.setDescripcionMensaje(descripcionError);
        logger.info(" Número de resultado al actualizar Sub Proceso: {}", numeroResultado);

        logger.info("Fin de SubProcesoDaoImpl - actualizarSubProceso");
        return dtoSubProceso;
    }
    
    

    
}

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
import pe.gob.fonafe.sistemagestionriesgoapi.dao.IMatrizEvidenciaControlDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOMatrizEvidenciaControl;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOParametro;
import pe.gob.fonafe.sistemagestionriesgoapi.models.MatrizEvidenciaControlBean;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Repository
public class MatrizEvidenciaControlDaoImpl implements IMatrizEvidenciaControlDao {

    private final JdbcTemplate jdbcTemplate;

    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    public MatrizEvidenciaControlDaoImpl(@Qualifier("jdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public DTOMatrizEvidenciaControl registrarMatrizEvidenciaControl(MatrizEvidenciaControlBean matrizEvidenciaControlBean) throws DataAccessException {
        logger.info("Inicio de MatrizEvidenciaControlDaoImpl - registrarMatrizEvidenciaControl");

        DTOMatrizEvidenciaControl dtoMatrizEvidenciaControl = new DTOMatrizEvidenciaControl();
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_ARCHIVOS)
                .withProcedureName(SNConstantes.SP_INSERT_MATRIZ_EVID_CONTROL)
                .declareParameters(new SqlParameter("p_ID_MATRIZ_RIESGO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_DETA_MATRIZ_RIESGO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_NOMBRE_ORIGINAL_ARCHIVO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_NOMBRE_ARCHIVO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_PESO_ARCHIVO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_RUTA_ARCHIVO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_USUA_CREA", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_USUA_CREA_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_TIPO_EVIDENCIA", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter("p_ID_EVIDENCIA_OUT", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_MATRIZ_RIESGO", matrizEvidenciaControlBean.getIdMatrizRiesgo())
                .addValue("p_ID_DETA_MATRIZ_RIESGO", matrizEvidenciaControlBean.getIdDetaMatrizRiesgo())
                .addValue("p_NOMBRE_ORIGINAL_ARCHIVO", matrizEvidenciaControlBean.getNombreOriginalArchivo())
                .addValue("p_NOMBRE_ARCHIVO", matrizEvidenciaControlBean.getNombreArchivo())
                .addValue("p_PESO_ARCHIVO", matrizEvidenciaControlBean.getPesoArchivo())
                .addValue("p_RUTA_ARCHIVO", matrizEvidenciaControlBean.getRutaArchivo())
                .addValue("p_ID_USUA_CREA", matrizEvidenciaControlBean.getUsuarioCreacion())
                .addValue("p_DE_USUA_CREA_IP", matrizEvidenciaControlBean.getIpCreacion())
                .addValue("p_TIPO_EVIDENCIA", matrizEvidenciaControlBean.getTipoEvidencia());

        Map<String, Object> out = call.execute(in);
        BigDecimal idEvidenciaOut = (BigDecimal) out.get("p_ID_EVIDENCIA_OUT");
        dtoMatrizEvidenciaControl.setIdEvidencia(idEvidenciaOut.longValue());
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        dtoMatrizEvidenciaControl.setCodigoResultado(numeroResultado);
        String descripcionResultado = out.get(SNConstantes.DE_ERROR).toString();
        dtoMatrizEvidenciaControl.setDescripcionResultado(descripcionResultado);

        logger.info("Fin de MatrizEvidenciaControlDaoImpl - registrarMatrizEvidenciaControl");

        return dtoMatrizEvidenciaControl;
    }

    @Override
    public DTOGenerico listarMatrizEvidenciaControl(MatrizEvidenciaControlBean matrizEvidenciaControlBean) throws DataAccessException {
        logger.info("Inicio de MatrizEvidenciaControlDaoImpl - listarMatrizEvidenciaControl");

        List<DTOMatrizEvidenciaControl> listMatrizEvidenciaControl;
        List<DTOMatrizEvidenciaControl> listMatrizEvidenciaPlan;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_ARCHIVOS)
                .withProcedureName(SNConstantes.SP_LISTAR_MATRIZ_EVID_CONTROL)
                .declareParameters(new SqlParameter("p_ID_MATRIZ_RIESGO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_DETA_MATRIZ_RIESGO", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR_CONTROL, OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR_PLAN, OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR))
                .returningResultSet(SNConstantes.O_CURSOR_CONTROL, BeanPropertyRowMapper.newInstance(DTOMatrizEvidenciaControl.class))
                .returningResultSet(SNConstantes.O_CURSOR_PLAN, BeanPropertyRowMapper.newInstance(DTOMatrizEvidenciaControl.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_MATRIZ_RIESGO", matrizEvidenciaControlBean.getIdMatrizRiesgo())
                .addValue("p_ID_DETA_MATRIZ_RIESGO", matrizEvidenciaControlBean.getIdDetaMatrizRiesgo());

        Map<String, Object> out = call.execute(in);
        listMatrizEvidenciaControl = (List<DTOMatrizEvidenciaControl>)out.get(SNConstantes.O_CURSOR_CONTROL);
        listMatrizEvidenciaPlan = (List<DTOMatrizEvidenciaControl>)out.get(SNConstantes.O_CURSOR_PLAN);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionResultado = (String) out.get(SNConstantes.DE_ERROR);
        logger.info("numero resultado:" + numeroResultado);
        logger.info("descripcion error:" + descripcionResultado);

        DTOGenerico dtoGenerico = new DTOGenerico();
        dtoGenerico.setCodigoResultado(numeroResultado);
        dtoGenerico.setDescripcionResultado(descripcionResultado);
        dtoGenerico.setListadoEvidenciaControl(listMatrizEvidenciaControl);
        dtoGenerico.setListadoEvidenciaPlan(listMatrizEvidenciaPlan);

        logger.info("Fin de MatrizEvidenciaControlDaoImpl - listarMatrizEvidenciaControl");

        return dtoGenerico;
    }

    @Override
    public DTOMatrizEvidenciaControl anularMatrizEvidenciaControl(MatrizEvidenciaControlBean matrizEvidenciaControlBean) throws DataAccessException {
        logger.info("Inicio de MatrizEvidenciaControlDaoImpl - anularMatrizEvidenciaControl");

        DTOMatrizEvidenciaControl dtoMatrizEvidenciaControl= new DTOMatrizEvidenciaControl();
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_ARCHIVOS)
                .withProcedureName(SNConstantes.SP_ANULAR_MATRIZ_EVID_CONTROL)
                .declareParameters(new SqlParameter("p_NOMBRE_ARCHIVO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_USUA_MODI", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_USUA_MODI_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_NOMBRE_ARCHIVO", matrizEvidenciaControlBean.getNombreArchivo())
                .addValue("p_ID_USUA_MODI", matrizEvidenciaControlBean.getUsuarioModificacion())
                .addValue("p_DE_USUA_MODI_IP", matrizEvidenciaControlBean.getIpModificacion());

        Map<String, Object> out = call.execute(in);
        BigDecimal result = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionError = (String) out.get(SNConstantes.DE_ERROR);

        dtoMatrizEvidenciaControl.setCodigoResultado(result);
        dtoMatrizEvidenciaControl.setDescripcionResultado(descripcionError);

        logger.info("Número de error anular Parametro : {}", result);
        logger.info("Descripcion de error anular Parametro : {}", descripcionError);

        logger.info("Fin de MatrizEvidenciaControlDaoImpl - anularMatrizEvidenciaControl");

        return dtoMatrizEvidenciaControl;
    }
}


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
import pe.gob.fonafe.sistemagestionriesgoapi.dao.IProcesoDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOProceso;
import pe.gob.fonafe.sistemagestionriesgoapi.models.ProcesoBean;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author joell
 */
@Repository
public class ProcesoDaoImpl implements IProcesoDao {

    private final JdbcTemplate jdbcTemplate;

    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    public ProcesoDaoImpl(@Qualifier("jdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public DTOGenerico registrarProceso(ProcesoBean procesoBean) throws DataAccessException {

        DTOGenerico dtoGenerico = new DTOGenerico();

        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONFIGURACION)
                .withProcedureName(SNConstantes.SP_INSERT_PROCESO)
                .declareParameters(new SqlParameter("p_DE_PROCESO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_PROC_MATRIZ", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_IN_MATRIZ_OPER", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_IN_MATRIZ_FRAUDE", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_IN_MATRIZ_CONT", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_IN_MATRIZ_ANTI", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_IN_MATRIZ_OPOR", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_IN_MATRIZ_EVENTO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_USUA_CREA", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_USUA_CREA_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter("p_ID_PROCESO_OUT", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_DE_PROCESO", procesoBean.getDeProceso())
                .addValue("p_ID_PROC_MATRIZ", procesoBean.getIdProcesoMatriz())
                .addValue("p_ID_EMPRESA", procesoBean.getIdEmpresa())
                .addValue("p_IN_MATRIZ_OPER", procesoBean.getInMatrizOperacional())
                .addValue("p_IN_MATRIZ_FRAUDE", procesoBean.getInMatrizFraude())
                .addValue("p_IN_MATRIZ_CONT", procesoBean.getInMatrizContinuidad())
                .addValue("p_IN_MATRIZ_ANTI", procesoBean.getInMatrizAnticorrupcion())
                .addValue("p_IN_MATRIZ_OPOR", procesoBean.getInMatrizOportunidad())
                .addValue("p_IN_MATRIZ_EVENTO", procesoBean.getInMatrizEvento())
                .addValue("p_ID_USUA_CREA", procesoBean.getUsuarioCreacion())
                .addValue("p_DE_USUA_CREA_IP", procesoBean.getIpCreacion());

        Map<String, Object> out = call.execute(in);
        BigDecimal idProcesoOut = (BigDecimal) out.get("p_ID_PROCESO_OUT");
        dtoGenerico.setCodigoResultado(idProcesoOut);
        String numeroResult = (String) out.get(SNConstantes.NUM_RESULT);
        dtoGenerico.setDescripcionResultado(numeroResult);
        String descripcionError = out.get(SNConstantes.DE_ERROR).toString();

        logger.info("Fin de ProcesoImpl - registrarProceso");
        return dtoGenerico;

    }

    @Override
    public DTOProceso obtenerProceso(Long idProceso) throws DataAccessException {

        logger.info("Inicio ProcesoDaoImpl - obtenerProceso");

        DTOProceso dtoProceso;

        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONSULTAS)
                .withProcedureName(SNConstantes.SP_OBTENER_PROCESO)
                .declareParameters(new SqlParameter("p_ID_PROCESO", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTOProceso.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_PROCESO", idProceso);

        Map<String, Object> out = call.execute(in);
        String numeroResultado = (String) out.get(SNConstantes.NUM_RESULT);
        String descripcionError = (String) out.get(SNConstantes.DE_ERROR);
        logger.info("numero resultado:" + numeroResultado);
        logger.info("descripcion error:" + descripcionError);

        ArrayList<DTOProceso> procesoArray = (ArrayList<DTOProceso>) out.get(SNConstantes.O_CURSOR);
        Iterator<DTOProceso> iteradorProceso = procesoArray.iterator();

        dtoProceso = new DTOProceso();
        while (iteradorProceso.hasNext()) {
            DTOProceso itrProceso = iteradorProceso.next();
            dtoProceso.setIdEmpresa(itrProceso.getIdEmpresa());
            dtoProceso.setDeProceso(itrProceso.getDeProceso());
            dtoProceso.setIndicadorBaja(itrProceso.getIndicadorBaja());
            dtoProceso.setFechaCreacion(itrProceso.getFechaCreacion());
        }

        logger.info("Fin ProcesoDaoImpl - obtenerProceso");
        return dtoProceso;

    }

    @Override
    public List<DTOProceso> listarProcesos(Long idEmpresa,Long idTipoMatriz) throws DataAccessException {

        logger.info("Inicio ProcesoDaoImpl - listarProceso");

        List<DTOProceso> listaProcesos;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONSULTAS)
                .withProcedureName(SNConstantes.SP_LISTAR_PROCESO)
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_TIPO_MATRIZ", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTOProceso.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_EMPRESA", idEmpresa)
                .addValue("p_ID_TIPO_MATRIZ", idTipoMatriz);

        Map<String, Object> out = call.execute(in);
        logger.info("numero resultado out:" + out.toString());

        listaProcesos = (List<DTOProceso>) out.get(SNConstantes.O_CURSOR);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionError = (String) out.get(SNConstantes.DE_ERROR);
        logger.info("numero resultado:" + numeroResultado);
        logger.info("descripcion error:" + descripcionError);

        logger.info("Fin ProcesoDaoImpl - listarProceso");
        return listaProcesos;

    }

    @Override
    public List<DTOProceso> listaProcesosMatriz(Long idProcesoMatriz) throws DataAccessException {
        logger.info("Inicio ProcesoDaoImpl - listaProcesosMatriz");

        List<DTOProceso> listaProcesos;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONSULTAS)
                .withProcedureName(SNConstantes.SP_LISTA_PROCESOMATRIZ)
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .declareParameters(new SqlParameter("p_ID_PROC_MATRIZ", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTOProceso.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_PROC_MATRIZ", idProcesoMatriz);

        Map<String, Object> out = call.execute(in);
        logger.info("numero resultado out:" + out.toString());

        listaProcesos = (List<DTOProceso>) out.get(SNConstantes.O_CURSOR);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionError = (String) out.get(SNConstantes.DE_ERROR);
        logger.info("numero resultado:" + numeroResultado);
        logger.info("descripcion error:" + descripcionError);

        logger.info("Fin ProcesoDaoImpl - listaProcesosMatriz");
        return listaProcesos;
    }

    @Override
    public DTOProceso actualizarProceso(ProcesoBean procesoBean) throws DataAccessException {

        logger.info("Inicio ProcesoDaoImpl - actualizarProceso");

        DTOProceso dtoProceso;

        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONFIGURACION)
                .withProcedureName(SNConstantes.SP_UPDATE_PROCESO)
                .declareParameters(new SqlParameter("p_ID_PROCESO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_PROC_MATRIZ", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_DE_PROCESO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_IN_MATRIZ_OPER", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_IN_MATRIZ_FRAUDE", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_IN_MATRIZ_CONT", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_IN_MATRIZ_ANTI", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_IN_MATRIZ_OPOR", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_IN_MATRIZ_EVENTO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_USUA_MODI", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_USUA_MODI_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_IN_BAJA", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_PROCESO", procesoBean.getIdProceso())
                .addValue("p_ID_PROC_MATRIZ", procesoBean.getIdProcesoMatriz())
                .addValue("p_DE_PROCESO", procesoBean.getDeProceso())
                .addValue("p_ID_EMPRESA", procesoBean.getIdEmpresa())
                .addValue("p_IN_MATRIZ_OPER", procesoBean.getInMatrizOperacional())
                .addValue("p_IN_MATRIZ_FRAUDE", procesoBean.getInMatrizFraude())
                .addValue("p_IN_MATRIZ_CONT", procesoBean.getInMatrizContinuidad())
                .addValue("p_IN_MATRIZ_ANTI", procesoBean.getInMatrizAnticorrupcion())
                .addValue("p_IN_MATRIZ_OPOR", procesoBean.getInMatrizOportunidad())
                .addValue("p_IN_MATRIZ_EVENTO", procesoBean.getInMatrizEvento())
                .addValue("p_ID_USUA_MODI", procesoBean.getUsuarioModificacion())
                .addValue("p_DE_USUA_MODI_IP", procesoBean.getIpModificacion())
                .addValue("p_IN_BAJA", procesoBean.getIndicadorBaja());

        Map<String, Object> out = call.execute(in);
        String numeroResultado = (String) out.get(SNConstantes.NUM_RESULT);
        String descripcionError = out.get(SNConstantes.DE_ERROR).toString();

        dtoProceso = new DTOProceso();
        dtoProceso.setIdProceso(procesoBean.getIdProceso());
        dtoProceso.setCodigoMensaje(numeroResultado);
        dtoProceso.setDescripcionMensaje(descripcionError);
        logger.info(" Número de resultado al actualizar Proceso: {}", numeroResultado);

        logger.info("Fin de ProcesoDaoImpl - actualizarProceso");
        return dtoProceso;

    }

    @Override
    public Byte anularProceso(ProcesoBean matriznivelBean) throws DataAccessException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DTOGenerico listarProcesosxMatriz(Long idEmpresa, Long idSede, Long matrizNivel, Long idTipoMatriz) throws DataAccessException {
        logger.info("Inicio ProcesoDaoImpl - listarProcesosxMatriz");

        List<DTOProceso> listaProcesos;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONSULTAS)
                .withProcedureName(SNConstantes.SP_LISTAR_PROCESO_X_MATRIZ)
                .declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_SEDE_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_MATRIZNIVEL", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_TIPO_MATRIZ", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTOProceso.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_EMPRESA", idEmpresa)
                .addValue("p_ID_SEDE_EMPRESA", idSede)
                .addValue("p_MATRIZNIVEL", matrizNivel)
                .addValue("p_ID_TIPO_MATRIZ", idTipoMatriz);

        Map<String,Object> out = call.execute(in);
        listaProcesos = (List<DTOProceso>) out.get(SNConstantes.O_CURSOR);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionResultado = (String) out.get(SNConstantes.DE_ERROR);
        logger.info("numero resultado:" + numeroResultado);
        logger.info("descripcion error:" + descripcionResultado);

        DTOGenerico dtoGenerico = new DTOGenerico();
        dtoGenerico.setCodigoResultado(numeroResultado);
        dtoGenerico.setDescripcionResultado(descripcionResultado);
        dtoGenerico.setListado(listaProcesos);

        logger.info("Fin ProcesoDaoImpl - listarProcesosxMatriz");
        return dtoGenerico;
    }

}

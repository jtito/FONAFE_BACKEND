package pe.gob.fonafe.sistemagestionriesgoapi.dao.impl;

import oracle.jdbc.OracleTypes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import pe.gob.fonafe.sistemagestionriesgoapi.dao.IDimensionDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTODimension;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Repository
public class DimensionDaoImpl implements IDimensionDao {

	private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	@Override
	public DTOGenerico listarDimension(Long p_ID_EMPRESA, Long p_ID_TIPO_CONTROL_RIESGO) throws DataAccessException {
		// TODO Auto-generated method stub
        DTOGenerico dTOGenerico = new DTOGenerico();
		List<DTODimension> listDimension;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
        		.withCatalogName(SNConstantes.PKG_CONSULTAS)
        		.withProcedureName(SNConstantes.SP_LISTAR_DIMENSION)
        		.declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.NUMBER))
        		.declareParameters(new SqlParameter("p_ID_TIPO_CONTROL_RIESGO", OracleTypes.NUMBER))
        		.declareParameters(new SqlOutParameter("oCURSOR", OracleTypes.CURSOR))
        		.declareParameters(new SqlOutParameter("p_NU_RESULT", OracleTypes.NUMBER))
        		.declareParameters(new SqlOutParameter("p_DE_ERROR", OracleTypes.VARCHAR))
        		.returningResultSet("oCURSOR", BeanPropertyRowMapper.newInstance(DTODimension.class));
        
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_EMPRESA", p_ID_EMPRESA)
		        .addValue("p_ID_TIPO_CONTROL_RIESGO", p_ID_TIPO_CONTROL_RIESGO);
        
        Map<String, Object> out = call.execute(in);
        listDimension = (List<DTODimension>) out.get("oCURSOR");
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionResultado = (String) out.get(SNConstantes.DE_ERROR);
        dTOGenerico.setCodigoResultado(numeroResultado);
        dTOGenerico.setDescripcionResultado(descripcionResultado);
        dTOGenerico.setListado(listDimension);
		return dTOGenerico;
	}

	@Override
	public DTODimension registrarDimension(DTODimension dimension) throws DataAccessException {
		// TODO Auto-generated method stub
		DTODimension dTODimension = new DTODimension();
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
        		.withCatalogName(SNConstantes.PKG_CONFIGURACION)
        		.withProcedureName(SNConstantes.SP_INSERT_DIMENSION)
				.declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.NUMBER))
        		.declareParameters(new SqlParameter("p_nu_DIM_RIESGO", OracleTypes.VARCHAR))
        		.declareParameters(new SqlParameter("p_nu_PROBABILIDAD", OracleTypes.NUMBER))
        		.declareParameters(new SqlParameter("p_nu_IMPACTO", OracleTypes.NUMBER))
        		.declareParameters(new SqlParameter("p_de_DIMENSION", OracleTypes.VARCHAR))
        		.declareParameters(new SqlParameter("p_id_TIPO_CONTROL_RIESGO", OracleTypes.NUMBER))
        		.declareParameters(new SqlOutParameter("p_id_DIM_RIESGO_OUT", OracleTypes.NUMBER))
        		.declareParameters(new SqlOutParameter("p_NU_RESULT", OracleTypes.NUMBER))
        		.declareParameters(new SqlOutParameter("p_DE_ERROR", OracleTypes.VARCHAR));
        
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_EMPRESA", dimension.getIdEmpresa())
		        .addValue("p_nu_DIM_RIESGO", dimension.getNuDimRiesgo())
		        .addValue("p_nu_PROBABILIDAD", dimension.getNuProbabilidad())
		        .addValue("p_nu_IMPACTO", dimension.getNuImpacto())
		        .addValue("p_de_DIMENSION", dimension.getDeDimension())
		        .addValue("p_id_TIPO_CONTROL_RIESGO", dimension.getIdTipoControlRiesgo());
        
        Map<String,Object> out = call.execute(in);
        BigDecimal idDimRiesgoOut = (BigDecimal)out.get("p_id_DIM_RIESGO_OUT");
        dTODimension.setIdDimRiesgo(idDimRiesgoOut.longValue());
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        dTODimension.setCodigoResultado(numeroResultado);
        String descripcionResultado = out.get(SNConstantes.DE_ERROR).toString();
        dTODimension.setDescripcionResultado(descripcionResultado);
		return dTODimension;
	}

	@Override
	public DTOGenerico<?> buscarDimension(Long p_id_DIM_RIESGO) throws DataAccessException {
		// TODO Auto-generated method stub
		DTOGenerico dTOGenerico = new DTOGenerico();
		List<DTODimension> listDimension;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
        		.withCatalogName(SNConstantes.PKG_CONSULTAS)
        		.withProcedureName(SNConstantes.SP_OBTENER_DIMENSION)
        		.declareParameters(new SqlParameter("p_id_dim_riesgo", OracleTypes.NUMBER))
        		.declareParameters(new SqlOutParameter("oCURSOR", OracleTypes.CURSOR))
        		.declareParameters(new SqlOutParameter("p_NU_RESULT", OracleTypes.NUMBER))
        		.declareParameters(new SqlOutParameter("p_DE_ERROR", OracleTypes.VARCHAR))
        		.returningResultSet("oCURSOR", BeanPropertyRowMapper.newInstance(DTODimension.class));
        SqlParameterSource in = new MapSqlParameterSource().addValue("p_id_dim_riesgo", p_id_DIM_RIESGO);
        Map<String,Object> out = call.execute(in);
        listDimension = (List<DTODimension>) out.get("oCURSOR");
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionResultado = (String) out.get(SNConstantes.DE_ERROR);
        dTOGenerico.setCodigoResultado(numeroResultado);
        dTOGenerico.setDescripcionResultado(descripcionResultado);
        dTOGenerico.setListado(listDimension);
		return dTOGenerico;
	}

	@Override
	public DTODimension actualizarDimension(DTODimension dimension) throws DataAccessException {
		// TODO Auto-generated method stub
		DTODimension dTODimension = new DTODimension();
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
        		.withCatalogName(SNConstantes.PKG_CONFIGURACION)
        		.withProcedureName(SNConstantes.SP_UPDATE_DIMENSION)
				.declareParameters(new SqlParameter("p_id_DIM_RIESGO", OracleTypes.NUMBER))
				.declareParameters(new SqlParameter("p_id_EMPRESA", OracleTypes.NUMBER))
        		.declareParameters(new SqlParameter("p_nu_DIM_RIESGO", OracleTypes.VARCHAR))
        		.declareParameters(new SqlParameter("p_nu_PROBABILIDAD", OracleTypes.NUMBER))
        		.declareParameters(new SqlParameter("p_id_USUA_MODI", OracleTypes.VARCHAR))
        		.declareParameters(new SqlParameter("p_de_USUA_MODI_IP", OracleTypes.VARCHAR))
        		.declareParameters(new SqlParameter("p_nu_IMPACTO", OracleTypes.NUMBER))
        		.declareParameters(new SqlParameter("p_de_DIMENSION", OracleTypes.VARCHAR))
                        .declareParameters(new SqlParameter("p_IN_BAJA", OracleTypes.NUMBER))
                        
        		.declareParameters(new SqlParameter("p_id_TIPO_CONTROL_RIESGO", OracleTypes.NUMBER))
        		.declareParameters(new SqlOutParameter("p_NU_RESULT", OracleTypes.NUMBER))
        		.declareParameters(new SqlOutParameter("p_DE_ERROR", OracleTypes.VARCHAR));
        
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_id_DIM_RIESGO", dimension.getIdDimRiesgo())
                .addValue("p_id_EMPRESA", dimension.getIdEmpresa())
		        .addValue("p_nu_DIM_RIESGO", dimension.getNuDimRiesgo())
		        .addValue("p_nu_PROBABILIDAD", dimension.getNuProbabilidad())
		        .addValue("p_id_USUA_MODI", dimension.getIdUsuaModi())
		        .addValue("p_de_USUA_MODI_IP", dimension.getDeUsuaModiIp())
		        .addValue("p_nu_IMPACTO", dimension.getNuImpacto())
		        .addValue("p_de_DIMENSION", dimension.getDeDimension())
                        .addValue("p_IN_BAJA", dimension.getInBaja())
		        .addValue("p_id_TIPO_CONTROL_RIESGO", dimension.getIdTipoControlRiesgo());
        
        Map<String,Object> out = call.execute(in);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        dTODimension.setCodigoResultado(numeroResultado);
        String descripcionResultado = out.get(SNConstantes.DE_ERROR).toString();
        dTODimension.setDescripcionResultado(descripcionResultado);
		return dTODimension;
	}

	@Override
	public DTODimension anularDimension(DTODimension dTODimension) throws DataAccessException {
		// TODO Auto-generated method stubDTOPeriodo dtoPeriodo = new DTOPeriodo();
		DTODimension dimension = new DTODimension();
		SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
				.withCatalogName(SNConstantes.PKG_CONFIGURACION)
				.withProcedureName(SNConstantes.SP_ANULAR_DIMENSION)
				.declareParameters(new SqlParameter("p_id_DIM_RIESGO", OracleTypes.NUMBER))
				.declareParameters(new SqlParameter("p_id_USUA_MODI", OracleTypes.VARCHAR))
				.declareParameters(new SqlParameter("p_de_USUA_MODI_IP", OracleTypes.VARCHAR))
				.declareParameters(new SqlOutParameter("p_NU_RESULT", OracleTypes.NUMBER))
				.declareParameters(new SqlOutParameter("p_DE_ERROR", OracleTypes.VARCHAR));
		
		SqlParameterSource in = new MapSqlParameterSource()
				.addValue("p_id_DIM_RIESGO", dTODimension.getIdDimRiesgo())
				.addValue("p_id_USUA_MODI", dTODimension.getIdUsuaModi())
				.addValue("p_de_USUA_MODI_IP", dTODimension.getDeUsuaModiIp());
		
		Map<String,Object> out = call.execute(in);
		BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
		dimension.setCodigoResultado(numeroResultado);
		String descripcionResultado = out.get(SNConstantes.DE_ERROR).toString();
		dimension.setDescripcionResultado(descripcionResultado);
		
		return dimension;
	}

}

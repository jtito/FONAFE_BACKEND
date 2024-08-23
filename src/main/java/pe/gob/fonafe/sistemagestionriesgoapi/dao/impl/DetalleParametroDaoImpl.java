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
import pe.gob.fonafe.sistemagestionriesgoapi.dao.IDetalleParametroDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTODetalleParametro;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOParametro;
import pe.gob.fonafe.sistemagestionriesgoapi.models.DetalleParametroBean;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Repository
public class DetalleParametroDaoImpl implements IDetalleParametroDao {

    private final JdbcTemplate jdbcTemplate;

    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    public DetalleParametroDaoImpl(@Qualifier("jdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public DTODetalleParametro registrarDetalleParametro(DetalleParametroBean detalleParametroBean) throws DataAccessException {
        logger.info("Inicio de DetalleParametroDaoImpl - registrarDetalleParametro");
        logger.info(detalleParametroBean);

        DTODetalleParametro dtoDetalleParametro = new DTODetalleParametro();
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_PARAMETRO)
                .withProcedureName(SNConstantes.SP_INSERT_DETALLE_PARAMETRO)
                .declareParameters(new SqlParameter("p_ID_PARAMETRO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_DE_PARAMETRO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_VALOR1", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_VALOR2", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_VALOR3", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_VALOR4", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_FE_VALOR1", OracleTypes.DATE))
                .declareParameters(new SqlParameter("p_FE_VALOR2", OracleTypes.DATE))
                .declareParameters(new SqlParameter("p_FE_VALOR3", OracleTypes.DATE))
                .declareParameters(new SqlParameter("p_FE_VALOR4", OracleTypes.DATE))
                .declareParameters(new SqlParameter("p_CO_PARAMETRO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_USUA_CREA", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_USUA_CREA_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter("p_ID_DETA_PARAMETRO_OUT", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));

        SimpleDateFormat parse = new SimpleDateFormat("dd/MM/yyyy");
        Date feValor1 = null;
        Date feValor2 = null;
        Date feValor3 = null;
        Date feValor4 = null;
        try {
            feValor1 = parse.parse(detalleParametroBean.getFeValor1());
            feValor2 = parse.parse(detalleParametroBean.getFeValor2());
            feValor3 = parse.parse(detalleParametroBean.getFeValor3());
            feValor4 = parse.parse(detalleParametroBean.getFeValor4());
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error de parseo");
        }


        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_PARAMETRO", detalleParametroBean.getIdParametro())
                .addValue("p_DE_PARAMETRO", detalleParametroBean.getDeParametro())
                .addValue("p_DE_VALOR1", detalleParametroBean.getDeValor1())
                .addValue("p_DE_VALOR2", detalleParametroBean.getDeValor2())
                .addValue("p_DE_VALOR3", detalleParametroBean.getDeValor3())
                .addValue("p_DE_VALOR4", detalleParametroBean.getDeValor4())
                .addValue("p_FE_VALOR1", feValor1)
                .addValue("p_FE_VALOR2", feValor2)
                .addValue("p_FE_VALOR3", feValor3)
                .addValue("p_FE_VALOR4", feValor4)
                .addValue("p_CO_PARAMETRO", detalleParametroBean.getCoParametro())
                .addValue("p_ID_USUA_CREA", detalleParametroBean.getUsuarioCreacion())
                .addValue("p_DE_USUA_CREA_IP", detalleParametroBean.getIpCreacion());

        Map<String, Object> out = call.execute(in);
        BigDecimal idDetaParametroOut = (BigDecimal) out.get("p_ID_DETA_PARAMETRO_OUT");
        dtoDetalleParametro.setIdDetaParametro(idDetaParametroOut.longValue());
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        dtoDetalleParametro.setCodigoResultado(numeroResultado);
        String descripcionResultado = out.get(SNConstantes.DE_ERROR).toString();
        dtoDetalleParametro.setDescripcionResultado(descripcionResultado);


        logger.info("Fin de DetalleParametroDaoImpl - registrarDetalleParametro");

        return dtoDetalleParametro;
    }

    @Override
    public DTODetalleParametro obtenerDetalleParametro(Long idDetalleParametro) throws DataAccessException {
        logger.info("Inicios de DetalleParametroDaoImpl - obtenerDetalleParametro");

        DTODetalleParametro detalleParametro;

        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_PARAMETRO)
                .withProcedureName(SNConstantes.SP_OBTENER_DETALLE_PARAMETRO)
                .declareParameters(new SqlParameter("p_ID_DETA_PARAMETRO", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTODetalleParametro.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_DETA_PARAMETRO", idDetalleParametro);

        Map<String, Object> out = call.execute(in);
        ArrayList<DTODetalleParametro> detalleParametroArray = (ArrayList<DTODetalleParametro>) out.get(SNConstantes.O_CURSOR);
        Iterator<DTODetalleParametro> iteradorDetalleParametro = detalleParametroArray.iterator();

        detalleParametro = new DTODetalleParametro();

        while (iteradorDetalleParametro.hasNext()){
            DTODetalleParametro itrDetaParam = iteradorDetalleParametro.next();
            detalleParametro.setIdDetaParametro(itrDetaParam.getIdDetaParametro());
            detalleParametro.setIdParametro(itrDetaParam.getIdParametro());
            detalleParametro.setDeParametro(itrDetaParam.getDeParametro());
            detalleParametro.setDeValor1(itrDetaParam.getDeValor1());
            detalleParametro.setDeValor2(itrDetaParam.getDeValor2());
            detalleParametro.setDeValor3(itrDetaParam.getDeValor3());
            detalleParametro.setDeValor4(itrDetaParam.getDeValor4());
            detalleParametro.setFeValor1(itrDetaParam.getFeValor1());
            detalleParametro.setFeValor2(itrDetaParam.getFeValor2());
            detalleParametro.setFeValor3(itrDetaParam.getFeValor3());
            detalleParametro.setFeValor4(itrDetaParam.getFeValor4());
            detalleParametro.setCoParametro(itrDetaParam.getCoParametro());
            detalleParametro.setIndicadorBaja(itrDetaParam.getIndicadorBaja());
            detalleParametro.setFechaCreacion(itrDetaParam.getFechaCreacion());
        }

        logger.info("Fin de DetalleParametroDaoImpl - obtenerDetalleParametro");
        return detalleParametro;
    }

    @Override
    public DTOGenerico listarDetalleParametro(Long idParametro) throws DataAccessException {
        logger.info("Inicio de DetalleParametroDaoImpl - listarDetalleParametro");

        List<DTODetalleParametro> listaDetalleParametros;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_PARAMETRO)
                .withProcedureName(SNConstantes.SP_LISTAR_DETALLE_PARAMETRO)
                .declareParameters(new SqlParameter("p_ID_PARAMETRO", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTODetalleParametro.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_PARAMETRO", idParametro);

        Map<String, Object> out = call.execute(in);
        listaDetalleParametros = (List<DTODetalleParametro>) out.get(SNConstantes.O_CURSOR);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionResultado = (String) out.get(SNConstantes.DE_ERROR);
        logger.info("numero resultado:" + numeroResultado);
        logger.info("descripcion error:" + descripcionResultado);

        DTOGenerico dtoGenerico = new DTOGenerico();
        dtoGenerico.setCodigoResultado(numeroResultado);
        dtoGenerico.setDescripcionResultado(descripcionResultado);
        dtoGenerico.setListado(listaDetalleParametros);

        logger.info("Fin de DetalleParametroDaoImpl - listarDetalleParametro");

        return dtoGenerico;
    }

    @Override
    public DTODetalleParametro actualizarDetalleParametro(DetalleParametroBean detalleParametroBean) throws DataAccessException {
        logger.info("Inicio de DetalleParametroDaoImpl - actualizarDetalleParametro");

        DTODetalleParametro dtoDetalleParametro;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_PARAMETRO)
                .withProcedureName(SNConstantes.SP_UPDATE_DETALLE_PARAMETRO)
                .declareParameters(new SqlParameter("p_ID_DETA_PARAMETRO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_PARAMETRO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_DE_PARAMETRO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_VALOR1", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_VALOR2", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_VALOR3", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_VALOR4", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_FE_VALOR1", OracleTypes.DATE))
                .declareParameters(new SqlParameter("p_FE_VALOR2", OracleTypes.DATE))
                .declareParameters(new SqlParameter("p_FE_VALOR3", OracleTypes.DATE))
                .declareParameters(new SqlParameter("p_FE_VALOR4", OracleTypes.DATE))
                .declareParameters(new SqlParameter("p_CO_PARAMETRO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_IN_BAJA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_USUA_MODI", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_USUA_MODI_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_DETA_PARAMETRO", detalleParametroBean.getIdDetaParametro())
                .addValue("p_ID_PARAMETRO", detalleParametroBean.getIdParametro())
                .addValue("p_DE_PARAMETRO", detalleParametroBean.getDeParametro())
                .addValue("p_DE_VALOR1", detalleParametroBean.getDeValor1())
                .addValue("p_DE_VALOR2", detalleParametroBean.getDeValor2())
                .addValue("p_DE_VALOR3", detalleParametroBean.getDeValor3())
                .addValue("p_DE_VALOR4", detalleParametroBean.getDeValor4())
                .addValue("p_FE_VALOR1", detalleParametroBean.getFeValor1())
                .addValue("p_FE_VALOR2", detalleParametroBean.getFeValor2())
                .addValue("p_FE_VALOR3", detalleParametroBean.getFeValor3())
                .addValue("p_FE_VALOR4", detalleParametroBean.getFeValor4())
                .addValue("p_CO_PARAMETRO", detalleParametroBean.getCoParametro())
                .addValue("p_IN_BAJA", detalleParametroBean.getIndicadorBaja())
                .addValue("p_ID_USUA_MODI", detalleParametroBean.getUsuarioCreacion())
                .addValue("p_DE_USUA_MODI_IP", detalleParametroBean.getIpModificacion());

        Map<String, Object> out = call.execute(in);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionResultado = out.get(SNConstantes.DE_ERROR).toString();


        dtoDetalleParametro = new DTODetalleParametro();
        dtoDetalleParametro.setIdDetaParametro(detalleParametroBean.getIdDetaParametro());
        dtoDetalleParametro.setCodigoResultado(numeroResultado);
        dtoDetalleParametro.setDescripcionResultado(descripcionResultado);
        logger.info(" Número de error actualizar Parametro : {}", numeroResultado);

        logger.info("Fin de DetalleParametroDaoImpl - actualizarDetalleParametro");
        return dtoDetalleParametro;
    }

    @Override
    public Byte anularDetalleParametro(DetalleParametroBean detalleParametroBean) throws DataAccessException {
        logger.info("Inicio de DetalleParametroDaoImpl - anularDetalleParametro");

        Byte resultadoAnularDetalleParametro;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_PARAMETRO)
                .withProcedureName(SNConstantes.SP_ANULAR_DETALLE_PARAMETRO)
                .declareParameters(new SqlParameter("p_ID_DETA_PARAMETRO", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_USUA_MODI", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_USUA_MODI_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_DETA_PARAMETRO", detalleParametroBean.getIdDetaParametro())
                .addValue("p_ID_USUA_MODI", detalleParametroBean.getUsuarioModificacion())
                .addValue("p_DE_USUA_MODI_IP", detalleParametroBean.getIpModificacion());

        Map<String, Object> out = call.execute(in);
        BigDecimal result = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        resultadoAnularDetalleParametro = result.byteValueExact();
        String descripcionError = (String) out.get(SNConstantes.DE_ERROR);
        logger.info("Número de error anular Detalle Parametro : {}", result);
        logger.info("Descripcion de error anular Detalle Parametro : {}", descripcionError);

        logger.info("Fin de DetalleParametroDaoImpl - anularDetalleParametro");
        return resultadoAnularDetalleParametro;
    }
}

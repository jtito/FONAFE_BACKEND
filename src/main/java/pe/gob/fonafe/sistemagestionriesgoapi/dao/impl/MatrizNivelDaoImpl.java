/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.fonafe.sistemagestionriesgoapi.dao.impl;

/**
 * @author joell
 */

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
import pe.gob.fonafe.sistemagestionriesgoapi.dao.IMatrizNivelDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOMatrizNivel;
import pe.gob.fonafe.sistemagestionriesgoapi.models.MatrizNivelBean;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Repository
public class MatrizNivelDaoImpl implements IMatrizNivelDao {

    private final JdbcTemplate jdbcTemplate;

    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    public MatrizNivelDaoImpl(@Qualifier("jdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public DTOMatrizNivel registrarMatriz(MatrizNivelBean matriznivelBean) throws DataAccessException {

        logger.info("DTO Inicio registrar Matriz " + matriznivelBean.getIdEmpresa());
        DTOMatrizNivel dTOMatrizNivel = new DTOMatrizNivel();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonPeticion = gson.toJson(matriznivelBean);
        logger.info("DTO Inicio registrar Matriz " + jsonPeticion);
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONFIGURACION)
                .withProcedureName(SNConstantes.SP_INSERT_MATRIZ_NIVEL)
                .declareParameters(new SqlParameter("p_ID_GERENCIA", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_USUA_CREA", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_USUA_CREA_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_MATRIZ_NIVEL", OracleTypes.VARCHAR))

                .declareParameters(new SqlParameter("p_ID_TIPO_MATRIZ", OracleTypes.VARCHAR))

                .declareParameters(new SqlParameter("p_ID_SEDE_EMPRESA", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_MATRIZ_NIVEL", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_MATRIZNIVEL", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_TIPO_MATRIZ", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter("p_ID_MATRIZ_NIVEL_OUT", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));


        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_GERENCIA", matriznivelBean.getIdGerencia())
                .addValue("p_ID_USUA_CREA", matriznivelBean.getUsuarioCreacion())
                .addValue("p_DE_USUA_CREA_IP", matriznivelBean.getIpCreacion())
                .addValue("p_ID_EMPRESA", matriznivelBean.getIdEmpresa())
                .addValue("p_ID_TIPO_MATRIZ", matriznivelBean.getIdTipoMatriz())
                .addValue("p_ID_SEDE_EMPRESA", matriznivelBean.getIdSedeEmpresa())
                .addValue("p_MATRIZNIVEL", matriznivelBean.getMatrizNivel())
                .addValue("p_ID_TIPO_MATRIZ", matriznivelBean.getIdTipoMatriz())
                .addValue("p_DE_MATRIZ_NIVEL", matriznivelBean.getDeMatrizNivel());
        logger.info("DTO Inicio registrar Matriz Cont 1 " + jsonPeticion);

        Map<String, Object> out = call.execute(in);
        BigDecimal idMatrizOut = (BigDecimal) out.get("p_ID_MATRIZ_NIVEL_OUT");
        logger.info("DTO Inicio registrar Matriz Cont 2" + idMatrizOut);
        String numeroResult = (String) out.get(SNConstantes.NUM_RESULT);
        String descripcionError = out.get(SNConstantes.DE_ERROR).toString();
        logger.info("DTO Inicio registrar Matriz Cont 3" + descripcionError);
        dTOMatrizNivel.setIdMatrizNivel(idMatrizOut.longValue());

        dTOMatrizNivel.setCodigoMensaje(numeroResult);

        logger.info("Error insert " + descripcionError);
        dTOMatrizNivel.setDescripcionMensaje(descripcionError);

        logger.info("Fin de MatrizNivelImpl - registrarProceso");
        return dTOMatrizNivel;

    }

    @Override
    public DTOMatrizNivel obtenerMatriz(Long idMatriz) throws DataAccessException {

        logger.info("Inicio MatrizDaoImpl - obtenerMatriz");

        DTOMatrizNivel dtoMatriz;

        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONSULTAS)
                .withProcedureName(SNConstantes.SP_OBTENER_MATRIZ_NIVEL)
                .declareParameters(new SqlParameter("p_ID_MATRIZ_NIVEL", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTOMatrizNivel.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_MATRIZ_NIVEL", idMatriz);

        Map<String, Object> out = call.execute(in);
        String numeroResultado = (String) out.get(SNConstantes.NUM_RESULT);
        String descripcionError = (String) out.get(SNConstantes.DE_ERROR);
        logger.info("numero resultado:" + numeroResultado);
        logger.info("descripcion error:" + descripcionError);

        ArrayList<DTOMatrizNivel> matrizArray = (ArrayList<DTOMatrizNivel>) out.get(SNConstantes.O_CURSOR);
        Iterator<DTOMatrizNivel> iteradorMatriz = matrizArray.iterator();

        dtoMatriz = new DTOMatrizNivel();
        while (iteradorMatriz.hasNext()) {
            DTOMatrizNivel itrMatrizNivel = iteradorMatriz.next();
            dtoMatriz.setIdMatrizNivel(itrMatrizNivel.getIdMatrizNivel());
            dtoMatriz.setDeMatrizNivel(itrMatrizNivel.getDeMatrizNivel());
            dtoMatriz.setIdEmpresa(itrMatrizNivel.getIdEmpresa());
            dtoMatriz.setIdSedeEmpresa(itrMatrizNivel.getIdSedeEmpresa());
            dtoMatriz.setIdGerencia(itrMatrizNivel.getIdGerencia());
            dtoMatriz.setIndicadorBaja(itrMatrizNivel.getIndicadorBaja());
            dtoMatriz.setFechaCreacion(itrMatrizNivel.getFechaCreacion());
            dtoMatriz.setIdTipoMatriz(itrMatrizNivel.getIdTipoMatriz());
            dtoMatriz.setMatrizNivel(itrMatrizNivel.getMatrizNivel());
        }

        logger.info("Fin MatrizDaoImpl - obtenerMatriz");
        return dtoMatriz;


    }

    @Override
    public List<DTOMatrizNivel> listarMatriz(Long idEmpresa, Long idSede, Long idTipoMatriz) throws DataAccessException {

        logger.info("Inicio MatrizDaoImpl - listarMatriz");

        List<DTOMatrizNivel> listaMatrizNivel;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONSULTAS)
                .withProcedureName(SNConstantes.SP_LISTAR_MATRIZ)
                .declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_SEDE_EMPRESA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_MATRIZNIVEL", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.O_CURSOR, OracleTypes.CURSOR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR))
                .returningResultSet(SNConstantes.O_CURSOR, BeanPropertyRowMapper.newInstance(DTOMatrizNivel.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_EMPRESA", idEmpresa)
                .addValue("p_ID_SEDE_EMPRESA", idSede)
                 .addValue("p_MATRIZNIVEL", idTipoMatriz);

        Map<String, Object> out = call.execute(in);

        listaMatrizNivel = (List<DTOMatrizNivel>) out.get(SNConstantes.O_CURSOR);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        String descripcionError = (String) out.get(SNConstantes.DE_ERROR);
        logger.info("numero resultado:" + numeroResultado);
        logger.info("descripcion error:" + descripcionError);

        logger.info("Fin MatrizDaoImpl - listarMatriz");
        return listaMatrizNivel;

    }

    @Override
    public DTOMatrizNivel actualizarMatriz(MatrizNivelBean matriznivelBean) throws DataAccessException {

        logger.info("Inicio MatrizDaoImpl - actualizarMatriz");

        DTOMatrizNivel dtoMatriz;


        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONFIGURACION)
                .withProcedureName(SNConstantes.SP_UPDATE_MATRIZ_NIVEL)
                .declareParameters(new SqlParameter("p_ID_MATRIZ_NIVEL", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_DE_MATRIZ_NIVEL", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_GERENCIA", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_USUA_MODI", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_USUA_MODI_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_IN_BAJA", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_EMPRESA", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ID_SEDE_EMPRESA", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_MATRIZ_NIVEL", matriznivelBean.getIdMatrizNivel())
                .addValue("p_DE_MATRIZ_NIVEL", matriznivelBean.getDeMatrizNivel())
                .addValue("p_ID_GERENCIA", matriznivelBean.getIdGerencia())
                .addValue("p_ID_USUA_MODI", matriznivelBean.getUsuarioModificacion())
                .addValue("p_DE_USUA_MODI_IP", matriznivelBean.getIpModificacion())
                .addValue("p_IN_BAJA", matriznivelBean.getIndicadorBaja())
                .addValue("p_ID_EMPRESA", matriznivelBean.getIdEmpresa())
                .addValue("p_ID_SEDE_EMPRESA", matriznivelBean.getIdSedeEmpresa());

        Map<String, Object> out = call.execute(in);
        String numeroResultado = (String) out.get(SNConstantes.NUM_RESULT);
        String descripcionError = out.get(SNConstantes.DE_ERROR).toString();

        dtoMatriz = new DTOMatrizNivel();
        dtoMatriz.setIdMatrizNivel(matriznivelBean.getIdMatrizNivel());
        dtoMatriz.setCodigoMensaje(numeroResultado);
        dtoMatriz.setDescripcionMensaje(descripcionError);
        logger.info(" Número de resultado al actualizar Matriz : {}", numeroResultado);

        logger.info("Fin de MatrizDaoImpl - actualizarMatriz");
        return dtoMatriz;
    }

    @Override
    public Byte anularMatriz(MatrizNivelBean matriznivelBean) throws DataAccessException {


        logger.info("Inicio ClaseDaoImpl - anularMatri");
        Byte resultadoAnularMatriz;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(SNConstantes.PKG_CONFIGURACION)
                .withProcedureName(SNConstantes.SP_ANULAR_MATRIZ_NIVEL)
                .declareParameters(new SqlParameter("p_ID_MATRIZ_NIVEL", OracleTypes.NUMBER))
                .declareParameters(new SqlParameter("p_ID_USUA_MODI", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DE_USUA_MODI_IP", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter(SNConstantes.NUM_RESULT, OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter(SNConstantes.DE_ERROR, OracleTypes.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID_MATRIZ_NIVEL", matriznivelBean.getIdMatrizNivel())
                .addValue("p_ID_USUA_MODI", matriznivelBean.getUsuarioModificacion())
                .addValue("p_DE_USUA_MODI_IP", matriznivelBean.getIpModificacion());

        Map<String, Object> out = call.execute(in);
        BigDecimal numeroResultado = (BigDecimal) out.get(SNConstantes.NUM_RESULT);
        resultadoAnularMatriz = numeroResultado.byteValueExact();
        String descripcionError = (String) out.get(SNConstantes.DE_ERROR);
        logger.info("Número de resultado anular Matriz : {}", numeroResultado);
        logger.info("Descripcion de error anular Matriz : {}", descripcionError);

        logger.info("Fin MatrizDaoImpl - anularMatriz");
        return resultadoAnularMatriz;

    }


}

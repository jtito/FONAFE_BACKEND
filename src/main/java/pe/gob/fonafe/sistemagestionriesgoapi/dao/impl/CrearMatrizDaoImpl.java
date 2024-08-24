package pe.gob.fonafe.sistemagestionriesgoapi.dao.impl;
import oracle.jdbc.OracleTypes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import pe.gob.fonafe.sistemagestionriesgoapi.dao.ICrearMatrizDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOCrearMatriz;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.System.out;

@Repository
public class CrearMatrizDaoImpl implements ICrearMatrizDao {
    // el JdbcTemplate es una clase de Spring que simplifica el uso de JDBC
    private final JdbcTemplate jdbcTemplate;

    // este Logger logger es para poder imprimir mensajes en la consola
    private static final Logger logger = LogManager.getLogger("CREAR_MATRIZ_API");

    public CrearMatrizDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public DTOCrearMatriz obtenerCrearMatriz() {
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CONFIGURACION") //establece el nombre del paquete de la base de datos que contiene el procedimiento almacenado que se va a llama
                .withProcedureName("SP_OBTENER_CREAR_MATRIZ") //establece el nombre del procedimiento almacenado que se va a llamar
                .declareParameters(new SqlOutParameter("NUM_RESULT", OracleTypes.NUMBER)) // este parámetro contendrá un valor numérico que el procedimiento almacenado ha producido
                .declareParameters(new SqlOutParameter("DE_ERROR", OracleTypes.VARCHAR)); // este parámetro contendrá un valor de cadena que el procedimiento almacenado ha producido

        Map<String, Object> out = call.execute(); // ejecuta el procedimiento y el resultado de la ejecución de este se devuelve como un MAP donde las claves son "NUM_RESULT" y "DE_ERROR" y sus valores puede ser 1, "Operacion exitosa"
        String numeroResultado = out.get("NUM_RESULT").toString();
        String descripcionResultado = out.get("DE_ERROR").toString();

        DTOCrearMatriz dtoCrearMatriz = new DTOCrearMatriz();
        dtoCrearMatriz.setCodigoRiesgo(numeroResultado); // esta linea de codigo lo que hace es setear el valor de la variable numeroResultado a la variable codigoRiesgo del objeto DTOCrearMatriz
        dtoCrearMatriz.setDescripcionRiesgo(descripcionResultado);

        return dtoCrearMatriz;
    }

    @Override
    public DTOCrearMatriz crearMatriz(DTOCrearMatriz matriz) {
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CONFIGURACION")
                .withProcedureName("SP_CREAR_MATRIZ")
                .declareParameters(new SqlParameter("p_TIPO_MATRIZ", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_AREA_GERENCIA", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_CODIGO_RIESGO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_CAUSA", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_CONSECUENCIA", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_DESCRIPCION_RIESGO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_ORIGEN", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_AGENTE", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_FRECUENCIA", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_TIPO", OracleTypes.VARCHAR))
                .declareParameters(new SqlParameter("p_SUB_TIPO", OracleTypes.VARCHAR))
                .declareParameters(new SqlOutParameter("NUM_RESULT", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter("DE_ERROR", OracleTypes.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_TIPO_MATRIZ", matriz.getTipoMatriz())
                .addValue("p_AREA_GERENCIA", matriz.getAreaGerencia().toString())
                .addValue("p_CODIGO_RIESGO", matriz.getCodigoRiesgo())
                .addValue("p_CAUSA", matriz.getCausa())
                .addValue("p_CONSECUENCIA", matriz.getConsecuencia())
                .addValue("p_DESCRIPCION_RIESGO", matriz.getDescripcionRiesgo())
                .addValue("p_ORIGEN", matriz.getOrigen().toString())
                .addValue("p_AGENTE", matriz.getAgente())
                .addValue("p_FRECUENCIA", matriz.getFrecuencia().toString())
                .addValue("p_TIPO", matriz.getTipo().toString())
                .addValue("p_SUB_TIPO", matriz.getSubTipo().toString());

        Map<String, Object> out = call.execute(in);

        String numeroResultado = out.get("NUM_RESULT").toString();
        String descripcionResultado = out.get("DE_ERROR").toString();

        matriz.setCodigoRiesgo(numeroResultado);
        matriz.setDescripcionRiesgo(descripcionResultado);

        return matriz;
    }

    @Override
    public List<DTOCrearMatriz> listarMatrices() {
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CONFIGURACION")
                .withProcedureName("SP_LISTAR_MATRICES")
                .declareParameters(new SqlOutParameter("NUM_RESULT", OracleTypes.NUMBER))
                .declareParameters(new SqlOutParameter("DE_ERROR", OracleTypes.VARCHAR))
                .returningResultSet("O_CURSOR", BeanPropertyRowMapper.newInstance(DTOCrearMatriz.class));

        Map<String, Object> out = call.execute();
        List<DTOCrearMatriz> listaMatrices = (List<DTOCrearMatriz>) out.get("O_CURSOR");

        String numeroResultado = (String) out.get("NUM_RESULT");
        String descripcionResultado = (String) out.get("DE_ERROR");

        for (DTOCrearMatriz matriz : listaMatrices) {
            matriz.setCodigoRiesgo(numeroResultado);
            matriz.setDescripcionRiesgo(descripcionResultado);
        }

        return listaMatrices;
    }
}

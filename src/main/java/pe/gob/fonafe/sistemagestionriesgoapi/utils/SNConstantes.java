package pe.gob.fonafe.sistemagestionriesgoapi.utils;

import java.math.BigDecimal;

public class SNConstantes {

    public static final String DOMINIO_AUTORIZADO = "http://localhost:4200";

    //-- Codigo Respuesta REST
    public static final String MENSAJE = "mensaje";
    public static final String CODIGO = "codigo";
    public static final BigDecimal CODIGO_EXITO = new BigDecimal(1);
    public static final BigDecimal CODIGO_ERROR = new BigDecimal(99);
    public static final BigDecimal CODIGO_DUPLICATE = new BigDecimal(101);

    public static final String NUM_RESULT = "p_NU_RESULT";
    public static final String DE_ERROR = "p_DE_ERROR";
    public static final String DE_RESULT_OK = "OK";

    //-- Descripcion Respuestas REST
    public static final String MENSAJE_EXITO = "SUCCESS";
    public static final String MENSAJE_ERR0R = "Se ha producido un error durante el Proceso";
    public static final String MENSAJE_DUPLICADO_PERIODO = "Ya existe una matriz para el periodo seleccionado";
    public static final String MENSAJE_ERR0R_BD = "Se ha producido un error de persistencia a la base de datos";

    public static final String MENSAJE_EXITO_REGISTRAR = " se creó con éxito";
    public static final String MENSAJE_EXITO_REGISTRAR_MULTIPLE = " se crearon con éxito";
    public static final String MENSAJE_EXITO_ACTUALIZAR = " se actualizó con éxito";
    public static final String MENSAJE_EXITO_ACTUALIZAR_MULTIPLE = " se actualizaron con éxito";
    public static final String MENSAJE_EXITO_ANULAR = " se anuló con éxito";

    /* Base de Datos */
    public static final String PKG_CONSULTAS = "PKG_CONSULTAS";
    public static final String PKG_SEGURIDAD = "PKG_SEGURIDAD";
    public static final String PKG_CONFIGURACION = "PKG_CONFIGURACION";
    public static final String PKG_PARAMETRO = "PKG_PARAMETRO";
    public static final String PKG_ARCHIVOS = "PKG_ARCHIVOS";
    public static final String PKG_TRANSACCION = "PKG_TRANSACCION";
    public static final String PKG_REPORTES = "PKG_REPORTES";
    public static final String SP_REPORTE_MATRIZ = "SP_REPORTE_MATRIZ";
    public static final String O_CURSOR = "oCURSOR";
    public static final String O_CURSORENTIDAD = "oCURSORENTIDAD";
    public static final String O_CURSORMAILCONFIG = "oCURSORMAILCONFIG";
    public static final String O_CURSOR_CONTROL = "oCURSOR_CONTROL";
    public static final String O_CURSOR_PLAN = "oCURSOR_PLAN";
    public static final String O_CURSOR_EVENTO = "oCURSOR_EVENTO";
    public static final String O_CURSOR_CUMP = "oCURSOR_CUMP";
    public static final String SP_LISTAR_PERIODO = "SP_LISTAR_PERIODO";
    public static final String SP_OBTENER_PERIODO = "SP_OBTENER_PERIODO";
    public static final String SP_INSERT_PERIODO = "SP_INSERT_PERIODO";
    public static final String SP_UPDATE_PERIODO = "SP_UPDATE_PERIODO";
    public static final String SP_ANULAR_PERIODO = "SP_ANULAR_PERIODO";
    public static final String SP_CODIGO_PERIODO = "SP_CODIGO_PERIODO";
    public static final String SP_LISTAR_DIMENSION = "SP_LISTAR_DIMENSION";
    public static final String SP_OBTENER_DIMENSION = "SP_OBTENER_DIMENSION";
    public static final String SP_INSERT_DIMENSION = "SP_INSERT_DIMENSION";
    public static final String SP_UPDATE_DIMENSION = "SP_UPDATE_DIMENSION";
    public static final String SP_ANULAR_DIMENSION = "SP_ANULAR_DIMENSION";

    public static final String SP_LISTAR_ROLES_USUARIO = "SP_LISTAR_ROLES_USUARIO";
    public static final String SP_OBTENER_USUARIO = "SP_OBTENER_USUARIO";
    public static final String SP_OBTENER_AD_INFO_USUARIO = "SP_OBTENER_AD_INFO_USUARIO";
    public static final String SP_LISTAR_MENU_USUARIO = "SP_LISTAR_MENU_USUARIO";
    public static final String SP_LISTAR_SUBMENU_USUARIO = "SP_LISTAR_SUBMENU_USUARIO";
    public static final String SP_OBTENER_DATOS_EMP_USU = "SP_OBTENER_DATOS_EMP_USU";

    //* Parametro */
    public static final String SP_LISTAR_PARAMETROS_CODIGO = "SP_LISTAR_PARAMETRO_X_COD";
    public static final String SP_INSERT_PARAMETRO = "SP_INSERT_PARAMETRO";
    public static final String SP_LISTAR_PARAMETRO = "SP_LISTAR_PARAMETRO";
    public static final String SP_UPDATE_PARAMETRO = "SP_UPDATE_PARAMETRO";
    public static final String SP_ANULAR_PARAMETRO = "SP_ANULAR_PARAMETRO";
    public static final String SP_OBTENER_PARAMETRO = "SP_OBTENER_PARAMETRO";

    public static final String MENSAJE_PARAMETRO = "El parametro ";
    public static final String ID_PARAMETRO = "idParametro";

    //* CORREO */
    public static final String SP_OBTENER_CONF_CORREO = "SP_OBTENER_CONF_CORREO";
    public static final String SP_LISTAR_FECH_VENCIMIENTO = "SP_LISTAR_FECH_VENCIMIENTO";
    public static final String SP_UPDATE_NOTIF_PLAN_ACC = "SP_UPDATE_NOTIF_PLAN_ACC";
    public static final String MENSAJE_CONFIG_CORREO = "La configuración ";
    public static final String ID_CONFIG_CORREO = "idConfigCorreo";

    //* MATRIZ_EVIDENCIACONTROL*/
    public static final String SP_INSERT_MATRIZ_EVID_CONTROL = "SP_INSERT_MATRIZ_EVID_CONTROL";
    public static final String SP_LISTAR_MATRIZ_EVID_CONTROL = "SP_LISTAR_MATRIZ_EVID_CONTROL";
    public static final String SP_ANULAR_MATRIZ_EVID_CONTROL = "SP_ANULAR_MATRIZ_EVID_CONTROL";

    //* Detalle Parametro */
    public static final String SP_INSERT_DETALLE_PARAMETRO = "SP_INSERT_DETALLE_PARAMETRO";
    public static final String SP_LISTAR_DETALLE_PARAMETRO = "SP_LISTAR_DETALLE_PARAMETRO";
    public static final String SP_UPDATE_DETALLE_PARAMETRO = "SP_UPDATE_DETALLE_PARAMETRO";
    public static final String SP_ANULAR_DETALLE_PARAMETRO = "SP_ANULAR_DETALLE_PARAMETRO";
    public static final String SP_OBTENER_DETALLE_PARAMETRO = "SP_OBTENER_DETALLE_PARAMETRO";

    public static final String MENSAJE_DETALLE_PARAMETRO = "El detalle parametro ";
    public static final String ID_DETALLE_PARAMETRO = "idDetalleParametro";

    //* Responsable */
    public static final String SP_LISTAR_RESPONSABLE = "SP_LISTAR_RESPONSABLE";
    public static final String SP_INSERT_RESPONSABLE = "SP_INSERT_USUARIO";
    public static final String SP_UPDATE_RESPONSABLE = "SP_UPDATE_USUARIO";
    public static final String SP_UPDATE_FILE_RESPONSABLE = "SP_UPDATE_FILE_USER";
    public static final String SP_ANULAR_RESPONSABLE = "SP_ANULAR_USUARIO";
    public static final String SP_OBTENER_RESPONSABLE = "SP_OBTENER_RESPONSABLE";
    public static final String SP_LISTAR_PERFIL = "SP_LISTAR_PERFIL";
    public static final String SP_INSERT_USUARIOPROCESO = "SP_INSERT_USUARIOPROCESO";
    public static final String SP_OBTENER_USUARIOPROCESO = "SP_OBTENER_USUARIOPROCESO";
    public static final String SP_UPDATE_PASSWORD = "SP_UPDATE_PASSWORD";

    public static final String MENSAJE_RESPONSABLE = "El Usuario ";
    public static final String ID_RESPONSABLE = "idResponsable";

    //* Empresa */
    public static final String SP_LISTAR_EMPRESA = "SP_LISTAR_EMPRESA";
    public static final String SP_INSERT_EMPRESA = "SP_INSERT_EMPRESA";
    public static final String SP_UPDATE_EMPRESA = "SP_UPDATE_EMPRESA";
    public static final String SP_ANULAR_EMPRESA = "SP_ANULAR_EMPRESA";
    public static final String SP_OBTENER_EMPRESA = "SP_OBTENER_EMPRESA";

    public static final String MENSAJE_EMPRESA = "La Empresa ";
    public static final String ID_EMPRESA = "idEmpresa";

    //* Sede */
    public static final String SP_LISTAR_SEDE = "SP_LISTAR_SEDE";
    public static final String SP_INSERT_SEDE = "SP_INSERT_SEDE";
    public static final String SP_UPDATE_SEDE = "SP_UPDATE_SEDE";
    public static final String SP_ANULAR_SEDE = "SP_ANULAR_SEDE";
    public static final String SP_OBTENER_SEDE = "SP_OBTENER_SEDE";

    public static final String MENSAJE_SEDE = "La Sede ";
    public static final String ID_SEDE = "idSede";

    //* Gerencia */
    public static final String SP_LISTAR_GERENCIA = "SP_LISTAR_GERENCIA";
    public static final String SP_INSERT_GERENCIA = "SP_INSERT_GERENCIA";
    public static final String SP_UPDATE_GERENCIA = "SP_UPDATE_GERENCIA";
    public static final String SP_ANULAR_GERENCIA = "SP_ANULAR_GERENCIA";
    public static final String SP_OBTENER_GERENCIA = "SP_OBTENER_GERENCIA";

    public static final String MENSAJE_GERENCIA = "La Gerencia ";
    public static final String ID_GERENCIA = "idGerencia";

    //* Matriz Nivel */
    public static final String SP_LISTAR_MATRIZ = "SP_LISTAR_MATRIZ_NIVEL";
    public static final String SP_INSERT_MATRIZ_NIVEL = "SP_INSERT_MATRIZ_NIVEL";
    public static final String SP_UPDATE_MATRIZ_NIVEL = "SP_UPDATE_MATRIZ_NIVEL";
    public static final String SP_ANULAR_MATRIZ_NIVEL = "SP_ANULAR_MATRIZ_NIVEL";
    public static final String SP_OBTENER_MATRIZ_NIVEL = "SP_OBTENER_MATRIZ_NIVEL";

    public static final String MENSAJE_MATRIZ = "La Cabecera de matriz nivel ";
    public static final String ID_MATRIZNIVEL = "idMatrizNivel";

    //* Proceso Matriz */
    public static final String SP_LISTAR_PROCESO_MATRIZ = "SP_LISTAR_PROC_MATRIZ";
    public static final String SP_INSERT_PROCESO_MATRIZ = "SP_INSERT_PROCESO_MATRIZ";
    public static final String SP_UPDATE_PROCESO_MATRIZ = "SP_UPDATE_PROCESO_MATRIZ";
    public static final String SP_ANULAR_PROCESO_MATRIZ = "SP_ANULAR_PROCESO_MATRIZ";
    public static final String SP_OBTENER_PROCESO_MATRIZ = "SP_OBTENER_PROC_MATRIZ";

    public static final String MENSAJE_PROCESO_MATRIZ = "El proceso matriz ";
    public static final String ID_PROC_MATRIZ = "idProcesoMatriz";

    //* Proceso */
    public static final String SP_LISTAR_PROCESO = "SP_LISTAR_PROCESO";
    public static final String SP_LISTA_PROCESOMATRIZ = "SP_LISTAR_PROCESOMATRIZ";
    public static final String SP_LISTAR_PROCESO_X_MATRIZ = "SP_LISTAR_PROCESO_X_MATRIZ";
    public static final String SP_INSERT_PROCESO = "SP_INSERT_PROCESO";
    public static final String SP_UPDATE_PROCESO = "SP_UPDATE_PROCESO";
    public static final String SP_ANULAR_PROCESO = "SP_ANULAR_PROCESO";
    public static final String SP_OBTENER_PROCESO = "SP_OBTENER_PROCESO";

    public static final Long MATRIZ_OPERACIONAL = 40L;
    public static final Long PROCESO_MATRIZ = 1L;


    //* Sub Proceso */
    public static final String SP_LISTAR_SUBPROCESO = "SP_LISTAR_SUBPROCESO";
    public static final String SP_INSERT_SUBPROCESO = "SP_INSERT_SUBPROCESO";
    public static final String SP_UPDATE_SUBPROCESO = "SP_UPDATE_SUBPROCESO";
    public static final String SP_ANULAR_SUBPROCESO = "SP_ANULAR_SUBPROCESO";
    public static final String SP_OBTENER_SUBPROCESO = "SP_OBTENER_SUBPROCESO";

    public static final String MENSAJE_PROCESO = "El proceso ";
    public static final String ID_PROCESO = "idProceso";

    //* Encuesta */
    public static final String SP_LISTAR_ENCUESTA = "SP_LISTAR_ENCUESTA";
    public static final String SP_INSERT_ENCUESTA = "SP_INSERT_ENCUESTA";
    public static final String SP_UPDATE_ENCUESTA = "SP_UPDATE_ENCUESTA";
    public static final String SP_ANULAR_ENCUESTA = "SP_ANULAR_ENCUESTA";
    public static final String SP_OBTENER_ENCUESTA = "SP_OBTENER_ENCUESTA";
    public static final String SP_OBTENER_REPORTE = "SP_OBTENER_REPORTE";
    public static final String SP_OBTENER_PUNTAJES = "SP_OBTENER_PUNTAJES";

    public static final String MENSAJE_ENCUESTA = "La encuesta ";
    public static final String ID_ENCUESTA = "idEncuesta";

    //* Pregunta */
    public static final String SP_LISTAR_PREGUNTA = "SP_LISTAR_PREGUNTA";
    public static final String SP_INSERT_PREGUNTA = "SP_INSERT_PREGUNTA";
    public static final String SP_UPDATE_PREGUNTA = "SP_UPDATE_PREGUNTA";
    public static final String SP_ANULAR_PREGUNTA = "SP_ANULAR_PREGUNTA";
    public static final String SP_OBTENER_PREGUNTA = "SP_OBTENER_PREGUNTA";

    public static final String MENSAJE_PREGUNTA = "La pregunta ";
    public static final String MENSAJE_PREGUNTAS = "Las preguntas ";
    public static final String ID_PREGUNTA = "idPregunta";

    //* Respuesta */
    public static final String SP_LISTAR_RESPUESTA = "SP_LISTAR_RESPUESTA";
    public static final String SP_INSERT_RESPUESTA = "SP_INSERT_RESPUESTA";
    public static final String SP_UPDATE_RESPUESTA = "SP_UPDATE_RESPUESTA";

    public static final String MENSAJE_RESPUESTA = "La respuesta ";
    public static final String ID_RESPUESTA = "idRespuesta";

    //* Matriz Riesgo */
    public static final String SP_LISTAR_BANDEJA_MATRIZ_RIESGO = "SP_LISTAR_BNDJA_MATRIZ_RSG";
    public static final String SP_INSERT_MATRIZ_RIESGO = "SP_INSERT_MATRIZ_RIESGO";
    public static final String SP_OBTENER_MATRIZ_RIESGO = "SP_OBTENER_MATRIZ_RIESGO";
    public static final String SP_OBTENER_COMENT_DETA_RIESGO = "SP_OBTENER_COMENT_DETA_RIESGO";
    public static final String SP_UPDATE_MATRIZ_RIESGO = "SP_UPDATE_MATRIZ_RIESGO";
    public static final String SP_ANULAR_MATRIZ_RIESGO = "SP_ANULAR_MATRIZ_RIESGO";
    public static final String SP_INSERT_DETALLE_MATRIZRIESGO = "SP_INSERT_DETALLE_MATRIZRIESGO";
    public static final String SP_UPDATE_DETALLE_MATRIZRIESGO = "SP_UPDATE_DETA_MATRIZRIESGO";
    public static final String SP_UPDATE_COMENT_DETA_RIESGO = "SP_UPDATE_COMENT_DETA_RIESGO";
    public static final String SP_LISTAR_DETALLE_MATRIZRIESGO = "SP_LISTAR_DETA_MATRIZRIESGO";
    public static final String SP_OBTENER_MATRIZ_RIESGO_DESCRIPCION = "SP_OBTENER_MAT_RIE_DESC";
    public static final String SP_OBTENER_MATRIZ_RIESGO_PERIODO = "SP_OBTENER_MAT_RIE_PER";
    public static final String MENSAJE_MATRIZ_RIESGO = "La matriz riesgo";
    public static final String MENSAJE_COMENTARIO_MATRIZ_RIESGO = "El comentario de matriz riesgo";
    public static final String ID_MATRIZ_RIESGO = "idMatrizRiesgo";
    public static final String SP_OBTENER_SEVERIDAD = "SP_OBTENER_SEVERIDAD";

    //* MATRIZ RIESGO FRAUDE*/

    public static final String SP_LISTAR_BANDEJA_MATRIZ_FRAUDE = "SP_LISTAR_BNDJA_MATRIZ_FRD";
    public static final String SP_INSERT_MATRIZ_FRAUDE = "SP_INSERT_MATRIZ_FRAUDE";
    public static final String SP_OBTENER_MATRIZ_FRAUDE = "SP_OBTENER_MATRIZ_FRAUDE";
    public static final String SP_UPDATE_MATRIZ_FRAUDE = "SP_UPDATE_MATRIZ_FRAUDE";
    public static final String SP_ANULAR_MATRIZ_FRAUDE = "SP_ANULAR_MATRIZ_FRAUDE";
    public static final String SP_INSERT_DETALLE_MATRIZFRAUDE = "SP_INSERT_DETALLE_MATRIZF";
    public static final String SP_UPDATE_DETALLE_MATRIZFRAUDE = "SP_UPDATE_DETA_MATRIZF";
    public static final String SP_LISTAR_DETALLE_MATRIZFRAUDE = "SP_LISTAR_DETA_MATRIZFRAUDE";


    public static final String MENSAJE_MATRIZ_FRAUDE = "La matriz fraude";
    public static final String ID_MATRIZ_FRAUDE = "idMatrizFraude";

    public static final String ID_MATRIZ_CONTINUIDAD = "idMatrizRiesgo";
    //* Matriz Continuidad */
    public static final String SP_INSERT_DETALLE_MATRIZCONTINUIDAD = "SP_INSERT_DETALLE_MATRIZCONT";
    public static final String SP_LISTAR_DETALLE_MATRIZCONTINUIDAD = "SP_OBTENER_DETALLE_MATRIZCONT";
    public static final String SP_UPDATE_DETALLE_MATRIZCONTINUIDAD = "SP_UPDATE_DETA_MATRIZCONT";
    public static final String SP_LISTAR_BANDEJA_MATRIZ_CONTINUIDAD = "SP_LISTAR_BNDJA_MATRIZ_CONT";

    //* Matriz Oportunidad */
    public static final String SP_INSERT_DETALLE_MATRIZOPORTUNIDAD = "SP_INSERT_DETALLE_MATRIZOPORT";
    public static final String SP_UPDATE_DETALLE_MATRIZOPORAD = "SP_UPDATE_DETALLE_MATRIZOPORAD";
    public static final String SP_OBTENER_DETALLE_MATRIZOPOR = "SP_OBTENER_DETALLE_MATRIZOPOR";
    public static final String MENSAJE_MATRIZ_OPORTUNIDAD = "La matriz oportunidad";
    public static final String SP_LISTAR_BNDJA_MATRIZ_OPOR = "SP_LISTAR_BNDJA_MATRIZ_OPORT";
    public static final String SP_ANULAR_MATRIZ_OPOR = "SP_ANULAR_MATRIZ_OPORTUNIDAD";

    //* Matriz Anticorrupcion */
    public static final String SP_INSERT_DETALLE_MATRIZANTICORRUPCION = "SP_INSERT_DETALLE_MATRIZANTIC";
    public static final String SP_UPDATE_DETALLE_MATRIZANTIC = "SP_UPDATE_DETA_MATRIZANTIC";
    public static final String SP_OBTENER_DETALLE_MATRIZANTIC = "SP_OBTENER_DETALLE_MATRIZANTIC";
    public static final String MENSAJE_MATRIZ_ANTICORRUPCION = "La matriz anticorrupcion";
    public static final String SP_LISTAR_BNDJA_MATRIZ_ANTIC = "SP_LISTAR_BNDJA_MATRIZ_ANTIC";
    public static final String SP_ANULAR_MATRIZ_ANTIC = "SP_ANULAR_MATRIZ_ANTIC";
    public static final String SP_LISTAR_DETALLE_MATRIZANTIC = "SP_LISTAR_DETA_MATRIZANTIC";


    //* Matriz Evento */
    public static final String SP_LISTAR_BANDEJA_MATRIZ_EVENTO = "SP_LISTAR_BNDJA_MATRIZ_EVE";
    public static final String SP_INSERT_MATRIZ_EVENTO = "SP_INSERT_MATRIZ_RIESGO";
    public static final String SP_OBTENER_MATRIZ_EVENTO = "SP_OBTENER_MATRIZ_EVENTO";
    public static final String SP_UPDATE_MATRIZ_EVENTO = "SP_UPDATE_MATRIZ_EVENTO";
    public static final String SP_ANULAR_MATRIZ_EVENTO = "SP_ANULAR_MATRIZ_RIESGO";
    public static final String SP_INSERT_DETALLE_MATRIZEVENTO = "SP_INSERT_DETALLE_MATRIZEVENTO";
    public static final String SP_UPDATE_DETALLE_MATRIZEVENTO = "SP_UPDATE_DETA_MATRIZE";
    public static final String SP_LISTAR_DETALLE_MATRIZEVENTO = "SP_LISTAR_DETA_MATRIZEVENTO";

    //* Detalle Grafico */
    public static final String SP_REPORTE_MATRIZ_OPER_INHE = "SP_REPORTE_MATRIZ_OPER_INHE";
    public static final String SP_REPORTE_MATRIZ_OPER_GER = "SP_REPORTE_MATRIZ_OPER_GER";
    public static final String SP_REPORTE_MATRIZ_OPER_KRI = "SP_REPORTE_MATRIZ_OPER_KRI";
    public static final String SP_REPORTE_MATRIZ_EVENTO = "SP_REPORTE_MATRIZ_EVENTO";

    // Report Excel Columns
    public static String COLUMN_CREATED_DATE = "CREACION";
    public static String COLUMN_MODIFICATED_DATE = "MODIFICACION";
    public static String COLUMN_NAMES = "NOMBRES Y APELLIDOS";
    public static String COLUMN_DOCUMENT = "DNI";
    public static String COLUMN_COMPANY = "EMPRESA";
    public static String COLUMN_SCORE = "PUNTAJE";
    public static String SHEET_NAME = "Report";
    public static final String DATE_FORMAT_REPORT = "yyyy-MM-dd hh:mm:ss";

}

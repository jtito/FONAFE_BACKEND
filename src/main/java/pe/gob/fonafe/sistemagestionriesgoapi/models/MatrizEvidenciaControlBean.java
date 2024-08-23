package pe.gob.fonafe.sistemagestionriesgoapi.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@ToString
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MatrizEvidenciaControlBean {

    private Long idEvidencia;
    private Long idMatrizRiesgo;
    private Long idDetaMatrizRiesgo;
    private String nombreOriginalArchivo;
    private String nombreArchivo;
    private Long pesoArchivo;
    private String rutaArchivo;
    private Byte indicadorBaja;
    private String usuarioCreacion;
    private String ipCreacion;
    private BigDecimal codigoResultado;
    private String descripcionResultado;
    private String ipModificacion;
    private String usuarioModificacion;
    private Long tipoEvidencia;

}

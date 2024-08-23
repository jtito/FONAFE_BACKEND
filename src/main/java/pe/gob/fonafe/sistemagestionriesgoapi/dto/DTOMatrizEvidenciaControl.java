package pe.gob.fonafe.sistemagestionriesgoapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@ToString
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DTOMatrizEvidenciaControl {

    private Long idEvidencia;
    private Long idMatrizRiesgo;
    private Long idDetaMatrizRiesgo;
    private String nombreOriginalArchivo;
    private String nombreArchivo;
    private Long pesoArchivo;
    private String rutaArchivo;
    private Integer indicadorBaja;
    private BigDecimal codigoResultado;
    private String descripcionResultado;
    private String fechaCreacion;
    private Long tipoEvidencia;
}

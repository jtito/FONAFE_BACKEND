package pe.gob.fonafe.sistemagestionriesgoapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DTOFechaVencimiento implements Serializable {
    private Long idMatrizRiesgo;
    private Long idDetaMatrizRiesgo;
    private String fechaVencimiento;
    private String planAccion;
    private String destinatario;
    private String destinatarioJefeInmediato;

}

package pe.gob.fonafe.sistemagestionriesgoapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DTOComentarioAuditoria implements Serializable {
    private Long idDetaMatrizRiesgo;
    private Long idMatrizRiesgo;
    private String comentarioAuditoria;
    private String usuarioModificacion;
    private String ipModificacion;

    private BigDecimal codigoResultado;
    private String descripcionResultado;
}

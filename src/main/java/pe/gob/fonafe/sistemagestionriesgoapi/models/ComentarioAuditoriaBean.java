package pe.gob.fonafe.sistemagestionriesgoapi.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ComentarioAuditoriaBean implements Serializable {
    private Long idMatrizRiesgo;
    private Long idDetaMatrizRiesgo;
    private String comentarioAuditoria;
    private String usuarioModificacion;
    private String ipModificacion;
}

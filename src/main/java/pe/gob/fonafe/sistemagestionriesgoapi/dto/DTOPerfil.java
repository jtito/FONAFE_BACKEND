package pe.gob.fonafe.sistemagestionriesgoapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@ToString
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DTOPerfil implements Serializable {

    private Long idPerfil;
    private String descripcionPerfil;
}

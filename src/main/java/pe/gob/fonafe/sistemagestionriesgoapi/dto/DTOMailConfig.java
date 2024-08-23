package pe.gob.fonafe.sistemagestionriesgoapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DTOMailConfig implements Serializable {

    private Long idDetaParametro;
    private String deParametro;
    private String deValor1;
}

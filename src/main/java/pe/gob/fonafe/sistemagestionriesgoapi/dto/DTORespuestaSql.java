package pe.gob.fonafe.sistemagestionriesgoapi.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DTORespuestaSql {
    private Date fechaEncuesta;
    private Date fechaModificacion;
    private String nomApellido;
    private String numDoc;
    private String nomCorto;
    private BigDecimal idRespEncuesta;
}

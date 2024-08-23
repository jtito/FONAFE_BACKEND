package pe.gob.fonafe.sistemagestionriesgoapi.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;


@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DTODetalleRespuesta {
    private BigDecimal idRespEncuesta;
    private BigDecimal idEncuesta;
    private BigDecimal idPregunta;
    private String dePregunta;
    private BigDecimal tipPregunta;
    private BigDecimal idDetResp;
    private String deResp;
    private String respuesta;
    private BigDecimal inAlternativa;
    private BigDecimal score;
    private BigDecimal tiPregunta;

}

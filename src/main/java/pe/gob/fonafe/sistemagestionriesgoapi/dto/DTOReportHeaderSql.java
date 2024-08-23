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
public class DTOReportHeaderSql {
    private BigDecimal idQuestion;
    private String question;
}

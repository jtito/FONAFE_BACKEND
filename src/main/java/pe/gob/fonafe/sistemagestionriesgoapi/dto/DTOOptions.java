package pe.gob.fonafe.sistemagestionriesgoapi.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DTOOptions {
    public BigDecimal idSurvey;
    public BigDecimal idQuestion;
    public BigDecimal idOption;
    public BigDecimal score;
    public String question;
    public String description;
}

package pe.gob.fonafe.sistemagestionriesgoapi.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DTOQuestions {
    public BigDecimal idSurvey;
    public BigDecimal idQuestion;
    public BigDecimal idTypeQuestion;
    public String question;
    public List<DTOOptions> options;
    public String concatOptions;
}

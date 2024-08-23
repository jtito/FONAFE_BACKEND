package pe.gob.fonafe.sistemagestionriesgoapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OptionsDetailAux {

    private Integer score;
    private String optionsJoining;

}

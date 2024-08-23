package pe.gob.fonafe.sistemagestionriesgoapi.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DTOFileResponse {
    private String idFile;
    private String fileName;
    private String originalFileName;
}

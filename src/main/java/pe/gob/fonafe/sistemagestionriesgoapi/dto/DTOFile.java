package pe.gob.fonafe.sistemagestionriesgoapi.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DTOFile implements Serializable {

    private String status;
    private Long idUser;
    private String name;
    private String routePath;
    private String size;
    private String type;

}


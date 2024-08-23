package pe.gob.fonafe.sistemagestionriesgoapi.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class DTOSubmenu implements Serializable {

    private Integer idSubmenu;
    private String nombreSubmenu;
    private String rutaSubmenu;
    private String iconoSubmenu;
    private String estado;
}

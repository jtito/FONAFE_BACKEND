package pe.gob.fonafe.sistemagestionriesgoapi.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class DTOMenu implements Serializable {

    private Integer idMenu;
    private String nombreMenu;
    private String rutaMenu;
    private String iconoMenu;
    private String estado;
    private List<DTOSubmenu> listaSubmenu;

}

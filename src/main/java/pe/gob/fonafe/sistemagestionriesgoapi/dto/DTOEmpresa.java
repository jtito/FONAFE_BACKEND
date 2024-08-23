package pe.gob.fonafe.sistemagestionriesgoapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pe.gob.fonafe.sistemagestionriesgoapi.models.SedeBean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@ToString
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DTOEmpresa implements Serializable {

    private Long idEmpresa;
    private Long idCartera;
    private String descripcionCartera;
    private String razonSocial;
    private String ruc;
    private String nombreCortoEmpresa;
    private String direccion;
    private Byte indicadorSede;
    private Byte indicadorBaja;
    private String usuarioCreacion;
    private String fechaCreacion;
    private String ipCreacion;
    private String usuarioModificacion;
    private String fechaModificacion;
    private String ipModificacion;
    private List<SedeBean> listaSedes;
    private BigDecimal codigoResultado;
    private String descripcionResultado;
}

package pe.gob.fonafe.sistemagestionriesgoapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pe.gob.fonafe.sistemagestionriesgoapi.models.GerenciaBean;
import pe.gob.fonafe.sistemagestionriesgoapi.models.SedeBean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@ToString
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DTOSede implements Serializable {

    private Long idSede;
    private String descripcionSede;
    private Long idEmpresa;
    private Byte indicadorBaja;
    private String usuarioCreacion;
    private String fechaCreacion;
    private String ipCreacion;
    private String usuarioModificacion;
    private String fechaModificacion;
    private String ipModificacion;
    private List<SedeBean> listaSedes;
    private List<GerenciaBean> listaGerencias;
    private BigDecimal codigoResultado;
    private String descripcionResultado;
}

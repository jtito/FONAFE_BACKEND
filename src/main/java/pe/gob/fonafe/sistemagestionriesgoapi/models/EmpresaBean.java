package pe.gob.fonafe.sistemagestionriesgoapi.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@ToString
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmpresaBean implements Serializable {

    private Long idEmpresa;
    private Long idCartera;
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
    private BigDecimal codigoResultado;
    private String descripcionResultado;
}

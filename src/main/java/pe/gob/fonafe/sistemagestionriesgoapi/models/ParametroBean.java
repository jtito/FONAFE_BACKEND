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
public class ParametroBean implements Serializable {

    private Long idParametro;
    private String deParametro;
    private String coParametro;
    private Byte indicadorBaja;
    private String usuarioCreacion;
    private String ipCreacion;
    private BigDecimal codigoResultado;
    private String descripcionResultado;
    private String ipModificacion;
    private String usuarioModificacion;
}

package pe.gob.fonafe.sistemagestionriesgoapi.dto;

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
public class DTOParametro implements Serializable {

    private Long idParametro;
    private String nombreParametro;
    private String deValor1;
    private String deValor2;
    private String deValor3;
    private String deValor4;
    private Integer indicadorBaja;
    private BigDecimal codigoResultado;
    private String descripcionResultado;
    private String deParametro;
    private String coParametro;
    private String fechaCreacion;

}

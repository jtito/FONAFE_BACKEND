package pe.gob.fonafe.sistemagestionriesgoapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@ToString
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DTODetalleParametro implements Serializable {

    private Long idDetaParametro;
    private Long idParametro;
    private String deParametro;
    private String coParametro;
    private String deValor1;
    private String deValor2;
    private String deValor3;
    private String deValor4;
    private String feValor1;
    private String feValor2;
    private String feValor3;
    private String feValor4;
    private Integer indicadorBaja;
    private String fechaCreacion;
    private BigDecimal codigoResultado;
    private String descripcionResultado;
}

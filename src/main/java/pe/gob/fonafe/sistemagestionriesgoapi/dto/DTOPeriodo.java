package pe.gob.fonafe.sistemagestionriesgoapi.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DTOPeriodo {

	private String nombreCorto;
    private Long idEmpresa;
    private Long idPeriodo;
    private String dePeriodo;
    private String deAnioPeriodo;
    private String feIni;
    private String feFin;
    private Long idFrecuencia;
    private int anio;
    private Byte inBaja;
    private String idUsuaCrea;
    private String deUsuaCreaIp;
    private String feUsuaCrea;
    private String idUsuaModi;
    private String deUsuaModiIp;
    private String feUsuaModi;
    private BigDecimal codigoResultado;
    private String descripcionResultado;
}

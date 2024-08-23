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
public class DTODimension {

	String nombreCorto;
	Long idEmpresa;
	Long idDimRiesgo;
    String nuDimRiesgo;
    BigDecimal nuProbabilidad;
    int inBaja;
    String idUsuaCrea;
    String deUsuaCreaIp;
    String feUsuaCrea;
    String idUsuaModi;
    String deUsuaModiIp;
    String feUsuaModi;
    double nuImpacto;
    String deDimension;
    int idTipoControlRiesgo; //tipo de evaluacion
    String deParametro;
    private BigDecimal codigoResultado;
    private String descripcionResultado;
	
}

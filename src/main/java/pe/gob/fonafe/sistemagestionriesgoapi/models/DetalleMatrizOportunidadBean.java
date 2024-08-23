package pe.gob.fonafe.sistemagestionriesgoapi.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import oracle.jdbc.OracleTypes;

import java.io.Serializable;
import java.util.Date;

import org.springframework.jdbc.core.SqlOutParameter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DetalleMatrizOportunidadBean implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long idDetalleMOportunidad;
    private Long idMatrizRiesgo;
    private Long idPeriodo;
    private Long idMatrizNivel;
    private Long idGerencia;
    private Long idEmpresa;
    private Long idSedeEmpresa;
    private String deOrigen;
    private String deGrupoInteres;
    private String deObjetivo;
    private Long idProceso;
    private Long IdSubproceso;
    private String codOpor;
    private String deOportunidad;
    private Float nivelComplejidad;
    private Float nivelCosto;
    private Float nuViabilidad;
    private Long idTipoBeneficio;
    private Float nuBeneficio;
    private Float nuNivelPriori;
    private String deNivelPriori;
    private Long idEstrategiaPlan;
    private String codSam;
    private String codPlanAccion;
    private String dePlanAccion;
    private String idRespPlanAccion;
    private Integer recursoFina;
    private Integer recursoOper;
    private Integer recursoTecno;
    private Integer recursoHuma;
    private Integer reqNego;
    private String feIniPlanAccion;
    private String feFinPlanAccion;
    private String deEntregable;
    private Long idEstadoPlanAccion;
    private String fePrevista;
    private Long inEficaz;
    private String feVerificacion;
    private String idVerificador;
    private String deEvidencia;
    private String deComentario;
    private String usuarioCreacion;
    private String ipCreacion;
    private String fechaCreacion;
    private Byte indicadorBaja;
    private String idUsuaCrea; 
    private String deUsuaCreaIp;
    private String feUsuaCrea;
    private String idUsuaModi;
    private String deUsuaModiIp;
    private String feUsuaModi;
    private Integer inBaja;
}

package pe.gob.fonafe.sistemagestionriesgoapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DTODetalleMatrizRiesgo implements Serializable {

    private Long idDetaMatrizRiesgo;
    private Long idMatrizRiesgo;
    private String codMatriz;
    private Long idCartera;
    private String deCartera;
    private Long idEmpresa;
    private Long idSede;
    private Long idGerencia;
    private Long idPeriodo;
    private String dePeriodo;
    private Long idMatrizNivel;
    private String deMatrizNivel;
    private Long idProceso;
    private Long idSubProceso;
    private String deTitulo;
    private String deRiesgo;
    private String deProcesoImpactado;
    private String deFoda;
    private String deGrupoInteres;
    private Integer idOrigenRiesgo;
    private Integer idFrecuenciaRiesgo;
    private Integer idTipoRiesgo;
    private Float nuProbabilidadInherente;
    private Float nuImpactoInherente;
    private Float nuPuntajeInherente;
    private Integer idControl;
    private String deControl;
    private String deSeveridadInherente;
    private String idAreaControl;
    private String idResponsableControl;
    private Integer idFrecuenciaControl;
    private Integer idOportunidadControl;
    private Integer idAutomatizacionControl;
    private String deEvidenciaControl;
    private Float nuProbabilidadResidual;
    private Float nuImpactoResidual;
    private Float nuPuntajeResidual;
    private String deSeveridadResidual;
    private String usuarioCreacion;
    private String ipCreacion;
    private String fechaCreacion;
    private String usuarioModificacion;
    private String ipModificacion;
    private String fechaModificacion;
    private String codRiesgo; //090821
    private String codControl; //090821
    private Byte indicadorBaja;
    private BigDecimal codigoResultado;
    private String descripcionResultado;
    //1108 Plan Accion
    private Integer idEstrategiaResp;
    private String codPlanAccion;
    private String desPlanAccion;
    private String idAreaPlanAccion;
    private String idResponsablePlanAccion;
    private String fechaInicioPlanAccion;
    private String estadoPlanAccion;
    private String fechaFinPlanAccion;
    
     //1108 Verificacion eficacia
    private String fechaPrevista;
    private String fueEficaz;
    private String fechaVerificacion;
    private String verificadoPor;
    private String evidenciaEficacia;
    private String observaciones;

    private String codkri;
    private String defKri;
    private String frecuencia;
    private String metkri;
    private String kriActual;
    private String kriResponsable;

    private Integer cantidadArchivosControl;
    private Integer cantidadArchivosPlan;
    private List<DTOProceso> listaProcesos;
    private List<DTOSubProceso> listaSubProcesos;

}

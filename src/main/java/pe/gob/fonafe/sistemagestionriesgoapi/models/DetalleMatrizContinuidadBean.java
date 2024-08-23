package pe.gob.fonafe.sistemagestionriesgoapi.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DetalleMatrizContinuidadBean implements Serializable {

    private Long idDetaMatrizContinuidad;
    private Long idMatrizRiesgo;
    private Long idPeriodo;
    private String codMatriz;
    private Long idMatrizNivel;
    private Long idGerencia;
    private Long idEmpresa;
    private Long idSede;
    private Long idProceso;
    private Long idSubProceso;
    private String codRiesgo;
    private String deRiesgo;
    private Integer nuProbabilidadInherente;
    private Integer nuImpactoInherente;
    private Integer nuPuntajeInherente;
    private String deSeveridadInherente;
    private String codControl;
    private String deControl;
    private Integer idEfecControl;
    private String adicControl;
    private String infraControl;
    private String reHuControl;
    private String reTiControl;
    private String regVitalControl;
    private String proveControl;
    private String otrosControl;
    private Float nuProbabilidadResidual;
    private Float nuImpactoResidual;
    private Float nuPuntajeResidual;
    private String deSeveridadResidual;
    private Integer idEstrategiaPlan;
    private String codPlanAccion;
    private String dePlanAccion;
    private String idAreaPlanAccion;
    private String idResPlanAccion;
    private String feIniPlanAccion;
    private Integer idEstadoPlanAccion;
    private String feFinPlanAccion;
    private String comePlanAccion;
    private String usuarioCreacion;
    private String ipCreacion;
    private String fechaCreacion;
    private String usuarioModificacion;
    private String ipModificacion;
    private String fechaModificacion;
    private Byte indicadorBaja;
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.fonafe.sistemagestionriesgoapi.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DetalleMatrizFraudeBean implements Serializable {
    
    private Integer idDetaMatrizFraude;
    private Integer idMatrizRiesgo;
    private String codMatriz;
    private String codRiesgo;
    private String codControl;
    private Integer idCartera;
    private String deCartera;
    private Integer idEmpresa;
    private Integer idSede;
    private Integer idGerencia;
    private Integer idPeriodo;
    private String dePeriodo;
    private Integer idMatrizNivel;
    private String deMatrizNivel;
    private Integer idProceso;
    private Integer idSubProceso;
    private String deRiesgo;
    private String riesgoAsociado;
    private Integer idOrigenRiesgo;
    private String agenteFraude;
    private Integer idFrecuenciaRiesgo;
    private Integer idTipoRiesgo;
    private float nuProbabilidadInherente;
    private float nuImpactoInherenteE;
    private float nuImpactoInherenteL;
    private float nuImpactoInherenteR;
    private float nuImpactoInherenteC;
    private float nuImpactoInherenteG;
    private float nuPuntajeInherente;
    private String deControl;
    private String deSeveridadInherente;
    private String idAreaControl;
    private String idResponsableControl;
    private Integer limitaOportunidaControl;
    private Integer motivaActoControl;
    private Integer actitudPotencialControl;
    private Integer idFrecuenciaControl;
    private Integer idOportunidadControl;
    private Integer idAutomatizacionControl;
    private String deEvidenciaControl;
    private float nuProbabilidadResidual;
    private float nuImpactoResidualE;
    private float nuImpactoResidualL;
    private float nuImpactoResidualR;
    private float nuImpactoResidualC;
    private float nuImpactoResidualG;
    private float nuPuntajeResidual;
    private String deSeveridadResidual;
    private String usuarioCreacion;
    private String ipCreacion;
    private String fechaCreacion;
    private String usuarioModificacion;
    private String ipModificacion;
    private String fechaModificacion;
    private Byte indicadorBaja;
    //1108 Plan Accion
    private Integer idEstrategiaResp;
    private String codPlanAccion;
    private String desPlanAccion;
    private String idResponsablePlanAccion;
    private String fechaInicioPlanAccion;
    private Integer estadoPlanAccion;
    private String fechaFinPlanAccion;
    
     //1108 Verificacion eficacia
    private String fechaPrevista;
    private Integer fueEficaz;
    private String fechaVerificacion;
    private String verificadoPor;
    private String evidenciaEficacia;
    private String observaciones;
    private String codkri;
    private String defkri;
    private String frecuencia;
    private String metkri;
    private String kriActual;
    private String kriResponsable;
    
    
}

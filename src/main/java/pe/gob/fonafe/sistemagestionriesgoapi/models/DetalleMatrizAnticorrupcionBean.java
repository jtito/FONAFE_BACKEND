/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.fonafe.sistemagestionriesgoapi.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author CANVIA
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DetalleMatrizAnticorrupcionBean {

    private Integer idDetaMatrizAntic;
    private Integer idMatrizRiesgo;
    private String codMatriz;
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
    private String decargoRiesgo;
    private String deRiesgo;
    private String deSociodelRiesgo;
    private String deAccion;
    private Integer idTipoRiesgo;
    private Integer nuImpaInhe;
    private Integer nuPuntaInhe;
    private String dePuntaInhe;
    private String deControl;
    private Integer nuFrecuInhe;
    private Integer nuFrecuRes;
    private Integer nuPuntaRes;
    private Integer nuImpaRes;
    private String deSeveridadRes;
    private String codRiesgo;
    private String codControl;
    private Integer idRespEstrategia;
    private String codPlanAccion;
    private String dePlanAccion;
    private Integer idEficaciaControl;
    private String idRespPlanAccion;
    private Integer idEstadoPlanAccion;
    private String feFinPlanAccion;
    private Integer inFicaz;
    private String feVerificacion;
    private Integer idTipoDelito;							
    private Integer idDebidaDilig;
    private Integer indFinPlanAccion;
    private String usuarioCreacion;
    private String ipCreacion;
    private String fechaCreacion;
    private String usuarioModificacion;
    private String ipModificacion;
    private String fechaModificacion;
    private Byte indicadorBaja;
}

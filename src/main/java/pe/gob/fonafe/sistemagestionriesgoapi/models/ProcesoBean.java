/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.fonafe.sistemagestionriesgoapi.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@ToString
@Getter
@Setter
public class ProcesoBean implements Serializable {
    
    private Long idProceso;
    private String deProceso;
    private String usuarioCreacion;
    private String ipCreacion;
    private String fechaCreacion;
    private String usuarioModificacion;
    private String ipModificacion;
    private String fechaModificacion;
    private Long idProcesoMatriz;
    private Long idEmpresa;
    private String deEmpresa;
    private Long inMatrizOperacional;
    private Long inMatrizFraude;
    private Long inMatrizContinuidad;
    private Long inMatrizAnticorrupcion;
    private Long inMatrizOportunidad;
    private Long inMatrizEvento;
    private Byte indicadorBaja;
    private String CodigoMensaje;
    private String DescripcionMensaje;
    private List<SubProcesoBean> lstProc;
    private List<SubProcesoBean> lstProcAct;
    private List<SubProcesoBean> lstProcAdd;
    private List<SubProcesoBean> lstProcDel;

}

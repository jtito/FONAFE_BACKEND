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

@ToString
@Getter
@Setter
public class SubProcesoBean implements Serializable {
    
    private Long idSubProceso;
    private Long idProceso;
    private String deSubProceso;
    private String usuarioCreacion;
    private String ipCreacion;
    private String fechaCreacion;
    private String usuarioModificacion;
    private String ipModificacion;
    private String fechaModificacion;
    private Byte indicadorBaja;
    private String CodigoMensaje;
    private String DescripcionMensaje;

}
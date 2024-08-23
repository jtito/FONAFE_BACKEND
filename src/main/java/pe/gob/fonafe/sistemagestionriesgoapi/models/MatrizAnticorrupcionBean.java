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
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MatrizAnticorrupcionBean {
    
    private Integer idMatrizRiesgo;
    private Integer idCartera;
    private String deCartera;
    private Integer idEmpresa;
    private Integer idSede;
    private Integer idGerencia;
    private Integer idPeriodo;
    private String dePeriodo;
    private Integer idTipoMatriz;
    private String deTipoMatriz;
    private Integer idMatrizNivel;
    private String deMatrizNivel;
    private Integer matrizNivel;
    private List<DetalleMatrizAnticorrupcionBean> listaDetalleMatrizAnticorrupcion;
    private String usuarioCreacion;
    private String ipCreacion;
    private String fechaCreacion;
    private String usuarioModificacion;
    private String ipModificacion;
    private String fechaModificacion;
    private Byte indicadorBaja;
    
}

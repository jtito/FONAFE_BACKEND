/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.fonafe.sistemagestionriesgoapi.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DTOMatrizNivel implements Serializable{
    
    private Long idMatrizNivel;
    private String idGerencia;
    private String deGerencia;
    private String usuarioCreacion;
    private String ipCreacion;
    private String fechaCreacion;
    private String usuarioModificacion;
    private String ipModificacion;
    private String fechaModificacion;
    private Byte indicadorBaja;
    private String idEmpresa;
    private String idSedeEmpresa;
    private String deMatrizNivel;
    private Long idTipoMatriz;
    private Long matrizNivel;
    private String deTipoMatriz;
    private String descripcionMatrizNivel;
    private String CodigoMensaje;
    private String procesos;
    private String DescripcionMensaje;
    
    
}

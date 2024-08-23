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
import pe.gob.fonafe.sistemagestionriesgoapi.models.SubProcesoBean;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DTOSubProceso implements Serializable {
    
    private Long idSubProceso;
    private Long idProceso;
    private String deSubProceso;
    private String usuarioCreacion;
    private String ipCreacion;
    private String fechaCreacion;
    private String usuarioModificacion;
    private String ipModificacion;
    private String fechaModificacion;
    private String idEmpresa;
    private String deEmpresa;
    private Byte indicadorBaja;
    private String CodigoMensaje;
    private String DescripcionMensaje;
    private List<SubProcesoBean> lstProcAdd;
    private List<SubProcesoBean> lstProcDel;
    
    

}

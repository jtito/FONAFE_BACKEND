/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.fonafe.sistemagestionriesgoapi.dto;

/**
 *
 * @author joell
 */
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import com.fasterxml.jackson.annotation.JsonInclude;
import pe.gob.fonafe.sistemagestionriesgoapi.models.PreguntaBean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DTOEncuesta implements Serializable{
    
    private Long idEncuesta;
    private String tituloEncuesta;
    private String feEncuesta;
    private String feFinEncuesta;
    private String subtituloEncuesta;
    private String idEmpresa;
    private String descripcionEmpresa;
    private Long idPeriodo;
    private String dePeriodo;
    private String textoEncuesta;
    private String contTextoEncuesta;
    private List<PreguntaBean> listaPreguntas;
    private Integer cantidadPreguntas;
    private String usuarioCreacion;
    private String fechaCreacion;
    private String ipCreacion;
    private String usuarioModificacion;
    private String ipModificacion;
    private String fechaModificacion;
    private Byte indicadorBaja;
    private BigDecimal codigoResultado;
    private String descripcionResultado;
    
}

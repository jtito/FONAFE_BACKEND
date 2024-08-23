/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.fonafe.sistemagestionriesgoapi.models;

/**
 *
 * @author joell
 */
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
public class EncuestaBean implements Serializable{
    
    private Long idEncuesta;
    private String tituloEncuesta;
    private String feEncuesta;
    private String feFinEncuesta;
    private String subtituloEncuesta;
    private String idEmpresa;
    private Long idPeriodo;
    private String textoEncuesta;
    private String contTextoEncuesta;
    private String usuarioCreacion;
    private String fechaCreacion;
    private String ipCreacion;
    private String ipModificacion;
    private String usuarioModificacion;
    private String fechaModificacion;
    private Byte indicadorBaja;
    
}


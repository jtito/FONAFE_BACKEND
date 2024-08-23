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
import pe.gob.fonafe.sistemagestionriesgoapi.models.GerenciaBean;
import pe.gob.fonafe.sistemagestionriesgoapi.models.PreguntaBean;
import pe.gob.fonafe.sistemagestionriesgoapi.models.RespuestaBean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DTOPregunta implements Serializable{
    
    private Long idPregunta;
    private String dePregunta;
    private String tiPregunta;
    private String grupoPregunta;
    private String puntaje;
    private String idEncuesta;
    private String idEmpresa;
    private List<RespuestaBean> listaRespuestas;
    private List<PreguntaBean> listaPreguntas;
    private String usuarioCreacion;
    private String fechaCreacion;
    private String ipCreacion;
    private String usuarioModificacion;
    private String ipModificacion;
    private String fechaModificacion;
    private Byte indicadorBaja;
    private BigDecimal codigoResultado;
    private String descripcionResultado;
    private String notaPregunta;
    
}

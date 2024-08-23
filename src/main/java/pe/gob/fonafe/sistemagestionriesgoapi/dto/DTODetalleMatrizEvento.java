/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.fonafe.sistemagestionriesgoapi.dto;

/**
 *
 * @author CANVIA
 */
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DTODetalleMatrizEvento {
    
    private Integer idDetaMatrizEvento;
    private Integer idMatrizRiesgo;
    private Integer idCartera;
    private String deCartera;
    private Integer idEmpresa;
    private Integer idSede;
    private Integer idGerencia;
    private Integer idPeriodo;
    private String dePeriodo;
    private Integer idMatrizNivel;
    private String deMatrizNivel;
    private String trimestre;
    private String tievento1;
    private String tievento2;
    private Float perdidaBruta;
    private Float interes;
    private Integer inPlanAccion;
    private Byte indicadorBaja;
    private Float importePerdida;
    private String usuarioCreacion;
    private String ipCreacion;
    private String usuarioModificacion;
    private String ipModificacion;
    private Integer idEstadoEvento;
    private String feOcurrencia;
    private String feMaterial;
    private Float essalud;
    private String dePlanAccion;
    private String deEvento;
    private String cuentaContable;
    private String comentario;
    private String codEvento;
    private String causaEvento;
    private String areaEvento;
    private Integer anio;
    private BigDecimal codigoResultado;
    private String descripcionResultado;
    		
}

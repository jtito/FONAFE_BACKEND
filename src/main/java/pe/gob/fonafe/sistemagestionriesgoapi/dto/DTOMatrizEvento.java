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
import lombok.Getter;
import lombok.Setter;
import pe.gob.fonafe.sistemagestionriesgoapi.models.DetalleMatrizEventoBean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DTOMatrizEvento implements Serializable {

    private Integer idMatrizRiesgo;
    private Integer idCartera;
    private String deCartera;
    private Integer idEmpresa;
    private String nombreCortoEmpresa;
    private Integer idSede;
    private Integer idGerencia;
    private Integer idPeriodo;
    private String dePeriodo;
    private String deAnioPeriodo;
    private Integer idTipoMatriz;
    private String deTipoMatriz;
    private Integer idMatrizNivel;
    private Integer matrizNivel;
    private String deMatrizNivel;
    private String fechaCreacion;
    private Byte indicadorBaja;
    private BigDecimal codigoResultado;
    private String descripcionResultado;
    

    private List<DetalleMatrizEventoBean> listaDetalleMatriz;
}

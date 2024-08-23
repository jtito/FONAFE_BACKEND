package pe.gob.fonafe.sistemagestionriesgoapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import pe.gob.fonafe.sistemagestionriesgoapi.models.DetalleMatrizContinuidadBean;
import pe.gob.fonafe.sistemagestionriesgoapi.models.DetalleMatrizRiesgoBean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DTOMatrizContinuidad implements Serializable {

    private Long idMatrizRiesgo;
    private Long idCartera;
    private String deCartera;
    private Long idEmpresa;
    private String nombreCortoEmpresa;
    private Long idSede;
    private Long idGerencia;
    private Long idPeriodo;
    private String dePeriodo;
    private String deAnioPeriodo;
    private Long idTipoMatriz;
    private String deTipoMatriz;
    private Long idMatrizNivel;
    private Integer matrizNivel;
    private String deMatrizNivel;
    private String fechaCreacion;
    private Byte indicadorBaja;
    private BigDecimal codigoResultado;
    private String descripcionResultado;
    private List<DetalleMatrizContinuidadBean> listaDetalleMatrizContinuidad;
}

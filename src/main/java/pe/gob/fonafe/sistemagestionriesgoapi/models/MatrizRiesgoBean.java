package pe.gob.fonafe.sistemagestionriesgoapi.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MatrizRiesgoBean implements Serializable {

    private Long idMatrizRiesgo;
    private Long idCartera;
    private String deCartera;
    private Long idEmpresa;
    private Long idSede;
    private Long idGerencia;
    private Long idPeriodo;
    private String dePeriodo;
    private Long idTipoMatriz;
    private String deTipoMatriz;
    private Long idMatrizNivel;
    private String deMatrizNivel;
    private Integer matrizNivel;
    private List<DetalleMatrizRiesgoBean> listaDetalleMatriz;
    private String usuarioCreacion;
    private String ipCreacion;
    private String fechaCreacion;
    private String usuarioModificacion;
    private String ipModificacion;
    private String fechaModificacion;
    private Byte indicadorBaja;
}

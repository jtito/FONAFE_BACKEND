package pe.gob.fonafe.sistemagestionriesgoapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pe.gob.fonafe.sistemagestionriesgoapi.models.CrearMatrizBean;

import java.io.Serializable;

@ToString
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DTOCrearMatriz implements Serializable {

    private CrearMatrizBean.Area_Gerencia areaGerencia;
    private CrearMatrizBean.Origen origen;
    private CrearMatrizBean.Frecuencia frecuencia;
    private CrearMatrizBean.Tipo tipo;
    private CrearMatrizBean.SubTipo subTipo;

    private String tipoMatriz;
    private String codigoRiesgo;
    private String causa;
    private String consecuencia;
    private String descripcionRiesgo;
    private String agente;
}

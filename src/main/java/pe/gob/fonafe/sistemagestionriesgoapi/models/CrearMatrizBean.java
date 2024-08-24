package pe.gob.fonafe.sistemagestionriesgoapi.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import pe.gob.fonafe.sistemagestionriesgoapi.models.enums.*;

import java.io.Serializable;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CrearMatrizBean implements Serializable {

    public Area_Gerencia areaGerencia;
    public Origen origen;
    public Frecuencia frecuencia;
    public Tipo tipo;
    public SubTipo subTipo;

    private String tipoMatriz;
    private String codigoRiesgo;
    private String causa;
    private String consecuencia;
    private String descripcionRiesgo;
    private String agente;
}

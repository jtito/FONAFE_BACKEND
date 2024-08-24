package pe.gob.fonafe.sistemagestionriesgoapi.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CrearMatrizBean implements Serializable {
    public enum Area_Gerencia{
        GERENCIA_GENERAL,
        AREA
    }

    public enum Origen {
        ORIGEN_1, ORIGEN_2, ORIGEN_3
    }

    public enum Frecuencia {
        FRECUENCIA_1, FRECUENCIA_2, FRECUENCIA_3
    }

    public enum Tipo {
        TIPO_1, TIPO_2, TIPO_3
    }

    public enum SubTipo {
        SUB_TIPO_1, SUB_TIPO_2, SUB_TIPO_3
    }

    private String tipoMatriz;
    private Area_Gerencia areaGerencia;
    private String codigoRiesgo;
    private String causa;
    private String consecuencia;
    private String descripcionRiesgo;
    private Origen origen;
    private String agente;
    private Frecuencia frecuencia;
    private Tipo tipo;
    private SubTipo subTipo;

}

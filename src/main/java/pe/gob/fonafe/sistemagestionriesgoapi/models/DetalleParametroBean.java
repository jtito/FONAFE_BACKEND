package pe.gob.fonafe.sistemagestionriesgoapi.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@ToString
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DetalleParametroBean implements Serializable {

    private Long idDetaParametro;
    private Long idParametro;
    private String deParametro;
    private String coParametro;
    private Byte indicadorBaja;
    private String deValor1;
    private String deValor2;
    private String deValor3;
    private String deValor4;
    private String feValor1;
    private String feValor2;
    private String feValor3;
    private String feValor4;
    private String usuarioCreacion;
    private String ipCreacion;
    private String ipModificacion;
    private String usuarioModificacion;
}

package pe.gob.fonafe.sistemagestionriesgoapi.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RespuestaBean implements Serializable{

    private Long idRespuesta;
    private String deRespuesta;
    private Long idPregunta;
    private Byte indicadorAlternativa;
    private Byte indicadorBaja;
    private String usuarioCreacion;
    private String fechaCreacion;
    private String ipCreacion;
    private String usuarioModificacion;
    private String fechaModificacion;
    private String ipModificacion;
    private BigDecimal codigoResultado;
    private String descripcionResultado;
}

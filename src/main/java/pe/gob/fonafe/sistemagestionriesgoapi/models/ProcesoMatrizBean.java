package pe.gob.fonafe.sistemagestionriesgoapi.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@ToString
@Getter
@Setter
public class ProcesoMatrizBean implements Serializable {

    private Long idProcesoMatriz;
    private Long idEmpresa;
    private String deEmpresa;
    private String usuarioCreacion;
    private String ipCreacion;
    private String fechaCreacion;
    private String usuarioModificacion;
    private String ipModificacion;
    private String fechaModificacion;
    private Byte indicadorBaja;
    private String CodigoMensaje;
    private String DescripcionMensaje;
}

package pe.gob.fonafe.sistemagestionriesgoapi.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOEmpresa;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOMenu;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOSede;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@ToString
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UsuarioBean implements Serializable {

    private Long idUsuario;
    private String username;
    private String password;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String dni;
    private String correo;
    private String prefijoTelefono;
    private String numeroTelefono;
    private String anexo;
    private String fileName;
    private String celular;
    private Long idCartera;
    private Long idEmpresa;
    private Long idSede;
    private String idCargo;
    private Long idResponsabilidad;
    private String correoJefe;
    private Long idPerfil;
    private Byte indicadorBaja;
    private String estado;
    private String usuarioCreacion;
    private String fechaCreacion;
    private String ipCreacion;
    private String usuarioModificacion;
    private String fechaModificacion;
    private String ipModificacion;
    private BigDecimal codigoResultado;
    private String descripcionResultado;
    private List<DTOMenu> listaMenu;
    private DTOEmpresa datosEmpresa;
    private DTOSede datosSede;
    private List<Long> procesos;
    private String indResolucion;
    private String dePerfil;

    public UsuarioBean(Long idUsuario, String fileName) {
        this.idUsuario = idUsuario;
        this.fileName = fileName;
    }

    public UsuarioBean() {
    }
}

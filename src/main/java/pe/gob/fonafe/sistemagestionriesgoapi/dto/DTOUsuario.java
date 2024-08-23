package pe.gob.fonafe.sistemagestionriesgoapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DTOUsuario implements Serializable {

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
    private String celular;
    private Long idCartera;
    private String descripcionCartera;
    private Long idEmpresa;
    private Long idSede;
    private String idCargo;
    private String descripcionCargo;
    private String fileName;
    private Long idResponsabilidad;
    private String correoJefe;
    private Long idPerfil;
    private Byte indicadorBaja;
    private String usuarioCreacion;
    private String fechaCreacion;
    private String ipCreacion;
    private String usuarioModificacion;
    private String fechaModificacion;
    private String ipModificacion;
    private String responsable;
    private String descripcionEmpresa;
    private String descripcionSede;
    private String descripcionResponsabilidad;
    private BigDecimal codigoResultado;
    private String descripcionResultado;
    private List<DTORoles> roles;
    private String estado;
    private DTOFile dtoFile;
    private List<Long> procesos;
    private String indResolucion;
    private String dePerfil;
    private String actPassword;
    private String newPassword;

}

package pe.gob.fonafe.sistemagestionriesgoapi.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class DTOGenerico<T> {

    private Long idGenerico;
    private BigDecimal codigoResultado;
    private String descripcionResultado;
    private String descripcionSeveridad;
    private String descripcionPeriodo;
    private List<T> listado;
    private List<T> listaCorreoEntidad;
    private List<T> listaMailConfig;

    private List<T> listadoEvidenciaControl;
    private List<T> listadoEvidenciaPlan;

    private String oderesult;
    private List<T> listaDetalleGrafico;
    private String porcentajeCumplimiento;

    private List<T> listaGraficoEvento;
    private List<T> listaGraficoCump;
    private BigDecimal nuPerdida;

}

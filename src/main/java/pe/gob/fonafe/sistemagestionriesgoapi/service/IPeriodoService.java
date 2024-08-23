package pe.gob.fonafe.sistemagestionriesgoapi.service;

import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOPeriodo;

public interface IPeriodoService {
	
	DTOGenerico<?> listarPeriodos(Long empresa, Long anio);
	DTOGenerico<?> buscarPeriodo(Long p_id_periodo);
    DTOPeriodo registrarPeriodo(DTOPeriodo dTOPeriodo);
    DTOPeriodo actualizarPeriodo(DTOPeriodo dTOPeriodo);
    DTOPeriodo anularPeriodo(DTOPeriodo dTOPeriodo);
    DTOGenerico generarDePeriodo(Long idEmpresa, Long anio, Long idFrecuencia);

}

package pe.gob.fonafe.sistemagestionriesgoapi.service;

import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTODimension;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;

public interface IDimensionService {

	public DTOGenerico<?> listarDimension(Long p_ID_EMPRESA, Long p_ID_TIPO_CONTROL_RIESGO);
	public DTODimension registrarDimension(DTODimension dimension);
	public DTOGenerico<?> buscarDimension(Long p_id_DIM_RIESGO);
	public DTODimension actualizarDimension(DTODimension dTODimension);
	public DTODimension anularDimension(DTODimension dTODimension);
	
}

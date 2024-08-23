package pe.gob.fonafe.sistemagestionriesgoapi.dao;

import org.springframework.dao.DataAccessException;

import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTODimension;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;

public interface IDimensionDao {
	
	public DTOGenerico<?> listarDimension(Long p_ID_EMPRESA, Long p_ID_TIPO_CONTROL_RIESGO) throws DataAccessException;
	public DTODimension registrarDimension(DTODimension dimension) throws DataAccessException;
	public DTOGenerico<?> buscarDimension(Long p_id_DIM_RIESGO) throws DataAccessException;
	public DTODimension actualizarDimension(DTODimension dTODimension) throws DataAccessException;
	public DTODimension anularDimension(DTODimension dTODimension) throws DataAccessException;

}

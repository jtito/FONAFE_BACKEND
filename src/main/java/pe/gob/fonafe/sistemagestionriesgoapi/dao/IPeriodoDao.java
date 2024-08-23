package pe.gob.fonafe.sistemagestionriesgoapi.dao;

import org.springframework.dao.DataAccessException;

import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOPeriodo;

public interface IPeriodoDao {
	
	DTOGenerico<?> listarPeriodos(Long empresa, Long anio) throws DataAccessException;
	DTOGenerico<?> buscarPeriodo(Long p_id_periodo) throws DataAccessException;
    DTOPeriodo registrarPeriodo(DTOPeriodo dTOPeriodo) throws DataAccessException;
    DTOPeriodo actualizarPeriodo(DTOPeriodo dTOPeriodo) throws DataAccessException;
    DTOPeriodo anularPeriodo(DTOPeriodo dTOPeriodo) throws DataAccessException;
    DTOGenerico generarDePeriodo(Long idEmpresa, Long anio, Long idFrecuencia) throws DataAccessException;

}

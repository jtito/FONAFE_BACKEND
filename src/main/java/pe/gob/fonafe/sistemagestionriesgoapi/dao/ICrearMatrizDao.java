package pe.gob.fonafe.sistemagestionriesgoapi.dao;

import org.springframework.dao.DataAccessException;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOCrearMatriz;

import java.util.List;

public interface ICrearMatrizDao {

    DTOCrearMatriz obtenerCrearMatriz() throws DataAccessException;
    DTOCrearMatriz crearMatriz(DTOCrearMatriz matriz) throws DataAccessException;
    List<DTOCrearMatriz> listarMatrices() throws DataAccessException;

}

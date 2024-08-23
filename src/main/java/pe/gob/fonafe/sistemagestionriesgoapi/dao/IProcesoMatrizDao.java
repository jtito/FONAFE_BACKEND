package pe.gob.fonafe.sistemagestionriesgoapi.dao;

import org.springframework.dao.DataAccessException;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOProcesoMatriz;
import pe.gob.fonafe.sistemagestionriesgoapi.models.ProcesoMatrizBean;

public interface IProcesoMatrizDao {

    DTOProcesoMatriz registrarProcesoMatriz(ProcesoMatrizBean procesoMatrizBean) throws DataAccessException;
    DTOProcesoMatriz obtenerProcesoMatriz(Long idProcesoMatriz) throws DataAccessException;
    DTOGenerico listarProcesoMatriz(Long idEmpresa) throws DataAccessException;
    DTOProcesoMatriz actualizarProcesoMatriz(ProcesoMatrizBean procesoMatrizBean) throws DataAccessException;
    Byte anularProcesoMatriz(ProcesoMatrizBean procesoMatrizBean) throws DataAccessException;
}

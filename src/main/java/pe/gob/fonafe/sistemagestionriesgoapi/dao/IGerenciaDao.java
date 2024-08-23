package pe.gob.fonafe.sistemagestionriesgoapi.dao;

import org.springframework.dao.DataAccessException;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGerencia;
import pe.gob.fonafe.sistemagestionriesgoapi.models.GerenciaBean;

public interface IGerenciaDao {

    DTOGerencia registrarGerencia(GerenciaBean gerenciaBean) throws DataAccessException;
    DTOGerencia obtenerGerencia(Long idGerencia) throws DataAccessException;
    DTOGenerico listarGerencias(Long idEmpresa, Long idSede) throws DataAccessException;
    DTOGerencia actualizarGerencia(GerenciaBean gerenciaBean) throws DataAccessException;
    Byte anularGerencia(GerenciaBean gerenciaBean) throws DataAccessException;
}

package pe.gob.fonafe.sistemagestionriesgoapi.dao;

import org.springframework.dao.DataAccessException;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOParametro;

import java.util.List;

public interface IDatosGeneralesDao {

    DTOGenerico listarParametrosxCodigo(String codigo) throws DataAccessException;
}

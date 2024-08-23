package pe.gob.fonafe.sistemagestionriesgoapi.dao;

import org.springframework.dao.DataAccessException;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTODetalleParametro;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOParametro;
import pe.gob.fonafe.sistemagestionriesgoapi.models.DetalleParametroBean;
import pe.gob.fonafe.sistemagestionriesgoapi.models.ParametroBean;

import java.util.List;

public interface IDetalleParametroDao {

    DTODetalleParametro registrarDetalleParametro(DetalleParametroBean detalleParametroBean) throws DataAccessException;
    DTODetalleParametro obtenerDetalleParametro(Long idDetalleParametro) throws DataAccessException;
    DTOGenerico listarDetalleParametro(Long idParametro) throws DataAccessException;
    DTODetalleParametro actualizarDetalleParametro(DetalleParametroBean detalleParametroBean) throws DataAccessException;
    Byte anularDetalleParametro(DetalleParametroBean detalleParametroBean) throws DataAccessException;
}

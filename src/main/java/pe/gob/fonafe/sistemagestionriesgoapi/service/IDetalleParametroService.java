package pe.gob.fonafe.sistemagestionriesgoapi.service;

import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTODetalleParametro;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOParametro;
import pe.gob.fonafe.sistemagestionriesgoapi.models.DetalleParametroBean;
import pe.gob.fonafe.sistemagestionriesgoapi.models.ParametroBean;

import java.util.List;

public interface IDetalleParametroService {

    DTODetalleParametro registrarDetalleParametro(DetalleParametroBean detalleParametroBean);
    DTODetalleParametro obtenerDetalleParametro(Long idParametro);
    DTOGenerico listarDetalleParametro(Long idParametro);
    DTODetalleParametro actualizarDetalleParametro(DetalleParametroBean detalleParametroBean);
    Byte anularDetalleParametro(DetalleParametroBean detalleParametroBean);
}

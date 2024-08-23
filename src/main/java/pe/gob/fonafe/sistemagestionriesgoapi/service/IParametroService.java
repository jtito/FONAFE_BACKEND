package pe.gob.fonafe.sistemagestionriesgoapi.service;

import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOParametro;
import pe.gob.fonafe.sistemagestionriesgoapi.models.ParametroBean;

public interface IParametroService {

    DTOParametro registrarParametro(ParametroBean parametroBean);
    DTOParametro obtenerParametro(Long idParametro);
    DTOGenerico listarParametro();
    DTOParametro actualizarParametro(ParametroBean parametroBean);
    Byte anularParametro(ParametroBean parametroBean);
}

package pe.gob.fonafe.sistemagestionriesgoapi.service;

import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOSede;
import pe.gob.fonafe.sistemagestionriesgoapi.models.SedeBean;

public interface ISedeService {

    DTOGenerico registrarSede(DTOSede dtoSede);
    DTOSede obtenerSede(Long idSede);
    DTOGenerico listarSedes(Long idEmpresa);
    DTOGenerico actualizarSede(DTOSede dtoSede);
    Byte anularSede(SedeBean sedeBean);
}

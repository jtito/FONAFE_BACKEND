package pe.gob.fonafe.sistemagestionriesgoapi.service;

import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGerencia;
import pe.gob.fonafe.sistemagestionriesgoapi.models.GerenciaBean;

public interface IGerenciaService {

    DTOGerencia registrarGerencia(GerenciaBean gerenciaBean);
    DTOGerencia obtenerGerencia(Long idGerencia);
    DTOGenerico listarGerencias(Long idEmpresa, Long idSede);
    DTOGerencia actualizarGerencia(GerenciaBean gerenciaBean);
    Byte anularGerencia(GerenciaBean gerenciaBean);
}

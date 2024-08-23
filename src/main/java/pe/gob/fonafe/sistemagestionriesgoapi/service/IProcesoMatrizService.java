package pe.gob.fonafe.sistemagestionriesgoapi.service;

import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOProcesoMatriz;
import pe.gob.fonafe.sistemagestionriesgoapi.models.ProcesoMatrizBean;

public interface IProcesoMatrizService {
    DTOProcesoMatriz registrarProcesoMatriz(ProcesoMatrizBean procesoMatrizBean);
    DTOProcesoMatriz obtenerProcesoMatriz(Long idProcesoMatriz);
    DTOGenerico listarProcesoMatriz(Long idEmpresa);
    DTOProcesoMatriz actualizarProcesoMatriz(ProcesoMatrizBean procesoMatrizBean);
    Byte anularProcesoMatriz(ProcesoMatrizBean procesoMatrizBean);
}

package pe.gob.fonafe.sistemagestionriesgoapi.service;

import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOCrearMatriz;

public interface ICrearMatrizService {
    DTOCrearMatriz crearMatriz(DTOCrearMatriz dtoCrearMatriz);
    DTOCrearMatriz obtenerMatriz();
    DTOCrearMatriz listarMatriz();
    DTOCrearMatriz eliminarMatriz();
}

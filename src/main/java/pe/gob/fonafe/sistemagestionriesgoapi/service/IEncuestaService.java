/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.fonafe.sistemagestionriesgoapi.service;

import java.util.List;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOEncuesta;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.models.EncuestaBean;

/**
 *
 * @author joell
 */
public interface IEncuestaService {
    
    DTOEncuesta registrarEncuesta(EncuestaBean encuestaBean);
    DTOEncuesta obtenerEncuesta(Long idEncuesta);
    DTOGenerico listarEncuesta(Long idEmpresa, Long idPeriodo);
    DTOEncuesta actualizarEncuesta(EncuestaBean encuestaBean);
    Byte anularEncuesta(EncuestaBean encuestaBean);
    
}

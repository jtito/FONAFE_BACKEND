/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.fonafe.sistemagestionriesgoapi.service;

import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOSubProceso;
import pe.gob.fonafe.sistemagestionriesgoapi.models.SubProcesoBean;
import java.util.ArrayList;
import java.util.List;


public interface ISubProcesoService {
    DTOGenerico registrarSubProceso(DTOSubProceso subprocesoBean);
    DTOGenerico registrarSubProceso(SubProcesoBean subprocesoBean);
    List<DTOSubProceso> listarSubProceso(Long idProceso);
    DTOSubProceso actualizarSubProceso(SubProcesoBean subprocesoBean);
    
}

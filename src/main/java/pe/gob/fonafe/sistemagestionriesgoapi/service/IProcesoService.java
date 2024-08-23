/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.fonafe.sistemagestionriesgoapi.service;

import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOProceso;
import pe.gob.fonafe.sistemagestionriesgoapi.models.ProcesoBean;
import java.util.ArrayList;
import java.util.List;


public interface IProcesoService {
    
    DTOGenerico registrarProceso(DTOProceso procesoBean);
    DTOProceso obtenerProceso(Long idProceso);
    List<DTOProceso> listarProceso(Long idEmpresa,Long idTipoMatriz);
    List<DTOProceso> listaProcesosMatriz(Long idProcesoMatriz);
    DTOProceso actualizarProceso(ProcesoBean procesoBean);
    Byte anularProceso(ProcesoBean procesoBean);
    DTOGenerico listarProcesosxMatriz(Long idEmpresa,Long idSede,Long matrizNivel, Long idTipoMatriz);
    
}

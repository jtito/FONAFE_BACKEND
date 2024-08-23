/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.fonafe.sistemagestionriesgoapi.dao;

import org.springframework.dao.DataAccessException;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOProceso;

import pe.gob.fonafe.sistemagestionriesgoapi.models.ProcesoBean;

import java.util.List;
import java.util.Optional;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;

public interface IProcesoDao {
    
    DTOGenerico registrarProceso(ProcesoBean procesoBean) throws DataAccessException;
    DTOProceso obtenerProceso(Long idProceso) throws DataAccessException;
    List<DTOProceso> listarProcesos(Long idEmpresa,Long idTipoMatriz) throws DataAccessException;
    List<DTOProceso> listaProcesosMatriz(Long idProcesoMatriz) throws DataAccessException;
    DTOProceso actualizarProceso(ProcesoBean procesoBean) throws DataAccessException;
    Byte anularProceso(ProcesoBean matriznivelBean) throws DataAccessException;
    DTOGenerico listarProcesosxMatriz(Long idEmpresa,Long idSede,Long matrizNivel, Long idTipoMatriz) throws DataAccessException;
    
}


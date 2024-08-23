/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.fonafe.sistemagestionriesgoapi.dao;

import org.springframework.dao.DataAccessException;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOSubProceso;

import pe.gob.fonafe.sistemagestionriesgoapi.models.SubProcesoBean;

import java.util.List;
import java.util.Optional;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;

public interface ISubProcesoDao {
    
     DTOGenerico registrarSubProceso(SubProcesoBean procesoBean) throws DataAccessException;
     List<DTOSubProceso> listarSubProcesos(Long idProceso) throws DataAccessException;
     DTOSubProceso actualizarSubProceso(SubProcesoBean subprocesoBean) throws DataAccessException;
}

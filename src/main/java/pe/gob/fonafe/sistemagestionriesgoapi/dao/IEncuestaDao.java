/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.fonafe.sistemagestionriesgoapi.dao;

/**
 * @author joell
 */

import org.springframework.dao.DataAccessException;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOEncuesta;
import pe.gob.fonafe.sistemagestionriesgoapi.models.EncuestaBean;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOPuntaje;


public interface IEncuestaDao {

    DTOEncuesta registrarEncuesta(EncuestaBean encuestaBean) throws DataAccessException;

    DTOEncuesta obtenerEncuesta(Long idEncuesta) throws DataAccessException;

    DTOGenerico listarEncuestas(Long idEmpresa, Long idPeriodo) throws DataAccessException;

    DTOEncuesta actualizarEncuesta(EncuestaBean encuestaBean) throws DataAccessException;

    Byte anularEncuesta(EncuestaBean encuestaBean) throws DataAccessException;

    Map<String, List<?>> getReportData(Long idSurvey);
    
    List<DTOPuntaje> listaPuntaje(long idEncuesta)  throws DataAccessException;


}

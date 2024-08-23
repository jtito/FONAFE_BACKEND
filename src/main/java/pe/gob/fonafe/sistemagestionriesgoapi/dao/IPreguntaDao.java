/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.fonafe.sistemagestionriesgoapi.dao;

/**
 *
 * @author joell
 */
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Optional;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOPregunta;
import pe.gob.fonafe.sistemagestionriesgoapi.models.PreguntaBean;


public interface IPreguntaDao {
    
    DTOPregunta registrarPregunta(PreguntaBean encuestaBean) throws DataAccessException;
    DTOPregunta obtenerPregunta(Long idPregunta) throws DataAccessException;
    DTOGenerico listarPreguntas(Long idEncuesta) throws DataAccessException;
    DTOPregunta actualizarPregunta(PreguntaBean preguntaBean) throws DataAccessException;
    Byte anularPregunta(PreguntaBean preguntaBean) throws DataAccessException;
    
}

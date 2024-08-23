/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.fonafe.sistemagestionriesgoapi.service;

import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOPregunta;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOPreguntas;
import pe.gob.fonafe.sistemagestionriesgoapi.models.PreguntaBean;

/**
 *
 * @author joell
 */
public interface IPreguntaService {
    
    DTOPregunta registrarPregunta(PreguntaBean preguntaBean);
    DTOPregunta obtenerPregunta(Long idPregunta);
    DTOGenerico listarPreguntas(Long idEncuesta);
    DTOGenerico registrarPreguntas(DTOPreguntas preguntas);
    DTOGenerico actualizarPreguntas(DTOPreguntas preguntas);
    DTOPregunta actualizarPregunta(PreguntaBean preguntaBean);
    Byte anularPregunta(PreguntaBean preguntaBean);
    
    
}

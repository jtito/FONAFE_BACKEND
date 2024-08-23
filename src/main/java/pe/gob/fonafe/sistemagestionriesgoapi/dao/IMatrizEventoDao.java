/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.fonafe.sistemagestionriesgoapi.dao;

/**
 *
 * @author CANVIA
 */
import org.springframework.dao.DataAccessException;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTODetalleMatrizEvento;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOMatrizEvento;
import pe.gob.fonafe.sistemagestionriesgoapi.models.DetalleMatrizEventoBean;
import pe.gob.fonafe.sistemagestionriesgoapi.models.MatrizEventoBean;

import java.util.List;

public interface IMatrizEventoDao {
    List<DTOMatrizEvento> listarBandejaMatrizEvento(int idEmpresa, int idPeriodo, int idMatrizNivel) throws DataAccessException;
    DTOMatrizEvento registrarMatrizEvento(MatrizEventoBean matrizEventoBean) throws DataAccessException;
    DTOGenerico registrarDetalleMatrizEvento(DetalleMatrizEventoBean detalleMatrizFraude) throws DataAccessException;
    DTOMatrizEvento registrarMatrizRiesgoEvento(MatrizEventoBean matrizFraudeBean) throws DataAccessException;
  
    
    DTOMatrizEvento obtenerMatrizEvento(Integer idMatrizEvento) throws DataAccessException;
    DTOGenerico listarDetalleMatrizEvento(Integer idMatrizEvento, Long idUsuario) throws DataAccessException;
    DTOMatrizEvento actualizarMatrizEvento(MatrizEventoBean matrizEventobean) throws DataAccessException;
    DTODetalleMatrizEvento actualizarDetaMatrizEvento(DetalleMatrizEventoBean detalleMatrizEvento) throws DataAccessException;

    Byte anularMatrizEvento(MatrizEventoBean matrizEventoBean) throws DataAccessException;
    
}

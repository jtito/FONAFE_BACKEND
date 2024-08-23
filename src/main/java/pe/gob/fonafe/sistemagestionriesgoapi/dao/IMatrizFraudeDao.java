/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.fonafe.sistemagestionriesgoapi.dao;

import org.springframework.dao.DataAccessException;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTODetalleMatrizRiesgo;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOMatrizFraude;
import pe.gob.fonafe.sistemagestionriesgoapi.models.DetalleMatrizFraudeBean;
import pe.gob.fonafe.sistemagestionriesgoapi.models.MatrizFraudeBean;

import java.util.List;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTODetalleMatrizFraude;
public interface IMatrizFraudeDao {
    
    List<DTOMatrizFraude> listarBandejaMatrizFraude(int idEmpresa, int idPeriodo, int idMatrizNivel) throws DataAccessException;
    DTOMatrizFraude registrarMatrizFraude(MatrizFraudeBean matrizFraudeBean) throws DataAccessException;
    DTOGenerico registrarDetalleMatrizFraude(DetalleMatrizFraudeBean detalleMatrizFraude) throws DataAccessException;
    DTOMatrizFraude registrarMatrizRiesgoFraude(MatrizFraudeBean matrizFraudeBean) throws DataAccessException;
  
    
    DTOMatrizFraude obtenerMatrizFraude(Integer idMatrizFraude) throws DataAccessException;
    DTOGenerico listarDetalleMatrizFraude(Integer idMatrizFraude, Long idUsuario) throws DataAccessException;
    DTOMatrizFraude actualizarMatrizFraude(MatrizFraudeBean matrizFraudebean) throws DataAccessException;
    DTODetalleMatrizFraude actualizarDetaMatrizRiesgo(DetalleMatrizFraudeBean detalleMatrizFraude) throws DataAccessException;
    DTODetalleMatrizFraude actualizarDetaMatrizFraude(DetalleMatrizFraudeBean detalleMatrizFraude) throws DataAccessException;
   
    Byte anularMatrizFraude(MatrizFraudeBean matrizFraudeBean) throws DataAccessException;
    
}

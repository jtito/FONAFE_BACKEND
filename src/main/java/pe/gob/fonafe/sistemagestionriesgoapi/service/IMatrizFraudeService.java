/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.fonafe.sistemagestionriesgoapi.service;


import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOMatrizFraude;
import pe.gob.fonafe.sistemagestionriesgoapi.models.MatrizFraudeBean;
import java.util.List;

public interface IMatrizFraudeService {
    
 
    List<DTOMatrizFraude> listarBandejaMatrizFraude(int idEmpresa, int idPeriodo, int idMatrizNivel);
    DTOMatrizFraude registrarMatrizFraude(MatrizFraudeBean matrizFraudeBean);
    DTOMatrizFraude obtenerMatrizFraude(Integer idMatrizFraude, Long idUsuario);
    DTOMatrizFraude actualizarMatrizFraude(MatrizFraudeBean matrizFraudeBean);
    Byte anularMatrizFraude(MatrizFraudeBean matrizFraudeBean);

    
}

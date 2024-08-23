/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.fonafe.sistemagestionriesgoapi.service;

/**
 *
 * @author CANVIA
 */

import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOMatrizAnticorrupcion;
import pe.gob.fonafe.sistemagestionriesgoapi.models.MatrizAnticorrupcionBean;

import java.util.List;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTODetalleMatrizAnticorrupcion;

public interface IMatrizAnticorrupcionService {
    
    List<DTOMatrizAnticorrupcion> listarBandejaMatrizAntic(int idEmpresa, int idPeriodo, int idMatrizNivel);
    DTOMatrizAnticorrupcion registrarMatrizAnticorrupcion(MatrizAnticorrupcionBean matrizAnticBean);
    DTOMatrizAnticorrupcion obtenerMatrizAnticorrupcion(Integer idMatrizRiesgo, Long idUsuario);
    DTOMatrizAnticorrupcion actualizarMatrizAntic(MatrizAnticorrupcionBean matrizAnticBean);
    Byte anularMatrizAnticorrupcion(MatrizAnticorrupcionBean matrizAnticBean);
    DTODetalleMatrizAnticorrupcion obtenerDescripcion(String codRiesgo);
    
}

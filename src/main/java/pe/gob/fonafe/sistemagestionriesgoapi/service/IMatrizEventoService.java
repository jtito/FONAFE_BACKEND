/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.fonafe.sistemagestionriesgoapi.service;

import java.util.List;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTODetalleMatrizEvento;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOMatrizEvento;
import pe.gob.fonafe.sistemagestionriesgoapi.models.MatrizEventoBean;
/**
 *
 * @author CANVIA
 */
public interface IMatrizEventoService {
    
    List<DTOMatrizEvento> listarBandejaMatrizEvento(int idEmpresa, int idPeriodo, int idMatrizNivel);
    DTOMatrizEvento registrarMatrizRiesgo(MatrizEventoBean matrizEventoBean);

    DTOMatrizEvento obtenerMatrizRiesgo(Integer idMatrizEvento, Long idUsuario);
    DTOMatrizEvento actualizarMatrizRiesgo(MatrizEventoBean matrizEventoBean);
    Byte anularMatrizEvento(MatrizEventoBean matrizEventoBean);
    DTODetalleMatrizEvento obtenerDescripcion(String codRiesgo);
}

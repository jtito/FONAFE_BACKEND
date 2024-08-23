/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.fonafe.sistemagestionriesgoapi.service;

import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOMatrizNivel;
import pe.gob.fonafe.sistemagestionriesgoapi.models.MatrizNivelBean;
import java.util.ArrayList;
import java.util.List;

public interface IMatrizNivelService {
    
    DTOMatrizNivel registrarMatriz(MatrizNivelBean matrizBean);
    DTOMatrizNivel obtenerMatriz(Long idMatriz);
    List<DTOMatrizNivel> listarMatriz(Long idEmpresa, Long idSede, Long idTipoMatriz);
    DTOMatrizNivel actualizarMatriz(MatrizNivelBean matrizNivelBean);
    Byte anularMatriz(MatrizNivelBean matrizBean);
    
}

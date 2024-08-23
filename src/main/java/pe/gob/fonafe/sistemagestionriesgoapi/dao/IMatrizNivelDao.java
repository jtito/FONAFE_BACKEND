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
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOMatrizNivel;

import pe.gob.fonafe.sistemagestionriesgoapi.models.MatrizNivelBean;

import java.util.List;
import java.util.Optional;


public interface IMatrizNivelDao {
    
    DTOMatrizNivel registrarMatriz(MatrizNivelBean matriznivelBean) throws DataAccessException;
    DTOMatrizNivel obtenerMatriz(Long idMatriz) throws DataAccessException;
    List<DTOMatrizNivel> listarMatriz(Long idEmpresa,Long idSede, Long idTipoMatriz) throws DataAccessException;
    DTOMatrizNivel actualizarMatriz(MatrizNivelBean matriznivelBean) throws DataAccessException;
    Byte anularMatriz(MatrizNivelBean matriznivelBean) throws DataAccessException;
    
}

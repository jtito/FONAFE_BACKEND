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
import java.util.List;
import org.springframework.dao.DataAccessException;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTODetalleMatrizAnticorrupcion;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOMatrizAnticorrupcion;
import pe.gob.fonafe.sistemagestionriesgoapi.models.DetalleMatrizAnticorrupcionBean;
import pe.gob.fonafe.sistemagestionriesgoapi.models.MatrizAnticorrupcionBean;


public interface IMatrizAnticorrupcionDao {
    List<DTOMatrizAnticorrupcion> listarBandejaMatrizAntic(int idEmpresa, int idPeriodo, int idMatrizNivel) throws DataAccessException;

    DTOMatrizAnticorrupcion registrarMatrizRiesgo(MatrizAnticorrupcionBean matrizRiesgoBean) throws DataAccessException;
    DTOGenerico registrarDetalleMatrizAnticorrupcion(DetalleMatrizAnticorrupcionBean detalleMatrizAnticBean) throws DataAccessException;

    DTOMatrizAnticorrupcion obtenerMatrizAntic(Integer idMatrizRiesgo) throws DataAccessException;
    DTOGenerico listarDetalleMatrizAnticorrupcion(Integer idMatrizRiesgo, Long idUsuario) throws DataAccessException;

    DTOMatrizAnticorrupcion actualizarMatrizRiesgo(MatrizAnticorrupcionBean matrizRiesgoBean) throws DataAccessException;
    DTODetalleMatrizAnticorrupcion actualizarDetaMatrizAnticorrupcion(DetalleMatrizAnticorrupcionBean detalleMatrizContinuidadBean) throws DataAccessException;
    Byte anularMatrizRiesgo(MatrizAnticorrupcionBean matrizRiesgoBean) throws DataAccessException;
}

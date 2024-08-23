package pe.gob.fonafe.sistemagestionriesgoapi.dao;

import org.springframework.dao.DataAccessException;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.*;
import pe.gob.fonafe.sistemagestionriesgoapi.models.DetalleMatrizContinuidadBean;
import pe.gob.fonafe.sistemagestionriesgoapi.models.MatrizRiesgoContinuidadBean;

import java.util.List;

public interface IMatrizContinuidadDao {

    List<DTOMatrizContinuidad> listarBandejaMatrizContinuidad(int idEmpresa, int idPeriodo, int idMatrizNivel) throws DataAccessException;

    DTOMatrizContinuidad registrarMatrizRiesgo(MatrizRiesgoContinuidadBean matrizRiesgoBean) throws DataAccessException;
    DTOGenerico registrarDetalleMatrizContinuidad(DetalleMatrizContinuidadBean detalleMatrizContinuidadBean) throws DataAccessException;

    DTOMatrizContinuidad obtenerMatrizRiesgo(Integer idMatrizRiesgo) throws DataAccessException;
    DTOGenerico listarDetalleMatrizContinuidad(Integer idMatrizRiesgo, Long idUsuario) throws DataAccessException;

    DTOMatrizContinuidad actualizarMatrizRiesgo(MatrizRiesgoContinuidadBean matrizRiesgoBean) throws DataAccessException;
    DTODetalleMatrizContinuidad actualizarDetaMatrizContinuidad(DetalleMatrizContinuidadBean detalleMatrizContinuidadBean) throws DataAccessException;
}

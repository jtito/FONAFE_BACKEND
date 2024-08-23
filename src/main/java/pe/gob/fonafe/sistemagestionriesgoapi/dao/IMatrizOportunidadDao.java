package pe.gob.fonafe.sistemagestionriesgoapi.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTODetalleMatrizContinuidad;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOMatrizContinuidad;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOMatrizOportunidad;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOMatrizRiesgo;
import pe.gob.fonafe.sistemagestionriesgoapi.models.DetalleMatrizContinuidadBean;
import pe.gob.fonafe.sistemagestionriesgoapi.models.DetalleMatrizOportunidadBean;
import pe.gob.fonafe.sistemagestionriesgoapi.models.MatrizRiesgoContinuidadBean;
import pe.gob.fonafe.sistemagestionriesgoapi.models.MatrizRiesgoOportunidadBean;

public interface IMatrizOportunidadDao {

    DTOMatrizOportunidad registrarMatrizOportunidad(MatrizRiesgoOportunidadBean matrizRiesgoOportunidadBean) throws DataAccessException;
    DTOGenerico registrarDetalleMatrizOportunidad(DetalleMatrizOportunidadBean detalleMatrizOportunidadBean) throws DataAccessException;
    DTOGenerico listarDetalleMatrizOportunidad(Long idMatrizRiesgo, Long idUsuario) throws DataAccessException;
    DTOMatrizOportunidad actualizarMatrizOportunidad(MatrizRiesgoOportunidadBean matrizRiesgoOportunidadBean) throws DataAccessException;
    DTOGenerico actualizarDetalleMatrizOportunidad(DetalleMatrizOportunidadBean matrizOportunidadBean) throws DataAccessException;
    DTOMatrizOportunidad obtenerMatrizOportunidad(Integer idMatrizRiesgo) throws DataAccessException;
    List<DTOMatrizRiesgo> listarBandejaMatrizOportunidad(int idEmpresa, int idPeriodo, int idMatrizNivel) throws DataAccessException;
    Byte anularMatrizRiesgo(MatrizRiesgoOportunidadBean matrizRiesgoBean) throws DataAccessException;

}

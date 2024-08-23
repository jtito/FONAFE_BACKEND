package pe.gob.fonafe.sistemagestionriesgoapi.service;

import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOMatrizContinuidad;
import pe.gob.fonafe.sistemagestionriesgoapi.models.MatrizRiesgoContinuidadBean;

import java.util.List;

public interface IMatrizContinuidadService {

    List<DTOMatrizContinuidad> listarBandejaMatrizContinuidad(int idEmpresa, int idPeriodo, int idMatrizNivel);
    DTOMatrizContinuidad registrarMatrizContinuidad(MatrizRiesgoContinuidadBean matrizRiesgoContinuidadBean);

    DTOMatrizContinuidad obtenerMatrizContinuidad(Integer idMatrizRiesgo, Long idUsuario);
    DTOMatrizContinuidad actualizarMatrizContinuidad(MatrizRiesgoContinuidadBean matrizRiesgoContinuidadBeanBean);
}

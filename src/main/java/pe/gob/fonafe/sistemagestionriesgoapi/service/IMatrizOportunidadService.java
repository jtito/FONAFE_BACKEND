package pe.gob.fonafe.sistemagestionriesgoapi.service;

import java.util.List;

import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOMatrizOportunidad;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOMatrizRiesgo;
import pe.gob.fonafe.sistemagestionriesgoapi.models.MatrizRiesgoOportunidadBean;
public interface IMatrizOportunidadService {


	DTOMatrizOportunidad registrarMatrizOportunidad(MatrizRiesgoOportunidadBean matrizRiesgoBean);
	DTOMatrizOportunidad UpdateMatrizOportunidad(MatrizRiesgoOportunidadBean matrizRiesgoBean);
	DTOMatrizOportunidad OptenerMatrizOportunidad(Long idMatrizRiesgo, Long idUsuario);
        List<DTOMatrizRiesgo> listarBandejaMatrizOportunidad(int idEmpresa, int idPeriodo, int idMatrizNivel);
        Byte anularMatrizOportunidad(MatrizRiesgoOportunidadBean matrizRiesgoBean);
}

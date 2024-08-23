package pe.gob.fonafe.sistemagestionriesgoapi.service;

import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOComentarioAuditoria;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOMatrizRiesgo;
import pe.gob.fonafe.sistemagestionriesgoapi.models.ComentarioAuditoriaBean;
import pe.gob.fonafe.sistemagestionriesgoapi.models.MatrizRiesgoBean;

import java.util.List;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTODetalleMatrizRiesgo;

public interface IMatrizRiesgoService {

    List<DTOMatrizRiesgo> listarBandejaMatrizRiesgo(int idEmpresa, int idPeriodo, int idMatrizNivel);
    DTOMatrizRiesgo registrarMatrizRiesgo(MatrizRiesgoBean matrizRiesgoBean);
    DTOMatrizRiesgo obtenerMatrizRiesgo(Long idMatrizRiesgo, Long idUsuario);
    DTOComentarioAuditoria obtenerComentarioAuditoriaRiesgo(ComentarioAuditoriaBean comentarioAuditoriaBean);
    DTOMatrizRiesgo actualizarMatrizRiesgo(MatrizRiesgoBean matrizRiesgoBean);
    DTOComentarioAuditoria actualizarComentarioAuditoriaRiesgo(ComentarioAuditoriaBean comentarioAuditoriaBean);
    Byte anularMatrizRiesgo(MatrizRiesgoBean matrizRiesgoBean);
    DTODetalleMatrizRiesgo obtenerDescripcion(String codRiesgo);
    DTOGenerico obtenerMatrizPeriodo(int idEmpresa, int idTipoMatriz, int idMatrizNivel);

    DTOGenerico obtenerSeveridad(Float probabilidad, Float impacto, Long idTipoMatriz);

}

package pe.gob.fonafe.sistemagestionriesgoapi.dao;

import org.springframework.dao.DataAccessException;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOComentarioAuditoria;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTODetalleMatrizRiesgo;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOMatrizRiesgo;
import pe.gob.fonafe.sistemagestionriesgoapi.models.ComentarioAuditoriaBean;
import pe.gob.fonafe.sistemagestionriesgoapi.models.DetalleMatrizRiesgoBean;
import pe.gob.fonafe.sistemagestionriesgoapi.models.MatrizRiesgoBean;

import java.util.List;

public interface IMatrizRiesgoDao {
    List<DTOMatrizRiesgo> listarBandejaMatrizRiesgo(int idEmpresa, int idPeriodo, int idMatrizNivel) throws DataAccessException;
    DTOMatrizRiesgo registrarMatrizRiesgo(MatrizRiesgoBean matrizRiesgoBean) throws DataAccessException;
    DTOGenerico registrarDetalleMatrizRiesgo(DetalleMatrizRiesgoBean detalleMatrizRiesgo) throws DataAccessException;
    
    DTOMatrizRiesgo obtenerMatrizRiesgo(Long idMatrizRiesgo) throws DataAccessException;
    DTOComentarioAuditoria obtenerComentarioAuditoriaRiesgo(ComentarioAuditoriaBean comentarioAuditoriaBean) throws DataAccessException;
    DTODetalleMatrizRiesgo obtenerDescripcion(String codRiesgo) throws DataAccessException;
    DTOGenerico listarDetalleMatrizRiesgo(Long idMatrizRiesgo, Long idUsuario) throws DataAccessException;
    DTOMatrizRiesgo actualizarMatrizRiesgo(MatrizRiesgoBean matrizRiesgoBean) throws DataAccessException;
    DTODetalleMatrizRiesgo actualizarDetaMatrizRiesgo(DetalleMatrizRiesgoBean detalleMatrizRiesgo) throws DataAccessException;
    DTOComentarioAuditoria actualizarComentarioAuditoriaRiesgo(ComentarioAuditoriaBean comentarioAuditoriaBean) throws DataAccessException;
    DTOGenerico obtenerMatrizPeriodo(int idEmpresa,int idTipoMatriz,int matrizNivel) throws DataAccessException;
    Byte anularMatrizRiesgo(MatrizRiesgoBean matrizRiesgoBean) throws DataAccessException;

    DTOGenerico ObtenerSeveridad(Float probabilidad, Float impacto, Long idTipoMatriz) throws DataAccessException;
}

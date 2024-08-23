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
import org.springframework.dao.DataAccessException;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTODetalleParametro;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOParametro;
import pe.gob.fonafe.sistemagestionriesgoapi.models.DetalleParametroBean;
import pe.gob.fonafe.sistemagestionriesgoapi.models.ParametroBean;

import java.util.List;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTODetalleGrafico;

public interface IDetalleGraficoDao {
     List<DTODetalleGrafico> listarCantidadRiesgoInhe(int idEmpresa, int idPeriodo, int idMatrizNivel, int idTipoMatriz) throws DataAccessException;
     List<DTODetalleGrafico> listarCantidadRiesgoGer(int idEmpresa, int idPeriodo, int idMatrizNivel) throws DataAccessException;
     DTOGenerico listarCantidadRiesgoKri(int idEmpresa, int idPeriodo, int idMatrizNivel, int idTipoMatriz) throws DataAccessException;
     DTOGenerico reporteMatrizEventos(Long idEmpresa, Long idPeriodo) throws DataAccessException;

}

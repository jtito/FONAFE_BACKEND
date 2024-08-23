/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.fonafe.sistemagestionriesgoapi.service;

import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTODetalleParametro;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOParametro;
import pe.gob.fonafe.sistemagestionriesgoapi.models.DetalleParametroBean;
import pe.gob.fonafe.sistemagestionriesgoapi.models.ParametroBean;

import java.util.List;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTODetalleGrafico;

public interface IDetalleGraficoService {
    List<DTODetalleGrafico> listarCantidadRiesgosInhe(int idEmpresa, int idPeriodo, int idMatrizNivel, int idTipoMatriz);
    
    List<DTODetalleGrafico> listarCantidadRiesgosGer(int idEmpresa, int idPeriodo, int idMatrizNivel);
    
    DTOGenerico listarCantidadRiesgosKri(int idEmpresa, int idPeriodo, int idMatrizNivel, int idTipoMatriz);

    DTOGenerico reporteMatrizEventos(Long idEmpresa, Long idPeriodo);
}

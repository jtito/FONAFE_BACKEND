/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.fonafe.sistemagestionriesgoapi.service.impl;

/**
 *
 * @author CANVIA
 */
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.fonafe.sistemagestionriesgoapi.dao.IDetalleGraficoDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTODetalleGrafico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.service.IDetalleGraficoService;

@Service
@Transactional
public class DetalleGraficoServiceImpl implements IDetalleGraficoService {

    private static final Logger logger = LogManager.getLogger("DETALLE_GRAFICO_API");

    final IDetalleGraficoDao iDetalleGraficoDao;

    public DetalleGraficoServiceImpl(IDetalleGraficoDao iDetalleGraficoDao) {
        this.iDetalleGraficoDao = iDetalleGraficoDao;
    }

    @Override
    public List<DTODetalleGrafico> listarCantidadRiesgosInhe(int idEmpresa, int idPeriodo, int idMatrizNivel, int idTipoMatriz) {
     
        logger.info("Inicio DetalleGraficoServiceImpl - listarCantidadRiesgoInhe");

        List<DTODetalleGrafico> listaBandejaMatrizEvento;

        try {
            listaBandejaMatrizEvento = iDetalleGraficoDao.listarCantidadRiesgoInhe(idEmpresa, idPeriodo, idMatrizNivel, idTipoMatriz);
        } catch (Exception ex) {
            listaBandejaMatrizEvento = new ArrayList<>();
            logger.error(ex.getMessage());
        }

        logger.info("Fin  DetalleGraficoServiceImpl - listarCantidadRiesgoInhe");
        return listaBandejaMatrizEvento;
    
    }

    @Override
    public List<DTODetalleGrafico> listarCantidadRiesgosGer(int idEmpresa, int idPeriodo, int idMatrizNivel) {
        logger.info("Inicio DetalleGraficoServiceImpl - listarCantidadRiesgosGer");

        List<DTODetalleGrafico> listaBandejaMatrizEvento;

        try {
            listaBandejaMatrizEvento = iDetalleGraficoDao.listarCantidadRiesgoGer(idEmpresa, idPeriodo, idMatrizNivel);
        } catch (Exception ex) {
            listaBandejaMatrizEvento = new ArrayList<>();
            logger.error(ex.getMessage());
        }

        logger.info("Fin  DetalleGraficoServiceImpl - listarCantidadRiesgosGer");
        return listaBandejaMatrizEvento;
    }

    @Override
    public DTOGenerico listarCantidadRiesgosKri(int idEmpresa, int idPeriodo, int idMatrizNivel, int idTipoMatriz) {
     logger.info("Inicio DetalleGraficoServiceImpl - listarCantidadRiesgosKri");

        DTOGenerico dtoGenerico;

        try {
            dtoGenerico = iDetalleGraficoDao.listarCantidadRiesgoKri(idEmpresa, idPeriodo, idMatrizNivel, idTipoMatriz);
        } catch (Exception ex) {
            dtoGenerico = new DTOGenerico();
            logger.error(ex.getMessage());
        }

        logger.info("Fin DetalleGraficoServiceImpl - listarCantidadRiesgosKri");
        return dtoGenerico;
    
    }

    @Override
    public DTOGenerico reporteMatrizEventos(Long idEmpresa, Long idPeriodo) {
        logger.info("Inicio de DetalleGraficoServiceImpl - reporteMatrizEventos");
        DTOGenerico dtoGenerico;
        try {
            dtoGenerico = iDetalleGraficoDao.reporteMatrizEventos(idEmpresa, idPeriodo);
        } catch (Exception ex) {
            dtoGenerico = new DTOGenerico();
            logger.error(ex.getMessage());
        }

        logger.info("Fin de DetalleGraficoServiceImpl - reporteMatrizEventos");
        return dtoGenerico;
    }


}

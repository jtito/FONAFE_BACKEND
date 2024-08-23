package pe.gob.fonafe.sistemagestionriesgoapi.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import pe.gob.fonafe.sistemagestionriesgoapi.dao.IMatrizRiesgoDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.*;
import pe.gob.fonafe.sistemagestionriesgoapi.models.ComentarioAuditoriaBean;
import pe.gob.fonafe.sistemagestionriesgoapi.models.DetalleMatrizRiesgoBean;
import pe.gob.fonafe.sistemagestionriesgoapi.models.MatrizRiesgoBean;
import pe.gob.fonafe.sistemagestionriesgoapi.service.IMatrizRiesgoService;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import java.util.ArrayList;
import java.util.List;

@Service
public class MatrizRiesgoServiceImpl implements IMatrizRiesgoService {

    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    IMatrizRiesgoDao iMatrizRiesgoDao;

    public MatrizRiesgoServiceImpl(IMatrizRiesgoDao iMatrizRiesgoDao) {
        this.iMatrizRiesgoDao = iMatrizRiesgoDao;
    }

    @Override
    public List<DTOMatrizRiesgo> listarBandejaMatrizRiesgo(int idEmpresa, int idPeriodo, int idMatrizNivel) {
        logger.info("Inicio MatrizRiesgoServiceImpl - listarBandejaMatrizRiesgo");

        List<DTOMatrizRiesgo> listaBandejaMatrizRiesgo;

        try {
            listaBandejaMatrizRiesgo = iMatrizRiesgoDao.listarBandejaMatrizRiesgo(idEmpresa, idPeriodo, idMatrizNivel);
        } catch (Exception ex) {
            listaBandejaMatrizRiesgo = new ArrayList<>();
            logger.error(ex.getMessage());
        }

        logger.info("Fin MatrizRiesgoServiceImpl - listarBandejaMatrizRiesgo");
        return listaBandejaMatrizRiesgo;
    }

    @Override
    public DTOMatrizRiesgo registrarMatrizRiesgo(MatrizRiesgoBean matrizRiesgoBean) {
        logger.info("Inicio de MatrizRiesgoServiceImpl - registrarMatrizRiesgo");

        DTOMatrizRiesgo matrizRiesgo = new DTOMatrizRiesgo();
        DTOGenerico detalleMatrizRiesgo = null;
        //List<DTOMatrizRiesgo> listamatriz=null;

        try {
            /*logger.info("2712 LISTAR MA "+ matrizRiesgoBean.getIdMatrizNivel().toString()+" ID PERIODO "+matrizRiesgoBean.getIdPeriodo());
            listamatriz= iMatrizRiesgoDao.listarBandejaMatrizRiesgo(0, matrizRiesgoBean.getIdPeriodo().intValue(), matrizRiesgoBean.getIdMatrizNivel().intValue());
            logger.info("2712 Check de MatrizRiesgoServiceImpl "+listamatriz.size());
            if(listamatriz.size()>0){
                
                matrizRiesgo = new DTOMatrizRiesgo();
                matrizRiesgo.setIdMatrizRiesgo(0L);
                matrizRiesgo.setCodigoResultado(SNConstantes.CODIGO_DUPLICATE);
                
            }
            else{
                
                detalleMatrizRiesgo = new DTOGenerico();
                matrizRiesgo = iMatrizRiesgoDao.registrarMatrizRiesgo(matrizRiesgoBean);
            

                if (matrizRiesgoBean.getListaDetalleMatriz().size() > 0) {

                  for (DetalleMatrizRiesgoBean detalleMatrizRiesgoBean : matrizRiesgoBean.getListaDetalleMatriz()) {
                      detalleMatrizRiesgoBean.setIdMatrizRiesgo(matrizRiesgo.getIdMatrizRiesgo());
                      detalleMatrizRiesgo = iMatrizRiesgoDao.registrarDetalleMatrizRiesgo(detalleMatrizRiesgoBean);
                  }

                }
                
            }*/

            detalleMatrizRiesgo = new DTOGenerico();

            matrizRiesgo = iMatrizRiesgoDao.registrarMatrizRiesgo(matrizRiesgoBean);

            if (matrizRiesgoBean.getListaDetalleMatriz().size() > 0 && matrizRiesgo.getDescripcionResultado().equals("OK")) {

                for (DetalleMatrizRiesgoBean detalleMatrizRiesgoBean : matrizRiesgoBean.getListaDetalleMatriz()) {
                    detalleMatrizRiesgoBean.setIdMatrizRiesgo(matrizRiesgo.getIdMatrizRiesgo());
                    detalleMatrizRiesgo = iMatrizRiesgoDao.registrarDetalleMatrizRiesgo(detalleMatrizRiesgoBean);
                }

            }

        } catch (Exception ex) {
            matrizRiesgo = new DTOMatrizRiesgo();
            matrizRiesgo.setIdMatrizRiesgo(0L);
            matrizRiesgo.setCodigoResultado(SNConstantes.CODIGO_ERROR);
            logger.error(ex.getMessage());
        }

        logger.info("Fin de MatrizRiesgoServiceImpl - registrarMatrizRiesgo");
        return matrizRiesgo;
    }

    @Override
    public DTOMatrizRiesgo obtenerMatrizRiesgo(Long idMatrizRiesgo, Long idUsuario) {
        logger.info("Inicio MatrizRiesgoServiceImpl - obtenerMatrizRiesgo");

        DTOMatrizRiesgo dtoMatrizRiesgo;
        DTOGenerico listaDetaMatrizRiesgo;

        try {
            dtoMatrizRiesgo = iMatrizRiesgoDao.obtenerMatrizRiesgo(idMatrizRiesgo);
            listaDetaMatrizRiesgo = iMatrizRiesgoDao.listarDetalleMatrizRiesgo(idMatrizRiesgo, idUsuario);
            dtoMatrizRiesgo.setListaDetalleMatriz(listaDetaMatrizRiesgo.getListado());
        } catch (Exception ex) {
            dtoMatrizRiesgo = new DTOMatrizRiesgo();
            logger.error(ex.getMessage());
        }

        logger.info("Fin MatrizRiesgoServiceImpl - obtenerMatrizRiesgo");
        return dtoMatrizRiesgo;
    }

    @Override
    public DTOComentarioAuditoria obtenerComentarioAuditoriaRiesgo(ComentarioAuditoriaBean comentarioAuditoriaBean) {
        logger.info("Inicio MatrizRiesgoServiceImpl - obtenerComentarioAuditoriaRiesgo");

        DTOComentarioAuditoria dtoComentarioAuditoria;
        try {

            dtoComentarioAuditoria = iMatrizRiesgoDao.obtenerComentarioAuditoriaRiesgo(comentarioAuditoriaBean);

        } catch (Exception ex) {
            dtoComentarioAuditoria = new DTOComentarioAuditoria();
            logger.error(ex.getMessage());
        }

        logger.info("Fin MatrizRiesgoServiceImpl - obtenerComentarioAuditoriaRiesgo");
        return dtoComentarioAuditoria;
    }

    @Override
    public DTOMatrizRiesgo actualizarMatrizRiesgo(MatrizRiesgoBean matrizRiesgoBean) {
        logger.info("Inicio MatrizRiesgoServiceImpl - actualizaMatrizRiesgo");

        DTOMatrizRiesgo matrizRiesgoActual = null;
        DTODetalleMatrizRiesgo detaMatrizRiesgoActual = null;
        DTOGenerico detaMatrizRiesgoNuevo = null;

        try {

            matrizRiesgoActual = new DTOMatrizRiesgo();
            detaMatrizRiesgoActual = new DTODetalleMatrizRiesgo();
            detaMatrizRiesgoNuevo = new DTOGenerico();

            matrizRiesgoActual = iMatrizRiesgoDao.actualizarMatrizRiesgo(matrizRiesgoBean);

            if (matrizRiesgoBean.getListaDetalleMatriz().size() > 0) {

                for (DetalleMatrizRiesgoBean detalleMatrizRiesgoBean : matrizRiesgoBean.getListaDetalleMatriz()) {
                    if (detalleMatrizRiesgoBean.getIdDetaMatrizRiesgo() == null) {
                        detalleMatrizRiesgoBean.setIdMatrizRiesgo(matrizRiesgoBean.getIdMatrizRiesgo());
                        detaMatrizRiesgoNuevo = iMatrizRiesgoDao.registrarDetalleMatrizRiesgo(detalleMatrizRiesgoBean);
                    } else {
                        detaMatrizRiesgoActual = iMatrizRiesgoDao.actualizarDetaMatrizRiesgo(detalleMatrizRiesgoBean);
                    }
                }

            }

        } catch (Exception ex) {
            matrizRiesgoActual = new DTOMatrizRiesgo();
            matrizRiesgoActual.setIdMatrizRiesgo(0L);
            matrizRiesgoActual.setCodigoResultado(SNConstantes.CODIGO_ERROR);
            logger.error(ex.getMessage());
        }

        logger.info("Fin MatrizRiesgoServiceImpl - actualizaMatrizRiesgo");
        return matrizRiesgoActual;
    }

    @Override
    public DTOComentarioAuditoria actualizarComentarioAuditoriaRiesgo(ComentarioAuditoriaBean comentarioAuditoriaBean) {
        logger.info("Inicio MatrizRiesgoServiceImpl - actualizarComentarioAuditoriaRiesgo");
        DTOComentarioAuditoria dtoComentarioAuditoria = null;

        try {

            dtoComentarioAuditoria= iMatrizRiesgoDao.actualizarComentarioAuditoriaRiesgo(comentarioAuditoriaBean);

        } catch (Exception ex) {
            dtoComentarioAuditoria = new DTOComentarioAuditoria();
            dtoComentarioAuditoria.setCodigoResultado(SNConstantes.CODIGO_ERROR);
            logger.error(ex.getMessage());
        }
        logger.info("Fin MatrizRiesgoServiceImpl - actualizarComentarioAuditoriaRiesgo");
        return dtoComentarioAuditoria;
    }

    @Override
    public Byte anularMatrizRiesgo(MatrizRiesgoBean matrizRiesgoBean) {
        logger.info("Inicio MatrizRiesgoServiceImpl - anularMatrizRiesgo");

        Byte indicadorAnularMatriz;

        try {
            indicadorAnularMatriz = iMatrizRiesgoDao.anularMatrizRiesgo(matrizRiesgoBean);
        } catch (Exception ex) {
            indicadorAnularMatriz = 0;
            logger.error(ex.getMessage());
        }

        logger.info("Fin MatrizRiesgoServiceImpl - anularMatrizRiesgo");
        return indicadorAnularMatriz;
    }

    @Override
    public DTODetalleMatrizRiesgo obtenerDescripcion(String codRiesgo) {
    
        logger.info("Inicio MatrizRiesgoServiceImpl - obtenerDescripcion");

        DTODetalleMatrizRiesgo dtoMatrizRiesgo;


        try {
            dtoMatrizRiesgo = iMatrizRiesgoDao.obtenerDescripcion(codRiesgo);
           
        } catch (Exception ex) {
            dtoMatrizRiesgo = new DTODetalleMatrizRiesgo();
            logger.error(ex.getMessage());
        }

        logger.info("Fin MatrizRiesgoServiceImpl - obtenerDescripcion");
        return dtoMatrizRiesgo;
    
    }

    @Override
    public DTOGenerico obtenerSeveridad(Float probabilidad, Float impacto, Long idTipoMatriz) {
        logger.info("Inicio MatrizRiesgoServiceImpl - obtenerSeveridad");

        DTOGenerico severidad;

        try {
            severidad = iMatrizRiesgoDao.ObtenerSeveridad(probabilidad,impacto,idTipoMatriz);

        } catch (Exception ex) {
            severidad = new DTOGenerico();
            logger.error(ex.getMessage());
        }

        logger.info("Fin MatrizRiesgoServiceImpl - obtenerSeveridad");
        return severidad;

    }

    public DTOGenerico obtenerMatrizPeriodo(int idEmpresa, int idTipoMatriz, int idMatrizNivel) {
     
        logger.info("Inicio MatrizRiesgoServiceImpl - obtenerMatrizPeriodo");

        DTOGenerico listaDetaMatrizRiesgo;

        try {
            listaDetaMatrizRiesgo = iMatrizRiesgoDao.obtenerMatrizPeriodo(idEmpresa,idTipoMatriz,idMatrizNivel);
           
        } catch (Exception ex) {
            listaDetaMatrizRiesgo = new DTOGenerico();
            logger.error(ex.getMessage());
        }

        logger.info("Fin MatrizRiesgoServiceImpl - obtenerMatrizPeriodo");
        return listaDetaMatrizRiesgo;
    
    }
}

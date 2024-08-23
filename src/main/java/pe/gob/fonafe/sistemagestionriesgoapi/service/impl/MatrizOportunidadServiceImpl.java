package pe.gob.fonafe.sistemagestionriesgoapi.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import pe.gob.fonafe.sistemagestionriesgoapi.dao.IMatrizOportunidadDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dao.IMatrizRiesgoDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.*;
import pe.gob.fonafe.sistemagestionriesgoapi.models.DetalleMatrizOportunidadBean;
import pe.gob.fonafe.sistemagestionriesgoapi.models.DetalleMatrizRiesgoBean;
import pe.gob.fonafe.sistemagestionriesgoapi.models.MatrizRiesgoBean;
import pe.gob.fonafe.sistemagestionriesgoapi.models.MatrizRiesgoOportunidadBean;
import pe.gob.fonafe.sistemagestionriesgoapi.service.IMatrizOportunidadService;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import java.util.ArrayList;
import java.util.List;

@Service
public class MatrizOportunidadServiceImpl implements IMatrizOportunidadService {

    @SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    IMatrizRiesgoDao iMatrizRiesgoDao;
    IMatrizOportunidadDao iMatrizOportunidadDao;

    public MatrizOportunidadServiceImpl(IMatrizRiesgoDao iMatrizRiesgoDao , IMatrizOportunidadDao iMatrizOportunidadDao ) {
        this.iMatrizRiesgoDao = iMatrizRiesgoDao;
        this.iMatrizOportunidadDao = iMatrizOportunidadDao;
    }


	@SuppressWarnings({ "unused", "rawtypes" })
	@Override
	public DTOMatrizOportunidad registrarMatrizOportunidad(MatrizRiesgoOportunidadBean matrizRiesgoOportunidadBean) {
	        logger.info("Inicio de MatrizOportunidadServiceImpl - registrarMatrizOportunidad");

	        DTOMatrizOportunidad matrizOportunidad = new DTOMatrizOportunidad();
	        DTOGenerico detalleMatrizRiesgo = null;
	        try {
	            detalleMatrizRiesgo = new DTOGenerico();
	            DTOMatrizRiesgo dTOMatrizRiesgo= new DTOMatrizRiesgo();
	            MatrizRiesgoBean riesgo= new MatrizRiesgoBean();
	            riesgo.setIdCartera(matrizRiesgoOportunidadBean.getIdCartera());
	            riesgo.setIdEmpresa(matrizRiesgoOportunidadBean.getIdEmpresa());
	            riesgo.setIdSede(matrizRiesgoOportunidadBean.getIdSede());
                riesgo.setIdGerencia(matrizRiesgoOportunidadBean.getIdGerencia());
                riesgo.setIdPeriodo(matrizRiesgoOportunidadBean.getIdPeriodo());
                riesgo.setDePeriodo(matrizRiesgoOportunidadBean.getDePeriodo());
                riesgo.setIdTipoMatriz(matrizRiesgoOportunidadBean.getIdTipoMatriz());
                riesgo.setDeTipoMatriz(matrizRiesgoOportunidadBean.getDeTipoMatriz());
                riesgo.setIdMatrizNivel(matrizRiesgoOportunidadBean.getIdMatrizNivel());
                riesgo.setIdMatrizNivel(matrizRiesgoOportunidadBean.getIdMatrizNivel());
                riesgo.setDeMatrizNivel(matrizRiesgoOportunidadBean.getDeMatrizNivel());
                riesgo.setMatrizNivel(matrizRiesgoOportunidadBean.getMatrizNivel());
                dTOMatrizRiesgo = iMatrizRiesgoDao.registrarMatrizRiesgo(riesgo);

	            if (matrizRiesgoOportunidadBean.getListaDetalleMatrizOportunidad().size() > 0) {
	            	 for(DetalleMatrizOportunidadBean matrizOpor :matrizRiesgoOportunidadBean.getListaDetalleMatrizOportunidad() )
	            	 {
	            		 matrizOpor.setIdMatrizRiesgo(dTOMatrizRiesgo.getIdMatrizRiesgo());
	            		 matrizOpor.setIdPeriodo(riesgo.getIdPeriodo());
	            		
	            		 matrizOpor.setIdEmpresa(riesgo.getIdEmpresa());
	             		 matrizOpor.setIdSedeEmpresa(riesgo.getIdSede());
	            		  if(matrizOpor.getIdDetalleMOportunidad() ==null) {
	            		 detalleMatrizRiesgo=iMatrizOportunidadDao.registrarDetalleMatrizOportunidad(matrizOpor);
	            		  }
	            	 }
	            	 matrizOportunidad.setCodigoResultado(detalleMatrizRiesgo.getCodigoResultado());
	            	
	            }

	        } catch (Exception ex) {
	        	matrizOportunidad = new DTOMatrizOportunidad();
	        	matrizOportunidad.setIdMatrizRiesgo(0L);
	            matrizOportunidad.setCodigoResultado(SNConstantes.CODIGO_ERROR);
	            logger.error(ex.getMessage());
	        }

	        logger.info("Fin de MatrizOportunidadServiceImpl - registrarMatrizOportunidad");
	        return matrizOportunidad;
	    
	    }

	@SuppressWarnings({ "unused", "rawtypes" })
	@Override
	public DTOMatrizOportunidad UpdateMatrizOportunidad(MatrizRiesgoOportunidadBean matrizRiesgoOportunidadBean) {
		  logger.info("Inicio de MatrizOportunidadServiceImpl - UpdateMatrizOportunidad");

	        DTOMatrizOportunidad matrizOportunidadActual = null;
	        DTOGenerico detalleMatrizOportunidadActual = null;
	        DTOGenerico detalleMatrizOportunidadNuevo = null;
	        try {
	        	matrizOportunidadActual = new DTOMatrizOportunidad();
	        	detalleMatrizOportunidadActual = new DTOGenerico();
	        	detalleMatrizOportunidadNuevo = new DTOGenerico();

	        	matrizOportunidadActual = iMatrizOportunidadDao.actualizarMatrizOportunidad(matrizRiesgoOportunidadBean);

	            if (matrizRiesgoOportunidadBean.getListaDetalleMatrizOportunidad().size() > 0) {
	            	 for(DetalleMatrizOportunidadBean detalleMatrizOportunidad :matrizRiesgoOportunidadBean.getListaDetalleMatrizOportunidad()) {
	            		if (detalleMatrizOportunidad.getIdDetalleMOportunidad() == null){
	            			detalleMatrizOportunidad.setIdMatrizRiesgo(matrizRiesgoOportunidadBean.getIdMatrizRiesgo());
	            			detalleMatrizOportunidadNuevo = iMatrizOportunidadDao.registrarDetalleMatrizOportunidad(detalleMatrizOportunidad);
						}else{
	            			detalleMatrizOportunidadActual = iMatrizOportunidadDao.actualizarDetalleMatrizOportunidad(detalleMatrizOportunidad);
						}
	            	 }
	            }

	        } catch (Exception ex) {
	        	matrizOportunidadActual = new DTOMatrizOportunidad();
	        	matrizOportunidadActual.setIdMatrizRiesgo(0L);
	            matrizOportunidadActual.setCodigoResultado(SNConstantes.CODIGO_ERROR);
	            logger.error(ex.getMessage());
	        }

	        logger.info("Fin de MatrizOportunidadServiceImpl - UpdateMatrizOportunidad");
	        return matrizOportunidadActual;
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "null", "unused" })
	@Override
	public DTOMatrizOportunidad OptenerMatrizOportunidad(Long idMatrizRiesgo, Long idUsuario) {
		   logger.info("Inicio MatrizOportunidadServiceImpl - OptenerMatrizOportunidad");	

		   DTOMatrizOportunidad dtoMatriOp = new DTOMatrizOportunidad() ;
	        DTOGenerico listaDetaMatrizRiesgo;
	       DTOMatrizRiesgo dtoMatrizRiesgo;
	
	        try {
	        	dtoMatrizRiesgo= iMatrizRiesgoDao.obtenerMatrizRiesgo(idMatrizRiesgo);
	        	dtoMatriOp.setIdMatrizRiesgo(dtoMatrizRiesgo.getIdMatrizRiesgo());
	        	dtoMatriOp.setIdCartera(dtoMatrizRiesgo.getIdCartera());
	        	dtoMatriOp.setDeCartera(dtoMatrizRiesgo.getDeCartera());
	        	dtoMatriOp.setIdEmpresa(dtoMatrizRiesgo.getIdEmpresa());
	        	dtoMatriOp.setNombreCortoEmpresa(dtoMatrizRiesgo.getNombreCortoEmpresa());
	        	dtoMatriOp.setIdSede(dtoMatrizRiesgo.getIdSede());
	        	dtoMatriOp.setIdGerencia(dtoMatrizRiesgo.getIdGerencia());
	        	dtoMatriOp.setIdPeriodo(dtoMatrizRiesgo.getIdPeriodo());
	        	dtoMatriOp.setDePeriodo(dtoMatrizRiesgo.getDePeriodo());
	        	dtoMatriOp.setIdTipoMatriz(dtoMatrizRiesgo.getIdTipoMatriz());
	        	dtoMatriOp.setIdMatrizNivel(dtoMatrizRiesgo.getIdMatrizNivel());
	        	dtoMatriOp.setMatrizNivel(dtoMatrizRiesgo.getMatrizNivel());
	        	dtoMatriOp.setDeMatrizNivel(dtoMatrizRiesgo.getDeMatrizNivel());
	        	dtoMatriOp.setFechaCreacion(dtoMatrizRiesgo.getFechaCreacion());
	        	dtoMatriOp.setIndicadorBaja(dtoMatrizRiesgo.getIndicadorBaja());
	        	dtoMatriOp.setCodigoResultado(dtoMatrizRiesgo.getCodigoResultado());
	        	dtoMatriOp.setDescripcionResultado(dtoMatrizRiesgo.getDescripcionResultado());
	            listaDetaMatrizRiesgo = iMatrizOportunidadDao.listarDetalleMatrizOportunidad(idMatrizRiesgo, idUsuario);
	            dtoMatriOp.setListaDetalleMatrizOportunidad(listaDetaMatrizRiesgo.getListado());
	            dtoMatriOp.setCodigoResultado(listaDetaMatrizRiesgo.getCodigoResultado());
	        } catch (Exception ex) {
	        	dtoMatriOp = new DTOMatrizOportunidad();
	            logger.error(ex.getMessage());
	        }

	        logger.info("Fin MatrizOportunidadServiceImpl - OptenerMatrizOportunidad");
	        return dtoMatriOp;
	}



	@Override
	public List<DTOMatrizRiesgo> listarBandejaMatrizOportunidad(int idEmpresa, int idPeriodo, int idMatrizNivel) {
	    logger.info("Inicio MatrizOportunidadServiceImpl - listarBandejaMatrizOportunidad");

        List<DTOMatrizRiesgo> listaBandejaMatrizRiesgo = new ArrayList<DTOMatrizRiesgo>();

        try {
            listaBandejaMatrizRiesgo = iMatrizOportunidadDao.listarBandejaMatrizOportunidad(idEmpresa, idPeriodo, idMatrizNivel);
        } catch (Exception ex) {
            listaBandejaMatrizRiesgo = new ArrayList<>();
            logger.error(ex.getMessage());
        }

        logger.info("Fin MatrizOportunidadServiceImpl - listarBandejaMatrizOportunidad");
        return listaBandejaMatrizRiesgo;
	}

    @Override
    public Byte anularMatrizOportunidad(MatrizRiesgoOportunidadBean matrizRiesgoBean) {
        
        logger.info("Inicio MatrizOportunidadServiceImpl - anularMatrizOportunidad");

        Byte indicadorAnularMatriz;

        try {
            indicadorAnularMatriz = iMatrizOportunidadDao.anularMatrizRiesgo(matrizRiesgoBean);
        } catch (Exception ex) {
            indicadorAnularMatriz = 0;
            logger.error(ex.getMessage());
        }

        logger.info("Fin MatrizOportunidadServiceImpl - anularMatrizRiesgo");
        return indicadorAnularMatriz;
    
    }

}

   


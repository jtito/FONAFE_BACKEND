/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.fonafe.sistemagestionriesgoapi.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.fonafe.sistemagestionriesgoapi.dao.IProcesoDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dao.ISubProcesoDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOProceso;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOSubProceso;
import pe.gob.fonafe.sistemagestionriesgoapi.models.ProcesoBean;
import pe.gob.fonafe.sistemagestionriesgoapi.models.SubProcesoBean;
import pe.gob.fonafe.sistemagestionriesgoapi.service.IProcesoService;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
public class ProcesoServiceImpl implements IProcesoService {

    IProcesoDao iProcesoDao;
    ISubProcesoDao iSubProcesoDao;

    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    public ProcesoServiceImpl(IProcesoDao iProcesoDao, ISubProcesoDao iSubProcesoDao) {
        this.iProcesoDao = iProcesoDao;
        this.iSubProcesoDao = iSubProcesoDao;
    }

    @Override
    public DTOGenerico registrarProceso(DTOProceso procesoClass) {

        logger.info("Inicio de ProcesoServiceImpl - registrarProceso");

        DTOProceso dtoProceso;

        DTOGenerico dtoGeneric = null;
        DTOGenerico dtoSubGeneric = null;
        try {
            dtoGeneric = new DTOGenerico();
            dtoSubGeneric = new DTOGenerico();
            for (ProcesoBean proceso : procesoClass.getLstProcAdd()) {
                dtoGeneric = iProcesoDao.registrarProceso(proceso);
                logger.info("Registro Proceso Service - " + dtoGeneric.getCodigoResultado());
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String json = gson.toJson(proceso);
                logger.info("Registro Proceso Lst Add- " + proceso.getLstProcAdd().size());
                if (proceso.getLstProcAdd().size() > 0) {

                    for (SubProcesoBean subproceso : proceso.getLstProcAdd()) {
                        logger.info("Registro SubProceso Lst Add- " + subproceso.getDeSubProceso() + " IdC " + dtoGeneric.getCodigoResultado().longValue());
                        subproceso.setIdProceso(dtoGeneric.getCodigoResultado().longValue());
                        dtoSubGeneric = iSubProcesoDao.registrarSubProceso(subproceso);

                    }

                }

            }
        } catch (Exception ex) {
            ;
            logger.error("Error " + ex.getMessage());
        }

        logger.info("Fin de ProcesoServiceImpl - registrarProceso");
        return dtoGeneric;

    }

    @Override
    public DTOProceso obtenerProceso(Long idProceso) {

        logger.info("Inicio ProcesoServiceImpl - obtenerProceso");

        DTOProceso dtoProceso;

        try {
            dtoProceso = iProcesoDao.obtenerProceso(idProceso);
        } catch (Exception ex) {
            dtoProceso = new DTOProceso();
            logger.error(ex.getMessage());
        }

        logger.info("Fin ProcesoServiceImpl - obtenerProceso");
        return dtoProceso;
    }

    @Override
    public List<DTOProceso> listarProceso(Long idEmpresa,Long idTipoMatriz) {

        logger.info("Inicio ProcesoServiceImpl - listarProceso");

        List<DTOProceso> listaProcesos;
        List<DTOSubProceso> listaSubProcesos;

        try {
            listaProcesos = iProcesoDao.listarProcesos(idEmpresa, idTipoMatriz);

            if (idTipoMatriz == SNConstantes.PROCESO_MATRIZ){

                for (DTOProceso proceso : listaProcesos) {
                    listaSubProcesos = iSubProcesoDao.listarSubProcesos(proceso.getIdProceso());
                    proceso.setLstProc(listaSubProcesos);
                }

            }

        } catch (Exception ex) {
            listaProcesos = new ArrayList<>();
            logger.error(ex.getMessage());
        }

        logger.info("Fin ProcesoServiceImpl - listarProceso");
        return listaProcesos;
    }

    @Override
    public List<DTOProceso> listaProcesosMatriz(Long idProcesoMatriz) {
        logger.info("Inicio ProcesoServiceImpl - listaProcesosMatriz");

        List<DTOProceso> listaProcesos;
        List<DTOSubProceso> listaSubProcesos;

        try {
            listaProcesos = iProcesoDao.listaProcesosMatriz(idProcesoMatriz);

            if (!listaProcesos.isEmpty()){

                for (DTOProceso proceso : listaProcesos) {
                    listaSubProcesos = iSubProcesoDao.listarSubProcesos(proceso.getIdProceso());
                    proceso.setLstProc(listaSubProcesos);
                }

            }

        } catch (Exception ex) {
            listaProcesos = new ArrayList<>();
            logger.error(ex.getMessage());
        }

        logger.info("Fin ProcesoServiceImpl - listaProcesosMatriz");
        return listaProcesos;
    }

    @Override
    public DTOProceso actualizarProceso(ProcesoBean procesoBean) {


        logger.info("Inicio ProcesoServiceImpl - actualizaProceso");

        DTOProceso dtoProceso = null;
        try {
            dtoProceso = new DTOProceso();

            dtoProceso = iProcesoDao.actualizarProceso(procesoBean);
        } catch (Exception ex) {
            dtoProceso = new DTOProceso();
            dtoProceso.setIdProceso(0L);
            dtoProceso.setCodigoMensaje(SNConstantes.DE_ERROR);
            logger.error(ex.getMessage());
        }

        logger.info("Fin ProcesoServiceImpl - actualizarProceso");
        return dtoProceso;

    }

    @Override
    public Byte anularProceso(ProcesoBean procesoBean) {

        logger.info("Inicio ProcesoServiceImpl - anularProceso");

        Byte indicadorAnularProceso;

        try {
            indicadorAnularProceso = iProcesoDao.anularProceso(procesoBean);
        } catch (Exception ex) {
            indicadorAnularProceso = 0;
            logger.error(ex.getMessage());
        }

        logger.info("Fin ProcesoServiceImpl - anularProceso");
        return indicadorAnularProceso;

    }

    @Override
    public DTOGenerico listarProcesosxMatriz(Long idEmpresa, Long idSede, Long matrizNivel, Long idTipoMatriz) {
        logger.info("Inicio ProcesoServiceImpl - listarProcesosxMatriz");

        DTOGenerico dtoGenerico = new DTOGenerico();

        try {
            dtoGenerico = iProcesoDao.listarProcesosxMatriz(idEmpresa, idSede, matrizNivel, idTipoMatriz);
        } catch (Exception ex) {
            dtoGenerico = new DTOGenerico();
            logger.error(ex.getMessage());
        }

        logger.info("Fin ProcesoServiceImpl - listarProcesosxMatriz");
        return dtoGenerico;
    }


}

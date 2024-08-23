/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.fonafe.sistemagestionriesgoapi.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.fonafe.sistemagestionriesgoapi.dao.ISubProcesoDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOSubProceso;
import pe.gob.fonafe.sistemagestionriesgoapi.models.SubProcesoBean;
import pe.gob.fonafe.sistemagestionriesgoapi.service.ISubProcesoService;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
public class SubProcesoServiceImpl implements ISubProcesoService {


    ISubProcesoDao iSubProcesoDao;

    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    public SubProcesoServiceImpl(ISubProcesoDao iSubProcesoDao) {
        this.iSubProcesoDao = iSubProcesoDao;
    }

    @Override
    public DTOGenerico registrarSubProceso(DTOSubProceso subprocesoBean) {

        logger.info("Inicio de SubprocesoServiceImpl - registrarProceso");

        DTOSubProceso dtoSubProceso;

        DTOGenerico dtoGeneric = null;
        try {
            dtoGeneric = new DTOGenerico();
            for (SubProcesoBean subproceso : subprocesoBean.getLstProcAdd()) {
                dtoGeneric = iSubProcesoDao.registrarSubProceso(subproceso);
            }
        } catch (Exception ex) {
            ;
            logger.error(ex.getMessage());
        }


        logger.info("Fin de SubProcesoServiceImpl - registrarSubProceso12");
        return dtoGeneric;
    }

    @Override
    public List<DTOSubProceso> listarSubProceso(Long idProceso) {

        logger.info("Inicio SubProcesoServiceImpl - listarSubProceso");

        List<DTOSubProceso> listaSubProcesos;

        try {
            listaSubProcesos = iSubProcesoDao.listarSubProcesos(idProceso);
        } catch (Exception ex) {
            listaSubProcesos = new ArrayList<>();
            logger.error(ex.getMessage());
        }

        logger.info("Fin SubProcesoServiceImpl - listarSubProceso");
        return listaSubProcesos;

    }

    @Override
    public DTOSubProceso actualizarSubProceso(SubProcesoBean subprocesoBean) {

        logger.info("Inicio SubProcesoServiceImpl - actualizaSubProceso");

        DTOSubProceso dtoProceso = null;
        try {
            dtoProceso = new DTOSubProceso();

            dtoProceso = iSubProcesoDao.actualizarSubProceso(subprocesoBean);
        } catch (Exception ex) {
            dtoProceso = new DTOSubProceso();
            dtoProceso.setIdProceso(0L);
            dtoProceso.setCodigoMensaje(SNConstantes.DE_ERROR);
            logger.error(ex.getMessage());
        }

        logger.info("Fin SubProcesoServiceImpl - actualizarSubProceso");
        return dtoProceso;

    }

    @Override
    public DTOGenerico registrarSubProceso(SubProcesoBean subprocesoBean) {

        DTOSubProceso dtoSubProceso;

        DTOGenerico dtoGeneric = null;
        try {
            dtoGeneric = new DTOGenerico();

            dtoGeneric = iSubProcesoDao.registrarSubProceso(subprocesoBean);

        } catch (Exception ex) {
            ;
            logger.error(ex.getMessage());
        }


        logger.info("Fin de SubProcesoServiceImpl - registrarSubProceso12");
        return dtoGeneric;

    }

}

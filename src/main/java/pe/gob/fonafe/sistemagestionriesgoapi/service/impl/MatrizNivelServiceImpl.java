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
import pe.gob.fonafe.sistemagestionriesgoapi.dao.IMatrizNivelDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOMatrizNivel;
import pe.gob.fonafe.sistemagestionriesgoapi.models.MatrizNivelBean;
import pe.gob.fonafe.sistemagestionriesgoapi.service.IMatrizNivelService;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class MatrizNivelServiceImpl implements IMatrizNivelService {

    IMatrizNivelDao iMatrizNivelDao;

    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    public MatrizNivelServiceImpl(IMatrizNivelDao iMatrizNivelDao) {
        this.iMatrizNivelDao = iMatrizNivelDao;
    }

    @Override
    public DTOMatrizNivel registrarMatriz(MatrizNivelBean matrizBean) {

        logger.info("Inicio de MatrizNivelServiceImpl - registrarMatriz");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonPeticion = gson.toJson(matrizBean);
        logger.info("Object a registrar Matriz : {}", jsonPeticion);

        DTOMatrizNivel dtoMatrizNivel;

        try {
            dtoMatrizNivel = iMatrizNivelDao.registrarMatriz(matrizBean);
        } catch (Exception ex) {
            dtoMatrizNivel = new DTOMatrizNivel();
            dtoMatrizNivel.setIdMatrizNivel(0L);
            dtoMatrizNivel.setCodigoMensaje(SNConstantes.DE_ERROR);
            logger.error(ex.getMessage());
        }

        logger.info("Fin de MatrizNivelServiceImpl - registrarMatrizNivel");
        return dtoMatrizNivel;
    }

    @Override
    public DTOMatrizNivel obtenerMatriz(Long idMatriz) {

        logger.info("Inicio MatrizNivelServiceImpl - obtenerMatrizNivel");

        DTOMatrizNivel dtoMatrizNivel;

        try {
            dtoMatrizNivel = iMatrizNivelDao.obtenerMatriz(idMatriz);
        } catch (Exception ex) {
            dtoMatrizNivel = new DTOMatrizNivel();
            logger.error(ex.getMessage());
        }

        logger.info("Fin MatriznivelServiceImpl - obtenerMatriz");
        return dtoMatrizNivel;
    }

    @Override
    public List<DTOMatrizNivel> listarMatriz(Long idEmpresa, Long idSede, Long idTipoMatriz) {

        logger.info("Inicio MatrizNivelServiceImpl - listarMatrices");

        List<DTOMatrizNivel> listaMatriz;


        try {
            listaMatriz = iMatrizNivelDao.listarMatriz(idEmpresa,idSede,idTipoMatriz);
        } catch (Exception ex) {
            listaMatriz = new ArrayList<>();
            logger.error(ex.getMessage());
        }

        logger.info("Fin MatrizNivelServiceImpl - listarMatrizNivel");
        return listaMatriz;
    }

    @Override
    public DTOMatrizNivel actualizarMatriz(MatrizNivelBean matrizNivelBean) {
        logger.info("Inicio MatrizNivelServiceImpl - actualizaMatriz");

        DTOMatrizNivel dtoMatrizNivel = null;
        try {
            dtoMatrizNivel = new DTOMatrizNivel();

            dtoMatrizNivel = iMatrizNivelDao.actualizarMatriz(matrizNivelBean);
        } catch (Exception ex) {
            dtoMatrizNivel = new DTOMatrizNivel();
            dtoMatrizNivel.setIdMatrizNivel(0L);
            dtoMatrizNivel.setCodigoMensaje(SNConstantes.DE_ERROR);
            logger.error(ex.getMessage());
        }

        logger.info("Fin MatrizNivelServiceImpl - actualizarMatriz");
        return dtoMatrizNivel;
    }

    @Override
    public Byte anularMatriz(MatrizNivelBean matrizBean) {
        logger.info("Inicio MatrizNivelServiceImpl - anularMatriz");

        Byte indicadorAnularMatriz;

        try {
            indicadorAnularMatriz = iMatrizNivelDao.anularMatriz(matrizBean);
        } catch (Exception ex) {
            indicadorAnularMatriz = 0;
            logger.error(ex.getMessage());
        }

        logger.info("Fin MatrizNivelServiceImpl - anularMatriz");
        return indicadorAnularMatriz;
    }

}

package pe.gob.fonafe.sistemagestionriesgoapi.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.fonafe.sistemagestionriesgoapi.dao.IGerenciaDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dao.ISedeDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGerencia;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOSede;
import pe.gob.fonafe.sistemagestionriesgoapi.models.GerenciaBean;
import pe.gob.fonafe.sistemagestionriesgoapi.models.SedeBean;
import pe.gob.fonafe.sistemagestionriesgoapi.service.ISedeService;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import java.util.List;

@Service
@Transactional
public class SedeServiceImpl implements ISedeService {

    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    final ISedeDao iSedeDao;

    final IGerenciaDao iGerenciaDao;

    public SedeServiceImpl(ISedeDao iSedeDao, IGerenciaDao iGerenciaDao) {
        this.iSedeDao = iSedeDao;
        this.iGerenciaDao = iGerenciaDao;
    }

    @Override
    public DTOGenerico registrarSede(DTOSede dtoSede) {
        logger.info("Inicio de SedeServiceImpl - registrarSede");

        DTOGenerico sede = null;
        DTOGerencia gerencia = null;

        try {

            sede = new DTOGenerico();
            gerencia = new DTOGerencia();

            for (SedeBean sedeBean : dtoSede.getListaSedes()) {
                sede = iSedeDao.registrarSede(sedeBean);

                if (sedeBean.getListaGerencias().size() > 0) {

                    for (GerenciaBean gerenciaBean : sedeBean.getListaGerencias()) {
                        gerenciaBean.setIdSede(sede.getIdGenerico());
                        gerencia = iGerenciaDao.registrarGerencia(gerenciaBean);
                    }

                }
            }

        } catch (Exception ex) {
            sede = new DTOGenerico();
            sede.setIdGenerico(0L);
            sede.setCodigoResultado(SNConstantes.CODIGO_ERROR);
            logger.error(ex.getMessage());
        }

        logger.info("Fin de SedeServiceImpl - registrarSede");
        return sede;
    }

    @Override
    public DTOSede obtenerSede(Long idSede) {
        logger.info("Inicio SedeServiceImpl - obtenerSede");

        DTOSede dtoSede;

        try {
            dtoSede = iSedeDao.obtenerSede(idSede);
        } catch (Exception ex) {
            dtoSede = new DTOSede();
            logger.error(ex.getMessage());
        }

        logger.info("Fin SedeServiceImpl - obtenerSede");
        return dtoSede;
    }

    @Override
    public DTOGenerico listarSedes(Long idEmpresa) {
        logger.info("Inicio SedeServiceImpl - listarSedes");

        DTOGenerico dtoGenerico;

        DTOGenerico listaGerencias;

        List<DTOSede> listaSedes;

        try {
            dtoGenerico = iSedeDao.listarSedes(idEmpresa);

            listaSedes = dtoGenerico.getListado();

            for (DTOSede sede : listaSedes) {
                listaGerencias = iGerenciaDao.listarGerencias(idEmpresa, sede.getIdSede());
                sede.setListaGerencias(listaGerencias.getListado());
            }

            dtoGenerico.setListado(listaSedes);

        } catch (Exception ex) {
            dtoGenerico = new DTOGenerico();
            logger.error(ex.getMessage());
        }

        logger.info("Fin SedeServiceImpl - listarSede");
        return dtoGenerico;
    }

    @Override
    public DTOGenerico actualizarSede(DTOSede dtoSede) {
        logger.info("Inicio SedeServiceImpl - actualizarSede");

        DTOGenerico sedeRegistro = null;
        DTOGenerico sedeActualizo = null;
        DTOGerencia gerenciaRegistro = null;
        DTOGerencia gerenciaActualizo = null;

        try {

            sedeRegistro = new DTOGenerico();
            sedeActualizo = new DTOGenerico();
            gerenciaRegistro = new DTOGerencia();
            gerenciaActualizo = new DTOGerencia();

            for (SedeBean sedeBean : dtoSede.getListaSedes()) {
                if (sedeBean.getIdSede() == null) {
                    sedeRegistro = iSedeDao.registrarSede(sedeBean);
                } else {
                    sedeActualizo = iSedeDao.actualizarSede(sedeBean);
                }

                if (sedeBean.getListaGerencias().size() > 0) {

                    for (GerenciaBean gerenciaBean : sedeBean.getListaGerencias()) {
                        if (gerenciaBean.getIdGerencia() == null) {
                            gerenciaBean.setIdSede(sedeBean.getIdSede());
                            gerenciaRegistro = iGerenciaDao.registrarGerencia(gerenciaBean);
                        } else {
                            gerenciaActualizo = iGerenciaDao.actualizarGerencia(gerenciaBean);
                        }
                    }

                }
            }
        } catch (Exception ex) {
            dtoSede = new DTOSede();
            dtoSede.setIdSede(0L);
            dtoSede.setCodigoResultado(SNConstantes.CODIGO_ERROR);
            logger.error(ex.getMessage());
        }

        logger.info("Fin SedeServiceImpl - actualizarSede");
        return sedeActualizo;
    }

    @Override
    public Byte anularSede(SedeBean sedeBean) {
        logger.info("Inicio SedeServiceImpl - anularSede");

        Byte indicadorAnularSede;

        try {
            indicadorAnularSede = iSedeDao.anularSede(sedeBean);
        } catch (Exception ex) {
            indicadorAnularSede = 0;
            logger.error(ex.getMessage());
        }

        logger.info("Fin SedeServiceImpl - anularSede");
        return indicadorAnularSede;
    }
}

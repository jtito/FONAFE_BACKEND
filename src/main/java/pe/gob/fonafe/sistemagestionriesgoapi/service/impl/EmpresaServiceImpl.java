package pe.gob.fonafe.sistemagestionriesgoapi.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.fonafe.sistemagestionriesgoapi.dao.IEmpresaDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOEmpresa;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.models.EmpresaBean;
import pe.gob.fonafe.sistemagestionriesgoapi.service.IEmpresaService;
import pe.gob.fonafe.sistemagestionriesgoapi.service.ISedeService;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

@Service
@Transactional
public class EmpresaServiceImpl implements IEmpresaService {

    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    final IEmpresaDao iEmpresaDao;

    final ISedeService iSedeService;

    public EmpresaServiceImpl(IEmpresaDao iEmpresaDao, ISedeService iSedeService) {
        this.iEmpresaDao = iEmpresaDao;
        this.iSedeService = iSedeService;
    }

    @Override
    public DTOEmpresa registrarEmpresa(EmpresaBean empresaBean) {
        logger.info("Inicio de EmpresaServiceImpl - registrarEmpresa");

        DTOEmpresa dtoEmpresa;

        try {

            empresaBean.setNombreCortoEmpresa(empresaBean.getNombreCortoEmpresa().toUpperCase());

            dtoEmpresa = iEmpresaDao.registrarEmpresa(empresaBean);
        } catch (Exception ex) {
            dtoEmpresa = new DTOEmpresa();
            dtoEmpresa.setIdEmpresa(0L);
            dtoEmpresa.setCodigoResultado(SNConstantes.CODIGO_ERROR);
            logger.error(ex.getMessage());
        }

        logger.info("Fin de EmpresaServiceImpl - registrarEmpresa");
        return dtoEmpresa;
    }

    @Override
    public DTOEmpresa obtenerEmpresa(Long idEmpresa) {
        logger.info("Inicio EmpresaServiceImpl - obtenerEmpresa");

        DTOEmpresa dtoEmpresa;
        DTOGenerico listaSedes;

        try {
            dtoEmpresa = iEmpresaDao.obtenerEmpresa(idEmpresa);
            listaSedes = iSedeService.listarSedes(idEmpresa);
            dtoEmpresa.setListaSedes(listaSedes.getListado());
        } catch (Exception ex) {
            dtoEmpresa = new DTOEmpresa();
            logger.error(ex.getMessage());
        }

        logger.info("Fin EmpresaServiceImpl - obtenerEmpresa");
        return dtoEmpresa;
    }

    @Override
    public DTOGenerico listarEmpresas() {
        logger.info("Inicio EmpresaServiceImpl - listarEmpresas");

        DTOGenerico dtoGenerico;

        try {
            dtoGenerico = iEmpresaDao.listarEmpresas();
        } catch (Exception ex) {
            dtoGenerico = new DTOGenerico();
            logger.error(ex.getMessage());
        }

        logger.info("Fin EmpresaServiceImpl - listarEmpresas");
        return dtoGenerico;
    }

    @Override
    public DTOEmpresa actualizarEmpresa(EmpresaBean empresaBean) {
        logger.info("Inicio EmpresaServiceImpl - actualizaEmpresa");

        DTOEmpresa dtoEmpresa = null;

        try {

            empresaBean.setNombreCortoEmpresa(empresaBean.getNombreCortoEmpresa().toUpperCase());

            dtoEmpresa = new DTOEmpresa();

            dtoEmpresa = iEmpresaDao.actualizarEmpresa(empresaBean);
        } catch (Exception ex) {
            dtoEmpresa = new DTOEmpresa();
            dtoEmpresa.setIdEmpresa(0L);
            dtoEmpresa.setCodigoResultado(SNConstantes.CODIGO_ERROR);
            logger.error(ex.getMessage());
        }

        logger.info("Fin EmpresaServiceImpl - actualizarEmpresa");
        return dtoEmpresa;
    }

    @Override
    public Byte anularEmpresa(EmpresaBean empresaBean) {
        logger.info("Inicio EmpresaServiceImpl - anularEmpresa");

        Byte indicadorAnularEmpresa;

        try {
            indicadorAnularEmpresa = iEmpresaDao.anularEmpresa(empresaBean);
        } catch (Exception ex) {
            indicadorAnularEmpresa = 0;
            logger.error(ex.getMessage());
        }

        logger.info("Fin EmpresaServiceImpl - anularEmpresa");
        return indicadorAnularEmpresa;
    }
}

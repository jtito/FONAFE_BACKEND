package pe.gob.fonafe.sistemagestionriesgoapi.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.fonafe.sistemagestionriesgoapi.dao.IResponsableDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOUsuario;
import pe.gob.fonafe.sistemagestionriesgoapi.models.UsuarioBean;
import pe.gob.fonafe.sistemagestionriesgoapi.service.IResponsableService;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class ResponsableServiceImpl implements IResponsableService {

    final IResponsableDao iResponsableDao;

    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    public ResponsableServiceImpl(IResponsableDao iResponsableDao) {
        this.iResponsableDao = iResponsableDao;
    }

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public DTOUsuario registrarResponsable(UsuarioBean usuarioBean) {
        logger.info("Inicio de ResponsableServiceImpl - registrarResponsable");

        DTOUsuario dtoUsuario;

        try {

            String passwordBcrypt = passwordEncoder.encode(usuarioBean.getPassword());
            usuarioBean.setPassword(passwordBcrypt);

            String[] textoSeparadoCorreo = usuarioBean.getCorreo().split("@");
            usuarioBean.setUsername(usuarioBean.getCorreo());
//        usuarioBean.setUsername(textoSeparadoCorreo[0]);
            usuarioBean.setNombre(usuarioBean.getNombre().toUpperCase());
            usuarioBean.setApellidoPaterno(usuarioBean.getApellidoPaterno().toUpperCase());
            usuarioBean.setApellidoMaterno(usuarioBean.getApellidoMaterno().toUpperCase());

            dtoUsuario = iResponsableDao.registrarResponsable(usuarioBean);

            if (dtoUsuario.getDescripcionResultado().equals("OK") && !usuarioBean.getProcesos().isEmpty()){
                for (Long usuarioProcesos: usuarioBean.getProcesos()){
                    iResponsableDao.registrarUsuarioProceso(usuarioProcesos.longValue(), usuarioBean, dtoUsuario);
                }
            }

        } catch (Exception ex) {
            dtoUsuario = new DTOUsuario();
            dtoUsuario.setIdUsuario(0L);
            dtoUsuario.setCodigoResultado(SNConstantes.CODIGO_ERROR);
            logger.error(ex.getMessage());
        }

        logger.info("Fin de ResponsableServiceImpl - registrarResponsable");
        return dtoUsuario;
    }

    @Override
    public DTOUsuario obtenerResponsable(Long idUsuario) {
        logger.info("Inicio ResponsableServiceImpl - obtenerResponsable");

        DTOUsuario dtoUsuario;

        try {
            dtoUsuario = iResponsableDao.obtenerResponsable(idUsuario);

            iResponsableDao.obtenerUsuarioProceso(idUsuario, dtoUsuario);
        } catch (Exception ex) {
            dtoUsuario = new DTOUsuario();
            logger.error(ex.getMessage());
        }

        logger.info("Fin ResponsableServiceImpl - obtenerResponsable");
        return dtoUsuario;
    }

    @Override
    public DTOGenerico listarResponsables(Long idEmpresa, Long idSede) {
        logger.info("Inicio ResponsableServiceImpl - listarResponsables");

        List<DTOUsuario> listaResponsables;

        DTOGenerico dtoGenerico = new DTOGenerico();

        try {
            dtoGenerico = iResponsableDao.listarResponsables(idEmpresa, idSede);
        } catch (Exception ex) {
            dtoGenerico = new DTOGenerico();
            logger.error(ex.getMessage());
        }

        logger.info("Fin ResponsableServiceImpl - listarResponsables");
        return dtoGenerico;
    }

    @Override
    public DTOUsuario actualizarResponsable(UsuarioBean usuarioBean) {
        logger.info("Inicio ResponsableServiceImpl - actualizaResponsable");

        DTOUsuario dtoUsuario = null;

        try {

            if (usuarioBean.getPassword() != "" ){
                String passwordBcrypt = passwordEncoder.encode(usuarioBean.getPassword());
                usuarioBean.setPassword(passwordBcrypt);
            }

            String[] textoSeparadoCorreo = usuarioBean.getCorreo().split("@");
            usuarioBean.setUsername(usuarioBean.getCorreo());
//        usuarioBean.setUsername(textoSeparadoCorreo[0]);
            usuarioBean.setNombre(usuarioBean.getNombre().toUpperCase());
            usuarioBean.setApellidoPaterno(usuarioBean.getApellidoPaterno().toUpperCase());
            usuarioBean.setApellidoMaterno(usuarioBean.getApellidoMaterno().toUpperCase());

            dtoUsuario = new DTOUsuario();

            dtoUsuario = iResponsableDao.actualizarResponsable(usuarioBean);

            if (dtoUsuario.getDescripcionResultado().equals("OK") && !usuarioBean.getProcesos().isEmpty()){
                for (Long usuarioProcesos: usuarioBean.getProcesos()){
                    iResponsableDao.registrarUsuarioProceso(usuarioProcesos.longValue(), usuarioBean, dtoUsuario);
                }
            }

        } catch (Exception ex) {
            dtoUsuario = new DTOUsuario();
            dtoUsuario.setIdUsuario(0L);
            dtoUsuario.setCodigoResultado(SNConstantes.CODIGO_ERROR);
            logger.error(ex.getMessage());
        }

        logger.info("Fin ResponsableServiceImpl - actualizarResponsable");
        return dtoUsuario;
    }

    @Override
    public Byte anularResponsable(UsuarioBean usuarioBean) {
        logger.info("Inicio ResponsableServiceImpl - anularResponsable");

        Byte indicadorAnularResponsable;

        try {
            indicadorAnularResponsable = iResponsableDao.anularResponsable(usuarioBean);
        } catch (Exception ex) {
            indicadorAnularResponsable = 0;
            logger.error(ex.getMessage());
        }

        logger.info("Fin ResponsableServiceImpl - anularResponsable");
        return indicadorAnularResponsable;
    }

    @Override
    public DTOGenerico listarPerfiles() {
        logger.info("Inicio ResponsableServiceImpl - listarPerfiles");

        DTOGenerico dtoGenerico;

        try {
            dtoGenerico = iResponsableDao.listarPerfiles();
        } catch (Exception ex) {
            dtoGenerico = new DTOGenerico();
            logger.error(ex.getMessage());
        }

        logger.info("Fin ResponsableServiceImpl - listarPerfiles");
        return dtoGenerico;
    }

    @Override
    public DTOGenerico actualizarPassword(Long idUsuario, String actPasswordEncript, String actualPassword, String newPassword) {
        logger.info("Inicio ResponsableServiceImpl - actualizarPassword");

        DTOGenerico dtoGenerico;

        try {

            if (passwordEncoder.matches(actualPassword, actPasswordEncript)){
                String passwordBcrypt = passwordEncoder.encode(newPassword);
                dtoGenerico = iResponsableDao.actualizarPassword(idUsuario, passwordBcrypt);

            }else{
                dtoGenerico = new DTOGenerico();
                dtoGenerico.setCodigoResultado(new BigDecimal(2));
            }

        } catch (Exception ex) {
            dtoGenerico = new DTOGenerico();
            logger.error(ex.getMessage());
        }

        logger.info("Fin ResponsableServiceImpl - actualizarPassword");
        return dtoGenerico;
    }
}

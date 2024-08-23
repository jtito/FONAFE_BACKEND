package pe.gob.fonafe.sistemagestionriesgoapi.dao;

import org.springframework.dao.DataAccessException;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOMatrizEvidenciaControl;
import pe.gob.fonafe.sistemagestionriesgoapi.models.MatrizEvidenciaControlBean;

public interface IMatrizEvidenciaControlDao {

    DTOMatrizEvidenciaControl registrarMatrizEvidenciaControl(MatrizEvidenciaControlBean matrizEvidenciaControlBean) throws DataAccessException;
    DTOGenerico listarMatrizEvidenciaControl(MatrizEvidenciaControlBean matrizEvidenciaControlBean) throws DataAccessException;
    DTOMatrizEvidenciaControl anularMatrizEvidenciaControl(MatrizEvidenciaControlBean matrizEvidenciaControlBean) throws DataAccessException;
}

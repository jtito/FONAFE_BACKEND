package pe.gob.fonafe.sistemagestionriesgoapi.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOMatrizEvidenciaControl;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOFileResponse;
import pe.gob.fonafe.sistemagestionriesgoapi.models.MatrizEvidenciaControlBean;

import java.io.IOException;

public interface IMatrizEvidenciaControlService {

    DTOFileResponse registrarMatrizEvidenciaControl(MultipartFile file, MatrizEvidenciaControlBean matrizEvidenciaControlBean);
    DTOGenerico listarMatrizEvidenciaControl(MatrizEvidenciaControlBean matrizEvidenciaControlBean);
    boolean anularMatrizEvidenciaControl(MatrizEvidenciaControlBean matrizEvidenciaControlBean);
    ByteArrayResource downloadMatrizEvidenciaControl(String fileName) throws IOException;
}

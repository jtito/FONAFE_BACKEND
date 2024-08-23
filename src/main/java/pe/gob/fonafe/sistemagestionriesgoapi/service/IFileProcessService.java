/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.fonafe.sistemagestionriesgoapi.service;

import org.springframework.web.multipart.MultipartFile;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOFileResponse;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOFileResponseDetalleMatriz;

/**
 *
 * @author CANVIA
 */
public interface IFileProcessService {
    DTOFileResponseDetalleMatriz processFile(MultipartFile file,Long idEmpresa,Long idSede,Long idTipo,Long idPeriodo);
}

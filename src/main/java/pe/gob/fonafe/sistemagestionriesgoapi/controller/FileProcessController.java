/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.fonafe.sistemagestionriesgoapi.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOFileResponse;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOFileResponseDetalleMatriz;
import pe.gob.fonafe.sistemagestionriesgoapi.service.IFileProcessService;

/**
 *
 * @author CANVIA
 */
@RestController
@RequestMapping("/gestionriesgo/matrizRiesgo/upload")
public class FileProcessController {
    
    
    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    final IFileProcessService fileProcessService;
    
    public FileProcessController(IFileProcessService fileProcessService) {
        this.fileProcessService = fileProcessService;
    }
    
    @PostMapping("/file")
    public DTOFileResponseDetalleMatriz handleFileUpload(@RequestPart(value = "file", required = true) MultipartFile file,
                        @RequestParam("idEmpresa") Long idEmpresa,@RequestParam("idSede") Long idSede,
                        @RequestParam("idTipo") Long idTipo,
                        @RequestParam("idPeriodo") Long idPeriodo,
			RedirectAttributes redirectAttributes) {

	DTOFileResponseDetalleMatriz response=fileProcessService.processFile(file, idEmpresa, idSede, idTipo,idPeriodo);
        System.out.println("FILE UPLOAD "+file.getOriginalFilename()+ " idEmpresa "+idEmpresa);

	return response;
    }

    
}

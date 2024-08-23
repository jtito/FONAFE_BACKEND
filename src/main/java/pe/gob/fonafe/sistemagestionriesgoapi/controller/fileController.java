package pe.gob.fonafe.sistemagestionriesgoapi.controller;


import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import pe.gob.fonafe.sistemagestionriesgoapi.service.FileService;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static java.util.Objects.isNull;

@Controller
@Slf4j
@RequestMapping("/file")
@AllArgsConstructor(onConstructor = @__(@Autowired))
@ApiOperation(value = "Endpoint para subir un archivo", notes = "Subir archivo")
public class fileController {

    private FileService fileService;


    @PostMapping("/{idUser}")
    public ResponseEntity<?> uploadFile(@RequestPart(value = "file") MultipartFile file, @PathVariable("idUser") Long idUser) {
        if (file == null || file.isEmpty() || Objects.requireNonNull(file.getOriginalFilename()).isEmpty()) {
            throw new MultipartException("File null");
        }
        return ResponseEntity.ok(fileService.uploadFile(file, idUser));
    }

    @DeleteMapping("/{idUser}/{fileName}")
    public ResponseEntity<?> deleteFile(@PathVariable("fileName") String fileName, @PathVariable("idUser") Long idUser) {
        if (isNull(fileName) || fileName.isEmpty() || isNull(idUser)) {
            throw new RuntimeException("Parameters null");
        }
        return ResponseEntity.ok(fileService.deleteFile(fileName, idUser));
    }


    @GetMapping("/{idUser}/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("fileName") String fileName, @PathVariable("idUser") Long idUser) throws IOException {
        if (isNull(fileName) || fileName.isEmpty() || isNull(idUser)) {
            throw new RuntimeException("Parameters null");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=".concat(fileName));
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(fileService.downloadFile(fileName, idUser));
    }
}

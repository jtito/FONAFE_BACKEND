package pe.gob.fonafe.sistemagestionriesgoapi.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    Object uploadFile(MultipartFile file, Long idUser);

    Object deleteFile(String fileName, Long idUser);

    ByteArrayResource downloadFile(String fileName, Long idUser) throws IOException;
}

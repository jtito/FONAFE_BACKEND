package pe.gob.fonafe.sistemagestionriesgoapi.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import pe.gob.fonafe.sistemagestionriesgoapi.dao.IResponsableDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOFile;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOUsuario;
import pe.gob.fonafe.sistemagestionriesgoapi.models.UsuarioBean;
import pe.gob.fonafe.sistemagestionriesgoapi.service.FileService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

    private IResponsableDao iResponsableDao;


    public FileServiceImpl(IResponsableDao iResponsableDao) {
        this.iResponsableDao = iResponsableDao;
    }

    @Value("${file.location.path}")
    private String pathFileLocation;

    @Override
    public Object uploadFile(MultipartFile file, Long idUser) {


        try {
            StringBuilder builder = new StringBuilder();
            builder.append(pathFileLocation);
            builder.append(File.separator);

            File newPath = new File(builder.toString());
            if (!newPath.exists()) {
                newPath.mkdirs();
            }

            String newName = getCurrentDate().concat("_").concat(Objects.requireNonNull(file.getOriginalFilename()));
            builder.append(newName);

            List<String> fileNameList = Arrays.asList(file.getOriginalFilename().split("\\."));

            File outputFile = new File(builder.toString());
            file.transferTo(outputFile);
            UsuarioBean usuarioBean = new UsuarioBean(idUser, outputFile.getName());
            DTOUsuario dtoUsuario = iResponsableDao.actualizarArchivoResponsable(usuarioBean);
            return DTOFile.builder()
                    .status("OK")
                    .name(outputFile.getName())
                    .idUser(dtoUsuario.getIdUsuario())
                    .routePath(outputFile.getName())
                    .size(String.valueOf(file.getSize()))
                    .type(fileNameList.get(fileNameList.size() - 1))
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new MultipartException(e.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error(ex.getMessage());
            throw new MultipartException(ex.getMessage());
        }
    }

    @Override
    public Object deleteFile(String fileName, Long idUser) {
        StringBuilder builder = new StringBuilder();
        builder.append(pathFileLocation);
        builder.append(File.separator);
        builder.append(fileName);
        UsuarioBean usuarioBean = new UsuarioBean(idUser, "");
        DTOUsuario dtoUsuario = iResponsableDao.actualizarArchivoResponsable(usuarioBean);
        File myFile = new File(builder.toString());
        if (myFile.delete()) {
            System.out.println("Deleted the file: " + myFile.getName());
            return DTOFile.builder().status("OK").build();
        } else {
            System.out.println("Failed to delete the file.");
            throw new MultipartException("Error in delete process");
        }
    }

    @Override
    public ByteArrayResource downloadFile(String fileName, Long idUser) throws IOException {
        Path path = Paths.get(pathFileLocation.concat(File.separator).concat(fileName));
        return new ByteArrayResource(Files.readAllBytes(path));
    }

    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(new Date());
    }

}

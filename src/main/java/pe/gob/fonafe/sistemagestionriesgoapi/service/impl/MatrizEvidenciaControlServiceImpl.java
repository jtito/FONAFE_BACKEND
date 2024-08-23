package pe.gob.fonafe.sistemagestionriesgoapi.service.impl;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import pe.gob.fonafe.sistemagestionriesgoapi.dao.IMatrizEvidenciaControlDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.*;
import pe.gob.fonafe.sistemagestionriesgoapi.models.MatrizEvidenciaControlBean;
import pe.gob.fonafe.sistemagestionriesgoapi.service.IMatrizEvidenciaControlService;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;



@Service
@Transactional
public class MatrizEvidenciaControlServiceImpl implements IMatrizEvidenciaControlService {

    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");

    @Value("${rutaRepositorio}")
    private String pathFileLocation;

    @Value("${rutaRepositorioDescarga}")
    private String pathFileLocationDownload;

    final IMatrizEvidenciaControlDao iMatrizEvidenciaControlDao;

    public MatrizEvidenciaControlServiceImpl(IMatrizEvidenciaControlDao iMatrizEvidenciaControlDao) {
        this.iMatrizEvidenciaControlDao = iMatrizEvidenciaControlDao;
    }

    @Override
    public DTOFileResponse registrarMatrizEvidenciaControl(MultipartFile file, MatrizEvidenciaControlBean matrizEvidenciaControlBean) {
        logger.info("Inicio de MatrizEvidenciaControlServiceImpl - DTOMatrizEvidenciaControl");

        DTOMatrizEvidenciaControl dtoMatrizEvidenciaControl;

        try {
            StringBuilder builder = new StringBuilder();
            builder.append(pathFileLocation);
            builder.append(File.separator);

            File newPath = new File(builder.toString());
            if (!newPath.exists()) {
                newPath.mkdirs();
            }

            /*String[] arrNombreOriginal = file.getOriginalFilename().split(".");
            String extension = arrNombreOriginal[arrNombreOriginal.length-1];*/

            String extension = FilenameUtils.getExtension(file.getOriginalFilename());

//            String newName = getCurrentDate().concat("_").concat(Objects.requireNonNull(file.getOriginalFilename()));
            String newName = "." + extension;
//            builder.append(newName);

            //List<String> fileNameList = Arrays.asList(file.getOriginalFilename().split("\\."));

            matrizEvidenciaControlBean.setRutaArchivo(pathFileLocation);
            matrizEvidenciaControlBean.setNombreArchivo(newName);
            matrizEvidenciaControlBean.setNombreOriginalArchivo(file.getOriginalFilename());
            matrizEvidenciaControlBean.setPesoArchivo(file.getSize());

            dtoMatrizEvidenciaControl = iMatrizEvidenciaControlDao.registrarMatrizEvidenciaControl(matrizEvidenciaControlBean);

            String fileNewName = dtoMatrizEvidenciaControl.getIdEvidencia() + newName;
            builder.append(fileNewName);
            File outputFile = new File(builder.toString());
            file.transferTo(outputFile);

            logger.info("Fin de MatrizEvidenciaControlServiceImpl - DTOMatrizEvidenciaControl");
            return DTOFileResponse.builder()
                    .idFile(dtoMatrizEvidenciaControl.getIdEvidencia().toString())
                    .originalFileName(file.getOriginalFilename())
                    //.fileName(newName)
                    .fileName(fileNewName)
                    .build();
        }catch (IOException e){
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new MultipartException(e.getMessage());
        }
        catch (Exception ex){
            dtoMatrizEvidenciaControl = new DTOMatrizEvidenciaControl();
            dtoMatrizEvidenciaControl.setIdEvidencia(0L);
            dtoMatrizEvidenciaControl.setCodigoResultado(SNConstantes.CODIGO_ERROR);
            ex.printStackTrace();
            logger.error(ex.getMessage());
            throw new MultipartException(ex.getMessage());
        }

    }

    @Override
    public DTOGenerico listarMatrizEvidenciaControl(MatrizEvidenciaControlBean matrizEvidenciaControlBean) {
        logger.info("Inicio de MatrizEvidenciaControlServiceImpl - listarMatrizEvidenciaControl");
        DTOGenerico dtoGenerico;
        try {
            dtoGenerico = iMatrizEvidenciaControlDao.listarMatrizEvidenciaControl(matrizEvidenciaControlBean);
        } catch (Exception ex) {
            dtoGenerico = new DTOGenerico();
            logger.error(ex.getMessage());
        }
        logger.info("Fin de MatrizEvidenciaControlServiceImpl - listarMatrizEvidenciaControl");
        return dtoGenerico;
    }

    @Override
    public boolean anularMatrizEvidenciaControl(MatrizEvidenciaControlBean matrizEvidenciaControlBean) {

        DTOMatrizEvidenciaControl dtoMatrizEvidenciaControl = iMatrizEvidenciaControlDao.anularMatrizEvidenciaControl(matrizEvidenciaControlBean);

        return deleteFileByName(matrizEvidenciaControlBean.getNombreArchivo());
    }

    @Override
    public ByteArrayResource downloadMatrizEvidenciaControl(String fileName) throws IOException {
        Path path = Paths.get(pathFileLocation.concat(File.separator).concat(fileName));
        return new ByteArrayResource(Files.readAllBytes(path));
    }

    private boolean deleteFileByName(String fileName) {
        StringBuilder builder = new StringBuilder();
        builder.append(pathFileLocation);
        builder.append(File.separator);
        builder.append(fileName);
        File myFile = new File(builder.toString());
        if (myFile.delete()) {
            System.out.println("Deleted the file: " + myFile.getName());
            return true;
        } else {
            System.out.println("Failed to delete the file.");
            throw new MultipartException("Error in delete process");
        }
    }

    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(new Date());
    }
}

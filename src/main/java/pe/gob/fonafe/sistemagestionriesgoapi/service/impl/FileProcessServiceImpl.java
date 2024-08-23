/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.fonafe.sistemagestionriesgoapi.service.impl;

/**
 *
 * @author CANVIA
 */
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pe.gob.fonafe.sistemagestionriesgoapi.dao.IDatosGeneralesDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOEncuesta;
import pe.gob.fonafe.sistemagestionriesgoapi.models.EncuestaBean;
import pe.gob.fonafe.sistemagestionriesgoapi.service.IEncuestaService;
import pe.gob.fonafe.sistemagestionriesgoapi.service.IPreguntaService;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import pe.gob.fonafe.sistemagestionriesgoapi.dao.IEncuestaDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dao.IGerenciaDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dao.IParametroDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dao.IProcesoDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dao.ISubProcesoDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTODetalleMatrizRiesgo;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOFileResponse;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOFileResponseDetalleMatriz;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGerencia;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOParametro;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOProceso;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOSubProceso;
import pe.gob.fonafe.sistemagestionriesgoapi.service.IFileProcessService;

@Service
@Transactional
public class FileProcessServiceImpl implements IFileProcessService{

    private static final Logger logger = LogManager.getLogger("GESTION_RIESGO_API");
    final IParametroDao iParametroDao;
    final IDatosGeneralesDao iDatosGeneralesDao;
    final IGerenciaDao iGerenciaDao;
    final IProcesoDao iProcesoDao;
    final ISubProcesoDao iSubProcesoDao;
    
     public FileProcessServiceImpl(IProcesoDao iProcesoDao, IGerenciaDao iGerenciaDao,
             IParametroDao iParametroDao,IDatosGeneralesDao iDatosGeneralesDao,ISubProcesoDao iSubProcesoDao) {
        this.iParametroDao = iParametroDao;
        this.iDatosGeneralesDao = iDatosGeneralesDao;
        this.iGerenciaDao = iGerenciaDao;
        this.iProcesoDao= iProcesoDao;
        this.iSubProcesoDao = iSubProcesoDao;
    }
    
    @Override
    public DTOFileResponseDetalleMatriz processFile(MultipartFile file,Long idEmpresa,Long idSede,Long idTipo,Long idPeriodo) {
        
        List<DTODetalleMatrizRiesgo> listaDetalle=new ArrayList<>();
        DTOGenerico dtoMatrizNiv = iDatosGeneralesDao.listarParametrosxCodigo("MATR0000");
        DTOGenerico dtoGerencias = iGerenciaDao.listarGerencias(idEmpresa, idSede);
        DTOGenerico dtoOrigenRie = iDatosGeneralesDao.listarParametrosxCodigo("ORIG0000");
        DTOGenerico dtoFrecRie   = iDatosGeneralesDao.listarParametrosxCodigo("FRIESG0000");
        DTOGenerico dtoTipoRie   = iDatosGeneralesDao.listarParametrosxCodigo("TRIESG0000");
        DTOGenerico dtoInEficaz   = iDatosGeneralesDao.listarParametrosxCodigo("PLACC0000");
        DTOGenerico dtoOpoCont   = iDatosGeneralesDao.listarParametrosxCodigo("OPOR0000");
        DTOGenerico dtoAutCont   = iDatosGeneralesDao.listarParametrosxCodigo("AUTO0000");
        DTOGenerico dtoEstResp   = iDatosGeneralesDao.listarParametrosxCodigo("ESTP0000");
        DTOGenerico dtoEstPlan   = iDatosGeneralesDao.listarParametrosxCodigo("ESPLA0000");
        DTOGenerico dtoFreCont   = iDatosGeneralesDao.listarParametrosxCodigo("FCONT0000");
        List<DTOProceso> listaProceso = iProcesoDao.listarProcesos(idEmpresa, 40L);
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonResultado = gson.toJson(listaProceso);
        
        System.out.println("1503 LISTA PROCESO "+jsonResultado);
        
        File convFile = new File(System.getProperty("java.io.tmpdir")+"/"+file.getOriginalFilename());
        try {
            file.transferTo(convFile);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(FileProcessServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex) {
            java.util.logging.Logger.getLogger(FileProcessServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            Workbook workbook = new XSSFWorkbook(convFile);
            
            Sheet sheet = workbook.getSheetAt(0);
            

            int i = 0;
            for (Row row : sheet) {
                DTODetalleMatrizRiesgo dto=new DTODetalleMatrizRiesgo();
                logger.info("Fila -> " + i);
                if(i>0){
                    if(idTipo==0){
                        dto = rowtoDtoE(idEmpresa,idSede,idPeriodo,row,dtoMatrizNiv,dtoGerencias,dtoOrigenRie,
                            dtoFrecRie,dtoTipoRie,dtoInEficaz,dtoOpoCont,dtoAutCont,dtoEstResp,dtoEstPlan,dtoFreCont,listaProceso);
                    }
                    else{
                        dto = rowtoDtoP(idEmpresa,idSede,idPeriodo,row,dtoMatrizNiv,dtoGerencias,dtoOrigenRie,
                          dtoFrecRie,dtoTipoRie,dtoInEficaz,dtoOpoCont,dtoAutCont,dtoEstResp,dtoEstPlan,dtoFreCont,listaProceso);
                    }
                
                    listaDetalle.add(dto);
                }

                i++;
            }          
//To change body of generated methods, choose Tools | Templates.
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(FileProcessServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidFormatException ex) {
            java.util.logging.Logger.getLogger(FileProcessServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        return DTOFileResponseDetalleMatriz.builder()
                    .idFile("1")
                    .originalFileName(file.getOriginalFilename())
                    .fileName(file.getName())
                    .listado(listaDetalle)
                    .build();
    
    }
    
    public DTODetalleMatrizRiesgo rowtoDtoP(
            Long idEmpresa,
            Long idSede,
            Long idPeriodo,
            Row row,
            DTOGenerico dtoMatrizNiv,
            DTOGenerico dtoGerencias,
            DTOGenerico dtoOrigenRie,
            DTOGenerico dtoFrecRie,
            DTOGenerico dtoTipoRie,
            DTOGenerico dtoInEficaz,
            DTOGenerico dtoOpoCont,
            DTOGenerico dtoAutCont,
            DTOGenerico dtoEstResp,
            DTOGenerico dtoEstPlan,
            DTOGenerico dtoFreCont,
            List<DTOProceso> listaProceso){
        
        DTODetalleMatrizRiesgo dto=new DTODetalleMatrizRiesgo();
        Cell cell1 = row.getCell(0);
        Cell cell2 = row.getCell(1);
        Cell cell3 = row.getCell(2);
        Cell cell4 = row.getCell(3);
        Cell cell5 = row.getCell(4);
        Cell cell6 = row.getCell(5);
        Cell cell7 = row.getCell(6);
        Cell cell8 = row.getCell(7);
        Cell cell9 = row.getCell(8);
        Cell cell10 = row.getCell(9);
        Cell cell11 = row.getCell(10);
        Cell cell12 = row.getCell(11);
        Cell cell13 = row.getCell(12);
        Cell cell14 = row.getCell(13);
        Cell cell15 = row.getCell(14);
        Cell cell16 = row.getCell(15);
        Cell cell17 = row.getCell(16);
        Cell cell18 = row.getCell(17);
        Cell cell19 = row.getCell(18);
        Cell cell20 = row.getCell(19);
        Cell cell21 = row.getCell(20);
        Cell cell22 = row.getCell(21);
        Cell cell23 = row.getCell(22);
        Cell cell24 = row.getCell(23);
        Cell cell25 = row.getCell(24);
        Cell cell26 = row.getCell(25);
        Cell cell27 = row.getCell(26);
        Cell cell28 = row.getCell(27);
        Cell cell29 = row.getCell(28);
        Cell cell30 = row.getCell(29);
        Cell cell31 = row.getCell(30);
        Cell cell32 = row.getCell(31);
        Cell cell33 = row.getCell(32);
        Cell cell34 = row.getCell(33);
        Cell cell35 = row.getCell(34);
        Cell cell36 = row.getCell(35);
        Cell cell37 = row.getCell(36);
        Cell cell38 = row.getCell(37);
        Cell cell39 = row.getCell(38);
        Cell cell40 = row.getCell(39);
        Cell cell41 = row.getCell(40);
        Cell cell42 = row.getCell(41);
        Cell cell43 = row.getCell(42);
        Cell cell44 = row.getCell(43);
        Cell cell45 = row.getCell(44);
        Cell cell46 = row.getCell(45);
        
        dto.setIdEmpresa(idEmpresa);
        dto.setIdSede(idSede);
        dto.setIdPeriodo(idPeriodo);
        if (cell1 != null){
            cell1.setCellType(CellType.STRING);
            dto.setCodMatriz(cell1.getStringCellValue());
        }
        if (cell2 != null){
            cell2.setCellType(CellType.STRING);
            Long idMatrizNivel = getIdMatrizNivel(dtoMatrizNiv,cell2.getStringCellValue());
            dto.setIdMatrizNivel(idMatrizNivel);
        }
        if (cell3 != null){
            cell3.setCellType(CellType.STRING);
            Long idGerencia = getIdGerencia(dtoGerencias,cell3.getStringCellValue());
            dto.setIdGerencia(idGerencia);
        }
        if (cell4 != null){
            cell4.setCellType(CellType.STRING);
            int idProceso = getProcesoId(listaProceso, cell4.getStringCellValue());
            dto.setIdProceso((long)idProceso);

            if (cell5 != null){
                cell5.setCellType(CellType.STRING);
                List<DTOSubProceso> listaSubProceso=iSubProcesoDao.listarSubProcesos((long)idProceso);
                int idSubProceso = getSubProcesoId(listaSubProceso,(long)idProceso, cell5.getStringCellValue());
                dto.setIdSubProceso((long)idSubProceso);
                dto.setListaSubProcesos(listaSubProceso);
            }
            dto.setListaProcesos(listaProceso);
        }

        if (cell6 != null){
            cell6.setCellType(CellType.STRING);
            dto.setCodRiesgo(cell6.getStringCellValue());
        }

        if (cell7 != null){
            cell7.setCellType(CellType.STRING);
            dto.setDeRiesgo(cell7.getStringCellValue());
        }

        if (cell8 != null){
            cell8.setCellType(CellType.STRING);
            int idOrigen =getIdOrigen(dtoOrigenRie, cell8.getStringCellValue());
            dto.setIdOrigenRiesgo(idOrigen);
        }

        if (cell9 != null){
            cell9.setCellType(CellType.STRING);
            int idFrecueRiesgo = getIdFrecuencia(dtoFrecRie, cell9.getStringCellValue());
            dto.setIdFrecuenciaRiesgo(idFrecueRiesgo);
        }

        if (cell10 != null){
            cell10.setCellType(CellType.STRING);
            int idTipoRiesgo = getIdTipoRiesgo(dtoTipoRie, cell10.getStringCellValue());
            dto.setIdTipoRiesgo(idTipoRiesgo);
        }

        if (cell11 != null){
            cell11.setCellType(CellType.NUMERIC);
            dto.setNuProbabilidadInherente((float)cell11.getNumericCellValue());
        }
        if (cell12 != null){
            cell12.setCellType(CellType.NUMERIC);
            dto.setNuImpactoInherente((float)cell12.getNumericCellValue());
        }
        if (cell13 != null){
            //cell13.setCellType(CellType.NUMERIC);
            dto.setNuPuntajeInherente((float) (Math.round(cell13.getNumericCellValue()*100.0)/100.0));
        }
        if (cell14 != null){
            cell14.setCellType(CellType.STRING);
            dto.setDeSeveridadInherente(cell14.getStringCellValue());
        }
        if (cell15 != null){
            cell15.setCellType(CellType.STRING);
            dto.setCodControl(cell15.getStringCellValue());
        }
        if (cell16 != null){
            cell16.setCellType(CellType.STRING);
            dto.setDeControl(cell16.getStringCellValue());
        }
        if (cell17 != null){
            cell17.setCellType(CellType.STRING);
            dto.setIdAreaControl(cell17.getStringCellValue());
        }
        if (cell18 != null){
            cell18.setCellType(CellType.STRING);
            dto.setIdResponsableControl(cell18.getStringCellValue());
        }

        if (cell19 != null){
            cell19.setCellType(CellType.STRING);
            int idFrecueControl = getIdFrecuencia(dtoFreCont, cell19.getStringCellValue());
            dto.setIdFrecuenciaControl((int)idFrecueControl);
        }
        if (cell20 != null){
            cell20.setCellType(CellType.STRING);
            long idOportControl = getOportCont(dtoOpoCont, cell20.getStringCellValue());
            dto.setIdOportunidadControl((int)idOportControl);
        }
        if (cell21 != null){
            cell21.setCellType(CellType.STRING);
            long idAutoControl = getOportCont(dtoAutCont, cell21.getStringCellValue());
            dto.setIdAutomatizacionControl((int)idAutoControl);
        }

        if (cell22 != null){
            cell22.setCellType(CellType.STRING);
            dto.setDeEvidenciaControl(cell22.getStringCellValue());
        }

        if (cell23 != null){
            cell23.setCellType(CellType.NUMERIC);
            dto.setNuProbabilidadResidual((float)cell23.getNumericCellValue());
        }

        if (cell24 != null){
            cell24.setCellType(CellType.NUMERIC);
            dto.setNuImpactoResidual((float)cell24.getNumericCellValue());
        }

        if (cell25 != null){
            cell25.setCellType(CellType.NUMERIC);
            dto.setNuPuntajeResidual((float)(Math.round(cell25.getNumericCellValue()*100.0)/100.0));
        }

        if (cell26 != null){
            cell26.setCellType(CellType.STRING);
            dto.setDeSeveridadResidual(cell26.getStringCellValue());
        }

        if (cell27 != null){
            cell27.setCellType(CellType.STRING);
            long idEstrategiaResp = getIdEstrategia(dtoEstResp, cell27.getStringCellValue());
            dto.setIdEstrategiaResp((int)idEstrategiaResp);
        }

        if (cell28 != null){
            cell28.setCellType(CellType.STRING);
            dto.setCodPlanAccion(cell28.getStringCellValue());
        }

        if (cell29 != null){
            cell29.setCellType(CellType.STRING);
            dto.setDesPlanAccion(cell29.getStringCellValue());
        }
        if (cell30 != null){
            cell30.setCellType(CellType.STRING);
            dto.setIdAreaPlanAccion(cell30.getStringCellValue());
        }

        if (cell31 != null){
            cell31.setCellType(CellType.STRING);
            dto.setIdResponsablePlanAccion(cell31.getStringCellValue());
        }

        if (cell32 != null){
            cell32.setCellType(CellType.STRING);
            dto.setFechaInicioPlanAccion(convertFecha(cell32.getStringCellValue()));
        }
        if (cell33 != null){
            cell33.setCellType(CellType.STRING);
            String idEstadoPlanAcci = getIdEstadoPlan(dtoEstPlan, cell33.getStringCellValue());
            dto.setEstadoPlanAccion(idEstadoPlanAcci);
        }

        if (cell34 != null){
            cell34.setCellType(CellType.STRING);
            dto.setFechaFinPlanAccion(convertFecha(cell34.getStringCellValue()));
        }
        if (cell35 != null){
            cell35.setCellType(CellType.STRING);
            dto.setFechaPrevista(convertFecha(cell35.getStringCellValue()));
        }
        if (cell36 != null){
            cell36.setCellType(CellType.STRING);
            String idFueEficaz = getIdFueEficazEstadoPlan(dtoInEficaz,cell36.getStringCellValue());
            dto.setFueEficaz(idFueEficaz);
        }
        if (cell37 != null){
            cell37.setCellType(CellType.STRING);
            dto.setFechaVerificacion(convertFecha(cell37.getStringCellValue()));
        }
        if (cell38 != null){
            cell38.setCellType(CellType.STRING);
            dto.setVerificadoPor(cell38.getStringCellValue());
        }
        if (cell39 != null){
            cell39.setCellType(CellType.STRING);
            dto.setEvidenciaEficacia(cell39.getStringCellValue());
        }
        if (cell40 != null){
            cell40.setCellType(CellType.STRING);
            dto.setObservaciones(cell40.getStringCellValue());
        }
        if (cell41 != null){
            cell41.setCellType(CellType.STRING);
            dto.setCodkri(cell41.getStringCellValue());
        }
        if (cell42 != null){
            cell42.setCellType(CellType.STRING);
            dto.setDefKri(cell42.getStringCellValue());
        }
        if (cell43 != null){
            cell43.setCellType(CellType.STRING);
            dto.setFrecuencia(cell43.getStringCellValue());
        }
        if (cell44 != null){
            cell44.setCellType(CellType.STRING);
            dto.setMetkri(cell44.getStringCellValue());
        }
        if (cell45 != null){
            cell45.setCellType(CellType.STRING);
            dto.setKriActual(cell45.getStringCellValue());
        }
        if (cell46 != null){
            cell46.setCellType(CellType.STRING);
            dto.setKriResponsable(cell46.getStringCellValue());
        }

        return dto;
    }
    
    
    public DTODetalleMatrizRiesgo rowtoDtoE(
            Long idEmpresa,
            Long idSede,
            Long idPeriodo,
            Row row,
            DTOGenerico dtoMatrizNiv,
            DTOGenerico dtoGerencias,
            DTOGenerico dtoOrigenRie,
            DTOGenerico dtoFrecRie,
            DTOGenerico dtoTipoRie,
            DTOGenerico dtoInEficaz,
            DTOGenerico dtoOpoCont,
            DTOGenerico dtoAutCont,
            DTOGenerico dtoEstResp,
            DTOGenerico dtoEstPlan,
            DTOGenerico dtoFreCont,
            List<DTOProceso> listaProceso){
        
        DTODetalleMatrizRiesgo dto=new DTODetalleMatrizRiesgo();
        Cell cell1 = row.getCell(0);
        Cell cell2 = row.getCell(1);
        Cell cell3 = row.getCell(2);
        Cell cell4 = row.getCell(3);
        Cell cell5 = row.getCell(4);
        Cell cell6 = row.getCell(5);
        Cell cell7 = row.getCell(6);
        Cell cell8 = row.getCell(7);
        Cell cell9 = row.getCell(8);
        Cell cell10 = row.getCell(9);
        Cell cell11 = row.getCell(10);
        Cell cell12 = row.getCell(11);
        Cell cell13 = row.getCell(12);
        Cell cell14 = row.getCell(13);
        Cell cell15 = row.getCell(14);
        Cell cell16 = row.getCell(15);
        Cell cell17 = row.getCell(16);
        Cell cell18 = row.getCell(17);
        Cell cell19 = row.getCell(18);
        Cell cell20 = row.getCell(19);
        Cell cell21 = row.getCell(20);
        Cell cell22 = row.getCell(21);
        Cell cell23 = row.getCell(22);
        Cell cell24 = row.getCell(23);
        Cell cell25 = row.getCell(24);
        Cell cell26 = row.getCell(25);
        Cell cell27 = row.getCell(26);
        Cell cell28 = row.getCell(27);
        Cell cell29 = row.getCell(28);
        Cell cell30 = row.getCell(29);
        Cell cell31 = row.getCell(30);
        Cell cell32 = row.getCell(31);
        Cell cell33 = row.getCell(32);
        Cell cell34 = row.getCell(33);
        Cell cell35 = row.getCell(34);
        Cell cell36 = row.getCell(35);
        Cell cell37 = row.getCell(36);
        Cell cell38 = row.getCell(37);
        Cell cell39 = row.getCell(38);
        Cell cell40 = row.getCell(39);
        Cell cell41 = row.getCell(40);
        Cell cell42 = row.getCell(41);
        Cell cell43 = row.getCell(42);
        Cell cell44 = row.getCell(43);
        Cell cell45 = row.getCell(44);
        Cell cell46 = row.getCell(45);
        Cell cell47 = row.getCell(46);
        Cell cell48 = row.getCell(47);
        
        dto.setIdEmpresa(idEmpresa);
        dto.setIdSede(idSede);
        dto.setIdPeriodo(idPeriodo);

        if (cell1 != null){
            cell1.setCellType(CellType.STRING);
            dto.setCodMatriz(cell1.getStringCellValue());
        }
        if (cell2 != null){
            cell2.setCellType(CellType.STRING);
            Long idMatrizNivel = getIdMatrizNivel(dtoMatrizNiv,cell2.getStringCellValue());
            dto.setIdMatrizNivel(idMatrizNivel);
        }
        if (cell3 != null){
            cell3.setCellType(CellType.STRING);
            Long idGerencia = getIdGerencia(dtoGerencias,cell3.getStringCellValue());
            dto.setIdGerencia(idGerencia);
        }
        if (cell4 != null){
            cell4.setCellType(CellType.STRING);
            dto.setDeTitulo(cell4.getStringCellValue());
        }
        if (cell5 != null){
            cell5.setCellType(CellType.STRING);
            dto.setCodRiesgo(cell5.getStringCellValue());
        }
        if (cell6 != null){
            cell6.setCellType(CellType.STRING);
            dto.setDeRiesgo(cell6.getStringCellValue());
        }
        if (cell7 != null){
            cell7.setCellType(CellType.STRING);
            dto.setDeProcesoImpactado(cell7.getStringCellValue());
        }
        if (cell8 != null){
            cell8.setCellType(CellType.STRING);
            dto.setDeFoda(cell8.getStringCellValue());
        }
        if (cell9 != null){
            cell9.setCellType(CellType.STRING);
            dto.setDeGrupoInteres(cell9.getStringCellValue());
        }

        if (cell10 != null){
            cell10.setCellType(CellType.STRING);
            int idOrigen =getIdOrigen(dtoOrigenRie, cell10.getStringCellValue());
            dto.setIdOrigenRiesgo(idOrigen);
        }

        if (cell11 != null){
            cell11.setCellType(CellType.STRING);
            int idFrecueRiesgo = getIdFrecuencia(dtoFrecRie, cell11.getStringCellValue());
            dto.setIdFrecuenciaRiesgo(idFrecueRiesgo);
        }

        if (cell12 != null){
            cell12.setCellType(CellType.STRING);
            int idTipoRiesgo = getIdTipoRiesgo(dtoTipoRie, cell12.getStringCellValue());
            dto.setIdTipoRiesgo(idTipoRiesgo);
        }

        if (cell13 != null){
            cell13.setCellType(CellType.NUMERIC);
            dto.setNuProbabilidadInherente((float)cell13.getNumericCellValue());
        }
        if (cell14 != null){
            cell14.setCellType(CellType.NUMERIC);
            dto.setNuImpactoInherente((float)cell14.getNumericCellValue());
        }
        if (cell15 != null){
            //cell15.setCellType(CellType.NUMERIC);
            dto.setNuPuntajeInherente((float) (Math.round(cell15.getNumericCellValue()*100.0)/100.0));
        }
        if (cell16 != null){
            cell16.setCellType(CellType.STRING);
            dto.setDeSeveridadInherente(cell16.getStringCellValue());
        }
        if (cell17 != null){
            cell17.setCellType(CellType.STRING);
            dto.setCodControl(cell17.getStringCellValue());
        }
        if (cell18 != null){
            cell18.setCellType(CellType.STRING);
            dto.setDeControl(cell18.getStringCellValue());
        }
        if (cell19 != null){
            cell19.setCellType(CellType.STRING);
            dto.setIdAreaControl(cell19.getStringCellValue());
        }
        if (cell20 != null){
            cell20.setCellType(CellType.STRING);
            dto.setIdResponsableControl(cell20.getStringCellValue());
        }

        if (cell21 != null){
            cell21.setCellType(CellType.STRING);
            int idFrecueControl = getIdFrecuencia(dtoFreCont, cell21.getStringCellValue());
            dto.setIdFrecuenciaControl((int)idFrecueControl);
        }
        if (cell22 != null){
            cell22.setCellType(CellType.STRING);
            long idOportControl = getOportCont(dtoOpoCont, cell22.getStringCellValue());
            dto.setIdOportunidadControl((int)idOportControl);
        }
        if (cell23 != null){
            cell23.setCellType(CellType.STRING);
            long idAutoControl = getOportCont(dtoAutCont, cell23.getStringCellValue());
            dto.setIdAutomatizacionControl((int)idAutoControl);
        }

        if (cell24 != null){
            cell24.setCellType(CellType.STRING);
            dto.setDeEvidenciaControl(cell24.getStringCellValue());
        }

        if (cell25 != null){
            cell25.setCellType(CellType.NUMERIC);
            dto.setNuProbabilidadResidual((float)cell25.getNumericCellValue());
        }

        if (cell26 != null){
            cell26.setCellType(CellType.NUMERIC);
            dto.setNuImpactoResidual((float)cell26.getNumericCellValue());
        }

        if (cell27 != null){
            //cell27.setCellType(CellType.NUMERIC);
            dto.setNuPuntajeResidual((float) (Math.round(cell27.getNumericCellValue()*100.0)/100.0));
        }

        if (cell28 != null){
            cell28.setCellType(CellType.STRING);
            dto.setDeSeveridadResidual(cell28.getStringCellValue());
        }

        if (cell29 != null){
            cell29.setCellType(CellType.STRING);
            long idEstrategiaResp = getIdEstrategia(dtoEstResp, cell29.getStringCellValue());
            dto.setIdEstrategiaResp((int)idEstrategiaResp);
        }

        if (cell30 != null){
            cell30.setCellType(CellType.STRING);
            dto.setCodPlanAccion(cell30.getStringCellValue());
        }

        if (cell31 != null){
            cell31.setCellType(CellType.STRING);
            dto.setDesPlanAccion(cell31.getStringCellValue());
        }
        if (cell32 != null){
            cell32.setCellType(CellType.STRING);
            dto.setIdAreaPlanAccion(cell32.getStringCellValue());
        }

        if (cell33 != null){
            cell33.setCellType(CellType.STRING);
            dto.setIdResponsablePlanAccion(cell33.getStringCellValue());
        }

        if (cell34 != null){
            cell34.setCellType(CellType.STRING);
            dto.setFechaInicioPlanAccion(convertFecha(cell34.getStringCellValue()));
        }
        if (cell35 != null){
            cell35.setCellType(CellType.STRING);
            String idEstadoPlanAcci = getIdEstadoPlan(dtoEstPlan, cell35.getStringCellValue());
            dto.setEstadoPlanAccion(idEstadoPlanAcci);
        }

        if (cell36 != null){
            cell36.setCellType(CellType.STRING);
            dto.setFechaFinPlanAccion(convertFecha(cell36.getStringCellValue()));
        }
        if (cell37 != null){
            cell37.setCellType(CellType.STRING);
            dto.setFechaPrevista(convertFecha(cell37.getStringCellValue()));
        }
        if (cell38 != null){
            cell38.setCellType(CellType.STRING);
            String idFueEficaz = getIdFueEficazEstadoPlan(dtoInEficaz,cell38.getStringCellValue());
            dto.setFueEficaz(idFueEficaz);
        }
        if (cell39 != null){
            cell39.setCellType(CellType.STRING);
            dto.setFechaVerificacion(convertFecha(cell39.getStringCellValue()));
        }
        if (cell40 != null){
            cell40.setCellType(CellType.STRING);
            dto.setVerificadoPor(cell40.getStringCellValue());
        }
        if (cell41 != null){
            cell41.setCellType(CellType.STRING);
            dto.setEvidenciaEficacia(cell41.getStringCellValue());
        }
        if (cell42 != null){
            cell42.setCellType(CellType.STRING);
            dto.setObservaciones(cell42.getStringCellValue());
        }
        if (cell43 != null){
            cell43.setCellType(CellType.STRING);
            dto.setCodkri(cell43.getStringCellValue());
        }
        if (cell44 != null){
            cell44.setCellType(CellType.STRING);
            dto.setDefKri(cell44.getStringCellValue());
        }
        if (cell45 != null){
            cell45.setCellType(CellType.STRING);
            dto.setFrecuencia(cell45.getStringCellValue());
        }
        if (cell46 != null){
            cell46.setCellType(CellType.STRING);
            dto.setMetkri(cell46.getStringCellValue());
        }
        if (cell47 != null){
            cell47.setCellType(CellType.STRING);
            dto.setKriActual(cell47.getStringCellValue());
        }
        if (cell48 != null){
            cell48.setCellType(CellType.STRING);
            dto.setKriResponsable(cell48.getStringCellValue());
        }
        
        return dto;
    }
    
    public int getProcesoId(List<DTOProceso> listaProceso,String proceso){
        int id=0;
        for(int i=0;i<listaProceso.size();i++){
            DTOProceso process = listaProceso.get(i);
            if(process.getDeProceso().equals(proceso)){
                id=process.getIdProceso().intValue();
            }
        }
        
        return id;
        
    }
    
    public int getSubProcesoId(List<DTOSubProceso> listaSubProceso,Long idProceso,String subproceso){
        int id=0;
        
        for(int i=0;i<listaSubProceso.size();i++){
            DTOSubProceso process = listaSubProceso.get(i);
            if(process.getDeSubProceso().equals(subproceso)){
                id=process.getIdSubProceso().intValue();
            }
        }
        
        return id;
        
    }
    
    public String getIdFueEficazEstadoPlan(DTOGenerico dtoEstadoPlan,String MatrizNivel){
        String id= "";
        for(int i=0;i<dtoEstadoPlan.getListado().size();i++){
            String json = new Gson().toJson(dtoEstadoPlan.getListado().get(i));
            DTOParametro model = new Gson().fromJson(json, DTOParametro.class);
            if(model.getNombreParametro().equals(MatrizNivel)){
                id=model.getDeValor1();
            }
        }
        
        return id;
    }
    
    public String getIdEstadoPlan(DTOGenerico dtoEstadoPlan,String MatrizNivel){
        String id="";
        for(int i=0;i<dtoEstadoPlan.getListado().size();i++){
            String json = new Gson().toJson(dtoEstadoPlan.getListado().get(i));
            DTOParametro model = new Gson().fromJson(json, DTOParametro.class);
            if(model.getNombreParametro().equals(MatrizNivel)){
                id=model.getIdParametro().toString();
            }
        }
        
        return id;
    }
    
    public Long getIdProceso(DTOGenerico dtoEstrategia,String MatrizNivel){
        Long id=0L;
        for(int i=0;i<dtoEstrategia.getListado().size();i++){
            String json = new Gson().toJson(dtoEstrategia.getListado().get(i));
            DTOParametro model = new Gson().fromJson(json, DTOParametro.class);
            if(model.getDeValor1()==MatrizNivel){
                id=Long.parseLong(model.getNombreParametro());
            }
        }
        
        return id;
    }
    
    public Long getIdEstrategia(DTOGenerico dtoEstrategia,String MatrizNivel){
        Long id=0L;
        for(int i=0;i<dtoEstrategia.getListado().size();i++){
            String json = new Gson().toJson(dtoEstrategia.getListado().get(i));
            DTOParametro model = new Gson().fromJson(json, DTOParametro.class);
            if(model.getNombreParametro().equals(MatrizNivel)){
                id=model.getIdParametro();
            }
        }
        
        return id;
    }
    
    public Long getIdMatrizNivel(DTOGenerico dtoMatrizNiv,String MatrizNivel){
        Long id=null;
        for(int i=0;i<dtoMatrizNiv.getListado().size();i++){
            String json = new Gson().toJson(dtoMatrizNiv.getListado().get(i));
            DTOParametro model = new Gson().fromJson(json, DTOParametro.class);
            if(model.getDeValor1().equals(MatrizNivel)){
                id=Long.parseLong(model.getNombreParametro());
            }
        }
        
        return id;
    }
    
    public Long getIdGerencia(DTOGenerico dtoGerencias,String MatrizNivel){
        Long id=null;

        for(int i=0;i<dtoGerencias.getListado().size();i++){
            String json = new Gson().toJson(dtoGerencias.getListado().get(i));
            DTOGerencia model = new Gson().fromJson(json, DTOGerencia.class);
            
            if(model.getDescripcionGerencia().equals(MatrizNivel)){
                id=model.getIdGerencia();
            }
        }
        
        return id;
    }
    
    public Long getOportCont(DTOGenerico dtoOportunidad,String MatrizNivel){
        Long id=0L;
        for(int i=0;i<dtoOportunidad.getListado().size();i++){
            String json = new Gson().toJson(dtoOportunidad.getListado().get(i));
            DTOParametro model = new Gson().fromJson(json, DTOParametro.class);
            if(model.getNombreParametro().equals(MatrizNivel)){
                id=model.getIdParametro();
            }
        }
        
        return id;
    }
    
    public int getIdOrigen(DTOGenerico dtoOrigenRie,String origen){
        int id=0;
        
        
        for(int i=0;i<dtoOrigenRie.getListado().size();i++){
            String json = new Gson().toJson(dtoOrigenRie.getListado().get(i));
            DTOParametro model = new Gson().fromJson(json, DTOParametro.class);
            if(model.getNombreParametro().equals(origen)){
                id=model.getIdParametro().intValue();
            }
        }
        
        return id;
    }
    
    public int getIdTipoRiesgo(DTOGenerico dtoTipoRie,String riesgo){
        int id=0;
//        System.out.println("1503 JSON TIPOR INI "+riesgo);
        for(int i=0;i<dtoTipoRie.getListado().size();i++){
            String json = new Gson().toJson(dtoTipoRie.getListado().get(i));
            DTOParametro model = new Gson().fromJson(json, DTOParametro.class);
            if(model.getNombreParametro().equals(riesgo)){
                id=model.getIdParametro().intValue();
            }
        }
        
        return id;
    }
    
    public int getIdFrecuenciaControl(DTOGenerico dtoFrecRie,String frecuencia){
        int id=0;
        for(int i=0;i<dtoFrecRie.getListado().size();i++){
            String json = new Gson().toJson(dtoFrecRie.getListado().get(i));
            DTOParametro model = new Gson().fromJson(json, DTOParametro.class);
            if(model.getNombreParametro().equals(frecuencia)){
                id=model.getIdParametro().intValue();
            }
        }
        
        return id;
    }
    
    public int getIdFrecuencia(DTOGenerico dtoFrecRie,String frecuencia){
        int id=0;
        for(int i=0;i<dtoFrecRie.getListado().size();i++){
            String json = new Gson().toJson(dtoFrecRie.getListado().get(i));
            DTOParametro model = new Gson().fromJson(json, DTOParametro.class);
            if(model.getNombreParametro().equals(frecuencia)){
                id=model.getIdParametro().intValue();
            }
        }
        
        return id;
    }
    
    public String convertFecha(String fecha){

        // console.log("CHECK FECHA" + fecha);

        if (fecha != null){
            if(this.checkFechaValid(fecha)){
                String dateString = fecha;
                String[] dateParts = dateString.split("/");
                String dateObject = dateParts[2]+"-"+dateParts[1]+"-"+dateParts[0];
                return dateObject.toString();
            }
            else{
                // Swal.fire("Existe fecha con formato inválido", "", 'error');
                return null;
            }
        }
        else{
            return null;
        }
    }

    public Date convertFecha1(String fecha){

        // console.log("CHECK FECHA" + fecha);

        SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
        Date fechaEnvio = null;
        try {
            fechaEnvio = parse.parse(fecha.toString());

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error de parseo " + e);
        }

        return fechaEnvio;

        /*if (fecha != null){
            if(this.checkFechaValid(fecha)){
                String dateString = fecha;
                String[] dateParts = dateString.split("/");
                String dateObject = dateParts[2]+"-"+dateParts[1]+"-"+dateParts[0];
                return dateObject.toString();
            }
            else{
                // Swal.fire("Existe fecha con formato inválido", "", 'error');
                return null;
            }
        }
        else{
            return null;
        }*/
    }
    
    public boolean checkFechaValid(String fecha){

        boolean valid=false;
        String[] dateParts =null;
        try {
            dateParts = fecha.split("/");
        } catch (Exception ex) {
            valid = false;
            // Swal.fire("Existe fecha con formato inválido", "", 'error');
        }

        if(dateParts!=null){

            if(dateParts.length<3){
                valid = false;
  
            }
            else{
                valid=true;
            }
        }
        else{
            valid=false;
        }
        
        return valid;

    }
    
}

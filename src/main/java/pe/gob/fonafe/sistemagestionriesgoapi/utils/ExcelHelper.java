package pe.gob.fonafe.sistemagestionriesgoapi.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTODetalleRespuesta;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOOptions;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOReportHeaderSql;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTORespuestaSql;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOPuntaje;

@Slf4j
public class ExcelHelper {
    public static ByteArrayInputStream reportToExcel(Map<String, List<?>> reportInfoMap,List<DTOPuntaje> listapuntajes) {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(SNConstantes.DATE_FORMAT_REPORT);

        List<DTORespuestaSql> userInfoList = (List<DTORespuestaSql>) reportInfoMap.get("userInfo");
        List<DTODetalleRespuesta> respInfoList = (List<DTODetalleRespuesta>) reportInfoMap.get("respInfo");
        List<DTOReportHeaderSql> respHeaders = (List<DTOReportHeaderSql>) reportInfoMap.get("respHeaders");

        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(SNConstantes.SHEET_NAME);

        AtomicInteger idxRow = new AtomicInteger(0);
        Row row = sheet.createRow(idxRow.getAndIncrement());
        AtomicInteger idxHeader = new AtomicInteger();

        XSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(77, 188, 249)));
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        List<String> indicesPreguntas = new ArrayList<>();
       // System.out.println("JSON REPORTE "+jsonResultado);

        final List<String> surveyColumns = ExcelHelper.getSurveyColumns(respHeaders,indicesPreguntas);
        for (String surveyColumn : surveyColumns) {
            Cell cellHeader = row.createCell(idxHeader.getAndIncrement());
            cellHeader.setCellStyle(headerStyle);
            cellHeader.setCellValue(surveyColumn);
        }

//        sheet.addMergedRegion(new CellRangeAddress(0, 0, 8, 9));

        AtomicInteger atomicCell = new AtomicInteger(1);
        AtomicReference<Row> rowAtomicReference = new AtomicReference<>();
        Map<String, Map<Integer, List<String>>> parseResponses = getResponse(respInfoList);
        
        
        for (DTORespuestaSql rpt : userInfoList) {
            rowAtomicReference.set(sheet.createRow(atomicCell.getAndIncrement()));

            rowAtomicReference.get().createCell(0).setCellValue(rpt.getFechaEncuesta().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().format(dateTimeFormatter));
            //rowAtomicReference.get().createCell(1).setCellValue(rpt.getFechaModificacion().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().format(dateTimeFormatter));
            rowAtomicReference.get().createCell(1).setCellValue(rpt.getNomApellido());
            rowAtomicReference.get().createCell(2).setCellValue(rpt.getNumDoc());
            rowAtomicReference.get().createCell(3).setCellValue(rpt.getNomCorto());
            
            AtomicInteger score = new AtomicInteger(0);
            AtomicInteger actualrpt = new AtomicInteger(0);
            actualrpt.set(0);
            
            
            // 5 - static columns
            parseResponses
                    .forEach((k, v) -> {
                        if (k.split("\\|")[0].equalsIgnoreCase(String.valueOf(rpt.getIdRespEncuesta()))) {
                            if(actualrpt.get()!=rpt.getIdRespEncuesta().intValue()){
                                actualrpt.set(rpt.getIdRespEncuesta().intValue());
//                                score.set(0);
                                
                                v.forEach((k1, v1) -> {
                                if (k1 != null) {
                                    
                                }
                                if(indicesPreguntas.contains(k.split("\\|")[1])){
                                    
                                    var indice = indicesPreguntas.indexOf(k.split("\\|")[1]);
//                                    score.addAndGet(k1);

                                    rowAtomicReference.get().createCell(5+ indice).setCellValue(String.join(", ", v1));
                                }
                               });
                                
                            }
                            else{
                                
                                
                                v.forEach((k1, v1) -> {
                                if (k1 != null) {
                                    
                                }
                                if(indicesPreguntas.contains(k.split("\\|")[1])){
                                    var indice = indicesPreguntas.indexOf(k.split("\\|")[1]);
//                                    score.addAndGet(k1);
                                    rowAtomicReference.get().createCell(5+ indice).setCellValue(String.join(", ", v1));
                                }
                               });
                                
                            }
                            
                        }
                    });
            
            for(DTOPuntaje puntaje: listapuntajes){
                if(puntaje.getIdRespEncuesta()==rpt.getIdRespEncuesta().intValue()){
                   rowAtomicReference.get().createCell(4).setCellValue(puntaje.getPuntaje());
            
                }
            }
        }

        for (int i = 0; i < surveyColumns.size(); i++) {
            sheet.autoSizeColumn(i);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            workbook.write(baos);
        } catch (IOException e) {
            throw new RuntimeException("failed to export data to Excel file: " + e.getMessage());
        }

        return new ByteArrayInputStream(baos.toByteArray());
    }

    public static Map<String, Map<Integer, List<String>>> getResponse(List<DTODetalleRespuesta> respInfoList) {
        Map<String, List<DTOOptions>> responsesList = respInfoList.stream()
                .map(a -> DTOOptions.builder()
                        .idSurvey(a.getIdRespEncuesta())
                        .idQuestion(a.getIdPregunta())
                        .idOption(a.getIdDetResp())
                        .score(a.getInAlternativa())
                        .question(a.getDePregunta())
                        .description(a.getRespuesta() == null ? a.getDeResp() : a.getRespuesta()).build())
                .collect(Collectors.groupingBy(p -> String.valueOf(p.getIdSurvey()).concat("|")
                                .concat(String.valueOf(p.getIdQuestion())),
                        Collectors.mapping(Function.identity(), Collectors.toList())));

        Map<String, Map<Integer, List<String>>> optionsMap = new HashMap<>();
        for (var responses : responsesList.entrySet()) {
            for (var dtoOptions : responses.getValue()) {
                if (responses.getKey().equals(String.valueOf(dtoOptions.getIdSurvey()).concat("|").concat(String.valueOf(dtoOptions.getIdQuestion())))) {
                    if (optionsMap.containsKey(responses.getKey())) {
                        var oldMap1 = optionsMap.get(responses.getKey());
                        var newMapCalculated = new HashMap<Integer, List<String>>();
                        List<String> list;
                        for (var detailAux : oldMap1.entrySet()) {
                            list = new ArrayList<>(detailAux.getValue());
                            list.add(dtoOptions.getDescription());
                            newMapCalculated.put(detailAux.getKey() + dtoOptions.getScore().intValue(), list);
                        }
                        optionsMap.put(responses.getKey(), newMapCalculated);
                    } else {
                        Map<Integer, List<String>> details = new HashMap<>();
                        details.put(dtoOptions.getScore() == null ? null : dtoOptions.getScore().intValue(), Collections.singletonList(dtoOptions.getDescription()));
                        optionsMap.put(responses.getKey(), details);
                    }
                }
            }
        }

        return optionsMap;
    }

    public static List<String> getSurveyColumns(List<DTOReportHeaderSql> respHeaders, List<String> indicePreguntas) {
        List<String> custodyColumns = new ArrayList<>();
        custodyColumns.add(SNConstantes.COLUMN_CREATED_DATE);
        //custodyColumns.add(SNConstantes.COLUMN_MODIFICATED_DATE);
        custodyColumns.add(SNConstantes.COLUMN_NAMES);
        custodyColumns.add(SNConstantes.COLUMN_DOCUMENT);
        custodyColumns.add(SNConstantes.COLUMN_COMPANY);
        custodyColumns.add(SNConstantes.COLUMN_SCORE);
        AtomicInteger countQuestion = new AtomicInteger(1);
        respHeaders
                .sort(Comparator.comparing(DTOReportHeaderSql::getIdQuestion));
        respHeaders
                .forEach(headerSql -> {
                    indicePreguntas.add(headerSql.getIdQuestion().toString());
                    custodyColumns.add("PREGUNTA Nª" + countQuestion.getAndIncrement() + "  " + headerSql.getQuestion());
                });
        return custodyColumns;
    }
}

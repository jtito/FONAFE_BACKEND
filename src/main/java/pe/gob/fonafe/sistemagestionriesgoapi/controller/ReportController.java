package pe.gob.fonafe.sistemagestionriesgoapi.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.gob.fonafe.sistemagestionriesgoapi.service.IReportService;

@Slf4j
@RestController
@RequestMapping("gestionriesgo/report")
public class ReportController {


    private final IReportService reportService;

    public ReportController(IReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/download/xlsx/{surveyId}")
    public ResponseEntity<Resource> getReportFile(@PathVariable("surveyId") Long surveyId) {
        String filename = "Reporte.xlsx";
        InputStreamResource file = new InputStreamResource(reportService.getReportBySurveyId(surveyId));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }

}

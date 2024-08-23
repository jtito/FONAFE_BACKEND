package pe.gob.fonafe.sistemagestionriesgoapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pe.gob.fonafe.sistemagestionriesgoapi.dao.IEncuestaDao;
import pe.gob.fonafe.sistemagestionriesgoapi.service.IReportService;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.ExcelHelper;
import java.io.ByteArrayInputStream;
import java.util.List;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOPuntaje;

@Slf4j
@Service
public class ReportServiceImpl implements IReportService {

    private IEncuestaDao encuestaDao;

    public ReportServiceImpl(IEncuestaDao encuestaDao) {
        this.encuestaDao = encuestaDao;
    }

    @Override
    public ByteArrayInputStream getReportBySurveyId(Long surveyId) {
        List<DTOPuntaje> lista = encuestaDao.listaPuntaje(surveyId);
        return ExcelHelper.reportToExcel(encuestaDao.getReportData(surveyId),lista);
    }
}

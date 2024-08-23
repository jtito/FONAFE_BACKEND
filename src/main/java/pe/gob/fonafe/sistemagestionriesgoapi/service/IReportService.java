package pe.gob.fonafe.sistemagestionriesgoapi.service;

import java.io.ByteArrayInputStream;

public interface IReportService {

    ByteArrayInputStream getReportBySurveyId(Long surveyId);

}

package com.example.reportingAndAnalytics.reporting.service;

import java.io.IOException;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.ResponseEntity;

public interface ReportingService {

  ResponseEntity<byte[]> getCourseReport(String documentType)
      throws JRException, ClassNotFoundException, IOException;

  ResponseEntity<byte[]> getGradeReport(String documentType, String courseCode)
      throws ClassNotFoundException, JRException;
}

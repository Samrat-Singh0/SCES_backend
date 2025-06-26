package com.example.reportingAndAnalytics.reporting.service;

import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import java.util.List;
import net.sf.jasperreports.engine.JRException;

public interface JasperReportService {
  <T> byte[] generatePdfReport(FastReportBuilder fastReportBuilder, List<T> data) throws ClassNotFoundException, JRException;
  <T> byte[] generateExcelReport(FastReportBuilder fastReportBuilder, List<T> data) throws ClassNotFoundException, JRException;
}

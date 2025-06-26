package com.example.reportingAndAnalytics.reporting.service;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import java.io.ByteArrayOutputStream;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.springframework.stereotype.Service;

@Service
public class JasperReportServiceImpl implements JasperReportService{

  @Override
  public <T> byte[] generatePdfReport(FastReportBuilder fastReportBuilder, List<T> data) throws JRException {

      DynamicReport report = fastReportBuilder.build();

      JRDataSource dataSource = new JRBeanCollectionDataSource(data);

      JasperPrint jasperPrint = DynamicJasperHelper.generateJasperPrint(report, new ClassicLayoutManager(), dataSource);

      return JasperExportManager.exportReportToPdf(jasperPrint);

  }

  @Override
  public <T> byte[] generateExcelReport(FastReportBuilder fastReportBuilder, List<T> data) throws JRException {

    DynamicReport report = fastReportBuilder.build();
    JRDataSource dataSource = new JRBeanCollectionDataSource(data);
    JasperPrint jasperPrint = DynamicJasperHelper.generateJasperPrint(report, new ClassicLayoutManager(), dataSource);

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    JRXlsxExporter exporter = new JRXlsxExporter();
    exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
    exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));

    SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
    configuration.setDetectCellType(true);
    configuration.setCollapseRowSpan(false);
    configuration.setWhitePageBackground(false);
    exporter.setConfiguration(configuration);

    exporter.exportReport();
    return outputStream.toByteArray();

  }
}

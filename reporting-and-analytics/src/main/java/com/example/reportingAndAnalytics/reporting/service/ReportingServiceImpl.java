package com.example.reportingAndAnalytics.reporting.service;

import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import com.example.mainBase.dto.CourseReportDto;
import com.example.mainBase.dto.GradeReportDto;
import com.example.mainBase.entities.Course;
import com.example.mainBase.repository.CourseRepository;
import com.example.mainBase.repository.GradeRepository;
import com.example.reportingAndAnalytics.reporting.ReportType;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportingServiceImpl implements ReportingService {

  private final CourseRepository courseRepository;
  private final JasperReportService jasperReportService;
  private final GradeRepository gradeRepository;

  @Override
  public ResponseEntity<byte[]> getCourseReport(String documentType)
      throws JRException, ClassNotFoundException {
    List<CourseReportDto> reportRows = courseRepository.fetchCourseReports();
    FastReportBuilder fastReportBuilder = new FastReportBuilder();

    fastReportBuilder
        .addColumn("Course Name", "courseName", String.class.getName(), 70)
        .addColumn("Semester", "semester", String.class.getName(), 30)
        .addColumn("Total Enrolled", "totalEnrollments", Long.class.getName(), 50)
        .addColumn("Currently Enrolled", "runningEnrollments", Long.class.getName(), 30)
        .addColumn("Average Grades", "averageGrade", BigDecimal.class.getName(), 50)
        .setTitle("Course Report")
        .setUseFullPageWidth(true);

    if(documentType.equalsIgnoreCase(ReportType.PDF.toString())) {
      byte[] pdf = jasperReportService.generatePdfReport(fastReportBuilder, reportRows);

      return ResponseEntity.ok()
          .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=course-report.pdf")
          .contentType(MediaType.APPLICATION_PDF)
          .body(pdf);

    }else {
      byte[] excel = jasperReportService.generateExcelReport(fastReportBuilder, reportRows);

      return ResponseEntity.ok()
          .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=course-report.xlsx")
          .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
          .body(excel);
    }

  }

  @Override
  public ResponseEntity<byte[]> getGradeReport(String documentType, String courseCode)
      throws ClassNotFoundException, JRException {

    Course course = Optional.ofNullable(courseRepository.findByCode(courseCode))
        .orElseThrow(()-> new ClassNotFoundException("Course not found"));

    List<GradeReportDto> reportRows = gradeRepository.fetchGradeReport(course.getId());
    FastReportBuilder fastReportBuilder = new FastReportBuilder();

    fastReportBuilder
        .addColumn("Student Name", "studentName", String.class.getName(), 70)
        .addColumn("Completion Status", "completionStatus", String.class.getName(), 30)
        .addColumn("Grades", "grade", BigDecimal.class.getName(), 50)
        .setTitle("Grade Report of " + course.getName())
        .setUseFullPageWidth(true);

    if(documentType.equalsIgnoreCase(ReportType.PDF.toString())) {
      byte[] pdf = jasperReportService.generatePdfReport(fastReportBuilder, reportRows);

      return ResponseEntity.ok()
//          .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=grade-report.pdf")
          .contentType(MediaType.APPLICATION_PDF)
          .body(pdf);

    }else {
      byte[] excel = jasperReportService.generateExcelReport(fastReportBuilder, reportRows);

      return ResponseEntity.ok()
          .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=grade-report.xlsx")
          .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
          .body(excel);
    }
  }
}

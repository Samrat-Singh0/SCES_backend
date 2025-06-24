package com.example.mainBase.service;

import com.example.mainBase.dto.ResponseDto;
import com.example.mainBase.dto.SemesterPayload;
import com.example.mainBase.entities.Semester;
import com.example.mainBase.mapper.SemesterMapper;
import com.example.mainBase.repository.SemesterRepository;
import com.example.mainBase.util.ResponseBuilder;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SemesterServiceImpl implements SemesterService {

  private final SemesterRepository semesterRepository;

  @Override
  public ResponseEntity<ResponseDto> getSemester(String label) {
    Optional<Semester> optionalSemester = semesterRepository.findByLabel(label);

    if (optionalSemester.isEmpty()) {
      return ResponseBuilder.getFailedMessage("Semester not found");
    }
    Semester semester = optionalSemester.get();
    SemesterPayload semesterResponse = SemesterMapper.INSTANCE.toSemesterResponsePayload(
        semester);
    return ResponseBuilder.success("Fetched Semester Successfully", semesterResponse);
  }

  @Override
  public ResponseEntity<ResponseDto> getAllSemesters() {
    try {
      SemesterMapper semesterMapper = SemesterMapper.INSTANCE;
      List<Semester> semesters = semesterRepository.findAll();
      List<SemesterPayload> semesterResponse = semesterMapper.toSemesterResponsePayloadList(
          semesters);
      return ResponseBuilder.success("Fetched Semester List Successfully.",
          semesterResponse);
    } catch (Exception error) {
      return ResponseBuilder.getFailedMessage(error.getMessage());
    }

  }

  @Override
  public ResponseEntity<ResponseDto> addSemester(SemesterPayload payload) {
    Optional<Semester> optionalSemester = semesterRepository.findByLabel(payload.getLabel());
    if (optionalSemester.isPresent()) {
      return ResponseBuilder.getFailedMessage("Semester already exists");
    }
    Semester semester = SemesterMapper.INSTANCE.toSemester(payload);
    semesterRepository.save(semester);
    return ResponseBuilder.success("Added Semester Successfully", semester);
  }

  @Override
  public ResponseEntity<ResponseDto> updateSemester(
      SemesterPayload semesterPayload) {
    try {
      Optional<Semester> optionalSemester = semesterRepository.findByLabel(
          semesterPayload.getLabel());
      if (optionalSemester.isEmpty()) {
        return ResponseBuilder.getFailedMessage("Semester not found.");
      }

      Semester semester = optionalSemester.get();

      semester.setFee(semesterPayload.getFee());
      semester.setStartDate(semesterPayload.getStartDate());
      semester.setEndDate(semesterPayload.getEndDate());

      semesterRepository.save(semester);
      return ResponseBuilder.success("Semester Updated.", null);
    } catch (Exception e) {
      return ResponseBuilder.getFailedMessage(e.getMessage());
    }
  }

  @Override
  public ResponseEntity<ResponseDto> deleteSemester(String label) {
    Optional<Semester> semester = semesterRepository.findByLabel(label);
    if (semester.isEmpty()) {
      return ResponseBuilder.getFailedMessage("Semester not found.");
    }
    semesterRepository.delete(semester.get());
    return ResponseBuilder.success("Semester Deleted.", null);
  }
  

}

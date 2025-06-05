package com.f1soft.sces.service;

import com.f1soft.sces.dto.SemesterDto;
import com.f1soft.sces.mapper.SemesterMapper;
import com.f1soft.sces.repository.SemesterRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SemesterServiceImpl implements SemesterService {

  private final SemesterRepository semesterRepository;

  @Override
  public ResponseEntity<List<SemesterDto>> getAllSemesters() {
    SemesterMapper semesterMapper = SemesterMapper.INSTANCE;

    return new ResponseEntity<>(semesterMapper.toSemesterDtoList(semesterRepository.findAll()), HttpStatus.OK);
  }
}

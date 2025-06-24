package com.example.mainBase.service;

import com.example.mainBase.dto.ResponseDto;
import com.example.mainBase.entities.Instructor;
import com.example.mainBase.enums.ActiveStatus;
import com.example.mainBase.repository.InstructorRepository;
import com.example.mainBase.repository.UserRepository;
import com.example.mainBase.util.ResponseBuilder;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InstructorServiceImpl implements InstructorService {

  private final InstructorRepository instructorRepository;
  private final UserRepository userRepository;

  @Override
  public ResponseEntity<ResponseDto> getAllInstructors() {
    try {
      List<Instructor> instructors = instructorRepository.findAll();
      if (instructors.isEmpty()) {
        return ResponseBuilder.getFailedMessage("No instructors found");
      }

      List<Instructor> activeInstructors = new ArrayList<>();

      for (Instructor instructor : instructors) {       //todo: use stream
        if (instructor.getUser().getActiveStatus() == ActiveStatus.ACTIVE) {
          activeInstructors.add(instructor);
        }
      }

      return ResponseBuilder.success("Fetch Instructors Successfully", activeInstructors);

    } catch (Exception e) {
      return ResponseBuilder.getFailedMessage(e.getMessage());
    }
  }
}

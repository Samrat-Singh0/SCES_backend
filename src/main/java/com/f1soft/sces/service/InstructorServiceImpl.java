package com.f1soft.sces.service;

import com.f1soft.sces.dto.ResponseDto;
import com.f1soft.sces.entities.Instructor;
import com.f1soft.sces.enums.Status;
import com.f1soft.sces.repository.InstructorRepository;
import com.f1soft.sces.repository.UserRepository;
import com.f1soft.sces.util.ResponseBuilder;
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
        if (instructor.getUser().getStatus() == Status.ACTIVE) {
          activeInstructors.add(instructor);
        }
      }

      return ResponseBuilder.success("Fetch Instructors Successfully", activeInstructors);

    } catch (Exception e) {
      return ResponseBuilder.getFailedMessage(e.getMessage());
    }
  }
}

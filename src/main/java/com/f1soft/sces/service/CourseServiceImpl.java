package com.f1soft.sces.service;

import com.f1soft.sces.dto.CourseResponsePayload;
import com.f1soft.sces.dto.ResponseDto;
import com.f1soft.sces.entities.Course;
import com.f1soft.sces.mapper.CourseMapper;
import com.f1soft.sces.repository.CourseRepository;
import com.f1soft.sces.util.ResponseBuilder;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

  private final CourseRepository courseRepository;

  @Override
  public ResponseEntity<ResponseDto> getAllCourses() {
    try {
      List<Course> courses = courseRepository.findAll();
      if (courses.isEmpty()) {
        return ResponseBuilder.getFailedMessage("No courses found");
      } else {
        List<CourseResponsePayload> courseResponseList = CourseMapper.INSTANCE.toCourseResponsePayloadList(
            courses);
        return ResponseBuilder.success("Fetched Courses Successfully", courseResponseList);
      }
    } catch (Exception e) {
      return ResponseBuilder.getFailedMessage(e.getMessage());
    }
  }

}

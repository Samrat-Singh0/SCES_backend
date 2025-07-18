package com.example.mainBase.mapper;

import com.example.mainBase.dto.CoursePayload;
import com.example.mainBase.entities.Course;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

@Mapper
public interface CourseMapper {

  CourseMapper INSTANCE = Mappers.getMapper(CourseMapper.class);

  Course toCourse(CoursePayload payload);

  CoursePayload toCoursePayload(Course course);

  default Page<CoursePayload> toCoursePayloadPage(Page<Course> coursePage) {
    List<CoursePayload> coursePayloadList = toCoursePayloadList(coursePage.getContent());
    return new PageImpl<>(coursePayloadList, coursePage.getPageable(),
        coursePage.getTotalElements());
  }

  List<CoursePayload> toCoursePayloadList(List<Course> courseList);

  Course courseResponsePayloadToCourse(CoursePayload coursePayload);

}

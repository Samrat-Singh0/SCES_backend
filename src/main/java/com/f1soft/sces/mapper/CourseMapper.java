package com.f1soft.sces.mapper;

import com.f1soft.sces.dto.CoursePayload;
import com.f1soft.sces.entities.Course;
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

  List<CoursePayload> toCoursePayloadList(List<Course> courseList);

  default Page<CoursePayload> toCoursePayloadPage(Page<Course> coursePage) {
    List<CoursePayload> coursePayloadList = toCoursePayloadList(coursePage.getContent());
    return new PageImpl<>(coursePayloadList, coursePage.getPageable(),
        coursePage.getTotalElements());
  }

  Course courseResponsePayloadToCourse(CoursePayload coursePayload);

}

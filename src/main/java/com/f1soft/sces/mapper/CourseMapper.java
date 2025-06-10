package com.f1soft.sces.mapper;

import com.f1soft.sces.dto.CourseResponsePayload;
import com.f1soft.sces.entities.Course;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CourseMapper {

  CourseMapper INSTANCE = Mappers.getMapper(CourseMapper.class);

  CourseResponsePayload courseToCourseResponsePayload(Course course);

  List<CourseResponsePayload> toCourseResponsePayloadList(List<Course> courseList);

  Course courseResponsePayloadToCourse(CourseResponsePayload courseResponsePayload);

}

package com.f1soft.sces.specification;

import com.f1soft.sces.entities.Course;
import com.f1soft.sces.model.FilterCourse;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class CourseSpecification {


  public static Specification<Course> hasName(String name) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
            "%" + name.toLowerCase() + "%");
  }

  public static Specification<Course> hasInstructor(String instructorName) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.like(
            criteriaBuilder.lower(root.join("instructor").get("user").get("fullName")),
            "%" + instructorName.toLowerCase() + "%"
        );
  }

  public static Specification<Course> hasSemester(String semesterName) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.like(
            criteriaBuilder.lower(root.join("semester").get("label")),
            "%" + semesterName.toLowerCase() + "%"
        );
  }

  public static Specification<Course> hasStatusChecked() {
    return (root, query, criteriaBuiler) ->
        criteriaBuiler.equal(root.get("checked"), "checked");
  }

  public static Specification<Course> buildSpec(FilterCourse filterCriteria) {
    Specification<Course> specification = hasStatusChecked();

    if (filterCriteria.getName() != null) {
      specification = specification.and(hasName(filterCriteria.getName()));
    }

    if (filterCriteria.getInstructor() != null) {
      specification = specification.and(hasInstructor(filterCriteria.getInstructor()));
    }

    if (filterCriteria.getSemester() != null) {
      specification = specification.and(hasSemester(filterCriteria.getSemester()));
    }

    return specification;
  }
}

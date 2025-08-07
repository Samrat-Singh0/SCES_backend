package com.example.mainBase.specification;

import com.example.mainBase.entities.Course;
import com.example.mainBase.entities.Instructor;
import com.example.mainBase.entities.Semester;
import com.example.mainBase.entities.SemesterCourse;
import com.example.mainBase.entities.SemesterCourseInstructor;
import com.example.mainBase.entities.User;
import com.example.mainBase.model.FilterCourse;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
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
    return (root, query, criteriaBuilder) -> {

      Join<Course, SemesterCourse> semesterCourseJoin = root.join("semesterCourse", JoinType.INNER);
      Join<SemesterCourse, SemesterCourseInstructor> semesterCourseInstructorJoin = semesterCourseJoin.join("semesterCourseInstructors", JoinType.INNER);
      Join<SemesterCourseInstructor, Instructor> instructorJoin = semesterCourseInstructorJoin.join("instructor", JoinType.INNER);
      Join<Instructor, User> userJoin = instructorJoin.join("user", JoinType.INNER);

      String likePattern = "%" + instructorName.toLowerCase() + "%";

      return criteriaBuilder.or(
          criteriaBuilder.like(criteriaBuilder.lower(userJoin.get("firstName")), likePattern),
          criteriaBuilder.like(criteriaBuilder.lower(userJoin.get("middleName")), likePattern),
          criteriaBuilder.like(criteriaBuilder.lower(userJoin.get("lastName")), likePattern)
      );
    };
  }

  public static Specification<Course> hasSemester(String semesterName) {
    return (root, query, criteriaBuilder) ->{
      Join<Course, SemesterCourse> semesterCourseJoin = root.join("semesterCourse", JoinType.INNER);
      Join<SemesterCourse, Semester> semesterJoin = semesterCourseJoin.join("semester", JoinType.INNER);

      String likePattern = "%" + semesterName.toLowerCase() + "%";

      return criteriaBuilder.like(criteriaBuilder.lower(semesterJoin.get("label")), likePattern);
    };
  }

  public static Specification<Course> hasStatusChecked() {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.equal(root.get("checked"), "checked");
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

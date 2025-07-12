package com.example.mainBase.specification;

import com.example.mainBase.entities.Enrollment;
import com.example.mainBase.entities.Student;
import com.example.mainBase.entities.User;
import com.example.mainBase.model.FilterEnrollment;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class EnrollmentSpecification {

  public static Specification<Enrollment> hasStudent(String studentName) {
    return (root, query, criteriaBuilder) -> {
      Join<Enrollment, Student> studentJoin= root.join("student", JoinType.INNER);
      Join<Student, User> userJoin = studentJoin.join("user", JoinType.INNER);

      String likePattern = "%" + studentName.toLowerCase() + "%";

      return criteriaBuilder.or(
          criteriaBuilder.like(criteriaBuilder.lower(userJoin.get("firstName")), likePattern),
          criteriaBuilder.like(criteriaBuilder.lower(userJoin.get("middleName")), likePattern),
          criteriaBuilder.like(criteriaBuilder.lower(userJoin.get("lastName")), likePattern)
      );
    };
  }

  public static Specification<Enrollment> hasSemester(String semesterName) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.like(
            criteriaBuilder.lower(root.join("semester").get("label")),
            "%" + semesterName.toLowerCase() + "%"
        );
  }

  public static Specification<Enrollment> hasPayStatus(String payStatus) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.like(criteriaBuilder.lower(root.get("paidStatus")), payStatus);
  }

  public static Specification<Enrollment> hasStatusNotPending() {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.notEqual(root.get("completionStatus"), "pending");
  }

  public static Specification<Enrollment> buildSpec(FilterEnrollment filterCriteria) {
    Specification<Enrollment> specification = hasStatusNotPending();

    if (filterCriteria.getStudentName() != null) {
      specification = specification.and(hasStudent(filterCriteria.getStudentName()));
    }

    if (filterCriteria.getPayStatus() != null) {
      specification = specification.and(hasPayStatus(filterCriteria.getPayStatus()));
    }

    if (filterCriteria.getSemester() != null) {
      specification = specification.and(hasSemester(filterCriteria.getSemester()));
    }

    return specification;
  }

}

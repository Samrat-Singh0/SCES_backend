package com.example.mainBase.specification;

import com.example.mainBase.entities.User;
import com.example.mainBase.model.FilterUser;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

  public static Specification<User> hasFirstName(String firstName) {
    return (root, query, criteriaBuilder) ->
        firstName == null ? null
            : criteriaBuilder.like(root.get("firstName"), "%" + firstName + "%");
  }

  public static Specification<User> hasMiddleName(String middleName) {
    return (root, query, criteriaBuilder) ->
        middleName == null ? null
            : criteriaBuilder.like(root.get("middleName"), "%" + middleName + "%");
  }

  public static Specification<User> hasLastName(String lastName) {
    return (root, query, criteriaBuilder) ->
        lastName == null ? null : criteriaBuilder.like(root.get("lastName"), "%" + lastName + "%");
  }

  public static Specification<User> hasRole(String role) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.equal(root.get("role"), role);
  }

  public static Specification<User> hasPhoneNumber(String phoneNumber) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.equal(root.get("phoneNumber"), phoneNumber);
  }

  public static Specification<User> hasStatusActive() {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.equal(root.get("activeStatus"), "active");
  }

  public static Specification<User> buildSpec(FilterUser filterCriteria) {
    Specification<User> specification = hasStatusActive();

    if (filterCriteria.getFirstName() != null) {
      specification = specification.and(hasFirstName(filterCriteria.getFirstName()));
    }
    if (filterCriteria.getLastName() != null) {
      specification = specification.and(hasMiddleName(filterCriteria.getMiddleName()));
    }
    if (filterCriteria.getLastName() != null) {
      specification = specification.and(hasLastName(filterCriteria.getLastName()));
    }
    if (filterCriteria.getRole() != null) {
      specification = specification.and(hasRole(filterCriteria.getRole()));
    }
    if (filterCriteria.getPhoneNumber() != null) {
      specification = specification.and(
          UserSpecification.hasPhoneNumber(filterCriteria.getPhoneNumber()));
    }

    return specification;
  }
}

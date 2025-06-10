package com.f1soft.sces;

import com.f1soft.sces.entities.User;
import com.f1soft.sces.model.FilterUser;
import com.f1soft.sces.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserSpecification {

  private final UserRepository userRepository;

  public static Specification<User> hasName(String name) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")),
            "%" + name.toLowerCase() + "%");
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
        criteriaBuilder.equal(root.get("status"), "active");
  }

  public static Specification<User> buildSpec(FilterUser filterCriteria) {
    Specification<User> specification = hasStatusActive();

    if (filterCriteria.getFullName() != null) {
      specification = specification.and(UserSpecification.hasName(filterCriteria.getFullName()));
    }
    if (filterCriteria.getRole() != null) {
      specification = specification.and(UserSpecification.
          hasRole(filterCriteria.getRole()));
    }
    if (filterCriteria.getPhoneNumber() != null) {
      specification = specification.and(
          UserSpecification.hasPhoneNumber(filterCriteria.getPhoneNumber()));
    }

    return specification;
  }
}

package com.f1soft.sces.repository;

import com.f1soft.sces.entities.User;
import com.f1soft.sces.enums.Status;
import com.f1soft.sces.model.FilterUser;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  List<User> findAllByStatus(Status status);
  Optional<User> findByEmail(String email);
  Optional<User> findByUserCode(String userCode);

  @Query("SELECT u FROM User u WHERE "
      + "LOWER(u.fullName) LIKE LOWER(CONCAT('%', :filterCriteria, '%') ) ")
  List<User> findUserBySearchText(FilterUser filterCriteria);
}

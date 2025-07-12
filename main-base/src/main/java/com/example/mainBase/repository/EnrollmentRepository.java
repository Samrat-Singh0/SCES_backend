package com.example.mainBase.repository;


import com.example.mainBase.entities.Enrollment;
import com.example.mainBase.enums.CompletionStatus;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

  @Query(value = "SELECT e from Enrollment e where e.student.id = :studentId")
  List<Enrollment> findEnrollmentForStudent(Long studentId);

  @Query(value = "SELECT e from Enrollment e where e.student.id = :studentId")
  Page<Enrollment> findEnrollmentPageForStudent(Long studentId, Pageable pageable);

  Page<Enrollment> findAll(Specification<Enrollment> spec, Pageable pageable);

  Enrollment findByCode(String code);

  @Query(value = "SELECT e from Enrollment e where e.student.id=:studentId and e.completionStatus=:completionStatus")
  List<Enrollment> findByStudent_idAndCompletionStatus(long studentId,
      CompletionStatus completionStatus);

  List<Enrollment> findAllByStudent_Id(long studentId);

  List<Enrollment> findByCompletionStatusNot(CompletionStatus completionStatus);

  Page<Enrollment> findByCompletionStatusNot(CompletionStatus completionStatus, Pageable pageable);

  List<Enrollment> findByCompletionStatus(CompletionStatus completionStatus);

}

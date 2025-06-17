package com.f1soft.sces.repository;

import com.f1soft.sces.entities.AuditLog;
import com.f1soft.sces.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

  @Query(value = "SELECT a.user FROM AuditLog a WHERE a.entityName = 'Course' AND a.action = 'CREATED' AND a.entityId = :courseId")
  User getCreator(long courseId);

}

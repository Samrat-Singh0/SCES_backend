package com.example.mainBase.repository;

import com.example.mainBase.entities.AuditLog;
import com.example.mainBase.entities.User;
import com.example.mainBase.enums.AuditAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

  @Query(value = "SELECT a.user FROM AuditLog a WHERE a.entityName = 'Course' AND a.action = 'CREATED' AND a.entityId = :courseId")
  User getCreator(long courseId);

  @Query(value = "SELECT a FROM AuditLog a WHERE a.action=:action AND a.entityId=:entityId AND a.entityName=:entityName")
  AuditLog findByEntityIdAndAction(AuditAction action, String entityName, long entityId);
}

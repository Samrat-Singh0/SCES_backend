package com.f1soft.sces.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Entity
@Data
public class Instructor {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long instructorId;

  @Column(unique = true, nullable = false)
  private String instructorCode;

  @OneToOne
  @JoinColumn(name = "user_id")
  @JsonBackReference
  private User user;

  @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Course> courses = new ArrayList<>();

}

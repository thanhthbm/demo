package com.example.demo.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "projects")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project {
  @Id
  @GeneratedValue
  private UUID id;
  private String name;
  private String description;

  @ManyToMany
  @JoinTable(
      name = "project_staffs",
      joinColumns = @JoinColumn(name = "project_id"),
      inverseJoinColumns = @JoinColumn(name = "staff_id")
  )
  private List<Staff> staffs;
}

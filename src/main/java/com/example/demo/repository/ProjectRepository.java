package com.example.demo.repository;

import com.example.demo.domain.entity.Project;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {
  boolean existsByName(String name);
}

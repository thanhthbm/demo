package com.example.demo.repository;

import com.example.demo.domain.entity.Department;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, UUID> {
  boolean existsByName(String name);
}

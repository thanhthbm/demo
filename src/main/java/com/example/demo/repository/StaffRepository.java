package com.example.demo.repository;

import com.example.demo.domain.entity.Staff;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<Staff, UUID>,
    JpaSpecificationExecutor<Staff> {

  Staff findByEmail(String email);

  boolean existsByEmail(String email);
}

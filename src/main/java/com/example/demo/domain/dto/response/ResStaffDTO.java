package com.example.demo.domain.dto.response;

import com.example.demo.domain.entity.Department;
import com.example.demo.domain.entity.Project;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResStaffDTO {
  private UUID id;
  private String name;
  private String email;
  private String phone;
  private List<Project> projects;
  private Department department;
}

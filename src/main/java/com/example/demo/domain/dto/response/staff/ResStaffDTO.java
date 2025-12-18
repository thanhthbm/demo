package com.example.demo.domain.dto.response.staff;

import com.example.demo.domain.dto.response.department.ResDepartmentDTO;
import com.example.demo.domain.dto.response.project.ResProjectDTO;
import com.example.demo.domain.dto.response.project.ResProjectStaffDTO;
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
  private List<ResProjectStaffDTO> projects;
  private ResDepartmentDTO department;
}

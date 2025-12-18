package com.example.demo.domain.dto.mapper;

import com.example.demo.domain.dto.response.department.ResDepartmentDTO;
import com.example.demo.domain.dto.response.project.ResProjectDTO;
import com.example.demo.domain.dto.response.project.ResProjectStaffDTO;
import com.example.demo.domain.dto.response.staff.ResRegisterDTO;
import com.example.demo.domain.dto.response.staff.ResStaffDTO;
import com.example.demo.domain.entity.Department;
import com.example.demo.domain.entity.Staff;
import java.util.List;

public class UserMapper {
  public static ResRegisterDTO staffToRegisterDTO(Staff staff) {
    return ResRegisterDTO.builder()
        .email(staff.getEmail())
        .phone(staff.getPhone())
        .name(staff.getName())
        .id(staff.getId())
        .build();
  }

  public static ResStaffDTO staffToStaffDTO(Staff staff) {
    Department dept = staff.getDepartment();
    ResDepartmentDTO deptDTO = null;

    if (dept != null) {
      deptDTO = ResDepartmentDTO.builder()
          .id(dept.getId())
          .name(dept.getName())
          .description(dept.getDescription())
          .build();
    }

    List<ResProjectStaffDTO> projectDTOs = null;
    if (staff.getProjects() != null) {
      projectDTOs = staff.getProjects().stream()
          .map(project -> ResProjectStaffDTO.builder()
              .id(project.getId())
              .name(project.getName())
              .description(project.getDescription())
              .build())
          .toList();
    }

    return ResStaffDTO.builder()
        .email(staff.getEmail())
        .phone(staff.getPhone())
        .name(staff.getName())
        .id(staff.getId())
        .department(deptDTO)
        .projects(projectDTOs)
        .build();
  }
}

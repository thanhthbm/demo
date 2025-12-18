package com.example.demo.domain.dto.mapper;

import com.example.demo.domain.dto.response.department.ResDepartmentDTO;
import com.example.demo.domain.dto.response.staff.ResRegisterDTO;
import com.example.demo.domain.dto.response.staff.ResStaffDTO;
import com.example.demo.domain.entity.Department;
import com.example.demo.domain.entity.Staff;

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

    return ResStaffDTO.builder()
        .email(staff.getEmail())
        .phone(staff.getPhone())
        .name(staff.getName())
        .id(staff.getId())
        .department(deptDTO)
        .projects(staff.getProjects())
        .build();
  }
}

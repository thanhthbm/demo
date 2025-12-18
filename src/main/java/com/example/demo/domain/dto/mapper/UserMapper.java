package com.example.demo.domain.dto.mapper;

import com.example.demo.domain.dto.request.ReqRegisterDTO;
import com.example.demo.domain.dto.response.ResRegisterDTO;
import com.example.demo.domain.dto.response.ResStaffDTO;
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
    return ResStaffDTO.builder()
        .email(staff.getEmail())
        .phone(staff.getPhone())
        .name(staff.getName())
        .id(staff.getId())
        .department(staff.getDepartment())
        .projects(staff.getProjects())
        .build();
  }
}

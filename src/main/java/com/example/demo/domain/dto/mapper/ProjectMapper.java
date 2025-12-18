package com.example.demo.domain.dto.mapper;

import static com.example.demo.domain.dto.mapper.UserMapper.staffToStaffDTO;

import com.example.demo.domain.dto.response.project.ResProjectDTO;
import com.example.demo.domain.dto.response.staff.ResStaffDTO;
import com.example.demo.domain.entity.Project;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectMapper {
  public static ResProjectDTO projectToProjectDTO(Project project) {
    List<ResStaffDTO> listStaffDTO = null;

    if (project.getStaffs() != null) {
      listStaffDTO = project.getStaffs().stream()
          .map(staff -> {
            ResStaffDTO dto = staffToStaffDTO(staff);
            dto.setProjects(null);
            return dto;
          })
          .collect(Collectors.toList());
    }

    return ResProjectDTO.builder()
        .id(project.getId())
        .name(project.getName())
        .description(project.getDescription())
        .staffs(listStaffDTO)
        .build();
  }
}

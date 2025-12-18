package com.example.demo.service;

import com.example.demo.domain.dto.mapper.ProjectMapper;
import com.example.demo.domain.dto.request.project.ReqProjectDTO;
import com.example.demo.domain.dto.request.project.ReqProjectStaffDTO;
import com.example.demo.domain.dto.response.ResPaginationDTO;
import com.example.demo.domain.dto.response.project.ResProjectDTO;
import com.example.demo.domain.entity.Project;
import com.example.demo.domain.entity.Staff;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.repository.StaffRepository;
import com.example.demo.util.exception.ResourceAlreadyExistException;
import com.example.demo.util.exception.ResourceNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectService {
  private final ProjectRepository projectRepository;
  private final StaffRepository staffRepository;

  public ResProjectDTO create(ReqProjectDTO reqProjectDTO) {
    if (projectRepository.existsByName(reqProjectDTO.getName())) {
      throw new ResourceAlreadyExistException("Project with name " + reqProjectDTO.getName() + " already exists");
    }

    Project p = Project.builder()
        .name(reqProjectDTO.getName())
        .description(reqProjectDTO.getDescription())
        .build();

    Project savedProject = projectRepository.save(p);

    return ProjectMapper.projectToProjectDTO(savedProject);
  }

  public ResProjectDTO update(UUID id, ReqProjectDTO req) {
    Project p = this.findById(id);
    p.setName(req.getName());
    p.setDescription(req.getDescription());
    Project updatedProject = projectRepository.save(p);
    return ProjectMapper.projectToProjectDTO(updatedProject);
  }

  public void delete(UUID id) {
    Project p = this.findById(id);
    projectRepository.delete(p);
  }

  public Project findById(UUID id) {
    return projectRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
  }

  public ResPaginationDTO getAll(Pageable pageable) {
    Page<Project> pageProjects = projectRepository.findAll(pageable);

    List<ResProjectDTO> dtos = pageProjects.getContent().stream()
        .map(ProjectMapper::projectToProjectDTO)
        .collect(Collectors.toList());

    ResPaginationDTO.Meta meta = ResPaginationDTO.Meta.builder()
        .page(pageable.getPageNumber() + 1)
        .pageSize(pageable.getPageSize())
        .pages(pageProjects.getTotalPages())
        .total((int) pageProjects.getTotalElements())
        .build();

    return ResPaginationDTO.builder()
        .meta(meta)
        .result(dtos)
        .build();
  }

  public ResProjectDTO addStaffToProject(UUID projectId, ReqProjectStaffDTO req) {
    Project project = this.findById(projectId);

    List<Staff> listStaff = staffRepository.findAllById(req.getStaffIds());

    if (listStaff.isEmpty()) {
      throw new ResourceNotFoundException("No valid staff IDs found");
    }

    List<Staff> currentStaffs = project.getStaffs();

    for (Staff s : listStaff) {
      if (!currentStaffs.contains(s)) {
        currentStaffs.add(s);
      }
    }

    project.setStaffs(currentStaffs);

    Project savedProject = projectRepository.save(project);
    return ProjectMapper.projectToProjectDTO(savedProject);
  }

  public ResProjectDTO removeStaffFromProject(UUID projectId, ReqProjectStaffDTO req) {
    Project project = this.findById(projectId);
    List<Staff> currentStaffs = project.getStaffs();

    currentStaffs.removeIf(staff -> req.getStaffIds().contains(staff.getId()));

    project.setStaffs(currentStaffs);

    Project savedProject = projectRepository.save(project);
    return ProjectMapper.projectToProjectDTO(savedProject);
  }

  public ResProjectDTO getProjectDTOById(UUID id) {
    return ProjectMapper.projectToProjectDTO(this.findById(id));
  }
}

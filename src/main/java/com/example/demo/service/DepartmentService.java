package com.example.demo.service;

import com.example.demo.domain.dto.request.department.ReqDepartmentDTO;
import com.example.demo.domain.dto.response.ResPaginationDTO;
import com.example.demo.domain.entity.Department;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.util.exception.ResourceAlreadyExistException;
import com.example.demo.util.exception.ResourceNotFoundException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DepartmentService {
  private final DepartmentRepository departmentRepository;

  public Department create(ReqDepartmentDTO reqDepartmentDTO) {
    if (departmentRepository.existsByName(reqDepartmentDTO.getName())) {
      throw new ResourceAlreadyExistException("Department with name " + reqDepartmentDTO.getName() + " already exists");
    }

    Department department = Department.builder()
        .name(reqDepartmentDTO.getName())
        .description(reqDepartmentDTO.getDescription())
        .build();

    return departmentRepository.save(department);
  }

  public Department update(UUID id, ReqDepartmentDTO reqDepartmentDTO) {
    Optional<Department> currentDepartment = this.departmentRepository.findById(id);

    if (!currentDepartment.isPresent()) {
      throw new ResourceNotFoundException("Department with id " + id + " not found");
    }

    if (!currentDepartment.get().getName().equals(reqDepartmentDTO.getName())
        && departmentRepository.existsByName(reqDepartmentDTO.getName()))
    {
      throw new ResourceAlreadyExistException("Department with name " + reqDepartmentDTO.getName() + " already exists");
    }

    currentDepartment.get().setName(reqDepartmentDTO.getName());
    currentDepartment.get().setDescription(reqDepartmentDTO.getDescription());
    return departmentRepository.save(currentDepartment.get());
  }

  public void delete(UUID id) {
    Optional<Department> currentDepartment = this.departmentRepository.findById(id);
    if (!currentDepartment.isPresent()) {
      throw new ResourceNotFoundException("Department with id " + id + " not found");
    }

    Department department = currentDepartment.get();
    if (department.getStaffs() != null && !department.getStaffs().isEmpty()) {
      throw new ResourceAlreadyExistException("Cannoit delete department that contains staff");
    }

    this.departmentRepository.delete(currentDepartment.get());
  }

  public Department findById(UUID id) {
    return departmentRepository.findById(id)
        .orElseThrow(()  -> new ResourceNotFoundException("Department with id " + id + " not found"));
  }

  public ResPaginationDTO getAll(Pageable pageable) {
    Page<Department> pageDepartments = departmentRepository.findAll(pageable);

    ResPaginationDTO.Meta meta = ResPaginationDTO.Meta.builder()
        .page(pageable.getPageNumber() + 1)
        .pageSize(pageable.getPageSize())
        .pages(pageDepartments.getTotalPages())
        .total((int) pageDepartments.getTotalElements())
        .build();

    return ResPaginationDTO.builder()
        .meta(meta)
        .result(pageDepartments.getContent())
        .build();
  }
}

package com.example.demo.service;

import com.example.demo.domain.dto.mapper.UserMapper;
import com.example.demo.domain.dto.request.staff.ReqRegisterDTO;
import com.example.demo.domain.dto.request.staff.ReqStaffUpdateDTO;
import com.example.demo.domain.dto.response.ResPaginationDTO;
import com.example.demo.domain.dto.response.staff.ResRegisterDTO;
import com.example.demo.domain.dto.response.staff.ResStaffDTO;
import com.example.demo.domain.entity.Department;
import com.example.demo.domain.entity.Staff;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.StaffRepository;
import com.example.demo.util.exception.ResourceAlreadyExistException;
import com.example.demo.util.exception.ResourceNotFoundException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StaffService {
  private final StaffRepository staffRepository;
  private final PasswordEncoder passwordEncoder;
  private final DepartmentRepository departmentRepository;

  public Staff findByUsername(String username) {
    return this.staffRepository.findByEmail(username);
  }

  public void updateRefreshToken(String refreshToken, String username) {
    Staff staff = this.findByUsername(username);

    if (staff != null) {
      staff.setRefreshToken(refreshToken);
      this.staffRepository.save(staff);
    }
  }

  public boolean existsByEmail(String email) {
    return this.staffRepository.existsByEmail(email);
  }

  public ResRegisterDTO createStaff(ReqRegisterDTO reqRegisterDTO) {
    boolean exists = this.existsByEmail(reqRegisterDTO.getEmail());

    if (exists) {
      throw new ResourceAlreadyExistException("An user with the same email already exists");
    }

    Staff staff = new Staff();
    staff.setEmail(reqRegisterDTO.getEmail());
    staff.setPassword(this.passwordEncoder.encode(reqRegisterDTO.getPassword()));
    staff.setName(reqRegisterDTO.getName());
    staff.setPhone(reqRegisterDTO.getPhone());
    Staff savedStaff = this.staffRepository.save(staff);

    return UserMapper.staffToRegisterDTO(savedStaff);

  }

  public ResStaffDTO getStaffById(UUID id){
    return UserMapper.staffToStaffDTO(this.staffRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Staff with id: " + id + " not found")));
  }

  public ResPaginationDTO getStaffs(String query, Pageable pageable) {
    Specification<Staff> spec = (root, criteriaQuery, criteriaBuilder) -> {
      if (query == null || query.isEmpty()) {
        return null;
      }

      String pattern = "%" + query + "%";

      return criteriaBuilder.or(
          criteriaBuilder.like(root.get("name"), pattern),
          criteriaBuilder.like(root.get("phone"), pattern),
          criteriaBuilder.like(root.get("email"), pattern)
      );
    };

    Page<Staff> staffPage = this.staffRepository.findAll(spec, pageable);

    List<ResStaffDTO> listStaffDTO = staffPage.getContent().stream()
        .map(staff -> UserMapper.staffToStaffDTO(staff))
        .collect(Collectors.toList());

    ResPaginationDTO.Meta meta = ResPaginationDTO.Meta.builder()
        .page(pageable.getPageNumber() + 1)
        .pageSize(pageable.getPageSize())
        .pages(staffPage.getTotalPages())
        .total((int) staffPage.getTotalElements())
        .build();

    return ResPaginationDTO.builder()
        .meta(meta)
        .result(listStaffDTO)
        .build();
  }


  public ResStaffDTO updateStaff(UUID id, ReqStaffUpdateDTO req) {
    Staff currentStaff = this.staffRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + id));

    if (req.getName() != null) currentStaff.setName(req.getName());
    if (req.getPhone() != null) currentStaff.setPhone(req.getPhone());

    if (req.getDepartmentId() != null) {
      Department department = departmentRepository.findById(req.getDepartmentId())
          .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + req.getDepartmentId()));

      currentStaff.setDepartment(department);
    } else {
      currentStaff.setDepartment(null);
    }

    Staff saved = this.staffRepository.save(currentStaff);
    return UserMapper.staffToStaffDTO(saved);
  }

  public void deleteStaff(UUID id) {
    Staff staff = this.staffRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + id));
    this.staffRepository.delete(staff);
  }




}

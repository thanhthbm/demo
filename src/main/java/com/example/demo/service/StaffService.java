package com.example.demo.service;

import com.example.demo.domain.dto.mapper.UserMapper;
import com.example.demo.domain.dto.request.ReqRegisterDTO;
import com.example.demo.domain.dto.response.ResRegisterDTO;
import com.example.demo.domain.dto.response.ResStaffDTO;
import com.example.demo.domain.entity.Staff;
import com.example.demo.repository.StaffRepository;
import com.example.demo.util.exception.ResourceAlreadyExistException;
import com.example.demo.util.exception.ResourceNotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StaffService {
  private final StaffRepository staffRepository;
  private final PasswordEncoder passwordEncoder;

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
}

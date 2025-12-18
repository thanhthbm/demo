package com.example.demo.controller;

import com.example.demo.domain.dto.response.ResStaffDTO;
import com.example.demo.service.StaffService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/staffs")
@RequiredArgsConstructor
public class StaffController {
  private final StaffService staffService;

  @GetMapping("/{id}")
  public ResponseEntity<ResStaffDTO> getStaffById(@PathVariable UUID id) {
    return ResponseEntity.ok()
        .body(staffService.getStaffById(id));
  }
}

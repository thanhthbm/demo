package com.example.demo.controller;

import com.example.demo.domain.dto.request.staff.ReqStaffUpdateDTO;
import com.example.demo.domain.dto.response.ResPaginationDTO;
import com.example.demo.domain.dto.response.staff.ResStaffDTO;
import com.example.demo.service.StaffService;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

  @GetMapping()
  public ResponseEntity<ResPaginationDTO> getStaffs(
      @RequestParam(value = "query", required = false) String query,
      @PageableDefault(page = 1, size = 10)
      Pageable pageable
  ) {
    return ResponseEntity.ok(this.staffService.getStaffs(query, pageable));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ResStaffDTO> updateStaff(@PathVariable UUID id, @RequestBody ReqStaffUpdateDTO req) {
    return ResponseEntity.ok(this.staffService.updateStaff(id, req));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteStaff(@PathVariable UUID id) {
    this.staffService.deleteStaff(id);
    return ResponseEntity.ok().build();
  }
}

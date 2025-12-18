package com.example.demo.controller;

import com.example.demo.domain.dto.request.department.ReqDepartmentDTO;
import com.example.demo.domain.entity.Department;
import com.example.demo.service.DepartmentService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/departments")
public class DepartmentController {
  private final DepartmentService departmentService;

  @PostMapping
  public ResponseEntity<Department> create(@RequestBody ReqDepartmentDTO reqDepartmentDTO) {
    return ResponseEntity.ok(departmentService.create(reqDepartmentDTO));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Department> update(@PathVariable UUID id, @RequestBody ReqDepartmentDTO reqDepartmentDTO) {
    return ResponseEntity.ok(departmentService.update(id, reqDepartmentDTO));
  }

  @GetMapping("/{id}")
  public ResponseEntity<Department> getById(@PathVariable UUID id) {
    return ResponseEntity.ok(departmentService.findById(id));
  }

  @GetMapping
  public ResponseEntity<Object> getAll(Pageable pageable) {
    return ResponseEntity.ok(departmentService.getAll(pageable));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    this.departmentService.delete(id);
    return ResponseEntity.noContent().build();
  }
}

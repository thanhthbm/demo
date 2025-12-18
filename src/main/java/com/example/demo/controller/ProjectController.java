package com.example.demo.controller;

import com.example.demo.domain.dto.request.project.ReqProjectDTO;
import com.example.demo.domain.dto.request.project.ReqProjectStaffDTO;
import com.example.demo.domain.dto.response.ResPaginationDTO;
import com.example.demo.domain.dto.response.project.ResProjectDTO;
import com.example.demo.service.ProjectService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/projects")
public class ProjectController {
  private final ProjectService projectService;

  @PostMapping
  public ResponseEntity<ResProjectDTO> create(@RequestBody ReqProjectDTO req) {
    return ResponseEntity.status(HttpStatus.CREATED).body(projectService.create(req));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ResProjectDTO> update(@PathVariable UUID id, @RequestBody ReqProjectDTO req) {
    return ResponseEntity.ok(projectService.update(id, req));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ResProjectDTO> getById(@PathVariable UUID id) {
    return ResponseEntity.ok(projectService.getProjectDTOById(id));
  }

  @GetMapping
  public ResponseEntity<ResPaginationDTO> getAll(Pageable pageable) {
    return ResponseEntity.ok(projectService.getAll(pageable));
  }

  @PostMapping("/{id}/staffs")
  public ResponseEntity<ResProjectDTO> addStaffs(@PathVariable UUID id, @RequestBody ReqProjectStaffDTO req) {
    return ResponseEntity.ok(projectService.addStaffToProject(id, req));
  }

  @DeleteMapping("/{id}/staffs")
  public ResponseEntity<ResProjectDTO> removeStaffs(@PathVariable UUID id, @RequestBody ReqProjectStaffDTO req) {
    return ResponseEntity.ok(projectService.removeStaffFromProject(id, req));
  }
}

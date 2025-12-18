package com.example.demo.domain.dto.request.department;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReqDepartmentDTO {
  private String name;
  private String description;
}

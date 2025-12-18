package com.example.demo.domain.dto.response.department;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResDepartmentDTO {
  private UUID id;
  private String name;
  private String description;
}

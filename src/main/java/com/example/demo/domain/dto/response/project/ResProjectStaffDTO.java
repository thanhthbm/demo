package com.example.demo.domain.dto.response.project;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResProjectStaffDTO {
  private UUID id;
  private String name;
  private String description;
}

package com.example.demo.domain.dto.response.project;

import com.example.demo.domain.dto.response.staff.ResStaffDTO;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResProjectDTO {
  private UUID id;
  private String name;
  private String description;

  private List<ResStaffDTO> staffs;
}

package com.example.demo.domain.dto.request.staff;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReqStaffUpdateDTO {
  private String name;
  private String phone;
  private UUID departmentId;
}

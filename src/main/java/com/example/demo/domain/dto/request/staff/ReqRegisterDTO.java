package com.example.demo.domain.dto.request.staff;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReqRegisterDTO {
  private String name;
  private String email;
  private String password;
  private String phone;
}

package com.example.demo.domain.dto.response;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResRegisterDTO {
  private UUID id;
  private String name;
  private String email;
  private String phone;
}

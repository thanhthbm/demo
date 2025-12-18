package com.example.demo.domain.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReqLoginDTO {
  private String username;
  private String password;
}

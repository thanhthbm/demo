package com.example.demo.domain.dto.response;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResLoginDTO {
  private String accessToken;
  private UserLogin user;

  @Data
  @Builder
  public static class UserLogin{
    private UUID id;
    private String email;
    private String phone;
    private String name;
  }
}

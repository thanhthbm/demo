package com.example.demo.domain.dto.response.staff;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

package com.example.demo.service;

import com.example.demo.domain.dto.request.staff.ReqLoginDTO;
import com.example.demo.domain.dto.response.staff.ResLoginDTO;
import com.example.demo.domain.dto.response.staff.ResLoginDTO.UserLogin;
import com.example.demo.domain.entity.Staff;
import com.example.demo.util.SecurityUtil;
import com.example.demo.util.exception.IdInvalidException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final AuthenticationManagerBuilder authenticationManagerBuilder;
  private final StaffService staffService;
  private final SecurityUtil securityUtil;

  public ResLoginDTO login(ReqLoginDTO loginDTO) {
    UsernamePasswordAuthenticationToken authenticationToken =new UsernamePasswordAuthenticationToken(
        loginDTO.getUsername(),
        loginDTO.getPassword()
    );

    Authentication authentication =  authenticationManagerBuilder.getObject().authenticate(authenticationToken);

    SecurityContextHolder.getContext().setAuthentication(authentication);

    Staff staff = staffService.findByUsername(loginDTO.getUsername());

    ResLoginDTO resLoginDTO = ResLoginDTO.builder()
        .user(
            UserLogin.builder()
                .id(staff.getId())
                .email(staff.getEmail())
                .name(staff.getName())
                .phone(staff.getPhone())
                .build()
        )
        .build();
    String accessToken = this.securityUtil.createAccessToken(resLoginDTO);
    resLoginDTO.setAccessToken(accessToken);

    return resLoginDTO;
  }

  public ResLoginDTO refreshToken(String refreshToken)
      throws IdInvalidException {
    if ("default".equals(refreshToken)) {
      throw new IdInvalidException("Invalid refresh token");
    }

    Jwt decodedToken = this.securityUtil.checkValidRefreshToken(refreshToken);
    String email = decodedToken.getSubject();

    Staff staff = staffService.findByUsername(email);
    if (staff == null) {
      throw new IdInvalidException("Invalid access token");
    }

    ResLoginDTO resLoginDTO = ResLoginDTO.builder()
        .user(ResLoginDTO.UserLogin.builder()
            .id(staff.getId())
            .email(staff.getEmail())
            .name(staff.getName())
            .phone(staff.getPhone())
            .build())
        .build();

    String accessToken = this.securityUtil.createAccessToken(resLoginDTO);

    resLoginDTO.setAccessToken(accessToken);

    return resLoginDTO;
  }

}

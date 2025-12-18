package com.example.demo.controller;

import com.example.demo.domain.dto.request.ReqLoginDTO;
import com.example.demo.domain.dto.request.ReqRegisterDTO;
import com.example.demo.domain.dto.response.ResLoginDTO;
import com.example.demo.domain.dto.response.ResLoginDTO.UserLogin;
import com.example.demo.domain.dto.response.ResRegisterDTO;
import com.example.demo.domain.entity.Staff;
import com.example.demo.service.StaffService;
import com.example.demo.util.SecurityUtil;
import com.example.demo.util.exception.IdInvalidException;
import com.example.demo.util.exception.ResourceAlreadyExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthenticationManagerBuilder authenticationManagerBuilder;
  private final SecurityUtil securityUtil;
  private final StaffService staffService;
  private final PasswordEncoder passwordEncoder;

  @Value("${thanhthbm.jwt.refresh-token-validity-in-seconds}")
  private int refreshTokenExpiresInSeconds;

  @PostMapping("/login")
  public ResponseEntity<ResLoginDTO> login(@RequestBody ReqLoginDTO loginDTO) {
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

    String refreshToken = this.securityUtil.createRefreshToken(resLoginDTO);
    ResponseCookie resCookies = ResponseCookie
        .from("refresh_token", refreshToken)
        .httpOnly(true)
        .secure(true)
        .path("/")
        .maxAge(refreshTokenExpiresInSeconds)
        .build();

    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, resCookies.toString()).body(resLoginDTO);
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout() {
    String email = SecurityUtil.getCurrentUserLogin().isPresent()
        ? SecurityUtil.getCurrentUserLogin().get() : "";
    if ("".equals(email)) {
      throw new IdInvalidException("Invalid access token");
    }

    this.staffService.updateRefreshToken(null, email);

    ResponseCookie deleteSpringCookie = ResponseCookie
        .from("refresh_token", null)
        .httpOnly(true)
        .secure(true)
        .path("/")
        .maxAge(0)
        .build();

    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, deleteSpringCookie.toString())
        .body(null);
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody ReqRegisterDTO reqRegisterDTO) {
    ResRegisterDTO dto = this.staffService.createStaff(reqRegisterDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(dto);
  }
}

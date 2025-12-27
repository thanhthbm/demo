package com.example.demo.controller;

import com.example.demo.domain.dto.request.staff.ReqLoginDTO;
import com.example.demo.domain.dto.request.staff.ReqRegisterDTO;
import com.example.demo.domain.dto.response.staff.ResLoginDTO;
import com.example.demo.domain.dto.response.staff.ResRegisterDTO;
import com.example.demo.service.AuthService;
import com.example.demo.service.StaffService;
import com.example.demo.util.SecurityUtil;
import com.example.demo.util.exception.IdInvalidException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
  private final SecurityUtil securityUtil;
  private final StaffService staffService;
  private final AuthService authService;

  @Value("${thanhthbm.jwt.refresh-token-validity-in-seconds}")
  private int refreshTokenExpiresInSeconds;

  @PostMapping("/login")
  public ResponseEntity<ResLoginDTO> login(@RequestBody ReqLoginDTO loginDTO)
      throws InterruptedException {


    ResLoginDTO resLoginDTO = this.authService.login(loginDTO);

    String refreshToken = this.securityUtil.createRefreshToken(resLoginDTO);
    staffService.updateRefreshToken(refreshToken, loginDTO.getUsername());

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
  public ResponseEntity<Void> logout() throws InterruptedException {
    String email = SecurityUtil.getCurrentUserLogin().isPresent()
        ? SecurityUtil.getCurrentUserLogin().get() : "";
    if (email.isEmpty()) {
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
  public ResponseEntity<ResRegisterDTO> register(@RequestBody ReqRegisterDTO reqRegisterDTO) {
    ResRegisterDTO dto = this.staffService.createStaff(reqRegisterDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(dto);
  }

  @GetMapping("/refresh")
  public ResponseEntity<ResLoginDTO> refresh(@CookieValue(name = "refresh_token", defaultValue = "default") String refreshToken)
      throws IdInvalidException, InterruptedException {

    ResLoginDTO resLoginDTO = this.authService.refreshToken(refreshToken);

    String newRefreshToken = this.securityUtil.createRefreshToken(resLoginDTO);
    this.staffService.updateRefreshToken(newRefreshToken, resLoginDTO.getUser().getEmail());

    ResponseCookie resCookies = ResponseCookie
        .from("refresh_token", newRefreshToken)
        .httpOnly(true)
        .secure(true)
        .path("/")
        .maxAge(refreshTokenExpiresInSeconds)
        .build();

    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, resCookies.toString()).body(resLoginDTO);
  }
}

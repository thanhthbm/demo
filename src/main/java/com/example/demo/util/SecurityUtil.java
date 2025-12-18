package com.example.demo.util;

import com.example.demo.domain.dto.response.staff.ResLoginDTO;
import com.nimbusds.jose.util.Base64;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Service;

@Service
public class SecurityUtil {
  private final JwtEncoder jwtEncoder;

  public SecurityUtil(JwtEncoder jwtEncoder) {
    this.jwtEncoder = jwtEncoder;
  }

  @Value("${thanhthbm.jwt.base64-secret}")
  private String jwtKey;

  public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS256;

  @Value("${thanhthbm.jwt.access-token-validity-in-seconds}")
  private int accessTokenExpiresInSeconds;

  @Value("${thanhthbm.jwt.refresh-token-validity-in-seconds}")
  private int refreshTokenExpiresInSeconds;

  private SecretKey getSecretKey() {
    byte[] keyBytes = Base64.from(jwtKey).decode();
    return new SecretKeySpec(keyBytes, 0, keyBytes.length,
        JWT_ALGORITHM.getName());
  }

  public String createAccessToken(ResLoginDTO dto) {
    Instant now = Instant.now();
    Instant validity = now.plus(this.accessTokenExpiresInSeconds, ChronoUnit.SECONDS);

    JwtClaimsSet claimsSet =JwtClaimsSet.builder()
        .issuedAt(now)
        .expiresAt(validity)
        .subject(dto.getUser().getEmail())
        .claim("user", dto)
        .build();

    JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
    return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claimsSet)).getTokenValue();
  }

  public String createRefreshToken(ResLoginDTO dto) {
    Instant now = Instant.now();
    Instant validity = now.plus(this.refreshTokenExpiresInSeconds, ChronoUnit.SECONDS);

    JwtClaimsSet claimsSet =JwtClaimsSet.builder()
        .issuedAt(now)
        .expiresAt(validity)
        .subject(dto.getUser().getEmail())
        .claim("user", dto)
        .build();

    JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
    return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claimsSet)).getTokenValue();
  }

  public Jwt checkValidRefreshToken(String token){
    NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(
        getSecretKey()).macAlgorithm(SecurityUtil.JWT_ALGORITHM).build();
    try {
      return jwtDecoder.decode(token);
    } catch (Exception e) {
      System.out.println(">>> Refresh Token error: " + e.getMessage());
      throw e;
    }
  }

  public static Optional<String> getCurrentUserLogin() {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
  }

  private static String extractPrincipal(Authentication authentication) {
    if (authentication == null) {
      return null;
    } else if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) {
      return springSecurityUser.getUsername();
    } else if (authentication.getPrincipal() instanceof Jwt jwt) {
      return jwt.getSubject();
    } else if (authentication.getPrincipal() instanceof String s) {
      return s;
    }
    return null;
  }
}

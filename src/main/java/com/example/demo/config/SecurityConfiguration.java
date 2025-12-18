package com.example.demo.config;

import com.example.demo.util.SecurityUtil;
import com.nimbusds.jose.util.Base64;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

  @Value("${thanhthbm.jwt.base64-secret}")
  private String jwtKey;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http,
      CustomAuthenticationEntryPoint customAuthenticationEntryPoint) throws Exception {
    String[] whiteList = {
        "/api/auth/login",
        "/api/auth/register",
    };

    http
        .csrf(c -> c.disable())
        .cors(Customizer.withDefaults())
        .authorizeHttpRequests(
            auth -> auth
                .requestMatchers(whiteList).permitAll()
                .anyRequest().authenticated()
        )
        .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults())
            .authenticationEntryPoint(customAuthenticationEntryPoint))
        .formLogin(form -> form.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    return http.build();
  }

  @Bean
  public JwtDecoder jwtDecoder() {
    NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(
        getSecretKey()).macAlgorithm(SecurityUtil.JWT_ALGORITHM).build();
    return token -> {
      try {
        return jwtDecoder.decode(token);
      } catch (Exception e) {
        System.out.println(">>> JWT error: " + e.getMessage());
        throw e;
      }
    };
  }

  private SecretKey getSecretKey() {
    byte[] keyBytes = Base64.from(jwtKey).decode();
    return new SecretKeySpec(keyBytes, 0, keyBytes.length,
        SecurityUtil.JWT_ALGORITHM.getName());
  }
}

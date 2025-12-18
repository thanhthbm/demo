package com.example.demo.config;

import com.example.demo.domain.entity.Staff;
import com.example.demo.service.StaffService;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component("userDetailService")
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
  private final StaffService staffService;
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Staff staff = staffService.findByUsername(username);

    if (staff == null) {
      throw new UsernameNotFoundException(username +  " not found");
    }

    return new User(
      staff.getEmail(),
        staff.getPassword(),
        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
    );
  }
}

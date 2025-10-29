package ch.teko.tablecupbackend.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityContextService {

  public String getRequestingUser() {
    return SecurityContextHolder.getContext().getAuthentication().getName();
  }
}

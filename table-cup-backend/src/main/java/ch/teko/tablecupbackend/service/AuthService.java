package ch.teko.tablecupbackend.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import ch.teko.tablecupbackend.dto.LoginDTO;
import ch.teko.tablecupbackend.entity.User;
import ch.teko.tablecupbackend.model.UserModel;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final DaoAuthenticationProvider authenticationProvider;

  public ResponseEntity<UserModel> login(LoginDTO loginDTO) {
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
        loginDTO.getUsername(), loginDTO.getPassword());
    Authentication authentication;
    try {
      authentication = authenticationProvider.authenticate(authenticationToken);
    } catch (AuthenticationException authenticationException) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(authentication);
    SecurityContextHolder.setContext(context);
    User userPrincipal = (User) authentication.getPrincipal();
    UserModel userModel = new UserModel(userPrincipal.getUsername(), userPrincipal.getRole().getInternalName().name());
    return ResponseEntity.ok(userModel);
  }
}

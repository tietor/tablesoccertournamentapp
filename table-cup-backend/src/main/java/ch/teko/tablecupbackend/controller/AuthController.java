package ch.teko.tablecupbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.teko.tablecupbackend.dto.LoginDTO;
import ch.teko.tablecupbackend.model.UserModel;
import ch.teko.tablecupbackend.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/auth")
@Slf4j
public class AuthController {

  private final AuthService authService;

  @PostMapping("/sessions")
  public ResponseEntity<UserModel> login(@RequestBody @Valid LoginDTO loginDTO) {
    return authService.login(loginDTO);
  }

  @GetMapping("/sessions/invalid")
  public ResponseEntity<Void> invalidSession() {
    log.info("invalidSession called");
    return ResponseEntity.ok().build();
  }

  @GetMapping("/sessions/expired")
  public ResponseEntity<Void> expiredSession() {
    log.info("expiredSession called");
    return ResponseEntity.ok().build();
  }

}

package ch.teko.tablecupbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.teko.tablecupbackend.dto.RegisterDTO;
import ch.teko.tablecupbackend.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/users")
@Validated
@Slf4j
public class UserController {

  private final UserService userService;

  @PostMapping
  public ResponseEntity<Void> registerUser(@Valid @RequestBody RegisterDTO registerDTO) {
    return userService.registerUser(registerDTO);
  }
}

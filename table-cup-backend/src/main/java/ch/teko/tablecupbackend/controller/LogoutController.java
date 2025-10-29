package ch.teko.tablecupbackend.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("/logout")
@Slf4j
public class LogoutController {


  @GetMapping("/success")
  public void logoutSuccessful() {
    log.info("User successfully logged out");
  }
}

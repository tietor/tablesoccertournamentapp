package ch.teko.tablecupbackend.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.teko.tablecupbackend.dto.GameResultDTO;
import ch.teko.tablecupbackend.service.GameService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@Validated
@RequestMapping("/games")
@Slf4j
public class GameController {

  private final GameService gameService;

  @PostMapping("{gameUuid}/result")
  public ResponseEntity<Void> addResultToGame(@PathVariable UUID gameUuid,
      @Valid @RequestBody GameResultDTO gameResultDTO) {
    return gameService.addResultToGame(gameUuid, gameResultDTO);
  }

}

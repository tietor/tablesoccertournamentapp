package ch.teko.tablecupbackend.exception;

import java.util.UUID;

public class GameNotCompletedException extends RuntimeException {

  public GameNotCompletedException(UUID uuid) {
    super("Game with UUID " + uuid + " not completed");
  }
}

package ch.teko.tablecupbackend.exception;

import java.util.UUID;

public class TournamentNotFoundException extends RuntimeException {

  public TournamentNotFoundException(UUID uuid) {
    super("Turnier mit UUID " + uuid + " existiert nicht!");
  }
}

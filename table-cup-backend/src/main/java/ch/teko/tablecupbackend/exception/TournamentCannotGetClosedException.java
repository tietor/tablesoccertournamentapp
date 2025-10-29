package ch.teko.tablecupbackend.exception;

import java.util.UUID;

public class TournamentCannotGetClosedException extends RuntimeException {

  public TournamentCannotGetClosedException(UUID tournamentUUID) {
    super("Cannot close tournament if phase is not final. TournamentUUID: " + tournamentUUID);
  }
}

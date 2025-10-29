package ch.teko.tablecupbackend.exception;

public class UserNotInTournamentException extends RuntimeException {

  public UserNotInTournamentException(String user, int tournamentId) {
    super("Benutzer " + user + " ist nicht in Turnier " + tournamentId + "!");
  }
}

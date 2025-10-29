package ch.teko.tablecupbackend.exception;

public class TournamentAlreadyExistsException extends RuntimeException {

  public TournamentAlreadyExistsException(String tournament) {
    super("Turnier " + tournament + " existiert bereits!");
  }
}

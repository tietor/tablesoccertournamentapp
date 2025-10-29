package ch.teko.tablecupbackend.exception;

public class WrongAmountOfPlayersException extends RuntimeException {

  public WrongAmountOfPlayersException(int amount) {
    super("Falsche Anzahl von Spielern: " + amount + "!");
  }

}

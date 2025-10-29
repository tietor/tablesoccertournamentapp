package ch.teko.tablecupbackend.exception;

public class UsernameAlreadyExistsException extends RuntimeException {

  public UsernameAlreadyExistsException(String username) {
    super("Benutzer" + username + " existiert bereits!");
  }


}

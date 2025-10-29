package ch.teko.tablecupbackend.exception;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(String username) {
    super("Benutzer " + username + " existiert nicht!");
  }
}

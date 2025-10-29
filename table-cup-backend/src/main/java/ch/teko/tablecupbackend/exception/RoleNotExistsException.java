package ch.teko.tablecupbackend.exception;

public class RoleNotExistsException extends RuntimeException {

  public RoleNotExistsException(String message) {
    super(message);
  }
}

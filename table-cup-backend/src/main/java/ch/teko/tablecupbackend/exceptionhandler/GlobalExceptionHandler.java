package ch.teko.tablecupbackend.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import ch.teko.tablecupbackend.exception.GameNotCompletedException;
import ch.teko.tablecupbackend.exception.GameNotFoundException;
import ch.teko.tablecupbackend.exception.PhaseNotFoundException;
import ch.teko.tablecupbackend.exception.RoleNotExistsException;
import ch.teko.tablecupbackend.exception.StatusNotFoundException;
import ch.teko.tablecupbackend.exception.TournamentAlreadyExistsException;
import ch.teko.tablecupbackend.exception.TournamentCannotGetClosedException;
import ch.teko.tablecupbackend.exception.TournamentNotFoundException;
import ch.teko.tablecupbackend.exception.UserNotFoundException;
import ch.teko.tablecupbackend.exception.UserNotInTournamentException;
import ch.teko.tablecupbackend.exception.UsernameAlreadyExistsException;
import ch.teko.tablecupbackend.exception.WrongAmountOfPlayersException;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> fallbackExceptionHandler() {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ein Serverfehler ist aufgetreten!");
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<String> handleMethodArgumentNotValidException() {
    return ResponseEntity.badRequest().body("Ungültige Eingaben!");
  }

  @ExceptionHandler(WrongAmountOfPlayersException.class)
  public ResponseEntity<String> handleWrongAmountOfPlayersException() {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Falsche Anzahl von Spielern, um Turnier zu starten!");
  }

  @ExceptionHandler(UserNotInTournamentException.class)
  public ResponseEntity<String> handleUserNotInTournamentException() {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Du bist nicht in diesem Turnier!");
  }

  @ExceptionHandler(GameNotFoundException.class)
  public ResponseEntity<String> handleGameNotFoundException() {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Das Spiel wurde nicht gefunden!");
  }

  @ExceptionHandler(GameNotCompletedException.class)
  public ResponseEntity<String> handleGameNotCompletedException() {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Es wurden nicht alle Spiele abgeschlossen!");
  }

  @ExceptionHandler(StatusNotFoundException.class)
  public ResponseEntity<String> handleStatusNotFoundException() {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Status existiert nicht!");
  }

  @ExceptionHandler(TournamentAlreadyExistsException.class)
  public ResponseEntity<String> handleTournamentAlreadyExistsException() {
    return ResponseEntity.status(HttpStatus.CONFLICT).body("Turnier existiert bereits!");
  }

  @ExceptionHandler(RoleNotExistsException.class)
  public ResponseEntity<String> handleRoleNotExistsException() {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ausgewählte Rolle existiert nicht!");
  }

  @ExceptionHandler(UsernameAlreadyExistsException.class)
  public ResponseEntity<String> handleUsernameAlreadyExistsException() {
    return ResponseEntity.status(HttpStatus.CONFLICT).body("Benutzername existiert bereits!");
  }

  @ExceptionHandler(TournamentCannotGetClosedException.class)
  public ResponseEntity<String> handleTournamentCannotGetClosedException() {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Turnier kann nach dem Finale abgeschlossen werden!");
  }

  @ExceptionHandler(PhaseNotFoundException.class)
  public ResponseEntity<String> handlePhaseNotFoundException() {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Phase existiert nicht!");
  }

  @ExceptionHandler(TournamentNotFoundException.class)
  public ResponseEntity<String> handleTournamentNotFoundException() {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Turnier existiert nicht!");
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<String> handleUserNotFoundException() {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Benutzer existiert nicht!");
  }

}

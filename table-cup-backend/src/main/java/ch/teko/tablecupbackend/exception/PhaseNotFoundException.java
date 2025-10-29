package ch.teko.tablecupbackend.exception;

import ch.teko.tablecupbackend.constant.TournamentPhase;

public class PhaseNotFoundException extends RuntimeException {

  public PhaseNotFoundException(TournamentPhase phase) {
    super("Phase " + phase.name() + " existiert nicht!");
  }
}

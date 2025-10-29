package ch.teko.tablecupbackend.exception;

import ch.teko.tablecupbackend.constant.TournamentStatus;

public class StatusNotFoundException extends RuntimeException {

  public StatusNotFoundException(TournamentStatus tournamentStatus) {
        super("Status " + tournamentStatus + " existiert nicht!");
    }

}

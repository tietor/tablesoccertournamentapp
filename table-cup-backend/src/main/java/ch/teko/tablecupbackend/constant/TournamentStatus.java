package ch.teko.tablecupbackend.constant;

import lombok.Getter;

@Getter
public enum TournamentStatus {
  OPEN("Offen"), RUNNING("Laufend"), DONE("Abgeschlossen");

  private final String displayValue;

  TournamentStatus(String displayValue) {
    this.displayValue = displayValue;
  }
}

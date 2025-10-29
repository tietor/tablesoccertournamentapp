package ch.teko.tablecupbackend.constant;

import lombok.Getter;

@Getter
public enum TournamentPhase {
  PRELIMINARY_ROUND("Vorrunde"), QUALIFYING_ROUND("Qualifikationsrunde"), ROUND_OF_SIXTEEN(
      "Achtelfinale"), QUARTERFINALS("Viertelfinale"), SEMIFINAL("Halbfinale"), FINAL("Finale");

  private final String displayValue;

  TournamentPhase(String displayValue) {
    this.displayValue = displayValue;
  }
}

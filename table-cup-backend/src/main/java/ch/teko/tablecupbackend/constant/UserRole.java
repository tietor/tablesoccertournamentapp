package ch.teko.tablecupbackend.constant;

import lombok.Getter;

@Getter
public enum UserRole {
  TOURNAMENT_DIRECTOR("Turnierleiter"), ATTENDEE("Teilnehmer");

  public final String displayValue;

  UserRole(String displayValue) {
    this.displayValue = displayValue;
  }
}

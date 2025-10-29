package ch.teko.tablecupbackend.model;

import java.util.UUID;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class TournamentModel {

  private final UUID uuid;
  private final String name;
  private final StatusModel status;
  private final PhaseModel phase;
  private boolean isUserAllowedToJoinTournament = false;
  private boolean isUserInTournament = false;
  private boolean isUserDisqualified = false;
}

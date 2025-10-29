package ch.teko.tablecupbackend.model;

import java.util.Set;
import java.util.UUID;

public record GameModel(UUID uuid, int pointsOfTeamBlue, int pointsOfTeamRed, Set<String> playersOfBlueTeam,
                        Set<String> playersOfRedTeam) {

}

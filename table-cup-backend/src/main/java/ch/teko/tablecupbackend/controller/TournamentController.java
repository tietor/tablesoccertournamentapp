package ch.teko.tablecupbackend.controller;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.teko.tablecupbackend.dto.TournamentDTO;
import ch.teko.tablecupbackend.model.GameModel;
import ch.teko.tablecupbackend.model.ParticipantModel;
import ch.teko.tablecupbackend.model.TournamentModel;
import ch.teko.tablecupbackend.service.GameService;
import ch.teko.tablecupbackend.service.TournamentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("/tournaments")
@EnableMethodSecurity()
public class TournamentController {

  private final TournamentService tournamentService;
  private final GameService gameService;

  @PostMapping
  @PreAuthorize("hasAuthority('TOURNAMENT_DIRECTOR')")
  public ResponseEntity<Void> createTournament(@Valid @RequestBody TournamentDTO tournamentDTO) {
    return tournamentService.createTournament(tournamentDTO);
  }

  @GetMapping
  public List<TournamentModel> getAllTournaments() {
    return tournamentService.getAllTournaments();
  }

  @GetMapping("/{tournamentUuid}/games")
  public ResponseEntity<Set<GameModel>> getGamesOfTournamentAndCurrentPhase(@PathVariable UUID tournamentUuid) {
    return gameService.getGamesOfTournamentAndCurrentPhase(tournamentUuid);
  }

  @PostMapping("/{tournamentUuid}/participants")
  @PreAuthorize("hasAuthority('ATTENDEE')")
  public ResponseEntity<Void> joinTournament(@PathVariable UUID tournamentUuid) {
    return tournamentService.joinTournament(tournamentUuid);
  }

  @GetMapping("/{tournamentUuid}/participants/all")
  public ResponseEntity<Set<ParticipantModel>> getAllParticipantsOfTournament(@PathVariable UUID tournamentUuid) {
    return tournamentService.getAllParticipantsOfTournament(tournamentUuid);
  }

  @GetMapping("/{tournamentUuid}/participants/current")
  public ResponseEntity<Set<ParticipantModel>> getParticipantsInCurrentPhase(@PathVariable UUID tournamentUuid) {
    return tournamentService.getParticipantsOfTournamentOfCurrentPhase(tournamentUuid);
  }

  @PostMapping("/{tournamentUuid}/start")
  @PreAuthorize("hasAuthority('TOURNAMENT_DIRECTOR')")
  public ResponseEntity<Void> startTournament(@PathVariable UUID tournamentUuid) {
    return tournamentService.startTournament(tournamentUuid);
  }

  @PostMapping("{tournamentUuid}/phases/next")
  @PreAuthorize("hasAuthority('TOURNAMENT_DIRECTOR')")
  public ResponseEntity<Void> nextPhase(@PathVariable UUID tournamentUuid) {
    return tournamentService.startNextPhaseTournament(tournamentUuid);
  }

  @PutMapping("{tournamentUuid}/close")
  @PreAuthorize("hasAuthority('TOURNAMENT_DIRECTOR')")
  public ResponseEntity<Void> closeTournament(@PathVariable UUID tournamentUuid) {
    return tournamentService.closeTournament(tournamentUuid);
  }

}

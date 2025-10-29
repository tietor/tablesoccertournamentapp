package ch.teko.tablecupbackend.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ch.teko.tablecupbackend.constant.TournamentPhase;
import ch.teko.tablecupbackend.constant.TournamentStatus;
import ch.teko.tablecupbackend.constant.UserRole;
import ch.teko.tablecupbackend.dto.TournamentDTO;
import ch.teko.tablecupbackend.entity.Game;
import ch.teko.tablecupbackend.entity.Phase;
import ch.teko.tablecupbackend.entity.Status;
import ch.teko.tablecupbackend.entity.Tournament;
import ch.teko.tablecupbackend.entity.User;
import ch.teko.tablecupbackend.exception.StatusNotFoundException;
import ch.teko.tablecupbackend.exception.TournamentAlreadyExistsException;
import ch.teko.tablecupbackend.exception.TournamentCannotGetClosedException;
import ch.teko.tablecupbackend.exception.TournamentNotFoundException;
import ch.teko.tablecupbackend.exception.UserNotFoundException;
import ch.teko.tablecupbackend.exception.WrongAmountOfPlayersException;
import ch.teko.tablecupbackend.model.ParticipantModel;
import ch.teko.tablecupbackend.model.PhaseModel;
import ch.teko.tablecupbackend.model.StatusModel;
import ch.teko.tablecupbackend.model.TournamentModel;
import ch.teko.tablecupbackend.repository.StatusRepository;
import ch.teko.tablecupbackend.repository.TournamentRepository;
import ch.teko.tablecupbackend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TournamentService {

  private final TournamentRepository tournamentRepository;
  private final StatusRepository statusRepository;
  private final UserRepository userRepository;
  private final SecurityContextService securityContextService;
  private final PhaseService phaseService;
  private final GameService gameService;
  private static final int MAX_PLAYERS_PER_TOURNAMENT = 64;
  private static final int MIN_PLAYERS_PER_TOURNAMENT = 36;

  public ResponseEntity<Void> createTournament(TournamentDTO tournamentDTO) {
    if (tournamentRepository.findByName(tournamentDTO.getName()).isPresent()) {
      throw new TournamentAlreadyExistsException(tournamentDTO.getName());
    }

    Status openStatus = statusRepository.findByInternalName(TournamentStatus.OPEN)
        .orElseThrow(() -> new StatusNotFoundException(TournamentStatus.OPEN));

    tournamentRepository.save(new Tournament(tournamentDTO.getName(), UUID.randomUUID(), openStatus));
    return ResponseEntity.ok().build();
  }

  public List<TournamentModel> getAllTournaments() {
    List<TournamentModel> tournamentModels = new ArrayList<>();

    tournamentRepository.findAll().forEach(tournament -> {
      Status status = tournament.getStatus();
      Phase currentPhase = tournament.getCurrentPhase();
      TournamentModel tournamentModel = new TournamentModel(tournament.getUuid(), tournament.getName(),
          new StatusModel(status.getInternalName().name(), status.getDisplayName()), currentPhase != null ?
          new PhaseModel(currentPhase.getInternalName().name(), currentPhase.getDisplayName()) :
          null);
      String username = securityContextService.getRequestingUser();
      User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
      if (isUserAttendee(user)) {
        if (isUserAllowedToJoinTournament(user, tournament)) {
          tournamentModel.setUserAllowedToJoinTournament(true);
        }
        boolean isUserInTournament = tournament.getUsers().contains(user);
        if (isUserInTournament) {
          tournamentModel.setUserInTournament(true);
        }
        if (isUserDisqualified(user, tournament)) {
          tournamentModel.setUserDisqualified(true);
        }
      }


      tournamentModels.add(tournamentModel);
    });
    return tournamentModels;
  }

  private boolean isUserDisqualified(User user, Tournament tournament) {
    if (isTournamentOpen(tournament)) {
      return false;
    }
    Set<User> allParticipants = tournament.getUsers();
    Set<User> usersInCurrentPhase = new HashSet<>();
    gameService.getGamesOfCurrentPhase(tournament).forEach(game -> usersInCurrentPhase.addAll(game.getPlayers()));
    return allParticipants.contains(user) && !usersInCurrentPhase.contains(user);
  }

  private boolean isUserAllowedToJoinTournament(User user, Tournament tournament) {
    return isTournamentOpen(tournament) && !isUserAlreadyJoinedInTournament(user, tournament);
  }

  private boolean isUserAlreadyJoinedInTournament(User user, Tournament tournament) {
    return tournament.getUsers().contains(user);
  }

  private static boolean isUserAttendee(User user) {
    return UserRole.ATTENDEE.name().equalsIgnoreCase(user.getRole().getInternalName().name());
  }

  private static boolean isTournamentOpen(Tournament tournament) {
    Status status = tournament.getStatus();
    return TournamentStatus.OPEN.name().equalsIgnoreCase(status.getInternalName().name());
  }

  public ResponseEntity<Void> joinTournament(UUID uuid) {
    Tournament tournament = tournamentRepository.findByUuid(uuid)
        .orElseThrow(() -> new TournamentNotFoundException(uuid));
    if (!TournamentStatus.OPEN.equals(tournament.getStatus().getInternalName())) {
      return ResponseEntity.badRequest().build();
    }
    String username = securityContextService.getRequestingUser();
    User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
    tournament.getUsers().add(user);
    user.getTournaments().add(tournament);
    userRepository.save(user);
    tournamentRepository.save(tournament);
    return ResponseEntity.ok().build();
  }

  public ResponseEntity<Set<ParticipantModel>> getAllParticipantsOfTournament(UUID uuid) {
    Tournament tournament = tournamentRepository.findByUuid(uuid)
        .orElseThrow(() -> new TournamentNotFoundException(uuid));
    Set<ParticipantModel> participants = new HashSet<>();
    tournament.getUsers().forEach(user -> participants.add(new ParticipantModel(user.getUsername())));
    return ResponseEntity.ok().body(participants);
  }

  public ResponseEntity<Set<ParticipantModel>> getParticipantsOfTournamentOfCurrentPhase(UUID uuid) {
    Tournament tournament = tournamentRepository.findByUuid(uuid)
        .orElseThrow(() -> new TournamentNotFoundException(uuid));
    Set<Game> games = gameService.getGamesOfCurrentPhase(tournament);
    Set<ParticipantModel> participantInCurrentPhase = new HashSet<>();
    games.forEach(game -> game.getPlayers()
        .forEach(player -> participantInCurrentPhase.add(new ParticipantModel(player.getUsername()))));
    return ResponseEntity.ok().body(participantInCurrentPhase);
  }

  public ResponseEntity<Void> startTournament(UUID tournamentUuid) {
    Tournament tournament = tournamentRepository.findByUuid(tournamentUuid)
        .orElseThrow(() -> new TournamentNotFoundException(tournamentUuid));
    checkIfTournamentHasRightAmountOfPlayers(tournament);
    Phase nextPhase = phaseService.getNextPhase(null);
    gameService.createFirstGames(tournament, nextPhase);
    tournament.setCurrentPhase(nextPhase);
    tournamentRepository.save(tournament);
    return ResponseEntity.ok().build();
  }

  public ResponseEntity<Void> startNextPhaseTournament(UUID tournamentUuid) {
    Tournament tournament = tournamentRepository.findByUuid(tournamentUuid)
        .orElseThrow(() -> new TournamentNotFoundException(tournamentUuid));
    gameService.checkIfAllGamesCompleted(tournament.getGames(), tournament.getCurrentPhase());
    TournamentPhase currentPhase = tournament.getCurrentPhase().getInternalName();
    Phase nextPhase = phaseService.getNextPhase(currentPhase);
    switch (currentPhase) {
      case PRELIMINARY_ROUND -> gameService.createFirstGames(tournament, nextPhase);
      case QUALIFYING_ROUND -> gameService.createGamesForRoundOfSixteen(tournament);
      case ROUND_OF_SIXTEEN, QUARTERFINALS, SEMIFINAL -> gameService.createFinalGames(tournament);
      default -> throw new IllegalStateException("No games created!");
    }
    tournament.setCurrentPhase(nextPhase);
    tournamentRepository.save(tournament);
    return ResponseEntity.ok().build();
  }

  public ResponseEntity<Void> closeTournament(UUID tournamentUuid) {
    Tournament tournament = tournamentRepository.findByUuid(tournamentUuid)
        .orElseThrow(() -> new TournamentNotFoundException(tournamentUuid));
    Phase currentPhase = tournament.getCurrentPhase();
    if (TournamentPhase.FINAL.equals(currentPhase.getInternalName())) {
      gameService.checkIfAllGamesCompleted(tournament.getGames(), currentPhase);
      Status closed = statusRepository.findByInternalName(TournamentStatus.DONE)
          .orElseThrow(() -> new StatusNotFoundException(TournamentStatus.DONE));
      tournament.setStatus(closed);
      tournamentRepository.save(tournament);
      return ResponseEntity.ok().build();
    } else {
      throw new TournamentCannotGetClosedException(tournamentUuid);
    }
  }

  private void checkIfTournamentHasRightAmountOfPlayers(Tournament tournament) {
    int amountOfPlayers = tournament.getUsers().size();
    if (amountOfPlayers > MAX_PLAYERS_PER_TOURNAMENT || amountOfPlayers < MIN_PLAYERS_PER_TOURNAMENT
        || amountOfPlayers % 4 != 0) {
      throw new WrongAmountOfPlayersException(amountOfPlayers);
    }
  }
}

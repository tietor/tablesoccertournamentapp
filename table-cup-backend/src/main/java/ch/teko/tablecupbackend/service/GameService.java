package ch.teko.tablecupbackend.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ch.teko.tablecupbackend.constant.TournamentPhase;
import ch.teko.tablecupbackend.constant.TournamentStatus;
import ch.teko.tablecupbackend.constant.UserRole;
import ch.teko.tablecupbackend.dto.GameResultDTO;
import ch.teko.tablecupbackend.entity.Game;
import ch.teko.tablecupbackend.entity.Phase;
import ch.teko.tablecupbackend.entity.Role;
import ch.teko.tablecupbackend.entity.Status;
import ch.teko.tablecupbackend.entity.Tournament;
import ch.teko.tablecupbackend.entity.User;
import ch.teko.tablecupbackend.exception.GameNotCompletedException;
import ch.teko.tablecupbackend.exception.GameNotFoundException;
import ch.teko.tablecupbackend.exception.PhaseNotFoundException;
import ch.teko.tablecupbackend.exception.StatusNotFoundException;
import ch.teko.tablecupbackend.exception.TournamentNotFoundException;
import ch.teko.tablecupbackend.exception.UserNotFoundException;
import ch.teko.tablecupbackend.exception.UserNotInTournamentException;
import ch.teko.tablecupbackend.model.GameModel;
import ch.teko.tablecupbackend.repository.GameRepository;
import ch.teko.tablecupbackend.repository.PhaseRepository;
import ch.teko.tablecupbackend.repository.StatusRepository;
import ch.teko.tablecupbackend.repository.TournamentRepository;
import ch.teko.tablecupbackend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GameService {


  private final GameRepository gameRepository;
  private final PhaseRepository phaseRepository;
  private final PhaseService phaseService;
  private final TournamentRepository tournamentRepository;
  private final StatusRepository statusRepository;
  private final UserRepository userRepository;
  private final SecurityContextService securityContextService;
  private static final int GAMES_PER_PLAYER_PER_PHASE = 3;
  private static final int AMOUNT_OF_PLAYERS_IN_ROUND_OF_SIXTEEN = 32;

  public void createFirstGames(Tournament tournament, Phase nextPhase) {
    Set<Game> createdGames = new HashSet<>();
    Set<User> allPlayers = new HashSet<>(tournament.getUsers());
    for (int amountOfGames = 1; amountOfGames <= GAMES_PER_PLAYER_PER_PHASE; amountOfGames++) {
      Set<User> choiceOfPlayersForAmountOfGames = new HashSet<>(allPlayers);
      for (User player : allPlayers) {
        if (choiceOfPlayersForAmountOfGames.contains(player)) {
          choiceOfPlayersForAmountOfGames.remove(player);
          User teammate = getTeamMate(player, choiceOfPlayersForAmountOfGames, createdGames, amountOfGames);
          choiceOfPlayersForAmountOfGames.remove(teammate);
          Set<User> enemies = getEnemies(choiceOfPlayersForAmountOfGames, createdGames, amountOfGames);
          Set<Integer> teamBluePlayerIds = Set.of(player.getId(), teammate.getId());
          Set<Integer> teamRedPlayerIds = enemies.stream().map(User::getId).collect(Collectors.toSet());
          Set<User> playersOfGame = new HashSet<>(Set.of(player, teammate));
          playersOfGame.addAll(enemies);
          Game game = new Game(tournament, UUID.randomUUID(), nextPhase, playersOfGame, teamBluePlayerIds,
              teamRedPlayerIds);
          createdGames.add(game);
        }
      }
    }

    gameRepository.saveAll(createdGames);
    Status runningStatus = statusRepository.findByInternalName(TournamentStatus.RUNNING)
        .orElseThrow(() -> new StatusNotFoundException(TournamentStatus.RUNNING));
    tournament.setStatus(runningStatus);
    tournamentRepository.save(tournament);
  }

  public ResponseEntity<Void> addResultToGame(UUID uuid, GameResultDTO gameResultDTO) {
    Game game = gameRepository.findByUuid(uuid).orElseThrow(() -> new GameNotFoundException(uuid));
    game.setBlueTeamScore(gameResultDTO.getPointOfTeamBlue());
    game.setRedTeamScore(gameResultDTO.getPointOfTeamRed());
    gameRepository.save(game);
    return ResponseEntity.ok().build();
  }

  public void checkIfAllGamesCompleted(Set<Game> games, Phase currentPhase) {
    games.stream()
        .filter(
            game -> game.getRedTeamScore() == 0 && game.getBlueTeamScore() == 0 && game.getPhase().equals(currentPhase))
        .findAny()
        .ifPresent(game -> {
          throw new GameNotCompletedException(game.getUuid());
        });
  }

  public void createGamesForRoundOfSixteen(Tournament tournament) {
    Set<Game> playedGames = tournament.getGames();
    Set<User> allPlayers = getAllPlayersByGames(playedGames);
    Map<User, Integer> playersWithPoints = getAllPlayersAsZeroPoints(allPlayers);
    givePointsForPlayers(playedGames, playersWithPoints);
    List<User> playersSortedByPointsDescending = getPlayersSortedByPointsDescending(playersWithPoints);
    playersSortedByPointsDescending = playersSortedByPointsDescending.subList(0, AMOUNT_OF_PLAYERS_IN_ROUND_OF_SIXTEEN);
    Phase roundOfSixteen = phaseRepository.findByInternalName(TournamentPhase.ROUND_OF_SIXTEEN)
        .orElseThrow(() -> new PhaseNotFoundException(TournamentPhase.ROUND_OF_SIXTEEN));
    int indexOfSecondPlayer = playersSortedByPointsDescending.size() - 1;
    List<Game> newGames = new ArrayList<>();
    for (int i = 0; i < 16; i += 2) {
      User firstPlayer = playersSortedByPointsDescending.get(i);
      User secondPlayer = playersSortedByPointsDescending.get(indexOfSecondPlayer);
      User firstEnemy = playersSortedByPointsDescending.get(i + 1);
      User secondEnemy = playersSortedByPointsDescending.get(indexOfSecondPlayer - 1);
      Set<User> players = new HashSet<>(Set.of(firstPlayer, secondPlayer, firstEnemy, secondEnemy));
      Set<Integer> teamBluePlayerIds = Set.of(firstPlayer.getId(), secondPlayer.getId());
      Set<Integer> teamRedPlayerIds = Set.of(firstEnemy.getId(), secondEnemy.getId());
      Game game = new Game(tournament, UUID.randomUUID(), roundOfSixteen, players, teamBluePlayerIds, teamRedPlayerIds);
      newGames.add(game);
      indexOfSecondPlayer -= 2;
    }
    gameRepository.saveAll(newGames);
  }

  private List<User> getPlayersSortedByPointsDescending(Map<User, Integer> playersWithPoints) {
    return playersWithPoints.entrySet()
        .stream()
        .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
        .map(Map.Entry::getKey)
        .collect(Collectors.toList());
  }

  private static Map<User, Integer> getAllPlayersAsZeroPoints(Set<User> allPlayers) {
    Map<User, Integer> playersWithPoints = new HashMap<>();
    allPlayers.forEach(player -> playersWithPoints.put(player, 0));
    return playersWithPoints;
  }

  private void givePointsForPlayers(Set<Game> games, Map<User, Integer> playersWithPoints) {
    games.forEach(game -> {
      if (game.getRedTeamScore() > game.getBlueTeamScore()) {
        giveWinnerPlayersOnePoint(game.getTeamRedPlayerIds(), playersWithPoints);
      } else {
        giveWinnerPlayersOnePoint(game.getTeamBluePlayerIds(), playersWithPoints);
      }
    });
  }

  private void giveWinnerPlayersOnePoint(Set<Integer> winnerTeamPlayerIds, Map<User, Integer> playersWithPoints) {
    Iterable<User> winnerPlayers = userRepository.findAllById(winnerTeamPlayerIds);
    winnerPlayers.forEach(winner -> playersWithPoints.put(winner, playersWithPoints.get(winner) + 1));
  }

  private Set<User> getAllPlayersByGames(Set<Game> games) {
    Set<User> allPlayers = new HashSet<>();
    games.forEach(game -> allPlayers.addAll(game.getPlayers()));
    return allPlayers;
  }

  public ResponseEntity<Set<GameModel>> getGamesOfTournamentAndCurrentPhase(UUID uuid) {
    String username = securityContextService.getRequestingUser();
    User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
    Role role = user.getRole();
    UserRole internalName = role.getInternalName();
    Tournament tournament = tournamentRepository.findByUuid(uuid)
        .orElseThrow(() -> new TournamentNotFoundException(uuid));
    Set<Game> currenPhaseGames = getGamesOfCurrentPhase(tournament);
    Set<GameModel> gameModels = new HashSet<>();
    if (UserRole.TOURNAMENT_DIRECTOR.equals(internalName)) {
      currenPhaseGames.forEach(game -> gameModels.add(mapGameToModel(game)));
    } else if (UserRole.ATTENDEE.equals(internalName)) {
      if (!tournament.getUsers().contains(user)) {
        throw new UserNotInTournamentException(username, tournament.getId());
      }
      currenPhaseGames.stream()
          .filter(game -> game.getPlayers().contains(user))
          .forEach(game -> gameModels.add(mapGameToModel(game)));
    }
    return ResponseEntity.ok().body(gameModels);
  }

  private GameModel mapGameToModel(Game game) {
    return new GameModel(game.getUuid(), game.getBlueTeamScore(), game.getRedTeamScore(),
        getUsernamesOfPlayers(game.getTeamBluePlayerIds()), getUsernamesOfPlayers(game.getTeamRedPlayerIds()));
  }

  private Set<String> getUsernamesOfPlayers(Set<Integer> idsOfPlayer) {
    Set<String> usernames = new HashSet<>();
    userRepository.findAllById(idsOfPlayer).forEach(user -> usernames.add(user.getUsername()));
    return usernames;
  }

  private Set<User> getEnemies(Set<User> choiceOfPlayersForCurrentGame, Set<Game> createdGames, int amountOfGames) {
    User firstEnemy = getRandomPlayer(choiceOfPlayersForCurrentGame);
    choiceOfPlayersForCurrentGame.remove(firstEnemy);
    User secondEnemy = getTeamMate(firstEnemy, choiceOfPlayersForCurrentGame, createdGames, amountOfGames);
    choiceOfPlayersForCurrentGame.remove(secondEnemy);
    return Set.of(firstEnemy, secondEnemy);
  }

  private User getTeamMate(User currentPlayer, Set<User> possiblePlayers, Set<Game> createdGames, int amountOfGames) {
    User teamMate = null;
    boolean teammateNotFound = true;
    Set<User> potentialTeamPartners = new HashSet<>(possiblePlayers);
    while (teammateNotFound) {
      User potentialTeamPartner = getRandomPlayer(potentialTeamPartners);
      boolean areAlreadyTogetherInATeam = areAlreadyTogetherInATeam(currentPlayer, potentialTeamPartner, createdGames);
      boolean hasTeamMateReachedAmountOfGames = hasPlayerReachedAmountOfGame(potentialTeamPartner, createdGames,
          amountOfGames);
      if (areAlreadyTogetherInATeam || hasTeamMateReachedAmountOfGames) {
        potentialTeamPartners.remove(potentialTeamPartner);
      } else {
        teamMate = potentialTeamPartner;
        teammateNotFound = false;
      }
    }
    return teamMate;
  }

  private boolean areAlreadyTogetherInATeam(User currentPlayer, User potentialTeamMate, Set<Game> createdGames) {
    int idOfCurrentPlayer = currentPlayer.getId();
    int idOfPossibleTeammate = potentialTeamMate.getId();

    Set<Game> gamesOfCurrentPlayer = createdGames.stream()
        .filter(game -> game.getPlayers().containsAll(List.of(currentPlayer, potentialTeamMate)))
        .collect(Collectors.toSet());
    for (Game game : gamesOfCurrentPlayer) {
      return teamMatePlaysInSameTeamInGame(game, idOfCurrentPlayer, idOfPossibleTeammate);
    }
    return false;
  }

  private boolean teamMatePlaysInSameTeamInGame(Game game, int idOfCurrentPlayer, int idOfPotentialTeammate) {
    Set<Integer> teamBluePlayerIds = game.getTeamBluePlayerIds();
    List<Integer> idOfCurrentPlayerAndTeammate = List.of(idOfCurrentPlayer, idOfPotentialTeammate);
    if (teamBluePlayerIds.containsAll(idOfCurrentPlayerAndTeammate)) {
      return true;
    }
    Set<Integer> teamRedPlayerIds = game.getTeamRedPlayerIds();
    return teamRedPlayerIds.containsAll(idOfCurrentPlayerAndTeammate);

  }

  private boolean hasPlayerReachedAmountOfGame(User player, Set<Game> createdGames, int currentAmountOfGames) {
    long amountOfGames;
    amountOfGames = createdGames.stream().filter(game -> game.getPlayers().contains(player)).count();
    return amountOfGames == currentAmountOfGames;
  }

  private User getRandomPlayer(Set<User> possiblePlayers) {
    Random random = new Random();
    int randomIndex = random.nextInt(possiblePlayers.size());
    User possibleTeammate = possiblePlayers.stream().skip(randomIndex).findFirst().orElse(null);
    if (possibleTeammate == null) {
      throw new IllegalStateException("No possible teammate found!");
    }
    return possibleTeammate;
  }

  public void createFinalGames(Tournament tournament) {
    Set<Game> playedGamesInCurrentPhase = getGamesOfCurrentPhase(tournament);
    List<Team> winnerTeams = getWinnerTeams(playedGamesInCurrentPhase);
    Phase currentPhase = tournament.getCurrentPhase();
    Phase nextPhase = phaseService.getNextPhase(currentPhase.getInternalName());
    createNewFinalGames(tournament, winnerTeams, nextPhase);
  }

  private List<Team> getWinnerTeams(Set<Game> playedGames) {
    List<Team> winnerTeams = new ArrayList<>();
    playedGames.forEach(game -> {
      if (game.getRedTeamScore() > game.getBlueTeamScore()) {
        winnerTeams.add(new Team(getPlayersByIds(game.getTeamRedPlayerIds())));
      } else {
        winnerTeams.add(new Team(getPlayersByIds(game.getTeamBluePlayerIds())));
      }
    });
    return winnerTeams;
  }

  private void createNewFinalGames(Tournament tournament, List<Team> winnerTeams, Phase nextPhase) {
    Set<Game> newGames = new HashSet<>();
    Random random = new Random();
    while (!winnerTeams.isEmpty()) {
      Set<User> blueTeamPlayers = winnerTeams.getFirst().players();
      List<Team> possibleEnemies = winnerTeams.subList(1, winnerTeams.size());
      int indexOfRedTeam = random.nextInt(possibleEnemies.size());
      Team redTeam = possibleEnemies.get(indexOfRedTeam);
      Set<User> redTeamPlayers = redTeam.players();
      Set<User> allPlayers = new HashSet<>();
      allPlayers.addAll(blueTeamPlayers);
      allPlayers.addAll(redTeamPlayers);
      Set<Integer> idsOfBlueTeamPlayers = new HashSet<>();
      blueTeamPlayers.forEach(blueTeamPlayer -> idsOfBlueTeamPlayers.add(blueTeamPlayer.getId()));
      Set<Integer> idsOfRedTeamPlayers = new HashSet<>();
      redTeamPlayers.forEach(redTeamPlayer -> idsOfRedTeamPlayers.add(redTeamPlayer.getId()));
      Game newGame = new Game(tournament, UUID.randomUUID(), nextPhase, allPlayers, idsOfBlueTeamPlayers,
          idsOfRedTeamPlayers);
      newGames.add(newGame);
      winnerTeams.removeFirst();
      winnerTeams.remove(indexOfRedTeam);
    }
    gameRepository.saveAll(newGames);
  }

  private Set<User> getPlayersByIds(Set<Integer> idsOfWinnerTeamPlayers) {
    Iterable<User> players = userRepository.findAllById(idsOfWinnerTeamPlayers);
    Set<User> winnerPlayers = new HashSet<>();
    players.forEach(winnerPlayers::add);
    return winnerPlayers;
  }

  public Set<Game> getGamesOfCurrentPhase(Tournament tournament) {
    Phase currentPhase = tournament.getCurrentPhase();
    return tournament.getGames()
        .stream()
        .filter(game -> game.getPhase().equals(currentPhase))
        .collect(Collectors.toSet());
  }

  private record Team(Set<User> players) {

  }
}
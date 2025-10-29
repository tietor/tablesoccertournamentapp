package ch.teko.tablecupbackend.entity;

import java.util.Set;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Game {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(unique = true, nullable = false)
  private UUID uuid;

  private int blueTeamScore;

  private int redTeamScore;

  @ManyToOne
  @JoinColumn(name = "tournament_id", nullable = false)
  private Tournament tournament;

  @ManyToOne
  @JoinColumn(name = "phase_id", nullable = false)
  private Phase phase;

  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(name = "game_players", joinColumns = @JoinColumn(name = "game_id"), inverseJoinColumns = @JoinColumn(name = "players_id"))
  private Set<User> players;

  @ElementCollection
  @CollectionTable(name = "game_team_blue_player_ids", joinColumns = @JoinColumn(name = "game_id", nullable = false))
  Set<Integer> teamBluePlayerIds;

  @ElementCollection
  @CollectionTable(name = "game_team_red_player_ids", joinColumns = @JoinColumn(name = "game_id", nullable = false))
  Set<Integer> teamRedPlayerIds;

  public Game(Tournament tournament,
      UUID uuid,
      Phase phase,
      Set<User> players,
      Set<Integer> teamBluePlayerIds,
      Set<Integer> teamRedPlayerIds) {
    this.tournament = tournament;
    this.uuid = uuid;
    this.phase = phase;
    this.players = players;
    this.teamBluePlayerIds = teamBluePlayerIds;
    this.teamRedPlayerIds = teamRedPlayerIds;
  }
}

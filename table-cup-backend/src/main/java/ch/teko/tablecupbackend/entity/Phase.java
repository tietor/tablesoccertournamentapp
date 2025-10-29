package ch.teko.tablecupbackend.entity;

import java.util.Set;

import ch.teko.tablecupbackend.constant.TournamentPhase;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Phase {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, unique = true)
  private TournamentPhase internalName;

  private String displayName;

  @OneToMany(mappedBy = "phase", cascade = CascadeType.ALL)
  private Set<Game> games;

  @OneToMany(mappedBy = "currentPhase", cascade = CascadeType.ALL)
  private Set<Tournament> tournaments;

  public Phase(TournamentPhase internalName, String displayName) {
    this.internalName = internalName;
    this.displayName = displayName;
  }

}

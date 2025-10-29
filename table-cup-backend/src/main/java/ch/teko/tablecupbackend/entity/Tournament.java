package ch.teko.tablecupbackend.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Tournament {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(unique = true, length = 30, nullable = false)
  private String name;

  @Column(unique = true, nullable = false)
  private UUID uuid;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "status_id", nullable = false)
  private Status status;

  @ManyToMany
  private Set<User> users;

  @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL, orphanRemoval = true)
  Set<Game> games = new HashSet<>();

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "current_phase_id")
  private Phase currentPhase;

  public Tournament(String name, UUID uuid, Status status) {
    this.name = name;
    this.uuid = uuid;
    this.status = status;
  }
}

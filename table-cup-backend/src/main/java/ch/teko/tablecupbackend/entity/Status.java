package ch.teko.tablecupbackend.entity;

import java.util.Set;

import ch.teko.tablecupbackend.constant.TournamentStatus;

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
public class Status {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @OneToMany(mappedBy = "status", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Tournament> tournaments;

  @Enumerated(EnumType.STRING)
  @Column(unique = true)
  private TournamentStatus internalName;

  private String displayName;

  public Status(TournamentStatus internalName, String displayName) {
    this.internalName = internalName;
    this.displayName = displayName;
  }


}

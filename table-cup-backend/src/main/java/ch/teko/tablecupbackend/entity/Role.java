package ch.teko.tablecupbackend.entity;


import java.util.Set;

import ch.teko.tablecupbackend.constant.UserRole;

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
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Enumerated(EnumType.STRING)
  @Column(unique = true, nullable = false)
  private UserRole internalName;

  @Column(nullable = false)
  private String displayName;

  @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
  private Set<User> users;

  public Role(UserRole internalName, String displayName) {
    this.internalName = internalName;
    this.displayName = displayName;
  }
}

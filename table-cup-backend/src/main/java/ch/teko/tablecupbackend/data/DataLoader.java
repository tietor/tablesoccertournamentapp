package ch.teko.tablecupbackend.data;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import ch.teko.tablecupbackend.constant.TournamentPhase;
import ch.teko.tablecupbackend.constant.TournamentStatus;
import ch.teko.tablecupbackend.constant.UserRole;
import ch.teko.tablecupbackend.entity.Phase;
import ch.teko.tablecupbackend.entity.Role;
import ch.teko.tablecupbackend.entity.Status;
import ch.teko.tablecupbackend.repository.PhaseRepository;
import ch.teko.tablecupbackend.repository.RoleRepository;
import ch.teko.tablecupbackend.repository.StatusRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class DataLoader implements CommandLineRunner {

  private final RoleRepository roleRepository;
  private final StatusRepository statusRepository;
  private final PhaseRepository phaseRepository;

  @Override
  public void run(String... args) {
    if (roleRepository.count() == 0) {
      UserRole[] allUserRoles = UserRole.values();
      for (UserRole userRole : allUserRoles) {
        roleRepository.save(new Role(userRole, userRole.getDisplayValue()));
      }
    }

    if (statusRepository.count() == 0) {
      TournamentStatus[] allTournamentStatus = TournamentStatus.values();
      for (TournamentStatus tournamentStatus : allTournamentStatus) {
        statusRepository.save(new Status(tournamentStatus, tournamentStatus.getDisplayValue()));
      }
    }

    if (phaseRepository.count() == 0) {
      TournamentPhase[] allPhases = TournamentPhase.values();
      for (TournamentPhase phase : allPhases) {
        phaseRepository.save(new Phase(phase, phase.getDisplayValue()));
      }

    }
  }
}

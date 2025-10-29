package ch.teko.tablecupbackend.service;

import org.springframework.stereotype.Service;

import ch.teko.tablecupbackend.constant.TournamentPhase;
import ch.teko.tablecupbackend.entity.Phase;
import ch.teko.tablecupbackend.exception.PhaseNotFoundException;
import ch.teko.tablecupbackend.repository.PhaseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PhaseService {

  private final PhaseRepository phaseRepository;


  public Phase getNextPhase(TournamentPhase currentTournamentPhase) {
    Phase nextPhase;
    switch (currentTournamentPhase) {
      case null -> nextPhase = phaseRepository.findByInternalName(TournamentPhase.PRELIMINARY_ROUND)
          .orElseThrow(() -> new PhaseNotFoundException(TournamentPhase.PRELIMINARY_ROUND));
      case PRELIMINARY_ROUND -> nextPhase = phaseRepository.findByInternalName(TournamentPhase.QUALIFYING_ROUND)
          .orElseThrow(() -> new PhaseNotFoundException(TournamentPhase.QUARTERFINALS));
      case QUALIFYING_ROUND -> nextPhase = phaseRepository.findByInternalName(TournamentPhase.ROUND_OF_SIXTEEN)
          .orElseThrow(() -> new PhaseNotFoundException(TournamentPhase.ROUND_OF_SIXTEEN));
      case ROUND_OF_SIXTEEN -> nextPhase = phaseRepository.findByInternalName(TournamentPhase.QUARTERFINALS)
          .orElseThrow(() -> new PhaseNotFoundException(TournamentPhase.QUARTERFINALS));
      case QUARTERFINALS -> nextPhase = phaseRepository.findByInternalName(TournamentPhase.SEMIFINAL)
          .orElseThrow(() -> new PhaseNotFoundException(TournamentPhase.SEMIFINAL));
      case SEMIFINAL -> nextPhase = phaseRepository.findByInternalName(TournamentPhase.FINAL)
          .orElseThrow(() -> new PhaseNotFoundException(TournamentPhase.FINAL));
      default -> throw new IllegalStateException("No next phase allowed!");
    }
    return nextPhase;
  }



}

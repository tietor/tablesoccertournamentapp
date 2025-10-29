package ch.teko.tablecupbackend.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ch.teko.tablecupbackend.constant.TournamentPhase;
import ch.teko.tablecupbackend.entity.Phase;

@Repository
public interface PhaseRepository extends CrudRepository<Phase, Integer> {
 Optional<Phase> findByInternalName(TournamentPhase internalName);
}

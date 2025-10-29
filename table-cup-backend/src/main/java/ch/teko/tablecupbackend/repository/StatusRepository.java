package ch.teko.tablecupbackend.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ch.teko.tablecupbackend.constant.TournamentStatus;
import ch.teko.tablecupbackend.entity.Status;

@Repository
public interface StatusRepository extends CrudRepository<Status, Long> {

  Optional<Status> findByInternalName(TournamentStatus internalName);
}

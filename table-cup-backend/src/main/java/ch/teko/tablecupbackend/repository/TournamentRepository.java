package ch.teko.tablecupbackend.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ch.teko.tablecupbackend.entity.Tournament;

@Repository
public interface TournamentRepository extends CrudRepository<Tournament, Long> {

  Optional<Tournament> findByName(String name);

  Optional<Tournament> findByUuid(UUID uuid);

}

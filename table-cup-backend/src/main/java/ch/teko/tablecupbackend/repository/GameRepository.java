package ch.teko.tablecupbackend.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ch.teko.tablecupbackend.entity.Game;

@Repository
public interface GameRepository extends CrudRepository<Game, Integer> {

  Optional<Game> findByUuid(UUID uuid);
}

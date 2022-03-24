package br.com.letscode.movies.batlle.data_providers.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.letscode.movies.batlle.core.entities.GameMatch;

@Repository
public interface GameMatchRepository extends JpaRepository<GameMatch, Long>  {
    Optional<GameMatch> findByUserId(final long userId);
}

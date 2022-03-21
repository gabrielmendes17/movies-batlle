package br.com.lets_code.Movies.Batlle.data_providers.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.lets_code.Movies.Batlle.core.entities.FilmCombination;

@Repository
public interface FilmCombinationRepository extends JpaRepository<FilmCombination, Long> {
    Optional<FilmCombination> findFirst1ByUserIdAndAttemptsLessThanOrderByIdAsc(final long userId, int attempts);
}

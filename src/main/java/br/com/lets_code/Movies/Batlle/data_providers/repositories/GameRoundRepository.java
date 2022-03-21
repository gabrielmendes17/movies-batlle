package br.com.lets_code.Movies.Batlle.data_providers.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.lets_code.Movies.Batlle.core.entities.GameRound;

@Repository
public interface GameRoundRepository extends JpaRepository<GameRound, Long> {
    
}

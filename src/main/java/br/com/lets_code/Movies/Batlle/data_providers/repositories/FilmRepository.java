package br.com.lets_code.Movies.Batlle.data_providers.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.lets_code.Movies.Batlle.core.entities.Film;

@Repository
public interface FilmRepository extends JpaRepository<Film, String> {
    
}

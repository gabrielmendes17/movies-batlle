package br.com.letscode.movies.batlle.data_providers.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.letscode.movies.batlle.core.entities.Film;

@Repository
public interface FilmRepository extends JpaRepository<Film, String> {
    
}

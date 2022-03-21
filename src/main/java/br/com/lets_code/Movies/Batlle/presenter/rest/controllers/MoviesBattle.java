package br.com.lets_code.Movies.Batlle.presenter.rest.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.lets_code.Movies.Batlle.core.entities.GameMatch;
import br.com.lets_code.Movies.Batlle.core.entities.User;
import br.com.lets_code.Movies.Batlle.core.services.FilmCombinationService;
import br.com.lets_code.Movies.Batlle.core.services.UserService;
import br.com.lets_code.Movies.Batlle.data_providers.repositories.FilmCombinationRepository;
import br.com.lets_code.Movies.Batlle.data_providers.repositories.FilmsRepository;
import br.com.lets_code.Movies.Batlle.data_providers.repositories.GameMatchRepository;
import br.com.lets_code.Movies.Batlle.data_providers.repositories.GameRoundRepository;
import br.com.lets_code.Movies.Batlle.data_providers.repositories.UserRepository;

@RestController
@RequestMapping("/api/movies_battle")
public class MoviesBattle {
    @Autowired
    UserRepository userRepository;

    @Autowired
    GameMatchRepository gameMatchRepository;

    @Autowired
    FilmsRepository filmsRepository;

    @Autowired
    FilmCombinationService filmCombinationService;

    @Autowired
    GameRoundRepository gameRoundRepository;

    @Autowired
    UserService userService;

    @Autowired
    FilmCombinationRepository filmCombinationRepository;

    @PostMapping("/begin")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<GameMatch> begin(Principal principal) {
        User user = userService.getUserFromPrincipal(principal);
        GameMatch game = gameMatchRepository.save(new GameMatch(user));
        System.out.println(game);
        String filmsLength = System.getProperty("films_length");
        filmCombinationService.generateFilmCombination(Integer.parseInt(filmsLength), 2, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(game);
    }
}

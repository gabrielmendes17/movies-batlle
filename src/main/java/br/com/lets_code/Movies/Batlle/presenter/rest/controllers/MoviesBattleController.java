package br.com.lets_code.Movies.Batlle.presenter.rest.controllers;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.lets_code.Movies.Batlle.core.entities.Film;
import br.com.lets_code.Movies.Batlle.core.entities.FilmCombination;
import br.com.lets_code.Movies.Batlle.core.entities.GameMatch;
import br.com.lets_code.Movies.Batlle.core.entities.User;
import br.com.lets_code.Movies.Batlle.core.exceptions.GameMatchOpenNotFound;
import br.com.lets_code.Movies.Batlle.core.exceptions.WrongFilmCombinationGuess;
import br.com.lets_code.Movies.Batlle.core.services.FilmCombinationService;
import br.com.lets_code.Movies.Batlle.core.services.GameRoundService;
import br.com.lets_code.Movies.Batlle.core.services.UserService;
import br.com.lets_code.Movies.Batlle.data_providers.repositories.FilmCombinationRepository;
import br.com.lets_code.Movies.Batlle.data_providers.repositories.FilmsRepository;
import br.com.lets_code.Movies.Batlle.data_providers.repositories.GameMatchRepository;
import br.com.lets_code.Movies.Batlle.data_providers.repositories.GameRoundRepository;
import br.com.lets_code.Movies.Batlle.data_providers.repositories.UserRepository;
import br.com.lets_code.Movies.Batlle.presenter.rest.dtos.request.QuizzRequest;
import br.com.lets_code.Movies.Batlle.presenter.rest.dtos.response.MessageResponse;

@RestController
@RequestMapping("/api/movies_battle")
public class MoviesBattleController {
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

    @Autowired
    GameRoundService gameRoundService;

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

    @PutMapping("/close")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> close(Principal principal) {
        User user = userService.getUserFromPrincipal(principal);
        Optional<GameMatch> gameMatch = gameMatchRepository.findByUserId(user.getId());
        if (gameMatch.isEmpty()) {
            return ResponseEntity
            .badRequest()
            .body(new MessageResponse("Error: No game match was found to close!"));
        }
        System.out.println(gameMatch);
        return ResponseEntity.status(HttpStatus.CREATED).body(gameMatch.get());
    }

    @GetMapping("/quizz")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> quizz(Principal principal) {
        User user = userService.getUserFromPrincipal(principal);
        Optional<GameMatch> gameMatch = gameMatchRepository.findByUserId(user.getId());
        if (gameMatch.isEmpty()) {
            return ResponseEntity
            .badRequest()
            .body(new MessageResponse("Error: No game match open was found!"));
        }
        FilmCombination filmCombination = filmCombinationRepository.findFirst1ByUserIdAndAttemptsLessThanOrderByIdAsc(user.getId(), 3).get();
        List<Film> films = filmsRepository.findAllById(Arrays.asList(String.valueOf(filmCombination.getFirstFilmCombination()), String.valueOf(filmCombination.getSecondFilmCombination())));
        return ResponseEntity.status(HttpStatus.OK).body(films);
    }

    @PostMapping("/quizz")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> quizzAnswer(Principal principal, @RequestBody QuizzRequest quizzRequest) throws GameMatchOpenNotFound, WrongFilmCombinationGuess {
        System.out.println("quizzAnswerquizzAnswer");
        System.out.println(quizzRequest);
        Boolean success = gameRoundService.handlePlayerGuess(quizzRequest, principal);
        String mesage = success ? "Congratulations, your guess was rigth" : "Sorry, your guess was wrong, try again!";
        return ResponseEntity.ok().body(new MessageResponse(mesage));
    }
}

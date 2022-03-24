package br.com.letscode.movies.batlle.presenter.rest.controllers;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

import br.com.letscode.movies.batlle.core.entities.Film;
import br.com.letscode.movies.batlle.core.entities.FilmCombination;
import br.com.letscode.movies.batlle.core.entities.GameMatch;
import br.com.letscode.movies.batlle.core.entities.User;
import br.com.letscode.movies.batlle.core.exceptions.GameMatchOpenNotFound;
import br.com.letscode.movies.batlle.core.exceptions.WrongFilmCombinationGuess;
import br.com.letscode.movies.batlle.core.services.FilmCombinationService;
import br.com.letscode.movies.batlle.core.services.GameRoundService;
import br.com.letscode.movies.batlle.core.services.UserService;
import br.com.letscode.movies.batlle.data_providers.repositories.FilmCombinationRepository;
import br.com.letscode.movies.batlle.data_providers.repositories.FilmRepository;
import br.com.letscode.movies.batlle.data_providers.repositories.GameMatchRepository;
import br.com.letscode.movies.batlle.data_providers.repositories.GameRoundRepository;
import br.com.letscode.movies.batlle.data_providers.repositories.UserRepository;
import br.com.letscode.movies.batlle.presenter.rest.dtos.request.QuizzRequest;
import br.com.letscode.movies.batlle.presenter.rest.dtos.response.MessageResponse;

@RestController
@RequestMapping("/api/movies_battle")
public class MovieBattleController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    GameMatchRepository gameMatchRepository;

    @Autowired
    FilmRepository filmsRepository;

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
        Optional<GameMatch> gameMatchOpational = gameMatchRepository.findByUserIdAndFinishedAtIsNull(user.getId());
        if (gameMatchOpational.isEmpty()) {
            return ResponseEntity
            .badRequest()
            .body(new MessageResponse("Error: No game match was found to close!"));
        }
        GameMatch gameMatch = gameMatchOpational.get();
        gameMatch.setFinishedAt(LocalDateTime.of(LocalDate.now(), LocalTime.now()));
        gameMatchRepository.save(gameMatch);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Game closed with success!"));
    }

    @GetMapping("/quizz")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> quizz(Principal principal) {
        User user = userService.getUserFromPrincipal(principal);
        Optional<GameMatch> gameMatch = gameMatchRepository.findByUserIdAndFinishedAtIsNull(user.getId());
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

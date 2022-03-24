package br.com.letscode.movies.batlle.presenter.rest.controllers;

import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import br.com.letscode.movies.batlle.core.entities.GameMatch;
import br.com.letscode.movies.batlle.core.entities.User;
import br.com.letscode.movies.batlle.core.exceptions.ExistingGameMatchOpen;
import br.com.letscode.movies.batlle.core.exceptions.GameMatchOpenNotFound;
import br.com.letscode.movies.batlle.core.exceptions.WrongFilmCombinationGuess;
import br.com.letscode.movies.batlle.core.services.FilmCombinationService;
import br.com.letscode.movies.batlle.core.services.GameMatchService;
import br.com.letscode.movies.batlle.core.services.GameRoundService;
import br.com.letscode.movies.batlle.core.services.UserService;
import br.com.letscode.movies.batlle.data_providers.repositories.FilmCombinationRepository;
import br.com.letscode.movies.batlle.data_providers.repositories.FilmRepository;
import br.com.letscode.movies.batlle.data_providers.repositories.GameMatchRepository;
import br.com.letscode.movies.batlle.data_providers.repositories.GameRoundRepository;
import br.com.letscode.movies.batlle.data_providers.repositories.UserRepository;
import br.com.letscode.movies.batlle.presenter.rest.dtos.request.QuizzGuessRequest;
import br.com.letscode.movies.batlle.presenter.rest.dtos.response.MessageResponse;

@RestController
@RequestMapping("/api/movies_battle")
@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
public class MovieBattleController {
    private static final Logger logger = LoggerFactory.getLogger(MovieBattleController.class);
    
    @Autowired
    UserRepository userRepository;

    @Autowired
    GameMatchRepository gameMatchRepository;

    @Autowired
    GameMatchService gameMatchService;

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
    public ResponseEntity<MessageResponse> begin(Principal principal) throws ExistingGameMatchOpen {
        User user = userService.getUserFromPrincipal(principal);
        GameMatch game = gameMatchService.createNewGameMatchFromSessionUser(user);
        String filmsLength = System.getProperty("films_length");
        filmCombinationService.generateFilmCombination(Integer.parseInt(filmsLength), 2, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Game match created with success!"));
    }

    @PutMapping("/close")
    public ResponseEntity<?> close(Principal principal) throws GameMatchOpenNotFound {
        User user = userService.getUserFromPrincipal(principal);
        gameMatchService.closeCurrentGameMatchOpen(user);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Game closed with success!"));
    }

    @GetMapping("/quizz")
    public ResponseEntity<?> quizz(Principal principal) throws GameMatchOpenNotFound {
        User user = userService.getUserFromPrincipal(principal);
        gameMatchService.getCurrentGameMatchFromSessionPlayer(user);
        List<Film> films = filmCombinationService.getCurrentFilmCombination(user);
        return ResponseEntity.status(HttpStatus.OK).body(films);
    }

    @PostMapping("/quizz")
    public ResponseEntity<?> quizzAnswer(Principal principal, @RequestBody QuizzGuessRequest quizzRequest) throws GameMatchOpenNotFound, WrongFilmCombinationGuess {
        logger.info("quizzAnswer: {}", quizzRequest.toString());
        logger.info(quizzRequest.toString());
        Boolean success = gameRoundService.handlePlayerGuess(quizzRequest, principal);
        String mesage = success ? "Congratulations, your guess was rigth" : "Sorry, your guess was wrong, try again!";
        return ResponseEntity.ok().body(new MessageResponse(mesage));
    }
}

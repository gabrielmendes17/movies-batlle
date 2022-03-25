package br.com.letscode.movies.batlle.core.services;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.letscode.movies.batlle.core.entities.Film;
import br.com.letscode.movies.batlle.core.entities.FilmCombination;
import br.com.letscode.movies.batlle.core.entities.GameMatch;
import br.com.letscode.movies.batlle.core.entities.GameRound;
import br.com.letscode.movies.batlle.core.entities.User;
import br.com.letscode.movies.batlle.core.exceptions.GameMatchOpenNotFound;
import br.com.letscode.movies.batlle.core.exceptions.WrongFilmCombinationGuess;
import br.com.letscode.movies.batlle.data_providers.repositories.FilmCombinationRepository;
import br.com.letscode.movies.batlle.data_providers.repositories.FilmRepository;
import br.com.letscode.movies.batlle.data_providers.repositories.GameRoundRepository;
import br.com.letscode.movies.batlle.presenter.rest.dtos.request.QuizzGuessRequest;

@Service
public class GameRoundService {

    private static final Logger log = LoggerFactory.getLogger(GameRoundService.class);

    @Autowired
    GameRoundRepository gameRoundRepository;

    @Autowired
    FilmRepository filmsRepository;

    @Autowired
    FilmCombinationRepository filmCombinationRepository;

    @Autowired
    GameMatchService gameMatchService;

    @Autowired
    UserService userService;
    
    private boolean guessSuccess(List<Film> films,QuizzGuessRequest quizzRequest) {
        Film winningMovie = films.stream().filter(film -> {
            return film.getId().equals(quizzRequest.getGuessWinningMovieId());
        }).collect(Collectors.toList()).get(0);
        Film losingMovie = films.stream().filter(film -> film.getId().equals(quizzRequest.getGuessLosingMovieId())).collect(Collectors.toList()).get(0);
        return winningMovie.getRating() > losingMovie.getRating();
    }

    public Boolean handlePlayerGuess(QuizzGuessRequest quizzRequest, Principal principal) throws GameMatchOpenNotFound, WrongFilmCombinationGuess {
        User user = userService.getUserFromPrincipal(principal);
        GameMatch gameMatch = gameMatchService.getCurrentGameMatchFromSessionPlayer(user);
        FilmCombination filmCombination = filmCombinationRepository.findFirst1ByUserIdAndAttemptsLessThanOrderByIdAsc(user.getId(), 3).get();
        validatePlayerGuess(quizzRequest, filmCombination);
        List<Film> films = filmsRepository.findAllById(Arrays.asList(quizzRequest.getGuessWinningMovieId(), quizzRequest.getGuessLosingMovieId()));
        if (this.guessSuccess(films, quizzRequest)) {
            log.info("Player guess success {}, {}", films.get(0).getRating(), films.get(1).getRating());
            gameRoundRepository.save(new GameRound(gameMatch, 1));
            filmCombination.setAttempts(3);
            filmCombinationRepository.save(filmCombination);
            return true;
        }
        log.info("Player guess failure {}, {}", films.get(0).getRating(), films.get(1).getRating());
        gameRoundRepository.save(new GameRound(gameMatch, 0));
        filmCombination.setAttempts(filmCombination.getAttempts()+1);
        filmCombinationRepository.save(filmCombination);
        return false;
}

    private void validatePlayerGuess(QuizzGuessRequest quizzRequest, FilmCombination filmCombination) throws WrongFilmCombinationGuess {
        Supplier<Stream<String>> filmsStreamSupplier = () ->  Arrays.asList(String.valueOf(filmCombination.getFirstFilmCombination()), String.valueOf(filmCombination.getSecondFilmCombination())).stream();
        boolean isPlayerGuessingTheRightFilmCombination = filmsStreamSupplier.get().anyMatch(quizzRequest.getGuessLosingMovieId()::equals) && filmsStreamSupplier.get().anyMatch(quizzRequest.getGuessLosingMovieId()::equals);
        if (isPlayerGuessingTheRightFilmCombination) {
            log.info("Player is trying to guess with wrong films ids {}", filmsStreamSupplier.get());
            return;
        }
        log.info("Films id validation passed {} ", filmsStreamSupplier.get());
        throw new WrongFilmCombinationGuess();
    }
}

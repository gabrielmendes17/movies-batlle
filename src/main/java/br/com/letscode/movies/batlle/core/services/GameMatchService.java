package br.com.letscode.movies.batlle.core.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.letscode.movies.batlle.core.entities.GameMatch;
import br.com.letscode.movies.batlle.core.entities.User;
import br.com.letscode.movies.batlle.core.exceptions.ExistingGameMatchOpen;
import br.com.letscode.movies.batlle.core.exceptions.GameMatchOpenNotFound;
import br.com.letscode.movies.batlle.data_providers.repositories.FilmCombinationRepository;
import br.com.letscode.movies.batlle.data_providers.repositories.GameMatchRepository;

@Service
public class GameMatchService {

    @Autowired
    GameMatchRepository gameMatchRepository;

    @Autowired
    FilmCombinationRepository filmCombinationRepository;

    @Autowired
    UserService userService;

    public GameMatch getCurrentGameMatchFromSessionPlayer(User user) throws GameMatchOpenNotFound {
        Optional<GameMatch> gameMatch = gameMatchRepository.findByUserIdAndFinishedAtIsNull(user.getId());
        if (gameMatch.isEmpty()) {
            System.out.println("Error: No game match open was found!");
            throw new GameMatchOpenNotFound();
        }
        return gameMatch.get();
    }

    @Transactional
    public void closeCurrentGameMatchOpen(User user) throws GameMatchOpenNotFound {
        GameMatch gameMatch = this.getCurrentGameMatchFromSessionPlayer(user);
        gameMatch.setFinishedAt(LocalDateTime.of(LocalDate.now(), LocalTime.now()));
        gameMatchRepository.save(gameMatch);
        filmCombinationRepository.deleteAllByUserId(user.getId());
    }

    public GameMatch createNewGameMatchFromSessionUser(User user) throws ExistingGameMatchOpen {
        Optional<GameMatch> existingGameMatchOpened = gameMatchRepository.findByUserIdAndFinishedAtIsNull(user.getId());
        if (existingGameMatchOpened.isPresent()) {
            throw new ExistingGameMatchOpen();
        }
        GameMatch game = gameMatchRepository.save(new GameMatch(user));
        return game;
    }
}

package br.com.letscode.movies.batlle.core.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.letscode.movies.batlle.core.entities.GameMatch;
import br.com.letscode.movies.batlle.core.entities.User;
import br.com.letscode.movies.batlle.core.exceptions.GameMatchOpenNotFound;
import br.com.letscode.movies.batlle.data_providers.repositories.GameMatchRepository;

@Service
public class GameMatchService {

    @Autowired
    GameMatchRepository gameMatchRepository;

    @Autowired
    UserService userService;

    public GameMatch getCurrentGameMatchFromSessionPlayer(User user) throws GameMatchOpenNotFound {
        Optional<GameMatch> gameMatch = gameMatchRepository.findByUserId(user.getId());
        if (gameMatch.isEmpty()) {
            System.out.println("Error: No game match open was found!");
            throw new GameMatchOpenNotFound();
        }
        return gameMatch.get();
    }
}

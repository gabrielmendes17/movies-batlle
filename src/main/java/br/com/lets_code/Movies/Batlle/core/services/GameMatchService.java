package br.com.lets_code.Movies.Batlle.core.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.lets_code.Movies.Batlle.core.entities.GameMatch;
import br.com.lets_code.Movies.Batlle.core.entities.User;
import br.com.lets_code.Movies.Batlle.core.exceptions.GameMatchOpenNotFound;
import br.com.lets_code.Movies.Batlle.data_providers.repositories.GameMatchRepository;

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

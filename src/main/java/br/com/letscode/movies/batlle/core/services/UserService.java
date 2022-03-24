package br.com.letscode.movies.batlle.core.services;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.letscode.movies.batlle.core.entities.User;
import br.com.letscode.movies.batlle.data_providers.repositories.UserJdbcRepository;
import br.com.letscode.movies.batlle.data_providers.repositories.UserRepository;
import br.com.letscode.movies.batlle.presenter.rest.dtos.response.PlayerScore;

@Service
public class UserService {
    
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserJdbcRepository usersJdbcRepository;
     
    public User getUserFromPrincipal(Principal principal) {
        System.out.println(principal.getName());
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(
                        () -> new UsernameNotFoundException("User Not Found with username: " + principal.getName()));
        return user;
    }

    public List<PlayerScore> listPlayerScore() {
        return usersJdbcRepository.listPlayerScore();
    }

}

package br.com.lets_code.Movies.Batlle.core.services;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.lets_code.Movies.Batlle.core.entities.User;
import br.com.lets_code.Movies.Batlle.data_providers.repositories.UserRepository;

@Service
public class UserService {
    
    @Autowired
    UserRepository userRepository;
    
    public User getUserFromPrincipal(Principal principal) {
        System.out.println(principal.getName());
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(
                        () -> new UsernameNotFoundException("User Not Found with username: " + principal.getName()));
        return user;
    }

}

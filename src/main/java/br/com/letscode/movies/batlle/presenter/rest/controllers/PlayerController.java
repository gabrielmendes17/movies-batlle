package br.com.letscode.movies.batlle.presenter.rest.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.letscode.movies.batlle.core.services.UserService;
import br.com.letscode.movies.batlle.presenter.rest.dtos.response.PlayerScore;

@RestController
@RequestMapping("/api/player")
public class PlayerController {

    @Autowired
    UserService userService;

    @GetMapping("/score")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<PlayerScore>> listPlayersScore() {
        var playerScore = userService.listPlayerScore();
        return ResponseEntity.status(HttpStatus.OK).body(playerScore);
    }
}

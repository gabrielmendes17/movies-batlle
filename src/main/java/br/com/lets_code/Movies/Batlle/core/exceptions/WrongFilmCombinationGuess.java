package br.com.lets_code.Movies.Batlle.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Player is guessing the wrong film combination!")
public class WrongFilmCombinationGuess extends Exception {
    
}
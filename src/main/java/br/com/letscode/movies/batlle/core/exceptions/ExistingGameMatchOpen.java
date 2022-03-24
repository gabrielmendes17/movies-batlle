package br.com.letscode.movies.batlle.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Game match is already opened, please enjoy your time!")
public class ExistingGameMatchOpen extends Exception {
    
}
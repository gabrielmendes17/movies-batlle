package br.com.lets_code.Movies.Batlle.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Game match open not found!")
public class GameMatchOpenNotFound extends Exception  {
    
}
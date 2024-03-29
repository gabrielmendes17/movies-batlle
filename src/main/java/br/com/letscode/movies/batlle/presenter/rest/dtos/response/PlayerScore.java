package br.com.letscode.movies.batlle.presenter.rest.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlayerScore {
    private long id;
    private String username;
    private String email;
    private String successRate;
}

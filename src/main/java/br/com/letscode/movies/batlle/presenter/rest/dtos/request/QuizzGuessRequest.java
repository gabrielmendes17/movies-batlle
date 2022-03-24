package br.com.letscode.movies.batlle.presenter.rest.dtos.request;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizzGuessRequest {
	@NotBlank
	private String guessWinningMovieId;

	@NotBlank
	private String guessLosingMovieId;
}

package br.com.lets_code.Movies.Batlle.presenter.rest.dtos.request;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizzRequest {
	@NotBlank
	private String guessWinningMovieId;

	@NotBlank
	private String guessLosingMovieId;
}

package br.com.lets_code.Movies.Batlle.data_providers.apis;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import br.com.lets_code.Movies.Batlle.core.entities.Film;
import br.com.lets_code.Movies.Batlle.data_providers.apis.dtos.FilmDetails;
import br.com.lets_code.Movies.Batlle.data_providers.apis.dtos.GetIMDBResponse;
import br.com.lets_code.Movies.Batlle.data_providers.apis.dtos.Search;
import br.com.lets_code.Movies.Batlle.data_providers.repositories.FilmsRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class IMDBFilmStartupRunner implements ApplicationRunner {
  private static final Logger LOG = LoggerFactory.getLogger(IMDBFilmStartupRunner.class);

  public static int counter;

  private final WebClient webClient;

  @Autowired
  FilmsRepository filmsRepository;

  @Value("${key_films_search}")
  private List<String> keyFilmsSearch;

  public IMDBFilmStartupRunner(WebClient.Builder webClientBuilder,
      @Value("${imdb_api_base_url}") String nationalRegostryBaseUrl) {
    this.webClient = webClientBuilder
        .baseUrl(nationalRegostryBaseUrl)
        .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .build();
  }

  public Mono<GetIMDBResponse> searchFilm(String value) {
    return this.webClient.get()
        .uri(uriBuilder -> uriBuilder
            .queryParam("apikey", "7700a4b9")
            .queryParam("s", value)
            .build(value))
        .retrieve()
        .bodyToMono(GetIMDBResponse.class);
  }

  public Mono<FilmDetails> getFilmDetails(Search search) {
    return this.webClient.get()
        .uri(uriBuilder -> uriBuilder
            .queryParam("apikey", "7700a4b9")
            .queryParam("i", search.imdbID)
            .build(search.imdbID))
        .retrieve()
        .bodyToMono(FilmDetails.class);
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    LOG.info("Application started with opti on names : {}",
        args.getOptionNames());  

    List<GetIMDBResponse> imdbResponses = Flux.fromIterable(keyFilmsSearch)
        .flatMap(this::searchFilm)
        .collectList()
        .block();

    List<Search> filmsSearch = imdbResponses
        .stream()
        .map(r -> r.search)
        .flatMap(response -> response.stream())
        .collect(Collectors.toList());

    List<FilmDetails> filmsWithDetails = Flux.fromIterable(filmsSearch)
      .flatMap(this::getFilmDetails)
      .collectList()
      .block();

    List<Film> films = filmsWithDetails
      .stream()
      .map(filmDetail -> filmDetail.convertToFilm())
      .collect(Collectors.toList());

    System.setProperty("films_length", String.valueOf(films.size()));

    filmsSearch.forEach(film -> LOG.info(film.toString()));

    filmsRepository.saveAll(films);
    
    System.out.println(filmsSearch);
  }
}
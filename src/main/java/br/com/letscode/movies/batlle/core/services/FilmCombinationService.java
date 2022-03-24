package br.com.letscode.movies.batlle.core.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.letscode.movies.batlle.core.entities.Film;
import br.com.letscode.movies.batlle.core.entities.FilmCombination;
import br.com.letscode.movies.batlle.core.entities.User;
import br.com.letscode.movies.batlle.data_providers.repositories.FilmCombinationRepository;
import br.com.letscode.movies.batlle.data_providers.repositories.FilmRepository;

@Service
public class FilmCombinationService {

    private static final Logger logger = LoggerFactory.getLogger(FilmCombinationService.class);
    
    @Autowired
    FilmCombinationRepository filmCombinationRepository;

    @Autowired
    FilmRepository filmsRepository;

    private void helper(List<int[]> combinations, int data[], int start, int end, int index) {
        if (index == data.length) {
            int[] combination = data.clone();
            combinations.add(combination);
        } else if (start <= end) {
            data[index] = start;
            helper(combinations, data, start + 1, end, index + 1);
            helper(combinations, data, start + 1, end, index);
        }
    }

    public List<int[]> generate(int n, int r) {
        List<int[]> combinations = new ArrayList<>();
        helper(combinations, new int[r], 1, n, 0);
        return combinations;
    }

    @Transactional
    public void generateFilmCombination(int n, int r, User user) {
        List<int[]> combination = this.generate(n, r);
        logger.info("films combination generated with success");
        Collections.shuffle(combination);
        logger.info("shuffling films combination");
        List<FilmCombination> filmsCombination = combination
            .stream()
            .map(comb -> new FilmCombination(0, comb[0], comb[1], user))
            .collect(Collectors.toList());
        List<FilmCombination> films = filmCombinationRepository.saveAllAndFlush(filmsCombination);
        logger.info("saved {} films combination", films.size());
    }

    public List<Film> getCurrentFilmCombination(User user) {
        FilmCombination filmCombination = filmCombinationRepository.findFirst1ByUserIdAndAttemptsLessThanOrderByIdAsc(user.getId(), 3).get();
        List<Film> films = filmsRepository.findAllById(Arrays.asList(String.valueOf(filmCombination.getFirstFilmCombination()), String.valueOf(filmCombination.getSecondFilmCombination())));
        return films;
    }
}

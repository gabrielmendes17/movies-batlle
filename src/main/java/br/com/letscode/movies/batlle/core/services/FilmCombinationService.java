package br.com.letscode.movies.batlle.core.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.letscode.movies.batlle.core.entities.FilmCombination;
import br.com.letscode.movies.batlle.core.entities.User;
import br.com.letscode.movies.batlle.data_providers.repositories.FilmCombinationRepository;

@Service
public class FilmCombinationService {
    
    @Autowired
    FilmCombinationRepository filmCombinationRepository;

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

    public void generateFilmCombination(int n, int r, User user) {
        List<int[]> combination = this.generate(n, r);
        List<FilmCombination> filmsCombination = combination
            .stream()
            .map(comb -> new FilmCombination(0, comb[0], comb[1], user))
            .collect(Collectors.toList());

        List<FilmCombination> saveAll = filmCombinationRepository.saveAll(filmsCombination);

        System.out.println(saveAll);
    }
}

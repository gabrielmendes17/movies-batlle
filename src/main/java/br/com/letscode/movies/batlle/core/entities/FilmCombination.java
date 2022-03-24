package br.com.letscode.movies.batlle.core.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "FILM_COMBINATION_TEMP")
public class FilmCombination implements Serializable {
    private static final long serialVersionUID = 1L;

    public FilmCombination(int attempts, int firstFilmCombination, int secondFilmCombination, User user) {
        this.attempts = attempts;
        this.firstFilmCombination = firstFilmCombination;
        this.secondFilmCombination = secondFilmCombination;
        this.user = user;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private String id;

    @Column(name = "ATTEMPTS")
    private int attempts;

    @Column(name = "FIRST_FILM_ID")
    private int firstFilmCombination;

    @Column(name = "SECOND_FILM_ID")
    private int secondFilmCombination;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;
}

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

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "GAME_ROUND")
public class GameRound implements Serializable {
    private static final long serialVersionUID = 1L;

    public GameRound(GameMatch gameMatch, int score) {
        this.gameMatch = gameMatch;
        this.score = score;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="GAME_MATCH_ID", nullable = false)
    private GameMatch gameMatch;

    @Column(name = "SCORE")
    private int score;
}

package br.com.lets_code.Movies.Batlle.core.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "GAME_MATCH")
public class GameMatch implements Serializable {
    private static final long serialVersionUID = 1L;

    public GameMatch(User user) {
        this.user = user;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name ="USER_ID", nullable = false)
    private User user;

    @CreationTimestamp
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "FINISHED_AT")
    private LocalDateTime finishedAt;

    @OneToMany(mappedBy = "gameMatch", cascade = CascadeType.ALL)
    private List<GameRound> gameRounds = new ArrayList<>();
}

package br.com.lets_code.Movies.Batlle.core.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Film")
public class Film implements Serializable {

    private static final long serialVersionUID = 1L;

    public Film(String id, String title, String year, String type, String poster, String rating, String votes) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.type = type;
        this.poster = poster;
        this.rating = Double.valueOf(rating);
        this.votes =  votes;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private String id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "YEAR")
    private String year;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "POSTER")
    private String poster;

    @CreationTimestamp
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    private LocalDateTime updateAt;

    @Column(name = "RATING")
    @JsonIgnore
    private Double rating;

    @Column(name = "VOTES")
    private String votes;

}
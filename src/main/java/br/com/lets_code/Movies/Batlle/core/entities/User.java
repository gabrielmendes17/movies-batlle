package br.com.lets_code.Movies.Batlle.core.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "USER", 
    uniqueConstraints = { 
      @UniqueConstraint(columnNames = "USERNAME"),
      @UniqueConstraint(columnNames = "EMAIL") 
    })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Size(max = 20)
  @Column(name = "USERNAME")
  private String username;

  @NotBlank
  @Size(max = 50)
  @Email
  @Column(name = "EMAIL")
  private String email;

  @NotBlank
  @Size(max = 120)
  @Column(name = "PASSWORD")
  private String password;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "USER_ROLE", 
        joinColumns = @JoinColumn(name = "USER_ID"), 
        inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
  private Set<Role> roles = new HashSet<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<FilmCombination> filmCombination = new ArrayList<>();

  public User(String username, String email, String password) {
    this.username = username;
    this.email = email;
    this.password = password;
  }
}

package br.com.lets_code.Movies.Batlle.data_providers.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.lets_code.Movies.Batlle.core.entities.Role;
import br.com.lets_code.Movies.Batlle.core.enums.ERole;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(ERole name);
}

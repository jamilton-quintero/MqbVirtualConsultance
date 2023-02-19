package com.example.analisis.repositories;


import com.example.analisis.entidad.Authority;
import com.example.analisis.entidad.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {
	
    User findByUsername(String username);

    List<User> findByAuthoritiesIn(List<Authority> authority);
}

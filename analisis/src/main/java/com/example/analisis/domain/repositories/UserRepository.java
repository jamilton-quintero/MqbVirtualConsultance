package com.example.analisis.domain.repositories;


import com.example.analisis.domain.entity.Authority;
import com.example.analisis.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {
	
    User findByUsername(String username);

    List<User> findByAuthoritiesIn(List<Authority> authority);
}

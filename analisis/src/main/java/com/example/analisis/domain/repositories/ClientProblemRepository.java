package com.example.analisis.domain.repositories;


import com.example.analisis.domain.entity.ClientProblem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ClientProblemRepository extends JpaRepository<ClientProblem, Long> {

}

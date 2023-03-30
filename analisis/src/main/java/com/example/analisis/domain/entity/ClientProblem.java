package com.example.analisis.domain.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "client_problem")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientProblem {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "PROBLEM", columnDefinition = "text")
    private String problem;

    @Column(name = "CLIENT_AGE")
    private byte clientAge;
    @Column(name = "CLIENT_GENDER")
    private char clientGender;
    @Column(name = "SOLUTION_SCIENTIFIC_PROBLEM", columnDefinition = "text")
    private String solutionScientificProblem;

    @Column(name = "SOLUTION_ROUTINE_PROBLEM", columnDefinition = "text")
    private String solutionRoutineProblem;

    @Column(name = "BRAND_PROBLEM", length = 100)
    private String brandProblem;

}

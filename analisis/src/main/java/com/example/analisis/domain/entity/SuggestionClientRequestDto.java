package com.example.analisis.domain.entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SuggestionClientRequestDto {

    @NotNull
    private char clientGender;
    @NotNull
    private byte clientAge;
    @NotNull
    private String message;

}

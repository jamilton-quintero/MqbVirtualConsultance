package com.example.analisis.entidad;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BoardClientRequestDto {

    @NotNull
    private char clientGender;
    @NotNull
    private byte clientAge;
    @NotNull
    private String message;

}

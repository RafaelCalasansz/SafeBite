package com.safebite.dto;

import lombok.Data;
import jakarta.validation.constraints.NotEmpty;

@Data
public class FoodDiaryDTO {

    @NotEmpty
    private String alimento;

    private String observacoes;
}
package com.safebite.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;


@Data
public class ReactionDTO {

    @NotEmpty(message = "Alimentos não pode ser vazio")
    private String alimentosConsumidos;

    @NotEmpty(message = "Intensidade não pode ser vazia")
    private String intensidadeSintomas;

    private String medicamentosUtilizados;
    private String localOcorrencia;

    @NotNull(message = "Data e hora não pode ser vazia")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)

    private LocalDateTime dataHoraReacao;
    private String evolucaoQuadro;
}
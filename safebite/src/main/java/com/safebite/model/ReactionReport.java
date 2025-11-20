package com.safebite.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Immutable;

import java.time.LocalDateTime;

@Entity
@Immutable
@Table(name = "view_relatorio_reacoes")
@Data
public class ReactionReport {

    @Id
    private Long reacaoId;

    private String nomePaciente;
    private String emailPaciente;
    private String alimentosConsumidos;
    private String intensidadeSintomas;
    private LocalDateTime dataHoraReacao;
    private String localOcorrencia;
}
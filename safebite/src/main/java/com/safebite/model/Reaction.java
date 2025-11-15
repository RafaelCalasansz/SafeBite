package com.safebite.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reacoes")
@Data
@NoArgsConstructor
public class Reaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String alimentosConsumidos;
    private String intensidadeSintomas;
    private String medicamentosUtilizados;
    private String localOcorrencia;
    private LocalDateTime dataHoraReacao;

    @Column(columnDefinition = "TEXT")
    private String evolucaoQuadro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
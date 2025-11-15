package com.safebite.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "carteirinha_emergencia")
@Data
@NoArgsConstructor
public class EmergencyCard {

    @Id
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String contatosEmergencia;

    @Column(columnDefinition = "TEXT")
    private String medicamentosContinuos;

    @Column(columnDefinition = "TEXT")
    private String instrucoesMedicas;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;
}
package com.safebite.service;

import com.safebite.dto.EmergencyCardDTO;
import com.safebite.exception.ResourceNotFoundException;
import com.safebite.model.EmergencyCard;
import com.safebite.model.User;
import com.safebite.repository.EmergencyCardRepository;
import com.safebite.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmergencyCardService {

    @Autowired
    private EmergencyCardRepository cardRepository;

    @Autowired
    private UserRepository userRepository;

    public EmergencyCard getCardByUserEmail(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + userEmail));


        return cardRepository.findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Carteirinha não encontrada para o usuário: " + user.getId()));
    }

    public EmergencyCard updateCard(String userEmail, EmergencyCardDTO dto) {
        EmergencyCard card = getCardByUserEmail(userEmail);

        card.setContatosEmergencia(dto.getContatosEmergencia());
        card.setMedicamentosContinuos(dto.getMedicamentosContinuos());
        card.setInstrucoesMedicas(dto.getInstrucoesMedicas());

        return cardRepository.save(card);
    }

    public EmergencyCard getCardByUserId(Long userId) {
        return cardRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Carteirinha não encontrada para o usuário: " + userId));
    }

    public EmergencyCard updateCard(Long userId, EmergencyCardDTO dto) {
        EmergencyCard card = getCardByUserId(userId);
        return cardRepository.save(card);
    }
}
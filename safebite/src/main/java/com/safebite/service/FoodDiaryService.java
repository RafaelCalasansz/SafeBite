package com.safebite.service;

import com.safebite.dto.FoodDiaryDTO;
import com.safebite.exception.ResourceNotFoundException;
import com.safebite.model.FoodDiary;
import com.safebite.model.User;
import com.safebite.repository.FoodDiaryRepository;
import com.safebite.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FoodDiaryService {

    @Autowired
    private FoodDiaryRepository foodDiaryRepository;

    @Autowired
    private UserRepository userRepository;

    public List<FoodDiary> getDiaryForUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + userEmail));

        return foodDiaryRepository.findByUserIdOrderByDataHoraDesc(user.getId());
    }


    public FoodDiary saveDiaryEntry(String userEmail, FoodDiaryDTO dto) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + userEmail));

        FoodDiary entry = new FoodDiary();
        entry.setUser(user);
        entry.setAlimento(dto.getAlimento());
        entry.setObservacoes(dto.getObservacoes());
        entry.setDataHora(LocalDateTime.now());

        return foodDiaryRepository.save(entry);
    }

    @Transactional
    public void deleteDiaryEntry(Long entryId, String userEmail) {

        FoodDiary entry = foodDiaryRepository.findById(entryId)
                .orElseThrow(() -> new ResourceNotFoundException("Registro do diário não encontrado: " + entryId));

        if (!entry.getUser().getEmail().equals(userEmail)) {
            throw new ResourceNotFoundException("Acesso negado. O registro não pertence a este usuário.");
        }

        foodDiaryRepository.delete(entry);
    }

}
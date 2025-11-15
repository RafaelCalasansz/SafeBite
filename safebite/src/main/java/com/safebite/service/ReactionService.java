package com.safebite.service;

import com.safebite.dto.ReactionDTO;
import com.safebite.exception.ResourceNotFoundException;
import com.safebite.model.Reaction;
import com.safebite.model.User;
import com.safebite.repository.ReactionRepository;
import com.safebite.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReactionService {

    @Autowired
    private ReactionRepository reactionRepository;

    @Autowired
    private UserRepository userRepository;

    public Reaction recordReaction(String userEmail, ReactionDTO dto) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com e-mail: " + userEmail));


        Reaction reaction = new Reaction();
        reaction.setUser(user);
        reaction.setAlimentosConsumidos(dto.getAlimentosConsumidos());
        reaction.setIntensidadeSintomas(dto.getIntensidadeSintomas());
        reaction.setMedicamentosUtilizados(dto.getMedicamentosUtilizados());
        reaction.setLocalOcorrencia(dto.getLocalOcorrencia());
        reaction.setDataHoraReacao(dto.getDataHoraReacao());


        reaction.setEvolucaoQuadro(dto.getEvolucaoQuadro());

        return reactionRepository.save(reaction);
    }

    public Reaction recordReaction(Long userId, ReactionDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + userId));

        Reaction reaction = new Reaction();

        reaction.setUser(user);
        reaction.setAlimentosConsumidos(dto.getAlimentosConsumidos());
        reaction.setIntensidadeSintomas(dto.getIntensidadeSintomas());
        reaction.setMedicamentosUtilizados(dto.getMedicamentosUtilizados());
        reaction.setLocalOcorrencia(dto.getLocalOcorrencia());
        reaction.setDataHoraReacao(dto.getDataHoraReacao());
        reaction.setEvolucaoQuadro(dto.getEvolucaoQuadro());


        return reactionRepository.save(reaction);
    }

    public List<Reaction> getReactionsForUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + userEmail));

        return reactionRepository.findByUserIdOrderByDataHoraReacaoDesc(user.getId());
    }

    public List<Reaction> getReactionsForUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Usuário não encontrado: " + userId);
        }
        return reactionRepository.findByUserIdOrderByDataHoraReacaoDesc(userId);
    }

    @Transactional
    public void deleteReaction(Long reactionId, String userEmail) {

        Reaction reaction = reactionRepository.findById(reactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Reação não encontrada: " + reactionId));

        if (!reaction.getUser().getEmail().equals(userEmail)) {
            throw new ResourceNotFoundException("Acesso negado. A reação não pertence a este usuário.");
        }

        reactionRepository.delete(reaction);
    }




}
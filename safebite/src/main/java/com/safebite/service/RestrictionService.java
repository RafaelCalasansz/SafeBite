package com.safebite.service;

import com.safebite.model.Restriction;
import com.safebite.model.User;
import com.safebite.repository.RestrictionRepository;
import com.safebite.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RestrictionService {

    @Autowired
    private RestrictionRepository restrictionRepository;

    @Autowired
    private UserRepository userRepository;


    public List<Restriction> getAllRestrictions() {
        return restrictionRepository.findAll();
    }

    public Restriction findOrCreateByName(String name) {

        String formattedName = name.trim();
        formattedName = formattedName.substring(0, 1).toUpperCase() + formattedName.substring(1).toLowerCase();

        Optional<Restriction> existing = restrictionRepository.findByNome(formattedName);

        if (existing.isPresent()) {

            return existing.get();
        } else {

            Restriction newRestriction = new Restriction();
            newRestriction.setNome(formattedName);
            newRestriction.setDescricao("Restrição adicionada pelo usuário.");
            return restrictionRepository.save(newRestriction);
        }
    }

    @Transactional
    public void deleteCustomRestriction(Long restrictionId) {
        Restriction restriction = restrictionRepository.findById(restrictionId)
                .orElseThrow(() -> new RuntimeException("Restrição não encontrada"));

        if (!"Restrição adicionada pelo usuário.".equals(restriction.getDescricao())) {
            throw new RuntimeException("Não é possível excluir uma restrição padrão do sistema.");
        }

        List<User> users = userRepository.findAllByRestrictionsId(restrictionId);

        for (User user : users) {
            user.getRestrictions().remove(restriction);
            userRepository.save(user);
        }
        restrictionRepository.delete(restriction);
    }

}
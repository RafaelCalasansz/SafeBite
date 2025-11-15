package com.safebite.service;

import com.safebite.dto.UserRegisterDTO;
import com.safebite.exception.ResourceNotFoundException;
import com.safebite.model.EmergencyCard;
import com.safebite.model.Restriction;
import com.safebite.model.User;
import com.safebite.repository.RestrictionRepository;
import com.safebite.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RestrictionRepository restrictionRepository;

    @Autowired
    private RestrictionService restrictionService;

    @Transactional
    public User registerUser(UserRegisterDTO dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email já cadastrado");
        }

        User user = new User();
        user.setNome(dto.getNome());
        user.setEmail(dto.getEmail());


        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        EmergencyCard card = new EmergencyCard();
        card.setUser(user);
        user.setEmergencyCard(card);

        return userRepository.save(user);
    }

    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + id));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));
    }

    @Transactional
    public void updateRestrictionsForUser(String userEmail, List<Long> restrictionIds, String outraRestricao) {
        User user = findByEmail(userEmail);
        Set<Restriction> newRestrictions = new HashSet<>();

        if (restrictionIds != null && !restrictionIds.isEmpty()) {
            List<Restriction> foundRestrictions = restrictionRepository.findAllById(restrictionIds);
            newRestrictions.addAll(foundRestrictions);
        }

        if (outraRestricao != null && !outraRestricao.trim().isEmpty()) {
            Restriction outra = restrictionService.findOrCreateByName(outraRestricao);
            newRestrictions.add(outra);
        }

        user.setRestrictions(newRestrictions);
        userRepository.save(user);
    }

}
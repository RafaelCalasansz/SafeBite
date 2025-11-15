package com.safebite.controller;

import com.safebite.dto.EmergencyCardDTO;
import com.safebite.model.EmergencyCard;
import com.safebite.service.EmergencyCardService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/{userId}/emergency-card")
public class EmergencyCardController {

    @Autowired
    private EmergencyCardService cardService;

    @GetMapping
    public ResponseEntity<EmergencyCard> getCard(@PathVariable Long userId) {
        EmergencyCard card = cardService.getCardByUserId(userId);
        return ResponseEntity.ok(card);
    }

    @PutMapping
    public ResponseEntity<EmergencyCard> updateCard(
            @PathVariable Long userId,
            @Valid @RequestBody EmergencyCardDTO dto) {

        EmergencyCard updatedCard = cardService.updateCard(userId, dto);
        return ResponseEntity.ok(updatedCard);
    }
}
package com.safebite.controller;

import com.safebite.dto.ReactionDTO;
import com.safebite.model.Reaction;
import com.safebite.service.ReactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/reactions")
public class ReactionController {

    @Autowired
    private ReactionService reactionService;

    @PostMapping
    public ResponseEntity<Reaction> recordReaction(
            @PathVariable Long userId,
            @Valid @RequestBody ReactionDTO dto) {

        Reaction newReaction = reactionService.recordReaction(String.valueOf(userId), dto);
        return ResponseEntity.created(URI.create("/api/users/" + userId + "/reactions/" + newReaction.getId())).body(newReaction);
    }

    @GetMapping
    public ResponseEntity<List<Reaction>> getReactions(@PathVariable Long userId) {
        List<Reaction> reactions = reactionService.getReactionsForUser(userId);
        return ResponseEntity.ok(reactions);
    }
}
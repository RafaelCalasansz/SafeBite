package com.safebite.controller;

import com.safebite.dto.FoodDiaryDTO;
import com.safebite.model.FoodDiary;
import com.safebite.service.FoodDiaryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("/api/diary")
public class DiaryController {

    @Autowired
    private FoodDiaryService diaryService;

    @GetMapping
    public ResponseEntity<List<FoodDiary>> getDiaryEntries(Principal principal) {
        List<FoodDiary> entries = diaryService.getDiaryForUser(principal.getName());
        return ResponseEntity.ok(entries);
    }

    @PostMapping
    public ResponseEntity<FoodDiary> saveDiaryEntry(
            @Valid @RequestBody FoodDiaryDTO dto,
            Principal principal) {

        FoodDiary newEntry = diaryService.saveDiaryEntry(principal.getName(), dto);

        return ResponseEntity.created(URI.create("/api/diary/" + newEntry.getId())).body(newEntry);
    }
}
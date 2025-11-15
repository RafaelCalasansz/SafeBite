package com.safebite.controller;

import com.safebite.dto.EmergencyCardDTO;
import com.safebite.dto.FoodDiaryDTO;
import com.safebite.dto.ReactionDTO;
import com.safebite.dto.UserRegisterDTO;
import com.safebite.model.*;
import com.safebite.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@Controller
public class ViewController {

    @Autowired
    private UserService userService;

    @Autowired
    private ReactionService reactionService;

    @Autowired
    private EmergencyCardService cardService;

    @Autowired
    private FoodDiaryService diaryService;

    @Autowired
    private RestrictionService restrictionService;


    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/")
    public String showHomePage(Model model, Principal principal) {

        String email = principal.getName();
        User user = userService.findByEmail(email);
        model.addAttribute("user", user);

        return "home";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new UserRegisterDTO());

        return "register";
    }

    @PostMapping("/register")
    public String processRegistration(@Valid @ModelAttribute("user") UserRegisterDTO dto) {

        try {
            userService.registerUser(dto);
        } catch (RuntimeException e) {
            return "redirect:/register?error";
        }

        return "redirect:/login?success";
    }

    @GetMapping("/reactions/new")
    public String showReactionForm(Model model) {

        model.addAttribute("reaction", new ReactionDTO());
        return "reactionForm";
    }

    @PostMapping("/reactions/new")
    public String processReaction(
            @Valid @ModelAttribute("reaction") ReactionDTO dto,
            Principal principal) {
        String userEmail = principal.getName();

        try {
            reactionService.recordReaction(userEmail, dto);
        } catch (Exception e) {
            return "redirect:/reactions/new?error";
        }

        return "redirect:/?reactionSuccess";
    }

    @GetMapping("/history")
    public String showHistoryPage(Model model, Principal principal) {

        String userEmail = principal.getName();
        List<Reaction> reactions = reactionService.getReactionsForUser(userEmail);
        model.addAttribute("reactions", reactions);

        return "history";
    }

    @GetMapping("/emergencyCard")
    public String showEmergencyCardPage(Model model, Principal principal) {
        EmergencyCard card = cardService.getCardByUserEmail(principal.getName());
        model.addAttribute("card", card);

        return "emergencyCard";
    }


    @PostMapping("/emergencyCard")
    public String updateEmergencyCard(
            @Valid @ModelAttribute("card") EmergencyCardDTO dto,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        try {
            cardService.updateCard(principal.getName(), dto);
            redirectAttributes.addFlashAttribute("successMessage", "Carteirinha atualizada com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao atualizar a carteirinha.");

            return "redirect:/emergencyCard";
        }

        return "redirect:/emergencyCard/view";
    }

    @GetMapping("/emergencyCard/view")
    public String showEmergencyCardViewPage(Model model, Principal principal) {

        EmergencyCard card = cardService.getCardByUserEmail(principal.getName());
        model.addAttribute("card", card);

        return "emergencyCardDisplay";
    }

    @GetMapping("/foodDiary")
    public String showFoodDiaryPage(Model model, Principal principal) {

        List<FoodDiary> entries = diaryService.getDiaryForUser(principal.getName());
        model.addAttribute("entries", entries);
        model.addAttribute("newEntry", new FoodDiaryDTO());

        return "foodDiary";
    }

    @PostMapping("/foodDiary")
    public String saveFoodDiaryEntry(
            @Valid @ModelAttribute("newEntry") FoodDiaryDTO dto,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        try {
            diaryService.saveDiaryEntry(principal.getName(), dto);
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao salvar registro.");
        }

        return "redirect:/foodDiary";
    }

    @GetMapping("/myRestrictions")
    public String showMyRestrictionsPage(Model model, Principal principal) {


        User user = userService.findByEmail(principal.getName());
        List<Restriction> allRestrictions = restrictionService.getAllRestrictions();
        Set<Restriction> myRestrictions = user.getRestrictions();
        model.addAttribute("allRestrictions", allRestrictions);
        model.addAttribute("myRestrictions", myRestrictions);

        return "myRestrictions";
    }

    @PostMapping("/myRestrictions")
    public String updateMyRestrictions(
            @RequestParam(value = "restrictionIds", required = false) List<Long> restrictionIds,
            @RequestParam(value = "outraRestricao", required = false) String outraRestricao, // 1. ADICIONE ESTE PARÂMETRO
            Principal principal,
            RedirectAttributes redirectAttributes) {

        try {
            userService.updateRestrictionsForUser(principal.getName(), restrictionIds, outraRestricao);
            redirectAttributes.addFlashAttribute("successMessage", "Restrições atualizadas com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao atualizar restrições.");
        }

        return "redirect:/myRestrictions";
    }

    @GetMapping("/myRestrictions/delete/{id}")
    public String deleteRestriction(
            @PathVariable("id") Long restrictionId,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        try {
            restrictionService.deleteCustomRestriction(restrictionId);
            redirectAttributes.addFlashAttribute("successMessage", "Restrição personalizada excluída com sucesso.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao excluir restrição: " + e.getMessage());
        }
        return "redirect:/myRestrictions";
    }

    @PostMapping("/history/delete/{id}")
    public String deleteReaction(
            @PathVariable("id") Long reactionId,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        try {
            reactionService.deleteReaction(reactionId, principal.getName());
            redirectAttributes.addFlashAttribute("successMessage", "Reação excluída com sucesso.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao excluir reação: " + e.getMessage());
        }
        return "redirect:/history";
    }

    @PostMapping("/foodDiary/delete/{id}")
    public String deleteFoodDiaryEntry(
            @PathVariable("id") Long entryId,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        try {
            diaryService.deleteDiaryEntry(entryId, principal.getName());
            redirectAttributes.addFlashAttribute("successMessage", "Registro do diário excluído com sucesso.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao excluir registro: " + e.getMessage());
        }
        return "redirect:/foodDiary";
    }

}
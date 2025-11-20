package com.safebite.controller;

import com.safebite.service.JdbcNativeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/test-db")
public class JdbcTestController {

    @Autowired
    private JdbcNativeService jdbcService;

    @GetMapping("/view")
    public List<Map<String, Object>> testarView() {
        return jdbcService.buscarRelatorioView();
    }

    @GetMapping("/count/{userId}")
    public String testarFunction(@PathVariable Long userId) {
        int qtd = jdbcService.contarReacoesUsuario(userId);
        return "O usuário " + userId + " tem " + qtd + " reações registradas (via Function).";
    }

    @PostMapping("/clean/{dias}")
    public String testarProcedure(@PathVariable int dias) {
        jdbcService.limparHistoricoAntigo(dias);
        return "Procedure executada! Reações com mais de " + dias + " dias foram excluídas.";
    }
}
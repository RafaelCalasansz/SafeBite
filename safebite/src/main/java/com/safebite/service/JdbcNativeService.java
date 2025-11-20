package com.safebite.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JdbcNativeService {

    @Autowired
    private DataSource dataSource;

    public List<Map<String, Object>> buscarRelatorioView() {
        List<Map<String, Object>> resultados = new ArrayList<>();
        String sql = "SELECT * FROM view_relatorio_reacoes";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> linha = new HashMap<>();
                linha.put("paciente", rs.getString("nome_paciente"));
                linha.put("alimentos", rs.getString("alimentos_consumidos"));
                linha.put("intensidade", rs.getString("intensidade_sintomas"));
                linha.put("data", rs.getTimestamp("data_hora_reacao"));
                resultados.add(linha);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultados;
    }

    public int contarReacoesUsuario(Long userId) {
        int qtd = 0;
        String sql = "SELECT fn_contar_reacoes(?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    qtd = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return qtd;
    }

    public void limparHistoricoAntigo(int dias) {
        String sql = "CALL sp_excluir_reacoes_antigas(?)";

        try (Connection conn = dataSource.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setInt(1, dias);
            cs.execute();
            System.out.println("Procedure executada: histórico antigo limpo via JDBC.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
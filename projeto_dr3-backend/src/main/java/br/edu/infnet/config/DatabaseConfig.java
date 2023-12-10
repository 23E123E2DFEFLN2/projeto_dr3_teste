package br.edu.infnet.config;

import java.sql.*;

public class DatabaseConfig {

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "");
    }

    public static void inicializarBancoDeDados() {
        try (Connection connection = conectar();
             Statement statement = connection.createStatement()) {

            // Criação da tabela de usuários
            statement.execute("CREATE TABLE IF NOT EXISTS usuarios (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "nome VARCHAR(255) NOT NULL," +
                    "senha VARCHAR(255) NOT NULL)");

            // Inserção de alguns dados de exemplo
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO usuarios (nome, senha) VALUES (?, ?)")) {
                preparedStatement.setString(1, "usuario");
                preparedStatement.setString(2, "senha123");
                preparedStatement.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
package br.edu.infnet.config;

import org.h2.tools.Server;

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


            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO usuarios (nome, senha) VALUES (?, ?)")) {
                preparedStatement.setString(1, "usuario");
                preparedStatement.setString(2, "senha123");
                preparedStatement.executeUpdate();
                Server.createWebServer("-web", "-webAllowOthers", "-webDaemon", "-webPort", "8082").start();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
package br.edu.infnet.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.h2.tools.Server;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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

            // Inserir usuários da API randomuser.me
            inserirUsuariosDaAPI(connection); // Inserir 5 usuários, por exemplo

            // Iniciar o console do H2
            Server.createWebServer("-web", "-webAllowOthers", "-webDaemon", "-webPort", "8082").start();

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void inserirUsuariosDaAPI(Connection connection) throws IOException, SQLException {
        String apiUrl = "https://randomuser.me/api/?results=" + 10;
        URL url = new URL(apiUrl);
        HttpURLConnection connectionAPI = (HttpURLConnection) url.openConnection();

        try (InputStream responseStream = connectionAPI.getInputStream()) {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(responseStream);

            if (jsonResponse.has("results")) {
                for (JsonNode userNode : jsonResponse.get("results")) {
                    String nome = userNode.get("name").get("first").asText();
                    String senha = userNode.get("login").get("password").asText();

                    try (PreparedStatement preparedStatement = connection.prepareStatement(
                            "INSERT INTO usuarios (nome, senha) VALUES (?, ?)")) {
                        preparedStatement.setString(1, nome);
                        preparedStatement.setString(2, senha);
                        preparedStatement.executeUpdate();
                    }
                }
            }
        }
    }
}

package br.edu.infnet.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConfig {

    private static final String URL = "jdbc:mysql://localhost:3306/pbback";
    private static final String USUARIO = "infnet";
    private static final String SENHA = "Pb@back";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }

    public static void inicializarBancoDeDados() {
        try (Connection connection = conectar();
             Statement statement = connection.createStatement()) {


            statement.execute("CREATE TABLE IF NOT EXISTS usuarios (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "nome VARCHAR(255) NOT NULL," +
                    "senha VARCHAR(255) NOT NULL)");


            inserirUsuariosDaAPI(connection);

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void inserirUsuariosDaAPI(Connection connection) throws IOException, SQLException {
        String apiUrl = "https://randomuser.me/api/?results=" + 3;
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

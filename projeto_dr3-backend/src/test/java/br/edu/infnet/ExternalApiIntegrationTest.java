package br.edu.infnet;
//todo nao exigido mas corrigir bug
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExternalApiIntegrationTest {

    @Test
    public void testInserirUsuarioViaApiExterna() throws Exception {
        // Chama a API externa para obter dados de um usuário
        URL externalApiUrl = new URL("https://randomuser.me/api/");
        HttpURLConnection externalConnection = (HttpURLConnection) externalApiUrl.openConnection();

        externalConnection.setRequestMethod("GET");
        int externalResponseCode = externalConnection.getResponseCode();

        // Verifica se o código de resposta é 200 (OK)
        assertEquals(200, externalResponseCode);

        // Lê a resposta da API externa
        BufferedReader externalIn = new BufferedReader(new InputStreamReader(externalConnection.getInputStream()));
        StringBuilder externalResponse = new StringBuilder();
        String externalInputLine;
        while ((externalInputLine = externalIn.readLine()) != null) {
            externalResponse.append(externalInputLine);
        }
        externalIn.close();

        // Converte a resposta da API externa para um objeto JsonNode
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode userData = objectMapper.readTree(externalResponse.toString()).get("results").get(0);

        // Substitua a URL abaixo pela URL real da sua aplicação
        URL localApiUrl = new URL("http://localhost:4567/usuarios");
        HttpURLConnection localConnection = (HttpURLConnection) localApiUrl.openConnection();

        localConnection.setRequestMethod("POST");
        localConnection.setRequestProperty("Content-Type", "application/json");
        localConnection.setDoOutput(true);

        // Envia os dados do usuário obtidos da API externa para a sua API local
        objectMapper.writeValue(localConnection.getOutputStream(), userData);

        int localResponseCode = localConnection.getResponseCode();

        // Verifica se o código de resposta é 201 (Created)
        assertEquals(201, localResponseCode);
    }
}

package br.edu.infnet;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IntegrationTest {

    @Test
    public void testListarUsuarios() throws Exception {
        // Substitua a URL abaixo pela URL real da sua aplicação
        URL url = new URL("http://localhost:4567/usuarios");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();

        // Verifica se o código de resposta é 200 (OK)
        assertEquals(200, responseCode);

        // Lê a resposta
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // Verifica se a resposta é uma lista não vazia
        assertEquals(false, response.toString().isEmpty());
    }
}

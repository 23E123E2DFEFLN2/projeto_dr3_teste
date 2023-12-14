package br.edu.infnet.controller;

import br.edu.infnet.config.DatabaseConfig;
import br.edu.infnet.dto.UsuarioDTOInput;
import br.edu.infnet.dto.UsuarioDTOOutput;
import br.edu.infnet.model.Usuario;
import br.edu.infnet.service.UsuarioService;
import br.edu.infnet.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mindrot.jbcrypt.BCrypt;
import spark.Request;
import spark.Response;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static spark.Spark.*;

public class UsuarioController {

    private UsuarioService usuarioService = new UsuarioService();
    private ObjectMapper objectMapper = new ObjectMapper();

    public void respostasRequisicoes() {

        get("/usuarios", (request, response) -> {
            response.type("application/json");
            return objectMapper.writeValueAsString(listarUsuarios(request, response));
        });


        get("/usuarios/:id", (request, response) -> {
            response.type("application/json");
            return objectMapper.writeValueAsString(buscarUsuario(request, response));
        });


        delete("/usuarios/:id", (request, response) -> {
            excluirUsuario(request, response);
            return "";
        });


        post("/usuarios", (request, response) -> {
            inserirUsuario(request, response);
            return "";
        });


        put("/usuarios", (request, response) -> {
            atualizarUsuario(request, response);
            return "";
        });


        post("/login", this::autenticar);
    }

    private List<UsuarioDTOOutput> listarUsuarios(Request request, Response response) {
        response.type("application/json");
        return usuarioService.listar();
    }

    private UsuarioDTOOutput buscarUsuario(Request request, Response response) {
        response.type("application/json");
        int id = Integer.parseInt(request.params(":id"));
        return usuarioService.buscar(id);
    }

    private void excluirUsuario(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        usuarioService.excluir(id);
        response.status(204); // No content
    }

    private void inserirUsuario(Request request, Response response) throws Exception {
        UsuarioDTOInput usuarioDTOInput = objectMapper.readValue(request.body(), UsuarioDTOInput.class);


        Usuario usuario = new Usuario();
        usuario.setNome(usuarioDTOInput.getNome());
        usuario.setSenha(usuarioDTOInput.getSenha());

        usuarioService.inserir(usuarioDTOInput);
        response.status(201); // Created
    }

    private void atualizarUsuario(Request request, Response response) throws Exception {
        UsuarioDTOInput usuarioDTOInput = objectMapper.readValue(request.body(), UsuarioDTOInput.class);
        usuarioService.alterar(usuarioDTOInput);
        response.status(200); // OK
    }

    private void verificarToken(Request request, Response response) {
        String token = request.headers("Authorization");

        if (token == null || !JwtUtil.validarToken(token)) {
            halt(401, "Token inválido ou ausente");
        }


        String username = JwtUtil.extrairUsername(token);
        try (Connection connection = DatabaseConfig.conectar();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM usuarios WHERE nome = ?")) {

            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                halt(401, "Usuário não encontrado");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            halt(500, "Erro ao verificar token");
        }
    }
    private String autenticar(Request request, Response response) {
        String username = request.queryParams("username");
        String senha = request.queryParams("senha");


        try (Connection connection = DatabaseConfig.conectar();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM usuarios WHERE nome = ?")) {

            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String senhaArmazenada = resultSet.getString("senha");
                // Comparação de senhas usando BCrypt
                if (BCrypt.checkpw(senha, senhaArmazenada)) {
                    String token = JwtUtil.criarToken(username);
                    return token;
                }
            }

            response.status(401); // Unauthorized
            return "Credenciais inválidas";

        } catch (SQLException e) {
            e.printStackTrace();
            response.status(500); // Internal Server Error
            return "Erro ao autenticar";
        }
    }
}
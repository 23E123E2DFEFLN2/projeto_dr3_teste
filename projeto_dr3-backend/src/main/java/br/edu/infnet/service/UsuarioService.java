package br.edu.infnet.service;

import br.edu.infnet.config.DatabaseConfig;
import br.edu.infnet.dto.UsuarioDTOInput;
import br.edu.infnet.dto.UsuarioDTOOutput;
import br.edu.infnet.model.Usuario;
import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioService {

    private ModelMapper modelMapper = new ModelMapper();

    public List<UsuarioDTOOutput> listar() {
        List<UsuarioDTOOutput> usuariosDTO = new ArrayList<>();
        try (Connection connection = DatabaseConfig.conectar();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM usuarios")) {

            while (resultSet.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(resultSet.getInt("id"));
                usuario.setNome(resultSet.getString("nome"));

                usuariosDTO.add(modelMapper.map(usuario, UsuarioDTOOutput.class));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuariosDTO;
    }


    public void inserir(UsuarioDTOInput usuarioDTOInput) {
        if (usuarioDTOInput == null || usuarioDTOInput.getNome() == null || usuarioDTOInput.getSenha() == null) {
            throw new IllegalArgumentException("Usuário ou senha não podem ser nulos");
        }

        try (Connection connection = DatabaseConfig.conectar();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO usuarios (nome, senha) VALUES (?, ?)")) {

            preparedStatement.setString(1, usuarioDTOInput.getNome());
            String senhaHash = BCrypt.hashpw(usuarioDTOInput.getSenha(), BCrypt.gensalt());
            preparedStatement.setString(2, senhaHash);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public void alterar(UsuarioDTOInput usuarioDTOInput) {
        if (usuarioDTOInput == null || usuarioDTOInput.getId() == 0) {
            throw new IllegalArgumentException("ID do usuário inválido");
        }

        try (Connection connection = DatabaseConfig.conectar();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE usuarios SET nome = ?, senha = ? WHERE id = ?")) {

            preparedStatement.setString(1, usuarioDTOInput.getNome());
            String senhaHash = BCrypt.hashpw(usuarioDTOInput.getSenha(), BCrypt.gensalt());
            preparedStatement.setString(2, senhaHash);
            preparedStatement.setInt(3, usuarioDTOInput.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public UsuarioDTOOutput buscar(int id) {
        List<UsuarioDTOOutput> usuarios = listar();
        for (UsuarioDTOOutput usuario : usuarios) {
            if (usuario.getId() == id) {
                return modelMapper.map(usuario, UsuarioDTOOutput.class);
            }
        }
        return null;
    }

    public void atualizar(UsuarioDTOInput usuarioDTOInput) {
        if (usuarioDTOInput == null || usuarioDTOInput.getId() == 0) {
            throw new IllegalArgumentException("ID do usuário inválido");
        }

        try (Connection connection = DatabaseConfig.conectar();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE usuarios SET nome = ?, senha = ? WHERE id = ?")) {

            preparedStatement.setString(1, usuarioDTOInput.getNome());
            String senhaHash = BCrypt.hashpw(usuarioDTOInput.getSenha(), BCrypt.gensalt());
            preparedStatement.setString(2, senhaHash);
            preparedStatement.setInt(3, usuarioDTOInput.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public void excluir(int id) {
        try (Connection connection = DatabaseConfig.conectar();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "DELETE FROM usuarios WHERE id = ?")) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public boolean usuariosEstaoVazios() {
        return listar().isEmpty();
    }

    public void limparBaseDeDados() {
        try (Connection connection = DatabaseConfig.conectar();
             Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM usuarios");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
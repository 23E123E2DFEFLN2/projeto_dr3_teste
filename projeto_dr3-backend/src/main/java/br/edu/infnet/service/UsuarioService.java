package br.edu.infnet.service;

import br.edu.infnet.config.DatabaseConfig;
import br.edu.infnet.dto.UsuarioDTOInput;
import br.edu.infnet.dto.UsuarioDTOOutput;
import br.edu.infnet.model.Usuario;
import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.ModelMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioService {

    private ModelMapper modelMapper = new ModelMapper();

    public List<UsuarioDTOOutput> listarUsuarios() {
        List<UsuarioDTOOutput> usuariosDTO = new ArrayList<>();
        try (Connection connection = DatabaseConfig.conectar();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM USUARIOS")) {

            while (resultSet.next()) {
                UsuarioDTOOutput usuarioDTO = new UsuarioDTOOutput();
                usuarioDTO.setId(resultSet.getInt("id"));
                usuarioDTO.setNome(resultSet.getString("nome"));

                usuariosDTO.add(usuarioDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuariosDTO;
    }

    public void inserir(UsuarioDTOInput usuarioDTOInput) {
        try (Connection connection = DatabaseConfig.conectar();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO usuarios (nome, senha) VALUES (?, ?)")) {

            preparedStatement.setString(1, usuarioDTOInput.getNome());
            // Hash da senha antes de armazenar
            String senhaHash = BCrypt.hashpw(usuarioDTOInput.getSenha(), BCrypt.gensalt());
            preparedStatement.setString(2, senhaHash);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void alterar(UsuarioDTOInput usuarioDTOInput) {
        Usuario usuario = modelMapper.map(usuarioDTOInput, Usuario.class);
        List<UsuarioDTOOutput> usuarios = listarUsuarios();
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getId() == usuario.getId()) {
                atualizar(usuarioDTOInput);
                break;
            }
        }
    }

    public UsuarioDTOOutput buscar(int id) {
        List<UsuarioDTOOutput> usuarios = listarUsuarios();
        for (UsuarioDTOOutput usuario : usuarios) {
            if (usuario.getId() == id) {
                return modelMapper.map(usuario, UsuarioDTOOutput.class);
            }
        }
        return null;
    }

    public void atualizar(UsuarioDTOInput usuarioDTOInput) {
        try (Connection connection = DatabaseConfig.conectar();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE usuarios SET nome = ?, senha = ? WHERE id = ?")) {

            preparedStatement.setString(1, usuarioDTOInput.getNome());
            // Hash da nova senha antes de atualizar
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
        List<UsuarioDTOOutput> usuarios = listarUsuarios();
        return usuarios.isEmpty();
    }
}

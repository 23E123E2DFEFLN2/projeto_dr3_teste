package br.edu.infnet.service;

import br.edu.infnet.config.DatabaseConfig;
import br.edu.infnet.dto.UsuarioDTOInput;
import br.edu.infnet.dto.UsuarioDTOOutput;
import br.edu.infnet.model.Usuario;
import org.modelmapper.ModelMapper;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioService {

    private List<Usuario> listaUsuarios = new ArrayList<>();
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
        for (int i = 0; i < listaUsuarios.size(); i++) {
            if (listaUsuarios.get(i).getId() == usuario.getId()) {
                listaUsuarios.set(i, usuario);
                break;
            }
        }
    }

    public UsuarioDTOOutput buscar(int id) {
        for (Usuario usuario : listaUsuarios) {
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
}
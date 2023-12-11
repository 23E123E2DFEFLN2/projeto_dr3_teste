package br.edu.infnet;

import br.edu.infnet.model.Usuario;
import br.edu.infnet.service.UsuarioService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServiceTest {

    @Test
    public void testInserirUsuario() {
        UsuarioService usuarioService = new UsuarioService();
        Usuario usuarioDTOInput = new Usuario();
        usuarioDTOInput.setId(1);
        usuarioDTOInput.setNome("John Doe");
        usuarioDTOInput.setSenha("password");

        usuarioService.inserir(usuarioDTOInput);

        // Verifica se o tamanho da lista é igual a 1 após a inserção
        assertEquals(1, usuarioService.listar().size());
    }
}

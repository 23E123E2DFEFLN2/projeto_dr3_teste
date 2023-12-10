package br.edu.infnet;

import br.edu.infnet.dto.UsuarioDTOInput;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServiceTest {

    @Test
    public void testInserirUsuario() {
        br.edu.infnet.service.UsuarioService usuarioService = new br.edu.infnet.service.UsuarioService();
        UsuarioDTOInput usuarioDTOInput = new UsuarioDTOInput();
        usuarioDTOInput.setId(1);
        usuarioDTOInput.setNome("John Doe");
        usuarioDTOInput.setSenha("password");

        usuarioService.inserir(usuarioDTOInput);

        // Verifica se o tamanho da lista é igual a 1 após a inserção
        assertEquals(1, usuarioService.listarUsuarios().size());
    }
}

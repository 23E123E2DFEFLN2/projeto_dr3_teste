package br.edu.infnet;

import br.edu.infnet.dto.UsuarioDTOInput;
import br.edu.infnet.dto.UsuarioDTOOutput;
import br.edu.infnet.service.UsuarioService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServiceTest {

    @Test
    public void testInsercaoUsuario() {
        // Criar uma instância da classe UsuarioService
        UsuarioService usuarioService = new UsuarioService();

        // Criar um objeto UsuarioDTOInput para inserção
        UsuarioDTOInput usuarioDTOInput = new UsuarioDTOInput();
        usuarioDTOInput.setNome("NovoUsuario");
        usuarioDTOInput.setSenha("SenhaNova");

        // Executar o método inserir da classe UsuarioService
        usuarioService.inserir(usuarioDTOInput);

        // Executar o método listar da classe UsuarioService
        List<UsuarioDTOOutput> listaUsuarios = usuarioService.listarUsuarios();

        // Validar se o tamanho da lista é igual a 1
        assertEquals(1, listaUsuarios.size());
    }
}

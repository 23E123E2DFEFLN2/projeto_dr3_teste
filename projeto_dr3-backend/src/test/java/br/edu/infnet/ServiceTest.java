package br.edu.infnet;

import br.edu.infnet.dto.UsuarioDTOInput;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import br.edu.infnet.service.UsuarioService;


public class ServiceTest {

    @Test
    public void testInsercaoUsuario() {
        // Criar uma instância de UsuarioService
        UsuarioService usuarioService = new UsuarioService();

        // Criar um objeto UsuarioDTOInput para inserção
        UsuarioDTOInput usuarioDTOInput = new UsuarioDTOInput();
        usuarioDTOInput.setNome("NovoUsuario");
        usuarioDTOInput.setSenha("senha123");

        // Executar o método inserir
        usuarioService.inserir(usuarioDTOInput);

        // Validar a inserção verificando o tamanho da lista retornada pelo método listar
        int tamanhoLista = usuarioService.listar().size();
        Assertions.assertEquals(1, tamanhoLista, "A inserção do usuário não foi bem-sucedida");
    }
}

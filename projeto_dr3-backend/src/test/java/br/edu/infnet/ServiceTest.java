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
        usuarioDTOInput.setNome("ServiceTestNovoUsuario");
        usuarioDTOInput.setSenha("senha123 (hash!) kkk!!");

        // Obter a quantidade de usuários antes da inserção
        int quantidadeAntes = usuarioService.listar().size();

        // Executar o método inserir
        usuarioService.inserir(usuarioDTOInput);

        // Obter a quantidade de usuários após a inserção
        int quantidadeDepois = usuarioService.listar().size();

        // Validar a inserção verificando a diferença na quantidade
        int diferenca = quantidadeDepois - quantidadeAntes;
        Assertions.assertEquals(1, diferenca, "A inserção do usuário não foi bem-sucedida");

        // Imprimir resultados
        System.out.println("Quantidade de usuários antes: " + quantidadeAntes);
        System.out.println("Quantidade de usuários depois: " + quantidadeDepois);
        System.out.println("Diferença após a inserção: " + diferenca);
    }
}

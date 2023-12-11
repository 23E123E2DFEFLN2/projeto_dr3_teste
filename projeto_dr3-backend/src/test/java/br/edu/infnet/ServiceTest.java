package br.edu.infnet;

import br.edu.infnet.dto.UsuarioDTOInput;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import br.edu.infnet.service.UsuarioService;

public class ServiceTest {

    @Test
    public void testInsercaoUsuario() {
        UsuarioService usuarioService = new UsuarioService();


        usuarioService.limparBaseDeDados();

        UsuarioDTOInput usuarioDTOInput = new UsuarioDTOInput();
        usuarioDTOInput.setNome("ServiceTestNovoUsuario");
        usuarioDTOInput.setSenha("senha123 (hash!) kkk!!");

        usuarioService.inserir(usuarioDTOInput);

        int tamanhoDepois = usuarioService.listar().size();

        Assertions.assertEquals(1, tamanhoDepois);

        System.out.println(+ tamanhoDepois);
    }
}

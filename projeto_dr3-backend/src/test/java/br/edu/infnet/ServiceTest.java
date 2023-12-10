package br.edu.infnet;

import br.edu.infnet.dto.UsuarioDTOInput;
import br.edu.infnet.model.Usuario;
import br.edu.infnet.service.UsuarioService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class ServiceTest {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ModelMapper modelMapper;

    @Test
    public void testInserirUsuario() {

        UsuarioDTOInput usuarioDTOInput = new UsuarioDTOInput();
        usuarioDTOInput.setNome("NovoUsuario");
        usuarioDTOInput.setSenha("NovaSenha");

        Usuario usuario = modelMapper.map(usuarioDTOInput, Usuario.class);


        usuarioService.inserir(usuario);

       int tamanhoListaAposInsercao = usuarioService.listar().size();


        Assert.assertEquals(1, tamanhoListaAposInsercao);
    }
}

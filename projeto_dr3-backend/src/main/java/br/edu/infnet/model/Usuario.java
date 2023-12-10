package br.edu.infnet.model;

import br.edu.infnet.dto.UsuarioDTOOutput;
import lombok.Data;

@Data
public class Usuario extends UsuarioDTOOutput {
    private int id;
    private String nome;
    private String senha;
}

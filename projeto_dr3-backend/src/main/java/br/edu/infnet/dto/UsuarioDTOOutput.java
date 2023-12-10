package br.edu.infnet.dto;

import lombok.Data;

@Data
public class UsuarioDTOOutput extends UsuarioDTOInput {
    private int id;
    private String nome;
}

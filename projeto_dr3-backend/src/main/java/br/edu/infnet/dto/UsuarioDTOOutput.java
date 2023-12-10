package br.edu.infnet.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UsuarioDTOOutput {
    private int id;
    private String nome;
}

package br.edu.infnet.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Usuario {
    private int id;
    private String nome;
    private String senha;
}

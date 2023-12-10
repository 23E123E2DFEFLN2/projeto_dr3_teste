package br.edu.infnet;

import br.edu.infnet.controller.UsuarioController;

import static spark.Spark.awaitInitialization;

public class MainApp {
    public static void main(String[] args) {
        UsuarioController usuarioController = new UsuarioController();
        usuarioController.respostasRequisicoes();
        awaitInitialization();
    }
}


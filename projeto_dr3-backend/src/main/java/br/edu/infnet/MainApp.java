package br.edu.infnet;

import br.edu.infnet.config.DatabaseConfig;
import br.edu.infnet.controller.UsuarioController;

import static spark.Spark.awaitInitialization;

public class MainApp {
    public static void main(String[] args) {
        // Inicializa o banco de dados
        DatabaseConfig.inicializarBancoDeDados();

        UsuarioController usuarioController = new UsuarioController();
        usuarioController.respostasRequisicoes();
        awaitInitialization();
    }
}


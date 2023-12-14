package br.edu.infnet;

import br.edu.infnet.config.DatabaseConfig;
import br.edu.infnet.controller.UsuarioController;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static spark.Spark.awaitInitialization;
@SpringBootApplication
public class MainApp {
    public static void main(String[] args) {

        DatabaseConfig.inicializarBancoDeDados();

        UsuarioController usuarioController = new UsuarioController();
        usuarioController.respostasRequisicoes();
        awaitInitialization();

    }
}


package com.fatec.pizzaria_mario;

import com.fatec.pizzaria_mario.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories("com.fatec.pizzaria_mario.repository") // Adicione esta linha
public class PizzariaMarioApplication implements CommandLineRunner {

    @Autowired
    private UsuarioService usuarioService; // Agora injetamos o serviço

    public static void main(String[] args) {
        SpringApplication.run(PizzariaMarioApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("--- INICIANDO CRIAÇÃO DE USUÁRIOS PADRÃO ---");
        // A lógica agora está no serviço, muito mais limpo!
        usuarioService.criarUsuarioAdminPadrao();
        usuarioService.criarUsuarioAtendentePadrao();
        System.out.println("--- FINALIZADA CRIAÇÃO DE USUÁRIOS PADRÃO ---");
    }
}
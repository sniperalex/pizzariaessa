package com.fatec.pizzaria_mario;

import com.fatec.pizzaria_mario.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan; // Adicione este import
import org.springframework.context.annotation.ComponentScan; // Adicione este import
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
// Diz ao Spring para procurar por @Document/.@Entity em '...domain'
@EntityScan(basePackages = "com.fatec.pizzaria_mario.domain")
// Diz ao Spring para procurar por @Repository em '...repository'
@EnableMongoRepositories(basePackages = "com.fatec.pizzaria_mario.repository")
// Diz ao Spring para procurar por @Controller, @Service, etc., em todo o projeto base
@ComponentScan(basePackages = "com.fatec.pizzaria_mario")
public class PizzariaMarioApplication implements CommandLineRunner {

    @Autowired
    private UsuarioService usuarioService;

    public static void main(String[] args) {
        SpringApplication.run(PizzariaMarioApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        usuarioService.criarUsuarioAdminPadrao();
        usuarioService.criarUsuarioAtendentePadrao();
    }
}
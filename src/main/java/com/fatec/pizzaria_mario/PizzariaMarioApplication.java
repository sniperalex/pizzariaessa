package com.fatec.pizzaria_mario;

import com.fatec.pizzaria_mario.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan; // ADICIONE ESTE IMPORT
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories("com.fatec.pizzaria_mario.repository")
// ADICIONE ESTA ANOTAÇÃO. Ela diz ao Spring para procurar componentes em todo o pacote base.
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
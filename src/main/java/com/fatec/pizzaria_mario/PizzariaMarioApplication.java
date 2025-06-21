package com.fatec.pizzaria_mario;

import com.fatec.pizzaria_mario.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired; // Import que faltava
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PizzariaMarioApplication {

    public static void main(String[] args) {
        SpringApplication.run(PizzariaMarioApplication.class, args);
    }

    /**
     * Este @Bean com CommandLineRunner garante que o código dentro dele
     * seja executado uma única vez, assim que a aplicação iniciar.
     * Nós o usamos para verificar e criar/atualizar nossos usuários padrão.
     *
     * O Spring automaticamente injeta o UsuarioService aqui para nós.
     */
    @Bean
    public CommandLineRunner run(UsuarioService usuarioService) {
        return args -> {
            try {
                usuarioService.criarUsuarioPadrao("admin", "admin123", "ADMIN");
                usuarioService.criarUsuarioPadrao("atendente", "atendente123", "ATENDENTE");
            } catch (Exception e) {
                System.err.println("Erro ao criar usuários padrão: " + e.getMessage());
            }
        };
    }
}
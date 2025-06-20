package com.fatec.Pizzaria_Mario;

// Imports necessários
import com.fatec.Pizzaria_Mario.domain.Usuario;
import com.fatec.Pizzaria_Mario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

@SpringBootApplication
public class PizzariaMarioApplication implements CommandLineRunner { // 1. Implemente a interface

    // 2. Injete as dependências que você vai usar
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(PizzariaMarioApplication.class, args);
    }

    // 3. Este método será executado automaticamente na inicialização
    @Override
    public void run(String... args) throws Exception {

        System.out.println("--- INICIANDO CRIAÇÃO DE USUÁRIOS PADRÃO ---");

        // --- Criação do Usuário ADMIN ---
        // Verifica se o usuário admin já existe antes de criar
        Optional<Usuario> adminOptional = usuarioRepository.findByUsername("admin");
        if (adminOptional.isEmpty()) {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            // SEMPRE SALVE A SENHA CRIPTOGRAFADA!
            admin.setPassword(passwordEncoder.encode("admin123")); 
            admin.setRoles(new HashSet<>(Arrays.asList("ROLE_ADMIN", "ROLE_ATENDENTE"))); // Admin tem ambos os papéis

            usuarioRepository.save(admin);
            System.out.println("Usuário ADMIN criado com sucesso!");
        } else {
            System.out.println("Usuário ADMIN já existe.");
        }


        // --- Criação do Usuário ATENDENTE ---
        // Verifica se o usuário atendente já existe antes de criar
        Optional<Usuario> atendenteOptional = usuarioRepository.findByUsername("atendente");
        if (atendenteOptional.isEmpty()) {
            Usuario atendente = new Usuario();
            atendente.setUsername("atendente");
            atendente.setPassword(passwordEncoder.encode("atendente123"));
            atendente.setRoles(new HashSet<>(Arrays.asList("ROLE_ATENDENTE"))); // Atendente tem apenas seu papel

            usuarioRepository.save(atendente);
            System.out.println("Usuário ATENDENTE criado com sucesso!");
        } else {
            System.out.println("Usuário ATENDENTE já existe.");
        }

        System.out.println("--- FINALIZADA CRIAÇÃO DE USUÁRIOS PADRÃO ---");
    }
}
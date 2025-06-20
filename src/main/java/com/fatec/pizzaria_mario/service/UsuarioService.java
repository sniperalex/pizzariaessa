package com.fatec.pizzaria_mario.service;

import com.fatec.pizzaria_mario.domain.Usuario;
import com.fatec.pizzaria_mario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.HashSet;
import java.util.Arrays;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void criarUsuarioAdminPadrao() {
        Optional<Usuario> adminOptional = usuarioRepository.findByUsername("admin");
        if (adminOptional.isEmpty()) {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRoles(new HashSet<>(Arrays.asList("ROLE_ADMIN", "ROLE_ATENDENTE")));
            usuarioRepository.save(admin);
            System.out.println("Usuário ADMIN criado com sucesso!");
        } else {
            System.out.println("Usuário ADMIN já existe.");
        }
    }

    public void criarUsuarioAtendentePadrao() {
        Optional<Usuario> atendenteOptional = usuarioRepository.findByUsername("atendente");
        if (atendenteOptional.isEmpty()) {
            Usuario atendente = new Usuario();
            atendente.setUsername("atendente");
            atendente.setPassword(passwordEncoder.encode("atendente123"));
            atendente.setRoles(new HashSet<>(Arrays.asList("ROLE_ATENDENTE")));
            usuarioRepository.save(atendente);
            System.out.println("Usuário ATENDENTE criado com sucesso!");
        } else {
            System.out.println("Usuário ATENDENTE já existe.");
        }
    }
}
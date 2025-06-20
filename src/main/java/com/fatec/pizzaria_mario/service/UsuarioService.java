package com.fatec.pizzaria_mario.service;

import com.fatec.pizzaria_mario.domain.Usuario;
import com.fatec.pizzaria_mario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = usuarioRepository.findByLogin(username);
        if (user == null) {
            throw new UsernameNotFoundException("Usuário não encontrado!");
        }
        return user;
    }

    public void criarUsuarioPadrao(String login, String senha, String role) {
        if (usuarioRepository.findByLogin(login) == null) {
            Usuario novoUsuario = new Usuario();
            
            // CORREÇÃO AQUI
            novoUsuario.setLogin(login);
            novoUsuario.setSenha(passwordEncoder.encode(senha));
            novoUsuario.addRole(role);
            
            usuarioRepository.save(novoUsuario);
            System.out.println(">>> Usuário " + role + " padrão criado: " + login);
        }
    }
}
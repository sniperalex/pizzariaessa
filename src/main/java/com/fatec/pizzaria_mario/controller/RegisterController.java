package com.fatec.pizzaria_mario.controller;

import com.fatec.pizzaria_mario.domain.Usuario;
import com.fatec.pizzaria_mario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(Usuario usuario) {
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        
        // MUDANÇA: Usando o novo método para garantir consistência
        usuario.addRole("CLIENTE"); 
        
        usuarioRepository.save(usuario);
        
        return "redirect:/login?success";
    }
}
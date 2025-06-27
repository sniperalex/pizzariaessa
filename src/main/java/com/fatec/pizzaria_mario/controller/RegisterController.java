package com.fatec.pizzaria_mario.controller;

import com.fatec.pizzaria_mario.domain.Usuario;
import com.fatec.pizzaria_mario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    // ATUALIZADO para receber o e-mail
    @PostMapping("/register")
    public String registerUser(Usuario usuario, @RequestParam("email") String email, RedirectAttributes redirectAttributes) {
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuario.setEmail(email);
        usuario.addRole("CLIENTE"); 
        usuarioRepository.save(usuario);
        redirectAttributes.addFlashAttribute("registerSuccessMessage", "Cadastro realizado com sucesso! Faça o login.");
        return "redirect:/login";
    }

    // Lógica do "Esqueci a Senha"
    @GetMapping("/esqueci-senha")
    public String showForgotPasswordForm() {
        return "esqueci-senha";
    }

    @PostMapping("/esqueci-senha")
    public String processForgotPassword(RedirectAttributes redirectAttributes) {
        // Lógica "fake": apenas mostra uma mensagem de sucesso
        redirectAttributes.addFlashAttribute("resetSuccessMessage", "Se o e-mail estiver cadastrado, um link de recuperação foi enviado.");
        return "redirect:/login";
    }
}
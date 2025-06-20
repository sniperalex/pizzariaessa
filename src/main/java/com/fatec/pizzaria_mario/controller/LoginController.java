package com.fatec.pizzaria_mario.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    // Redireciona da raiz para o login
    @GetMapping("/")
    public String redirectToLogin() {
        return "redirect:/login";
    }

    // Mostra a página de login
    @GetMapping("/login")
    public String loginPage() {
        return "login"; 
    }

    // MOSTRA A PÁGINA HOME APÓS O LOGIN BEM-SUCEDIDO
    @GetMapping("/home")
    public String homePage() {
        return "home"; // Este método estava faltando
    }
}
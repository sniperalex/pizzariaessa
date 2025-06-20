package com.fatec.pizzaria_mario.controller;

import org.springframework.stereotype.Controller; // <-- Garanta que é este o import
import org.springframework.web.bind.annotation.GetMapping;

// AQUI ESTÁ A ANOTAÇÃO MAIS IMPORTANTE DE TODAS:
// Deve ser @Controller, e NÃO @RestController.
@Controller
public class LoginController {

    @GetMapping("/")
    public String redirectToLogin() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login"; 
    }
}
package com.fatec.pizzaria_mario.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        // Retorna o nome do arquivo "login.html"
        return "login";
    }

    // O método home() vai aqui DENTRO da classe
    @GetMapping("/home")
    public String home() {
        // Retorna o nome do arquivo "home.html"
        return "home";
    }

} // Fim da classe LoginController
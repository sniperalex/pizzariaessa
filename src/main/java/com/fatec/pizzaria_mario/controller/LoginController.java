package com.fatec.pizzaria_mario.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    // ÚNICA RESPONSABILIDADE: MOSTRAR A PÁGINA DE LOGIN
    @GetMapping("/login")
    public String loginPage() {
        return "login"; 
    }
}
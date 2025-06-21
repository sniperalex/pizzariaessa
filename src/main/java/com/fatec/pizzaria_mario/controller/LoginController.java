package com.fatec.pizzaria_mario.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    // Este método foi REMOVIDO para resolver o conflito
    // @GetMapping("/")
    // public String redirectToLogin() {
    //     return "redirect:/login";
    // }

    // Este método é o único que precisa estar aqui agora
    @GetMapping("/login")
    public String loginPage() {
        return "login"; 
    }

    // O método @GetMapping("/home") foi REMOVIDO daqui.
}
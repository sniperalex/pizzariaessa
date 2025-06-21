package com.fatec.pizzaria_mario.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/atendente")
public class AtendenteController {

    @GetMapping("/home")
    public String atendenteHomePage() {
        return "atendente-home";
    }
}
package com.fatec.pizzaria_mario.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @GetMapping("/home")
    public String home() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            boolean isAtendente = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ATENDENTE"));

            if (isAdmin) {
                return "redirect:/admin/dashboard"; // << Redireciona para o novo dashboard
            }
            if (isAtendente) {
                return "redirect:/atendente/home";
            }
        }
        return "redirect:/cardapio"; // Clientes vão direto para o cardápio
    }
}
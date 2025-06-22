package com.fatec.pizzaria_mario.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.fatec.pizzaria_mario.domain.Usuario;

@Controller
public class HomeController {
    
    @GetMapping({"/", "/home"})
    public String home() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return "redirect:/login";
        }
        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isAtendente = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ATENDENTE"));
        if (isAdmin) {
            return "redirect:/admin/dashboard";
        }
        if (isAtendente) {
            return "redirect:/atendente/home";
        }
        return "redirect:/cardapio/inicio";
    }

    @GetMapping("/pizzaria")
    public String pizzariaInfo() {
        return "pizzaria-info";
    }

    // Removido método inicioPedido(HttpSession, Usuario) para evitar conflito de mapping
}
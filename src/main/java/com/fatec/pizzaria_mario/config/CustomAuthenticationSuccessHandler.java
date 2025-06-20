package com.fatec.pizzaria_mario.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private RequestCache requestCache = new HttpSessionRequestCache();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        
        // Passo 1: Verifica se o Spring Security salvou uma "requisição original"
        // Isso acontece quando o usuário tentou acessar uma página protegida (como /checkout) e foi redirecionado para o login.
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest != null) {
            // Se existe uma requisição salva, redireciona o usuário de volta para ela.
            response.sendRedirect(savedRequest.getRedirectUrl());
            return;
        }

        // Passo 2: Se não havia requisição salva, decide para onde ir com base no papel (ROLE) do usuário.
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));

        if (isAdmin) {
            // Se for ADMIN, vai para a home de admin
            response.sendRedirect("/home"); 
        } else {
            // Se for qualquer outro usuário (CLIENTE, ATENDENTE), vai para o cardápio
            response.sendRedirect("/cardapio");
        }
    }
}
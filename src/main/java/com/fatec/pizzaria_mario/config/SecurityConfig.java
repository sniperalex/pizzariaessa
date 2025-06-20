package com.fatec.pizzaria_mario.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest; // <-- IMPORT IMPORTANTE
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // Desabilitar CSRF para o formulário
                
                .authorizeHttpRequests(authorize -> authorize
                        // REGRA 1: A forma mais recomendada para liberar recursos estáticos.
                        // Isso libera /css, /js, /images, etc. automaticamente.
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        
                        // REGRA 2: Liberar nossas páginas públicas (raiz e a página de login)
                        .requestMatchers("/", "/login").permitAll()
                        
                        // REGRA 3: Qualquer outra requisição (como /home) precisa de autenticação.
                        .anyRequest().authenticated()
                )
                
                .formLogin(form -> form
                        .loginPage("/login")                // Diz ao Spring onde está nossa página de login.
                        .defaultSuccessUrl("/home", true)  // Após o sucesso, envia para /home.
                        .permitAll()                        // Permite acesso a todos os processos de login.
                )
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
package com.fatec.pizzaria_mario.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
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
        http
            .authorizeHttpRequests(authorize -> authorize
                // ESTA É A FORMA MAIS SEGURA DE LIBERAR ARQUIVOS ESTÁTICOS
                // Ela tem prioridade máxima.
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                
                // Agora liberamos as rotas de login
                .requestMatchers("/", "/login").permitAll()
                
                // Qualquer outra requisição precisa de autenticação
                .anyRequest().authenticated()
            )
            // Desabilita CSRF, que não é necessário para a nossa API stateless
            .csrf(csrf -> csrf.disable());

        return http.build();
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
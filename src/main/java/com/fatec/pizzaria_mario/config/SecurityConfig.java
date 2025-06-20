package com.fatec.pizzaria_mario.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Simplifica, mas em produção deve ser estudado
            .authorizeHttpRequests(authorize -> authorize
                // Permite que qualquer um acesse a página de login e recursos estáticos
                .requestMatchers("/login", "/css/**", "/js/**", "/webjars/**").permitAll()
                // Acesso à API de produtos continua como antes
                .requestMatchers("/api/**").authenticated() 
                // Qualquer outra requisição precisa de autenticação
                .anyRequest().authenticated()
            )
            // Configuração do Formulário de Login
            .formLogin(form -> form
                .loginPage("/login") // Diz ao Spring qual é a nossa página de login customizada
                .loginProcessingUrl("/login") // A URL para onde o formulário envia os dados (Spring intercepta)
                .defaultSuccessUrl("/home", true) // Para onde o usuário é redirecionado após o sucesso
                .failureUrl("/login?error=true") // Para onde vai se o login falhar
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout=true") // Para onde vai após o logout
            )
            // Também habilitamos o HTTP Basic para continuar usando o Postman
            .httpBasic(); 
            
        return http.build();
    }
}
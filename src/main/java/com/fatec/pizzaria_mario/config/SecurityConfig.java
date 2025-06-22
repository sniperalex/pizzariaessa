package com.fatec.pizzaria_mario.config;

import com.fatec.pizzaria_mario.service.UsuarioService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationProvider authenticationProvider) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .requestMatchers("/", "/home", "/cardapio/inicio", "/cardapio/um-sabor", "/cardapio/meio-a-meio", "/cardapio/um-sabor/selecionar", "/cardapio/meio-a-meio/selecionar").permitAll()
                .requestMatchers(HttpMethod.POST, "/register", "/pedidos", "/esqueci-senha").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/produtos/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/produtos/*/imagem").permitAll()
                .requestMatchers("/admin/pedidos").hasAnyRole("ADMIN", "ATENDENTE")
                .requestMatchers(HttpMethod.POST, "/pedidos/alterar-status").hasAnyRole("ADMIN", "ATENDENTE")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/atendente/**").hasRole("ATENDENTE")
                .requestMatchers("/carrinho/adicionar", "/carrinho/atualizar", "/carrinho/remover", "/carrinho/resumo").permitAll()
                .requestMatchers("/acompanhamentos", "/bebidas").permitAll()
                .requestMatchers("/register", "/register/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .authenticationProvider(authenticationProvider)
            .build();
    }
    
    @Bean
    public AuthenticationProvider authenticationProvider(UsuarioService usuarioService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(usuarioService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
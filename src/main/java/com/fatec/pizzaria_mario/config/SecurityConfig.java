package com.fatec.pizzaria_mario.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest; // Import importante

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // 1. Desabilitamos o CSRF
            .authorizeHttpRequests(authorize -> authorize
                // 2. REGRA PARA RECURSOS ESTÁTICOS (A FORMA RECOMENDADA)
                // Libera /css/**, /js/**, /images/**, etc.
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                
                // 3. REGRA PARA PÁGINAS PÚBLICAS
                // Libera o acesso para qualquer um a estas páginas específicas.
                .requestMatchers("/", "/login", "/register", "/cardapio", "/acompanhamentos", "/bebidas").permitAll()
                .requestMatchers(HttpMethod.POST, "/register").permitAll() // Permite o cadastro
                .requestMatchers("/carrinho/**").permitAll() // Permite adicionar ao carrinho
                .requestMatchers(HttpMethod.GET, "/produtos/**").permitAll() // Permite ver imagens dos produtos

                // 4. REGRA PARA O ADMIN
                .requestMatchers("/admin/**").hasRole("ADMIN")

                // 5. REGRA FINAL: QUALQUER OUTRA COISA PRECISA DE LOGIN
                .anyRequest().authenticated()
            )
            // 6. CONFIGURAÇÃO DO FORMULÁRIO DE LOGIN
            .formLogin(form -> form
                .loginPage("/login") // Nossa página de login customizada
                .usernameParameter("username") // O campo de usuário no HTML é 'username'
                .passwordParameter("password") // O campo de senha no HTML é 'password'
                .defaultSuccessUrl("/home", true) // Após login, vá para /home (simples por enquanto)
                .failureUrl("/login?error=true") // Se falhar, volte para /login com um erro
                .permitAll() // Permite acesso total ao processo de login
            )
            // 7. CONFIGURAÇÃO DE LOGOUT
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout") // Após sair, vá para o login com msg de sucesso
                .permitAll()
            );

        return http.build();
    }

    // Este bean é necessário para a autenticação
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    // Este bean é crucial para comparar as senhas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
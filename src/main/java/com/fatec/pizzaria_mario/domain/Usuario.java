package com.fatec.pizzaria_mario.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Document(collection = "usuarios")
public class Usuario implements UserDetails {

    @Id
    private String id;

    // CORREÇÃO 1: Renomeado para "login" para ser consistente com o resto da aplicação
    @Indexed(unique = true)
    private String login;

    // CORREÇÃO 2: Renomeado para "senha"
    private String senha;

    // Manteve-se como Set<String> para flexibilidade
    private Set<String> roles = new HashSet<>();

    // --- MÉTODOS OBRIGATÓRIOS DA INTERFACE UserDetails ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // CORREÇÃO 3: Adiciona o prefixo "ROLE_" automaticamente, que é o padrão do Spring Security
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                .collect(Collectors.toList());
    }

    // Adaptado para o nome "senha"
    @Override
    public String getPassword() {
        return this.senha;
    }

    // Adaptado para o nome "login"
    @Override
    public String getUsername() {
        return this.login;
    }
    
    // Método extra para adicionar papéis de forma segura
    public void addRole(String role) {
        this.roles.add(role.toUpperCase());
    }


    // Os métodos abaixo continuam retornando 'true' por padrão
    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
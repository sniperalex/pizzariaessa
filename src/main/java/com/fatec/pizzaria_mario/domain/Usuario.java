package com.fatec.pizzaria_mario.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

// Anotação do Lombok para gerar Getters e Setters automaticamente
@Getter
@Setter
// Anotação do Spring Data MongoDB para mapear esta classe para uma coleção
@Document(collection = "usuarios")
public class Usuario implements UserDetails { // Implementa a interface do Spring Security

    @Id
    private String id;

    @Indexed(unique = true) // Garante que não haverá usernames duplicados
    private String username;

    private String password;

    // Usamos um Set para garantir que os papéis (roles) não se repitam
    private Set<String> roles = new HashSet<>();

    // --- MÉTODOS OBRIGATÓRIOS DA INTERFACE UserDetails ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Transforma nosso Set<String> de papéis em uma coleção que o Spring Security entende
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    // O Spring Security usará este método para pegar a senha
    @Override
    public String getPassword() {
        return this.password;
    }

    // O Spring Security usará este método para pegar o nome de usuário
    @Override
    public String getUsername() {
        return this.username;
    }

    // Os métodos abaixo podem retornar 'true' por padrão para simplificar
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
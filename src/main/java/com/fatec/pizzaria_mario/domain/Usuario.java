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
    @Indexed(unique = true)
    private String login;
    private String senha;
    private String email;
    private Set<String> roles = new HashSet<>();
    private String nomeCompleto;
    private String telefone;
    private String endereco;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() { return this.senha; }
    @Override
    public String getUsername() { return this.login; }
    
    public void addRole(String role) {
        if (this.roles == null) { this.roles = new HashSet<>(); }
        this.roles.add(role.toUpperCase());
    }

    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }
}
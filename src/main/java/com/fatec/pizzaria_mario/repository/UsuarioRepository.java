package com.fatec.Pizzaria_Mario.repository;

import com.fatec.Pizzaria_Mario.domain.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, String> {

    // O Spring Data criará automaticamente a implementação deste método
    // Ele será usado pelo nosso CustomUserDetailsService para buscar um usuário pelo nome
    Optional<Usuario> findByUsername(String username);

}
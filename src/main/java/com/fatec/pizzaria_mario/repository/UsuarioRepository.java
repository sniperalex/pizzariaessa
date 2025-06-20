package com.fatec.pizzaria_mario.repository; // <-- CORREÇÃO 1: pacote em minúsculo

import com.fatec.pizzaria_mario.domain.Usuario; // <-- CORREÇÃO 2: import em minúsculo
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
// A correção dos imports fará com que o "Usuario" aqui seja reconhecido
public interface UsuarioRepository extends MongoRepository<Usuario, String> {

    // E aqui também
    Optional<Usuario> findByUsername(String username);

}
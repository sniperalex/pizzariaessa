package com.fatec.pizzaria_mario.repository;

import com.fatec.pizzaria_mario.domain.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {

    UserDetails findByLogin(String login);

}
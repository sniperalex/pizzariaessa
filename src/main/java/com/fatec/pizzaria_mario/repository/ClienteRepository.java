package com.fatec.pizzaria_mario.repository;

import com.fatec.pizzaria_mario.domain.Cliente;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClienteRepository extends MongoRepository<Cliente, String> {
}
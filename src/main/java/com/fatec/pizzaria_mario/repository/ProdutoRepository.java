package com.fatec.pizzaria_mario.repository;

import com.fatec.pizzaria_mario.domain.Produto;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List; // Import necessário

public interface ProdutoRepository extends MongoRepository<Produto, String> {

    // MÉTODO NOVO
    List<Produto> findByCategoria(String categoria);

}
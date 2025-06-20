package com.fatec.pizzaria_mario.repository;

import com.fatec.pizzaria_mario.domain.Produto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends MongoRepository<Produto, String> {

    // (Opcional) Podemos adicionar métodos de busca customizados aqui depois
    // Ex: List<Produto> findByCategoria(String categoria);

}
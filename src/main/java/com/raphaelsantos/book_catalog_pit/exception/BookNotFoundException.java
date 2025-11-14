package com.raphaelsantos.book_catalog_pit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção lançada quando um livro não é encontrado pelo ID informado.
 * Mapeada para a resposta HTTP 404 (NOT_FOUND).
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class BookNotFoundException extends RuntimeException{
    public BookNotFoundException(Long id) {
        super("Livro não encontrado: " + id);
    }
}

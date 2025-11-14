package com.raphaelsantos.book_catalog_pit.repository;

import com.raphaelsantos.book_catalog_pit.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositório JPA responsável pelo acesso aos dados da entidade {@link Book}.
 * Herda operações CRUD básicas e define consultas específicas.
 */
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * Encontra livros cujo título ou autor contenham os valores informados,
     * ignorando diferenças entre maiúsculas e minúsculas.
     *
     * @param title  parte do título a ser pesquisada
     * @param author parte do nome do autor a ser pesquisada
     * @return lista de livros que atendem ao filtro
     */
    List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author);
}

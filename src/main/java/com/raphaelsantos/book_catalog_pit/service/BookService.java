package com.raphaelsantos.book_catalog_pit.service;

import com.raphaelsantos.book_catalog_pit.exception.BookNotFoundException;
import com.raphaelsantos.book_catalog_pit.model.Book;
import com.raphaelsantos.book_catalog_pit.repository.BookRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Serviço responsável pelas regras de negócio relacionadas à entidade {@link Book}.
 * Centraliza operações de consulta, cadastro, atualização e exclusão de livros.
 */
@Service
public class BookService {

    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    /**
     * Lista todos os livros cadastrados.
     *
     * @return lista contendo todos os livros
     */
    public List<Book> listAll() {
        return repository.findAll();
    }

    /**
     * Realiza uma busca por termo, procurando o valor informado
     * no título ou no autor, ignorando maiúsculas e minúsculas.
     * Caso o termo seja nulo ou em branco, retorna todos os livros.
     *
     * @param term termo de busca (pode ser nulo ou vazio)
     * @return lista de livros filtrados ou lista completa, se o termo for vazio
     */
    public List<Book> findByTerm(String term) {
        if (term == null || term.isBlank()) {
            return listAll();
        }

        return repository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(term, term);
    }

    /**
     * Busca um livro pelo seu identificador.
     *
     * @param id identificador do livro
     * @return livro encontrado
     * @throws BookNotFoundException caso não exista livro com o ID informado
     */
    public Book findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    /**
     * Salva um novo livro no banco de dados.
     *
     * @param book livro a ser salvo
     * @return livro já persistido, com ID gerado
     */
    @Transactional
    public Book save(Book book) {
        return repository.save(book);
    }

    /**
     * Atualiza os dados de um livro existente a partir do ID informado.
     * Apenas campos relevantes são copiados do objeto de dados para a
     * instância já persistida.
     *
     * @param id   identificador do livro a ser atualizado
     * @param data dados atualizados do livro
     * @return livro atualizado
     * @throws BookNotFoundException caso não exista livro com o ID informado
     */
    @Transactional
    public Book update(Long id, Book data) {
        Book existentBook = findById(id);

        // Atualiza os campos do livro existente com os dados recebidos
        existentBook.setTitle(data.getTitle());
        existentBook.setAuthor(data.getAuthor());
        existentBook.setGenre(data.getGenre());
        existentBook.setSynopsis(data.getSynopsis());
        existentBook.setPublicationYear(data.getPublicationYear());

        return repository.save(existentBook);
    }

    /**
     * Exclui um livro pelo seu identificador.
     *
     * @param id identificador do livro a ser excluído
     * @throws BookNotFoundException caso não exista livro com o ID informado
     */
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new BookNotFoundException(id);
        }

        repository.deleteById(id);
    }
}

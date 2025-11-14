package com.raphaelsantos.book_catalog_pit.controller;

import com.raphaelsantos.book_catalog_pit.model.Book;
import com.raphaelsantos.book_catalog_pit.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository repository;

    @BeforeEach
    void setup() {
        repository.deleteAll();

        Book book = new Book();
        book.setTitle("O Hobbit");
        book.setAuthor("J.R.R Tolkien");
        book.setPublicationYear(1937);
        book.setGenre("Fantasia");
        book.setSynopsis("lorem ispum...");
        repository.save(book);
    }

    @Test
    void list_shouldReturnPageWithBookList() throws Exception {
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(view().name("books/list"))
                .andExpect(model().attributeExists("books"));
    }

    @Test
    void detail_shouldDisplayBookDetails() throws Exception {
        Long id = repository.findAll().getFirst().getId();

        mockMvc.perform(get("/books/{id}", id))
                .andExpect(status().isOk())
                .andExpect(view().name("books/detail"))
                .andExpect(model().attributeExists("book"));
    }

    @Test
    void create_whenValidData_shouldRedirectToList() throws Exception {
        mockMvc.perform(post("/books")
                        .param("title", "Novo Livro")
                        .param("author", "Novo Autor")
                        .param("publicationYear", "2024")
                        .param("genre", "Fantasia")
                        .param("synopsis", ""))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));
    }

    @Test
    void create_whenInvalidData_shouldReturnToForm() throws Exception {
        mockMvc.perform(post("/books")
                        .param("title", "")
                        .param("author", "Autor")
                        .param("publicationYear", "2024"))
                .andExpect(status().isOk())
                .andExpect(view().name("books/form"))
                .andExpect(model().attributeHasFieldErrors("book", "title"));
    }

}

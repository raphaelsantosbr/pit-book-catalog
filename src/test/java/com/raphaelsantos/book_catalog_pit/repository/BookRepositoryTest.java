package com.raphaelsantos.book_catalog_pit.repository;

import com.raphaelsantos.book_catalog_pit.model.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    private BookRepository repository;

    @Test
    void findByTitleOrAuthor_shouldFilterIgnoringCase() {
        Book b1 = new Book();
        b1.setTitle("Spring Boot in Action");
        b1.setAuthor("Raphael");
        b1.setPublicationYear(2024);
        repository.save(b1);

        List<Book> result = repository
                .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase("spring", "spring");

        assertEquals(1, result.size());
        assertEquals("Spring Boot in Action", result.getFirst().getTitle());
    }
}

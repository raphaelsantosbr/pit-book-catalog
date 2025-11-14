package com.raphaelsantos.book_catalog_pit.service;

import com.raphaelsantos.book_catalog_pit.exception.BookNotFoundException;
import com.raphaelsantos.book_catalog_pit.model.Book;
import com.raphaelsantos.book_catalog_pit.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository repository;

    @InjectMocks
    private BookService service;

    @Test
    void listAll_shouldReturnAllBooks() {
        Book book = new Book();
        book.setTitle("O Hobbit");

        when(repository.findAll()).thenReturn(List.of(book));

        List<Book> result = service.listAll();

        assertEquals(1, result.size());
        assertEquals("O Hobbit", result.getFirst().getTitle());
        verify(repository).findAll();
    }

    @Test
    void findByTerm_whenTermIsEmpty_shouldReturnAll() {
        Book book = new Book();
        when(repository.findAll()).thenReturn(List.of(book));

        List<Book> result = service.findByTerm(null);

        assertEquals(1, result.size());
        verify(repository).findAll();
        verify(repository, never())
                .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(any(), any());
    }

    @Test
    void findByTerm_whenTermFilled_shouldSearchByTitleOrAuthor() {
        Book book = new Book();
        when(repository
                .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase("java", "java"))
                .thenReturn(List.of(book));

        List<Book> result = service.findByTerm("java");

        assertEquals(1, result.size());
        verify(repository)
                .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase("java", "java");
    }

    @Test
    void findById_whenExist_shouldReturnBook() {
        Book book = new Book();
        book.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(book));

        Book result = service.findById(1L);

        assertEquals(1L, result.getId());
        verify(repository).findById(1L);
    }

    @Test
    void findById_whenNotExist_shouldThrowException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> service.findById(99L));
        verify(repository).findById(99L);
    }

    @Test
    void save_shouldSaveBook() {
        Book book = new Book();
        book.setTitle("Novo Livro");

        when(repository.save(book)).thenReturn(book);

        Book result = service.save(book);

        assertEquals("Novo Livro", result.getTitle());
        verify(repository).save(book);
    }

    @Test
    void update_shouldUpdateBookFields() {
        Book original = new Book();
        original.setId(1L);
        original.setTitle("Livro Antigo");
        original.setAuthor("Autor Antigo");

        Book data = new Book();
        data.setTitle("Livro Novo");
        data.setAuthor("Autor Novo");
        data.setGenre("Aventura");
        data.setSynopsis("lorem ipsum...");
        data.setPublicationYear(2024);

        when(repository.findById(1L)).thenReturn(Optional.of(original));
        when(repository.save(original)).thenReturn(original);

        Book result = service.update(1L, data);

        assertEquals("Livro Novo", result.getTitle());
        assertEquals("Autor Novo", result.getAuthor());
        assertEquals("Aventura", result.getGenre());
        assertEquals("lorem ipsum...", result.getSynopsis());
        assertEquals(2024, result.getPublicationYear());
        verify(repository).findById(1L);
        verify(repository).save(original);
    }

    @Test
    void delete_whenExist_shouldDelete() {
        when(repository.existsById(1L)).thenReturn(true);

        service.delete(1L);

        verify(repository).existsById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void delete_whenNotExist_shouldThrowException() {
        when(repository.existsById(1L)).thenReturn(false);

        assertThrows(BookNotFoundException.class, () -> service.delete(1L));
        verify(repository).existsById(1L);
        verify(repository, never()).deleteById(anyLong());
    }
}

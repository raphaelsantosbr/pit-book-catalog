package com.raphaelsantos.book_catalog_pit.controller;

import com.raphaelsantos.book_catalog_pit.model.Book;
import com.raphaelsantos.book_catalog_pit.service.BookService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controlador responsável por atender às requisições relacionadas ao catálogo de livros.
 */
@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    /**
     * Exibe a página de listagem de livros.
     * Permite também a busca opcional por título ou autor através do parâmetro "q".
     *
     * @param q     termo de busca (opcional)
     * @param model modelo utilizado para enviar dados à view
     * @return nome da view de listagem de livros
     */
    @GetMapping
    public String list(@RequestParam(value = "q", required = false) String q, Model model) {
        List<Book> books = service.findByTerm(q);
        model.addAttribute("books", books);
        model.addAttribute("q", q == null ? "" : q);
        return "books/list";
    }

    /**
     * Exibe os detalhes de um livro específico.
     *
     * @param id    identificador do livro
     * @param model modelo utilizado para enviar dados à view
     * @return nome da view de detalhes do livro
     */
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("book", service.findById(id));
        return "books/detail";
    }

    /**
     * Exibe o formulário para cadastro de um novo livro.
     *
     * @param model modelo utilizado para enviar dados à view
     * @return nome da view de formulário
     */
    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("action", "create");
        return "books/form";
    }

    /**
     * Processa o cadastro de um novo livro.
     * Em caso de erro de validação, retorna ao formulário exibindo as mensagens.
     *
     * @param book   objeto preenchido a partir do formulário
     * @param result resultado da validação
     * @param ra     atributos para mensagens após redirecionamento
     * @return redirecionamento para a listagem ou retorno ao formulário em caso de erro
     */
    @PostMapping
    public String create(@Valid @ModelAttribute("book") Book book,
                         BindingResult result,
                         RedirectAttributes ra,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("action", "create");
            return "books/form";
        }
        service.save(book);
        ra.addFlashAttribute("msg", "Livro cadastrado com sucesso!");
        return "redirect:/books";
    }

    /**
     * Exibe o formulário para edição de um livro existente.
     *
     * @param id    identificador do livro a ser editado
     * @param model modelo utilizado para enviar dados à view
     * @return nome da view de formulário
     */
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("book", service.findById(id));
        model.addAttribute("action", "update");
        return "books/form";
    }

    /**
     * Processa a atualização de um livro existente.
     * Em caso de erro de validação, retorna ao formulário exibindo as mensagens.
     *
     * @param id     identificador do livro
     * @param book   objeto preenchido a partir do formulário
     * @param result resultado da validação
     * @param ra     atributos para mensagens após redirecionamento
     * @return redirecionamento para a listagem ou retorno ao formulário em caso de erro
     */
    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("book") Book book,
                         BindingResult result,
                         RedirectAttributes ra,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("action", "update");
            return "books/form";
        }
        service.update(id, book);
        ra.addFlashAttribute("msg", "Livro atualizado com sucesso!");
        return "redirect:/books";
    }

    /**
     * Processa a exclusão de um livro.
     *
     * @param id identificador do livro
     * @param ra atributos para mensagens após redirecionamento
     * @return redirecionamento para a listagem de livros
     */
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        service.delete(id);
        ra.addFlashAttribute("msg", "Livro excluído com sucesso!");
        return "redirect:/books";
    }
}

package example.springboot.elastic.controller;

import example.springboot.elastic.model.Book;
import example.springboot.elastic.processor.BookProcessor;
import example.springboot.elastic.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    private final BookProcessor bookProcessor;

    public BookController(BookService bookService, BookProcessor bookProcessor) {

        this.bookService = bookService;
        this.bookProcessor = bookProcessor;
    }

    @GetMapping("/{id}")
    public Book getById(@PathVariable String id) {
        return bookService.findOne(id).orElseThrow(() -> new RuntimeException("Can't find book with id " + id));
    }

    @GetMapping()
    public Page<Book> findAll(Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @PostMapping()
    public Book add(@RequestBody Book book) {
        Book savableBook = new Book(UUID.randomUUID().toString(), book.getTitle(), book.getAuthors());

        bookProcessor.processAdd(savableBook);

        return savableBook;
    }

}

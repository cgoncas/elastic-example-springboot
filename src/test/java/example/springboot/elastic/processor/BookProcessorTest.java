package example.springboot.elastic.processor;

import example.springboot.elastic.model.Author;
import example.springboot.elastic.model.Book;
import example.springboot.elastic.service.AuthorService;
import example.springboot.elastic.service.BookService;
import example.springboot.elastic.util.Result;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookProcessorTest {

    @Autowired
    private BookProcessor bookProcessor;

    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorService authorService;

    @After
    public void tearDown() {
        bookService.deleteAll();
        authorService.deleteAll();
    }

    @Test
    public void testProcessSuccessBook() throws ExecutionException, InterruptedException {
        Book book = new Book(null, "Sapiens", Collections.singletonList(new Book.Author("Yuval Noah Harari")));

        CompletableFuture<Result<Book>> process = bookProcessor.processAdd(book);

        Result<Book> resultBook = process.get();

        assertTrue(!resultBook.isError());

        Book finalBook = resultBook.getValue();

        Optional<Book> foundBook = bookService.findOne(finalBook.getId());

        assertEquals(finalBook, foundBook.get());

        Optional<Author> author = authorService.findByName(book.getAuthors().get(0).getName());

        assertEquals(book.getAuthors().get(0).getName(), author.get().getName());
    }

    @Test
    public void testProcessFailBook() throws ExecutionException, InterruptedException {
        Book book = new Book(null, "Sapiens", Collections.singletonList(new Book.Author("Yuval Noah Harari")));

        bookService.save(book);

        CompletableFuture<Result<Book>> process = bookProcessor.processAdd(book);

        Result<Book> resultBook = process.get();

        assertTrue(resultBook.isError());

        Throwable error = resultBook.getError();

        assertThat(error.getMessage()).endsWith("There is a book saved with the same title");
    }

}
package example.springboot.elastic.processor;

import example.springboot.elastic.model.Author;
import example.springboot.elastic.model.Book;
import example.springboot.elastic.service.AuthorService;
import example.springboot.elastic.service.BookService;
import example.springboot.elastic.util.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class BookProcessorUtil {

    protected static List<Author> getAuthors(List<Book.Author> bookAuthors, Function<Book.Author, Optional<Author>> finder) {
        return bookAuthors.stream().map(
                bookAuthor -> getAuthor(bookAuthor, finder)
        ).collect(
                Collectors.toList()
        );
    }

    protected static Author getAuthor(Book.Author bookAuthor, Function<Book.Author, Optional<Author>> finder) {
        return finder.apply(bookAuthor).map(
                author ->
                    new Author(author.getId(), author.getName(), author.getNumberOfBooks() + 1)
        ).orElseGet(
                () -> new Author(null, bookAuthor.getName(), 1));
    }

}

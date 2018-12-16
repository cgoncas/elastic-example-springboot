package example.springboot.elastic.processor;

import example.springboot.elastic.model.Book;
import example.springboot.elastic.service.AuthorService;
import example.springboot.elastic.service.BookService;
import example.springboot.elastic.util.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class BookProcessor {

    private static final Logger _log = LoggerFactory.getLogger(BookProcessor.class);
    private final AuthorService authorService;
    private final BookService bookService;

    public BookProcessor(AuthorService authorService, BookService bookService) {
        this.authorService = authorService;
        this.bookService = bookService;
    }

    public CompletableFuture<Result<Book>> processAdd(Book book) {

        return CompletableFuture.supplyAsync(
                () -> {
                    authorService.saveAll(
                            BookProcessorUtil.getAuthors(book.getAuthors(), bookAuthor -> authorService.findByName(bookAuthor.getName())));

                    return Result.success(bookService.save(book));
                }
        ).handle((result, e) -> {
            if (result != null) {
                return result;
            } else {
                return Result.error(e);
            }
        }).thenApply(result -> {
            if (result.isError()) {
                _log.error("Error found processing the book " + book, result.getError());
            } else if (_log.isInfoEnabled()) {
                _log.info("The has been succesfully processed " + book);
            }

            return (Result<Book>) result;
        });
    }

}

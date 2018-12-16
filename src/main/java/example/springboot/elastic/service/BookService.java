package example.springboot.elastic.service;

import example.springboot.elastic.model.Book;
import example.springboot.elastic.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book save(Book book) {
        Assert.notNull(book.getTitle(), "The title can't be null");

        if (bookRepository.findByTitle(book.getTitle()).isPresent()) {
            throw new RuntimeException("There is a book saved with the same title");
        }

        return bookRepository.save(book);
    }

    public Optional<Book> findOne(String id) {
        return bookRepository.findById(id);
    }

    public Page<Book> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    public long count() {
        return bookRepository.count();
    }

    public Book delete(Book book) {
        bookRepository.delete(book);

        return book;
    }

    public void deleteAll() {
        bookRepository.deleteAll();
    }
}

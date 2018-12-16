package example.springboot.elastic.service;

import example.springboot.elastic.model.Book;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookServiceTest {

    @Autowired
    private BookService bookService;

    @After
    public void tearDown() {
        bookService.deleteAll();
    }

    @Test
    public void testAddBooks() {
        Book book = bookService.save(new Book(null, "Sapiens", Collections.singletonList(new Book.Author("Yuval Noah Harari"))));

        Assert.assertNotNull(book.getId());
        Assert.assertEquals("Sapiens", book.getTitle());
        Assert.assertEquals(1, book.getAuthors().size());
        Assert.assertEquals("Yuval Noah Harari", book.getAuthors().get(0).getName());
    }

    @Test
    public void testUpdateBooks() {
        Book book = bookService.save(new Book(null, "Sapiens", Collections.singletonList(new Book.Author("Yuval Noah Harari"))));

        Book updatedBook = bookService.save(new Book(book.getId(), "Sapiens New Release", book.getAuthors()));

        Assert.assertEquals(book.getId(), updatedBook.getId());
        Assert.assertEquals("Sapiens New Release", updatedBook.getTitle());
        Assert.assertEquals(1, updatedBook.getAuthors().size());
        Assert.assertEquals("Yuval Noah Harari", updatedBook.getAuthors().get(0).getName());
    }

    @Test
    public void testFindAllBooks() {
        Book book = bookService.save(new Book(null, "Sapiens", Collections.singletonList(new Book.Author("Yuval Noah Harari"))));

        Page<Book> booksPage = bookService.findAll(PageRequest.of(0, 1));

        Assert.assertEquals(1, booksPage.getTotalElements());

        List<Book> booksList = booksPage.getContent();

        Assert.assertTrue(booksList.contains(book));
    }

    @Test
    public void testFindOneBook() {
        Book book = bookService.save(new Book(null, "Sapiens", Collections.singletonList(new Book.Author("Yuval Noah Harari"))));

        Book foundBook = bookService.findOne(book.getId()).get();

        Assert.assertEquals(book.getId(), foundBook.getId());
        Assert.assertEquals(book.getTitle(), foundBook.getTitle());
        Assert.assertEquals(book.getAuthors().get(0), foundBook.getAuthors().get(0));
    }
}
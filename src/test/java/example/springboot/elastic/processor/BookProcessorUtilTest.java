package example.springboot.elastic.processor;

import example.springboot.elastic.model.Author;
import example.springboot.elastic.model.Book;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class BookProcessorUtilTest {

    @Test
    public void testGetAuthor() {
        Book.Author bookAuthor = new Book.Author("Some Author Name");

        Author author = BookProcessorUtil.getAuthor(
                bookAuthor, b -> Optional.of(new Author("1", b.getName(), 1)));

        assertEquals(bookAuthor.getName(), author.getName());
        assertEquals(2, author.getNumberOfBooks());
        assertEquals("1", author.getId());
    }

    @Test
    public void testGetAuthorWithNoAuthor() {
        Book.Author bookAuthor = new Book.Author("Some Author Name");

        Author author = BookProcessorUtil.getAuthor(bookAuthor, b -> Optional.empty());

        assertNull(author.getId());
        assertEquals(bookAuthor.getName(), author.getName());
        assertEquals(1, author.getNumberOfBooks());
    }

}

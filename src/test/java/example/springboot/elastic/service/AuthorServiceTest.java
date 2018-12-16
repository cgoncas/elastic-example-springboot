package example.springboot.elastic.service;

import example.springboot.elastic.model.Author;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthorServiceTest {

    @Autowired
    private AuthorService authorService;

    @After
    public void tearDown() {
        authorService.deleteAll();
    }

    @Test
    public void testAdd() {
        Author author = authorService.save(new Author(null, "Yuval Noah Harari", 1));

        Assert.assertNotNull(author.getId());
        Assert.assertEquals("Yuval Noah Harari", author.getName());
        Assert.assertEquals(1, author.getNumberOfBooks());
    }

    @Test
    public void testUpdate() {
        Author author = authorService.save(new Author(null, "Yuval Noah Harari", 1));

        Author savedAuthor = authorService.save(new Author(author.getId(), author.getName(), author.getNumberOfBooks() + 1));

        Assert.assertEquals(author.getId(), savedAuthor.getId());
        Assert.assertEquals(author.getName(), savedAuthor.getName());
        Assert.assertEquals(author.getNumberOfBooks() + 1, savedAuthor.getNumberOfBooks());
    }

    @Test
    public void testFindAll() {
        Author author = authorService.save(new Author(null, "Yuval Noah Harari", 1));

        Page<Author> authorsPage = authorService.findAll(PageRequest.of(0, 1));

        Assert.assertEquals(1, authorsPage.getTotalElements());

        List<Author> authorsList = authorsPage.getContent();

        Assert.assertTrue(authorsList.contains(author));
    }

    @Test
    public void testFindOne() {
        Author author = authorService.save(new Author(null, "Yuval Noah Harari", 1));

        Optional<Author> foundAuthor = authorService.findOne(author.getId());

        Assert.assertEquals(author.getId(), foundAuthor.get().getId());
        Assert.assertEquals(author.getName(), foundAuthor.get().getName());
        Assert.assertEquals(author.getNumberOfBooks(), foundAuthor.get().getNumberOfBooks());
    }

    @Test
    public void testFindByName() {
        Author author = authorService.save(new Author(null, "Yuval Noah Harari", 1));

        Optional<Author> foundAuthor = authorService.findByName(author.getName());

        Assert.assertEquals(author.getId(), foundAuthor.get().getId());
        Assert.assertEquals(author.getName(), foundAuthor.get().getName());
        Assert.assertEquals(author.getNumberOfBooks(), foundAuthor.get().getNumberOfBooks());
    }

    @Test
    public void testFindByNameNotFound() {
        Optional<Author> foundAuthor = authorService.findByName("Not Author");

        Assert.assertTrue(!foundAuthor.isPresent());
    }

    @Test
    public void testFindOneNotFound() {
        Optional<Author> foundAuthor = authorService.findOne("122345");

        Assert.assertTrue(!foundAuthor.isPresent());
    }

}
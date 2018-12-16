package example.springboot.elastic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.springboot.elastic.model.Book;
import example.springboot.elastic.service.BookService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URL;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {

    @LocalServerPort
    private int port;

    private URL base;

    @Autowired
    private BookService bookService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp() throws Exception {
        this.base = new URL("http://localhost:" + port + "/");
    }

    @After
    public void tearDown() {
        bookService.deleteAll();
    }

    @Test
    public void testAddBooks() throws Exception {
        Book book = new Book(null, "Sapiens", Collections.singletonList(new Book.Author("Yuval Noah Harari")));

        String bookId = given().baseUri(this.base.toExternalForm())
                .header("Content-Type", "application/json")
                .body(objectMapper.writeValueAsString(book))
                .post("/books").then().log().all()
                .body(notNullValue()).extract().path("id");

        await().atMost(5, TimeUnit.SECONDS).until(
                () -> bookService.findOne(bookId).isPresent());
    }

    @Test
    public void testGetBooks() {
        Book book = bookService.save(new Book(null, "Sapiens", Collections.singletonList(new Book.Author("Yuval Noah Harari"))));

        given().baseUri(this.base.toExternalForm()).get("/books").then()
                .body("content.find {it.title == '" + book.getTitle() + "'}.authors.name", hasItem(book.getAuthors().get(0).getName()));
    }

    @Test
    public void testGetBookById() {
        Book book = bookService.save(new Book(null, "Sapiens", Collections.singletonList(new Book.Author("Yuval Noah Harari"))));

        given().baseUri(this.base.toExternalForm()).get("/books/" + book.getId()).then().log().all()
                .body("id", equalTo(book.getId()))
                .body("title", equalTo(book.getTitle()))
                .body("authors.name", hasItem(book.getAuthors().get(0).getName()));
    }
}
package example.springboot.elastic.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Document(indexName = "examples_elastic_cloud_book", type = "book")
public class Book {
    @Id
    private String id;
    private String title;
    @Field(type = FieldType.Nested, includeInParent = true)
    private List<Author> authors;

    @JsonCreator
    public Book(@JsonProperty("id") String id, @JsonProperty("title") String title, @JsonProperty("authors") List<Author> authors) {
        this.id = id;
        this.title = title;
        if (authors == null) {
            this.authors = Collections.emptyList();
        } else {
            this.authors = Collections.unmodifiableList(authors);
        }
    }

    public Book() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id) &&
                Objects.equals(title, book.title) &&
                Objects.equals(authors, book.authors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, authors);
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public static class Author {

        private String name;

        @JsonCreator
        public Author(@JsonProperty("name") String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Author author = (Author) o;
            return Objects.equals(name, author.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }

        public String getName() {
            return name;
        }
    }

}

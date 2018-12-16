package example.springboot.elastic.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Objects;

@Document(indexName = "examples_elastic_cloud_author", type = "author")
public class Author {

    private String name;

    private int numberOfBooks;
    @Id
    private String id;

    @JsonCreator
    public Author(@JsonProperty("id") String id, @JsonProperty("name") String name, @JsonProperty("numberOfBooks") int numberOfBooks) {
        this.id = id;
        this.name = name;
        this.numberOfBooks = numberOfBooks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return numberOfBooks == author.numberOfBooks &&
                Objects.equals(name, author.name) &&
                Objects.equals(id, author.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, numberOfBooks);
    }

    public String getName() {
        return name;
    }

    public int getNumberOfBooks() {
        return numberOfBooks;
    }

    public String getId() {
        return id;
    }


}

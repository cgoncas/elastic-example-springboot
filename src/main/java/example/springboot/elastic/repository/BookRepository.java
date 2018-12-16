package example.springboot.elastic.repository;

import example.springboot.elastic.model.Book;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;

public interface BookRepository extends ElasticsearchRepository<Book, String> {

    Optional<Book> findByTitle(String title);

}

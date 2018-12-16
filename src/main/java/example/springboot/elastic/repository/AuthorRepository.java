package example.springboot.elastic.repository;

import example.springboot.elastic.model.Author;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;

public interface AuthorRepository extends ElasticsearchRepository<Author, String> {

    Optional<Author> findByName(String name);

}

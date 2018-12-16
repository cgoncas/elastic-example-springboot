package example.springboot.elastic.service;

import example.springboot.elastic.model.Author;
import example.springboot.elastic.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Author save(Author author) {
        return authorRepository.save(author);
    }

    public List<Author> saveAll(List<Author> authors) {
        return (List<Author>) authorRepository.saveAll(authors);
    }

    public Optional<Author> findByName(String name) {
        return authorRepository.findByName(name);
    }

    public Optional<Author> findOne(String id) {
        return authorRepository.findById(id);
    }

    public Page<Author> findAll(Pageable pageable) {
        return authorRepository.findAll(pageable);
    }

    public long count() {
        return authorRepository.count();
    }

    public Author delete(Author author) {
        authorRepository.delete(author);

        return author;
    }

    public void deleteAll() {
        authorRepository.deleteAll();
    }
}

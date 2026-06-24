package project.controller.services;
import project.controller.model.Book;
import org.springframework.stereotype.Service;
import project.controller.repository.BookRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public Book save(Book book) {
        if (!repository.findByName(book.getName()).isEmpty()) {
            throw new RuntimeException("Name already exists");
        }
        repository.save(book);
        return book;
    }

    public Book update(Book newBook) {
        Book oldBook = repository.findById(newBook.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (newBook.getName() != null) {
            oldBook.setName(newBook.getName());
        }

        if (newBook.getGender() != null) {
            oldBook.setGender(newBook.getGender());
        }

        if (newBook.getAuthor() != null) {
            oldBook.setAuthor(newBook.getAuthor());
        }

        if (newBook.getPublicationYear() > 0) {
            oldBook.setPublicationYear(newBook.getPublicationYear());
        }


        if (!repository.findByName(newBook.getName()).isEmpty()) {
            throw new RuntimeException("Name already exists");
        }

        repository.save(oldBook);
        return oldBook;
    }

    public Book findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
    }
    public Optional<Book> findByName(String name){
        return repository.findByName(name);
    }

    public List<Book> findAll() {
        return repository.findAll();
    }

    public void delete(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        repository.deleteById(id);
    }
}
package project.controller;

import org.springframework.web.bind.annotation.*;
import project.controller.dto.BookDTO;
import project.controller.dto.Create.CreateBookRequest;
import project.controller.dto.Create.CreateUserRequest;
import project.controller.dto.Update.UpdateBookRequest;
import project.controller.dto.Update.UpdateUserRequest;
import project.controller.dto.UserDTO;
import project.controller.model.Book;
import project.controller.model.User;
import project.controller.services.BookService;
import project.controller.services.UserService;

import java.util.List;
import java.util.stream.Collectors;



@RestController
@RequestMapping("/book")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/{id}")
    public BookDTO getMensager(@PathVariable Long id) {
        Book book = bookService.findById(id);
        return new BookDTO(
                book.getBookId(),
                book.getName(),
                book.getGender(),
                book.getAuthor(),
                book.getPublicationYear(),
                book.isAvailable()
        );
    }

    @GetMapping
    public List<BookDTO> getAllBooks() {
        List<Book> books = bookService.findAll();
        return books.stream().map(book -> new BookDTO(
                        book.getBookId(),
                        book.getName(),
                        book.getGender(),
                        book.getAuthor(),
                        book.getPublicationYear(),
                        book.isAvailable()
                ))
                .collect(Collectors.toList());
    }

    @PostMapping
    public BookDTO createBook(@RequestBody CreateBookRequest request) {
        Book createBook = new Book();
        createBook.setName(request.getName());
        createBook.setAuthor(request.getAuthor());
        createBook.setGender(request.getGender());
        createBook.setPublicationYear(request.getPublicationYear());
        createBook.setAvailable(true);
        Book savedBook = bookService.save(createBook);

        return new BookDTO(
                savedBook.getBookId(),
                savedBook.getName(),
                savedBook.getGender(),
                savedBook.getAuthor(),
                savedBook.getPublicationYear(),
                savedBook.isAvailable()
        );
    }

    @PutMapping("/{id}")
    public BookDTO updateBook(@PathVariable Long id, @RequestBody UpdateBookRequest request) {
        Book existingBook = bookService.findById(id);
        String oldName = existingBook.getName();
        String newName = request.getName();

        if ( newName != null && !newName.equals(oldName)) {
            if (bookService.findByName(newName).isPresent()) {
                throw new RuntimeException("This Book name is already in use by another user");
            }
        }

        if (request.getName() != null) {
            existingBook.setName(request.getName());
        }
        if (request.getGender() != null) {
            existingBook.setGender(request.getGender());
        }
        if (request.getAuthor() != null) {
            existingBook.setAuthor(request.getAuthor());
        }
        if (request.getPublicationYear() != null && request.getPublicationYear() > 0) {
            existingBook.setPublicationYear(request.getPublicationYear());
        }

        Book updateBook = bookService.update(existingBook);

        return new BookDTO(
                updateBook.getBookId(),
                updateBook.getName(),
                updateBook.getGender(),
                updateBook.getAuthor(),
                updateBook.getPublicationYear(),
                updateBook.isAvailable()
        );

    }
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id){
        bookService.delete(id);

    }
}
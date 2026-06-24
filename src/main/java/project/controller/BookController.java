package project.controller;

import org.springframework.web.bind.annotation.*;
import project.controller.dto.BookDTO;
import project.controller.dto.Create.CreateBookRequest;
import project.controller.dto.Update.UpdateBookRequest;
import project.controller.model.Book;
import project.controller.services.BookService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller responsável pelos endpoints relacionados a livros.
 *
 * Fornece operações CRUD (Create, Read, Update, Delete) para a entidade Book.
 *
 * Endpoints disponíveis:
 * - GET    /book          - Lista todos os livros
 * - GET    /book/{id}     - Busca um livro por ID
 * - POST   /book          - Cria um novo livro
 * - PUT    /book/{id}     - Atualiza um livro existente
 * - DELETE /book/{id}     - Deleta um livro
 *
 * @author Heitor Ivonica
 * @version 1.0
 */
@RestController
@RequestMapping("/book")
public class BookController {

    /**
     * Service injetado para acesso às regras de negócio.
     */
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Busca um livro pelo ID.
     *
     * @param id ID do livro a ser buscado (passado na URL)
     * @return BookDTO com os dados do livro
     * @throws RuntimeException se o livro não for encontrado
     *
     * Exemplo de requisição: GET /book/1
     */
    @GetMapping("/{id}")
    public BookDTO getBookById(@PathVariable Long id) {
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

    /**
     * Lista todos os livros cadastrados no sistema.
     *
     * @return Lista de BookDTO com todos os livros
     *
     * Exemplo de requisição: GET /book
     */
    @GetMapping
    public List<BookDTO> getAllBooks() {
        List<Book> books = bookService.findAll();
        return books.stream()
                .map(book -> new BookDTO(
                        book.getBookId(),
                        book.getName(),
                        book.getGender(),
                        book.getAuthor(),
                        book.getPublicationYear(),
                        book.isAvailable()
                ))
                .collect(Collectors.toList());
    }

    /**
     * Cria um novo livro no sistema.
     *
     * Regras de negócio:
     * - O nome do livro é obrigatório e deve ser único
     * - O livro inicia com available = true
     * - Os campos name, author, gender e publicationYear são obrigatórios
     *
     * @param request DTO com os dados do livro a ser criado
     * @return BookDTO com os dados do livro criado (incluindo ID gerado)
     * @throws RuntimeException se o nome já estiver cadastrado
     *
     * Exemplo de requisição: POST /book
     * Body: { "name": "Dom Casmurro", "gender": "Romance", "author": "Machado de Assis", "publicationYear": 1899 }
     */
    @PostMapping
    public BookDTO createBook(@RequestBody CreateBookRequest request) {
        Book createBook = new Book();
        createBook.setName(request.getName());
        createBook.setAuthor(request.getAuthor());
        createBook.setGender(request.getGender());
        createBook.setPublicationYear(request.getPublicationYear());
        createBook.setAvailable(true); // Livro começa disponível

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

    /**
     * Atualiza um livro existente no sistema.
     *
     * Regras de negócio:
     * - Todos os campos são opcionais (atualização parcial)
     * - O nome só é atualizado se enviado e deve ser único
     * - publicationYear só é atualizado se > 0
     * - available é gerenciado pelo sistema (empréstimos/devoluções)
     *
     * @param id ID do livro a ser atualizado (passado na URL)
     * @param request DTO com os campos a serem atualizados
     * @return BookDTO com os dados do livro atualizado
     * @throws RuntimeException se o livro não for encontrado
     * @throws RuntimeException se o novo nome já estiver em uso
     *
     * Exemplo de requisição: PUT /book/1
     * Body: { "name": "Dom Casmurro - Edição Especial" }
     */
    @PutMapping("/{id}")
    public BookDTO updateBook(@PathVariable Long id, @RequestBody UpdateBookRequest request) {
        Book existingBook = bookService.findById(id);
        String oldName = existingBook.getName();
        String newName = request.getName();

        // Valida se o nome foi alterado e se já existe outro livro com o mesmo nome
        if (newName != null && !newName.equals(oldName)) {
            if (bookService.findByName(newName).isPresent()) {
                throw new RuntimeException("This Book name is already in use by another user");
            }
        }

        // Atualiza apenas os campos que vieram na requisição
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

        Book updatedBook = bookService.update(existingBook);

        return new BookDTO(
                updatedBook.getBookId(),
                updatedBook.getName(),
                updatedBook.getGender(),
                updatedBook.getAuthor(),
                updatedBook.getPublicationYear(),
                updatedBook.isAvailable()
        );
    }

    /**
     * Deleta um livro do sistema.
     *
     * @param id ID do livro a ser deletado (passado na URL)
     * @throws RuntimeException se o livro não for encontrado
     *
     * Exemplo de requisição: DELETE /book/1
     */
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        bookService.delete(id);
    }
}
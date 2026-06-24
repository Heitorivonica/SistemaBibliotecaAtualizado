package project.controller.services;

import project.controller.model.Book;
import org.springframework.stereotype.Service;
import project.controller.repository.BookRepository;

import java.util.List;
import java.util.Optional;

/**
 * Camada de Serviço para a entidade Book.
 *
 * Responsável por aplicar as regras de negócio relacionadas a livros.
 *
 * @author Heitor Ivonica
 * @version 1.0
 */
@Service
public class BookService {

    /**
     * Repository injetado para acesso aos dados.
     * Utiliza injeção de dependência via construtor.
     */
    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    /**
     * Salva um novo livro no sistema.
     *
     * Regras de negócio:
     * - Valida se já existe um livro com o mesmo nome (título)
     * - Se existir, lança exceção
     * - Se não existir, salva o livro
     *
     * @param book livro a ser salvo
     * @return livro salvo
     * @throws RuntimeException se o nome já estiver cadastrado
     */
    public Book save(Book book) {
        if (!repository.findByName(book.getName()).isEmpty()) {
            throw new RuntimeException("Name already exists");
        }
        repository.save(book);
        return book;
    }

    /**
     * Atualiza um livro existente no sistema.
     *
     * Regras de negócio:
     * - Busca o livro pelo ID (se não existir, lança exceção)
     * - Atualiza apenas os campos que vieram (não nulos)
     * - Valida se o novo nome já existe em outro livro
     *
     * @param newBook livro com os dados atualizados
     * @return livro atualizado
     * @throws RuntimeException se o livro não for encontrado
     * @throws RuntimeException se o nome já estiver em uso
     */
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

    /**
     * Busca um livro pelo ID.
     *
     * @param id ID do livro
     * @return livro encontrado
     * @throws RuntimeException se o livro não for encontrado
     */
    public Book findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
    }

    /**
     * Busca um livro pelo nome.
     *
     * @param name nome do livro
     * @return Optional contendo o livro se encontrado
     */
    public Optional<Book> findByName(String name) {
        return repository.findByName(name);
    }

    /**
     * Lista todos os livros do sistema.
     *
     * @return lista de livros
     */
    public List<Book> findAll() {
        return repository.findAll();
    }

    /**
     * Deleta um livro pelo ID.
     *
     * Regras de negócio:
     * - Verifica se o livro existe antes de deletar
     * - Se não existir, lança exceção
     *
     * @param id ID do livro a ser deletado
     * @throws RuntimeException se o livro não for encontrado
     */
    public void delete(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        repository.deleteById(id);
    }
}
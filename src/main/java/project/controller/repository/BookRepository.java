package project.controller.repository;

import project.controller.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Interface Repository para a entidade Book.
 *
 * Esta interface fornece métodos CRUD (Create, Read, Update, Delete)
 * automaticamente através do Spring Data JPA.
 *
 * Ao estender JpaRepository, a interface herda métodos como:
 * - save(Book entity)
 * - findById(Long id)
 * - findAll()
 * - deleteById(Long id)
 * - count()
 * - existsById(Long id)
 *
 * @author Heitor Ivonica
 * @version 1.0
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * Busca um livro pelo nome (título).
     *
     * Método personalizado que utiliza a convenção de nomenclatura do Spring Data JPA.
     * O Spring Data JPA gera automaticamente a consulta SQL com base no nome do método.
     *
     * SQL gerado: SELECT * FROM book WHERE name = ?
     *
     * @param name título do livro a ser buscado
     * @return Optional contendo o livro se encontrado, ou empty se não encontrado
     *
     * Utilizado para:
     * - Validar se já existe um livro com o mesmo nome (BookService.save)
     * - Validar se já existe um livro com o mesmo nome ao atualizar (BookService.update)
     */
    Optional<Book> findByName(String name);
}
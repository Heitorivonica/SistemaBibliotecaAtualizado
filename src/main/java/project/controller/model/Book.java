package project.controller.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidade que representa um livro no sistema.
 *
 * Esta classe é mapeada para a tabela 'book' no banco de dados MySQL.
 * Utiliza JPA (Java Persistence API) para ORM (Object-Relational Mapping).
 *
 * Lombok é utilizado para gerar automaticamente getters e setters,
 * reduzindo o código boilerplate.
 *
 * @author Heitor Ivonica
 * @version 1.0
 */
@Getter
@Setter
@Entity
public class Book {

    /**
     * ID único do livro (chave primária).
     *
     * - @Id: indica que este campo é a chave primária
     * - @GeneratedValue: define que o ID será gerado automaticamente
     * - GenerationType.IDENTITY: o banco de dados gera o ID (auto-increment)
     *
     * O nome do campo é 'bookId' para diferenciar de outros IDs no sistema.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;

    /**
     * Título do livro.
     *
     * Obrigatório para cadastro.
     * Utilizado para busca e identificação do livro.
     * Exemplo: "Dom Casmurro"
     */
    private String name;

    /**
     * Gênero do livro.
     *
     * Classificação do livro por categoria.
     * Exemplo: "Romance", "Ficção Científica", "História"
     */
    private String gender;

    /**
     * Nome do autor do livro.
     *
     * Obrigatório para cadastro.
     * Exemplo: "Machado de Assis"
     */
    private String author;

    /**
     * Ano de publicação do livro.
     *
     * Deve ser um valor maior que 0.
     * Exemplo: 1899
     */
    private int publicationYear;

    /**
     * Status de disponibilidade do livro.
     *
     * - true: livro disponível para empréstimo
     * - false: livro emprestado ou indisponível
     *
     * Gerenciado automaticamente pelo sistema:
     * - Inicia como true ao criar o livro
     * - Muda para false quando emprestado (LoanService.borrowBook)
     * - Muda para true quando devolvido (LoanService.returnBook)
     */
    private boolean available;
}
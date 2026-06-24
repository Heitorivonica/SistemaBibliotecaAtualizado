package project.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO (Data Transfer Object) para resposta de livros.
 *
 * Utilizado para retornar os dados de um livro nas requisições:
 * - GET /book/{id}
 * - GET /book
 * - POST /book
 * - PUT /book/{id}
 *
 * Contém todos os campos do livro, incluindo disponibilidade.
 *
 * @author Heitor Ivonica
 * @version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {

    /**
     * ID único do livro (gerado automaticamente)
     */
    private Long bookId;

    /**
     * Título do livro
     */
    private String name;

    /**
     * Gênero do livro
     */
    private String gender;

    /**
     * Autor do livro
     */
    private String author;

    /**
     * Ano de publicação
     */
    private int publicationYear;

    /**
     * Status de disponibilidade
     * - true: livro disponível para empréstimo
     * - false: livro emprestado
     *
     * Gerenciado automaticamente pelo sistema:
     * - true ao criar livro
     * - false ao emprestar
     * - true ao devolver
     */
    private boolean available;
}
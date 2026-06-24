package project.controller.dto.Create;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO (Data Transfer Object) para criar um novo livro.
 *
 * Utilizado para receber os dados da requisição POST /book.
 * Contém apenas os campos necessários para a criação de um livro.
 * O campo 'available' não está presente pois o sistema define como true por padrão.
 *
 * @author Heitor Ivonica
 * @version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookRequest {

    /**
     * Título do livro (obrigatório)
     * Exemplo: "Dom Casmurro"
     */
    private String name;

    /**
     * Gênero do livro (obrigatório)
     * Exemplo: "Romance", "Ficção", "Ciência"
     */
    private String gender;

    /**
     * Nome do autor do livro (obrigatório)
     * Exemplo: "Machado de Assis"
     */
    private String author;

    /**
     * Ano de publicação do livro (obrigatório)
     * Exemplo: 1899
     * Deve ser um ano válido (maior que 0)
     */
    private int publicationYear;
}
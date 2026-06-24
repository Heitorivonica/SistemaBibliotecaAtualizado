package project.controller.dto.Update;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO (Data Transfer Object) para atualizar um livro existente.
 *
 * Utilizado para receber os dados da requisição PUT /book/{id}.
 * Todos os campos são opcionais, permitindo atualização parcial.
 * Campos não enviados mantêm seus valores originais.
 *
 * @author Heitor Ivonica
 * @version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBookRequest {

    /**
     * Novo título do livro (opcional)
     * Se enviado, será validado para evitar duplicidade
     * Se não enviado, mantém o título atual
     */
    private String name;

    /**
     * Novo gênero do livro (opcional)
     * Se não enviado, mantém o gênero atual
     */
    private String gender;

    /**
     * Novo autor do livro (opcional)
     * Se não enviado, mantém o autor atual
     */
    private String author;

    /**
     * Novo ano de publicação (opcional)
     * Deve ser maior que 0 se enviado
     * Se não enviado, mantém o ano atual
     * Usado Integer para permitir null (campo opcional)
     */
    private Integer publicationYear;
}
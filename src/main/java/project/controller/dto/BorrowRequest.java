package project.controller.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO (Data Transfer Object) para realizar um empréstimo.
 *
 * Utilizado para receber os dados da requisição POST /loan/borrowbook.
 * Contém as informações necessárias para emprestar um livro a um usuário.
 *
 * @author Heitor Ivonica
 * @version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BorrowRequest {

    /**
     * ID do usuário que está pegando o livro (obrigatório)
     * Deve existir no sistema
     * O usuário não pode estar bloqueado
     */
    @NotNull(message = "The UserId is required")
    private Long userId;

    /**
     * ID do livro a ser emprestado (obrigatório)
     * Deve existir no sistema
     * O livro deve estar disponível (available = true)
     */
    @NotNull(message = "The BookId is required")
    private Long bookId;

    /**
     * Dias de empréstimo (obrigatório)
     * Define o prazo para devolução
     * Deve ser maior que 0
     *
     * Exemplo: 14 = 14 dias de empréstimo
     */
    @Positive(message = "Loan days must be greater than 0")
    private int loanDays;
}
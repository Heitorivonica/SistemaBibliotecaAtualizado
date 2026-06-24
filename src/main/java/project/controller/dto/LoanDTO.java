package project.controller.dto;

import project.controller.Enums.Status;
import project.controller.Enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO (Data Transfer Object) para resposta de empréstimos.
 *
 * Utilizado para retornar os dados de um empréstimo nas requisições:
 * - GET /loan/{id}
 * - GET /loan
 * - POST /loan/borrowbook
 * - POST /loan/return
 *
 * Contém todos os campos do empréstimo, incluindo os IDs do livro e usuário.
 *
 * @author Heitor Ivonica
 * @version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoanDTO {

    /**
     * ID único do empréstimo (gerado automaticamente)
     */
    private Long id;

    /**
     * ID do livro emprestado (relacionamento)
     * Permite identificar qual livro foi emprestado
     */
    private Long userId;

    /**
     * ID do usuário que fez o empréstimo (relacionamento)
     * Permite identificar quem pegou o livro
     */
    private Long bookId;

    /**
     * Data em que o livro foi emprestado
     * Definida automaticamente no momento do empréstimo
     */
    private LocalDate loanDate;

    /**
     * Data prevista para devolução
     * Calculada como: loanDate + loanDays
     */
    private LocalDate expectedReturnDate;

    /**
     * Data real da devolução
     * - null: livro ainda não foi devolvido
     * - data: livro devolvido nesta data
     */
    private LocalDate actualReturnDate;

    /**
     * Status do empréstimo
     *
     * Valores possíveis:
     * - ACTIVE: empréstimo em andamento
     * - RETURNED: devolvido no prazo
     * - OVERDUE: devolvido com atraso
     */
    private Status status;

    /**
     * Valor da multa calculada
     * - 0: sem multa
     * - valor: multa por atraso (limitada a R$100,00)
     *
     * Calculado com regras:
     * - 3 dias de cortesia
     * - Limite máximo de R$100
     * - Arredondamento com HALF_UP
     */
    private BigDecimal fine;
}
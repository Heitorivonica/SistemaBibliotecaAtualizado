package project.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * DTO (Data Transfer Object) para realizar uma devolução.
 *
 * Utilizado para receber os dados da requisição POST /loan/return.
 * Contém as informações necessárias para devolver um livro.
 *
 * @author Heitor Ivonica
 * @version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReturnRequest {

    /**
     * ID do empréstimo a ser finalizado (obrigatório)
     * Deve existir no sistema
     * O empréstimo deve estar com status ACTIVE
     */
    @NotNull(message = "The LoanId is required")
    private Long loanId;

    /**
     * Data real da devolução (obrigatória)
     * Comparada com a data prevista para calcular multa
     *
     * Se a data for posterior à prevista:
     * - status = OVERDUE
     * - multa = diasAtraso * valorPorDia
     *
     * Se a data for anterior ou igual à prevista:
     * - status = RETURNED
     * - multa = 0
     */
    @NotNull(message = "The return date is required")
    private LocalDate returnDate;
}
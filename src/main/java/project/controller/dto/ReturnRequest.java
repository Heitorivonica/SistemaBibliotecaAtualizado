package project.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReturnRequest {
    @NotNull(message = "The LoanId is required")
    private Long loanId;
    @NotNull(message = "The return date is required" )
    private LocalDate returnDate;
}

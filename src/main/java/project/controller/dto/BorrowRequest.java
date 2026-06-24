package project.controller.dto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BorrowRequest {
    @NotNull(message = "The UserId is required")
    private Long userId;
    @NotNull(message = "The BookId is required")
    private Long bookId;
    @Positive(message = "Loan days must be greater than 0")
    private int loanDays;
}

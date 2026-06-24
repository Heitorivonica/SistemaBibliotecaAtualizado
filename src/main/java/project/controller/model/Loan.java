package project.controller.model;

import project.controller.Enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
@Getter
@Setter
@Entity



public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Book book;
    @ManyToOne
    private User user;
    private LocalDate loanDate;
    private LocalDate expectedReturnDate;
    private LocalDate  actualReturnDate;
    private Status status;
    private BigDecimal fine;

}

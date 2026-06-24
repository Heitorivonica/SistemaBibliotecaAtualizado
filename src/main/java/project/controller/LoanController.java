package project.controller;

import org.springframework.web.bind.annotation.*;
import project.controller.dto.BorrowRequest;
import project.controller.dto.LoanDTO;
import project.controller.dto.ReturnRequest;
import project.controller.model.Book;
import project.controller.model.Loan;
import project.controller.model.User;
import project.controller.services.BookService;
import project.controller.services.LoanServices;
import project.controller.services.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/loan")
public class LoanController {
    private final LoanServices loanServices;
    private final BookService bookService;
    private final UserService userService;

    public LoanController(LoanServices loanServices, BookService bookService, UserService userService) {
        this.loanServices = loanServices;
        this.bookService = bookService;
        this.userService = userService;
    }
    @GetMapping("/{id}")
    public LoanDTO getLoanById(@PathVariable Long id){
        Loan loan = loanServices.findById(id);

        Book book = loan.getBook();
        User user = loan.getUser();

        return new LoanDTO(
                loan.getId(),
                book.getBookId(),
                user.getId(),
                loan.getLoanDate(),
                loan.getExpectedReturnDate(),
                loan.getActualReturnDate(),
                loan.getStatus(),
                loan.getFine()

        );
    }
    @GetMapping
    public List<LoanDTO> getAllLoans(){
        List<Loan> loans = loanServices.findAll();
        return loans.stream().map( loan -> {
            Book book = loan.getBook();
            User user = loan.getUser();


            return new LoanDTO(
                    loan.getId(),
                    book.getBookId(),
                    user.getId(),
                    loan.getLoanDate(),
                    loan.getExpectedReturnDate(),
                    loan.getActualReturnDate(),
                    loan.getStatus(),
                    loan.getFine()

        );

    }) .collect(Collectors.toList());

}
    @PostMapping("/borrowbook")
    public LoanDTO  createLoan(@RequestBody BorrowRequest request){
        LocalDate hoje = LocalDate.now();
        User user = userService.findById(request.getUserId());
        Book book = bookService.findById(request.getBookId());
        Loan loan = new Loan();
        loan.setUser(user);
        loan.setBook(book);
        loan.setLoanDate(hoje);
        loan.setExpectedReturnDate(hoje.plusDays(request.getLoanDays()));
        Loan savedLoan = loanServices.borrowBook(loan);

        return new LoanDTO(
                savedLoan.getId(),
                savedLoan.getUser().getId(),
                savedLoan.getBook().getBookId(),
                savedLoan.getLoanDate(),
                savedLoan.getExpectedReturnDate(),
                savedLoan.getActualReturnDate(),
                savedLoan.getStatus(),
                savedLoan.getFine()
        );

    }
    @PostMapping("/return")
    public LoanDTO returnBook(@RequestBody ReturnRequest request){
        Loan loan = loanServices.findById(request.getLoanId());
        loan.setActualReturnDate(request.getReturnDate());
        Loan returnedLoan = loanServices.returnBook(loan);

        return new LoanDTO(
                returnedLoan.getId(),
                returnedLoan.getBook().getBookId(),
                returnedLoan.getUser().getId(),
                returnedLoan.getLoanDate(),
                returnedLoan.getExpectedReturnDate(),
                returnedLoan.getActualReturnDate(),
                returnedLoan.getStatus(),
                returnedLoan.getFine()
        );
    }
    @DeleteMapping("/{id}")
    public void deleteLoan(@PathVariable Long id){
        loanServices.delete(id);
    }
}


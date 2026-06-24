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

/**
 * Controller responsável pelos endpoints relacionados a empréstimos.
 *
 * Fornece operações para gerenciar empréstimos de livros, incluindo:
 * - Empréstimo de livros (borrow)
 * - Devolução de livros (return)
 * - Consulta de empréstimos
 * - Deleção de empréstimos
 *
 * Endpoints disponíveis:
 * - GET    /loan           - Lista todos os empréstimos
 * - GET    /loan/{id}      - Busca um empréstimo por ID
 * - POST   /loan/borrowbook - Realiza um empréstimo
 * - POST   /loan/return    - Realiza uma devolução
 * - DELETE /loan/{id}      - Deleta um empréstimo
 *
 * @author Heitor Ivonica
 * @version 1.0
 */
@RestController
@RequestMapping("/loan")
public class LoanController {

    /**
     * Services injetados para acesso às regras de negócio.
     */
    private final LoanServices loanServices;
    private final BookService bookService;
    private final UserService userService;

    public LoanController(LoanServices loanServices, BookService bookService, UserService userService) {
        this.loanServices = loanServices;
        this.bookService = bookService;
        this.userService = userService;
    }

    /**
     * Busca um empréstimo pelo ID.
     *
     * Retorna todas as informações do empréstimo, incluindo:
     * - Dados do livro (ID)
     * - Dados do usuário (ID)
     * - Datas do empréstimo
     * - Status atual
     * - Valor da multa (se houver)
     *
     * @param id ID do empréstimo a ser buscado (passado na URL)
     * @return LoanDTO com os dados do empréstimo
     * @throws RuntimeException se o empréstimo não for encontrado
     *
     * Exemplo de requisição: GET /loan/1
     */
    @GetMapping("/{id}")
    public LoanDTO getLoanById(@PathVariable Long id) {
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

    /**
     * Lista todos os empréstimos cadastrados no sistema.
     *
     * @return Lista de LoanDTO com todos os empréstimos
     *
     * Exemplo de requisição: GET /loan
     */
    @GetMapping
    public List<LoanDTO> getAllLoans() {
        List<Loan> loans = loanServices.findAll();
        return loans.stream().map(loan -> {
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
        }).collect(Collectors.toList());
    }

    /**
     * Realiza o empréstimo de um livro para um usuário.
     *
     * Regras de negócio:
     * - O livro deve estar disponível (available = true)
     * - O usuário não pode estar bloqueado (blocked = false)
     * - O prazo de devolução é calculado como: data atual + loanDays
     * - O status do empréstimo é definido como ACTIVE
     * - O livro é marcado como indisponível (available = false)
     *
     * @param request DTO com os dados do empréstimo (userId, bookId, loanDays)
     * @return LoanDTO com os dados do empréstimo realizado
     * @throws RuntimeException se o livro não estiver disponível
     * @throws RuntimeException se o usuário estiver bloqueado
     *
     * Exemplo de requisição: POST /loan/borrowbook
     * Body: { "userId": 1, "bookId": 1, "loanDays": 14 }
     */
    @PostMapping("/borrowbook")
    public LoanDTO createLoan(@RequestBody BorrowRequest request) {
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

    /**
     * Realiza a devolução de um livro.
     *
     * Regras de negócio:
     * - O empréstimo deve estar com status ACTIVE
     * - A data de devolução é registrada
     * - Se a data de devolução for posterior à data prevista:
     *   - status = OVERDUE
     *   - multa é calculada automaticamente
     * - Se a data de devolução for anterior ou igual à data prevista:
     *   - status = RETURNED
     *   - multa = 0
     * - O livro é marcado como disponível (available = true)
     *
     * @param request DTO com os dados da devolução (loanId, returnDate)
     * @return LoanDTO com os dados do empréstimo atualizado
     * @throws RuntimeException se o empréstimo não estiver ativo
     *
     * Exemplo de requisição: POST /loan/return
     * Body: { "loanId": 1, "returnDate": "2026-07-07" }
     */
    @PostMapping("/return")
    public LoanDTO returnBook(@RequestBody ReturnRequest request) {
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

    /**
     * Deleta um empréstimo do sistema.
     *
     * @param id ID do empréstimo a ser deletado (passado na URL)
     * @throws RuntimeException se o empréstimo não for encontrado
     *
     * Exemplo de requisição: DELETE /loan/1
     */
    @DeleteMapping("/{id}")
    public void deleteLoan(@PathVariable Long id) {
        loanServices.delete(id);
    }
}
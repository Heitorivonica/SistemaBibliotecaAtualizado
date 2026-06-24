package project.controller.services;

import project.controller.Enums.UserType;
import project.controller.model.Loan;
import org.springframework.stereotype.Service;
import project.controller.Enums.Status;
import project.controller.repository.LoanRepository;

import java.math.RoundingMode;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Camada de Serviço para a entidade Loan (Empréstimo).
 *
 * Responsável por aplicar as regras de negócio relacionadas a empréstimos,
 * incluindo:
 * - Cálculo de multa por atraso
 * - Cortesia de 3 dias
 * - Limite máximo de R$ 100,00
 * - Bloqueio automático de usuário
 * - Disponibilidade de livros
 *
 * @author Heitor Ivonica
 * @version 1.0
 */
@Service
public class LoanServices {

    /**
     * Repository injetado para acesso aos dados.
     * Utiliza injeção de dependência via construtor.
     */
    private final LoanRepository repository;

    public LoanServices(LoanRepository repository) {
        this.repository = repository;
    }

    /**
     * Salva um novo empréstimo no sistema.
     *
     * Regras de negócio:
     * - Verifica se o ID já existe
     * - Verifica se o livro existe
     * - Verifica se o usuário existe
     * - Verifica se o livro está disponível
     * - Verifica se o usuário não está bloqueado
     *
     * @param loan empréstimo a ser salvo
     * @return empréstimo salvo
     * @throws RuntimeException se alguma validação falhar
     */
    public Loan save(Loan loan) {
        if (!repository.findById(loan.getId()).isEmpty()) {
            throw new RuntimeException("Id already exists");
        }
        if (loan.getBook() == null) {
            throw new RuntimeException("Book is required");
        }
        if (loan.getUser() == null) {
            throw new RuntimeException("User is required");
        }
        if (!loan.getBook().isAvailable()) {
            throw new RuntimeException("The book is not available for loan");
        }
        if (loan.getUser().isBlocked()) {
            throw new RuntimeException("The user is blocked and cannot borrow books");
        }

        repository.save(loan);
        return loan;
    }

    /**
     * Atualiza um empréstimo existente.
     *
     * @param newLoan empréstimo com dados atualizados
     * @return empréstimo atualizado
     * @throws RuntimeException se o empréstimo não for encontrado
     */
    public Loan update(Loan newLoan) {
        Loan oldLoan = repository.findById(newLoan.getId())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (newLoan.getUser() != null) {
            oldLoan.setUser(newLoan.getUser());
        }
        if (newLoan.getBook() != null) {
            oldLoan.setBook(newLoan.getBook());
        }
        if (newLoan.getLoanDate() != null) {
            oldLoan.setLoanDate(newLoan.getLoanDate());
        }
        if (newLoan.getActualReturnDate() != null) {
            oldLoan.setActualReturnDate(newLoan.getActualReturnDate());
        }
        if (newLoan.getExpectedReturnDate() != null) {
            oldLoan.setExpectedReturnDate(newLoan.getExpectedReturnDate());
        }
        if (newLoan.getStatus() != null) {
            oldLoan.setStatus(newLoan.getStatus());
        }
        repository.save(oldLoan);
        return oldLoan;
    }

    /**
     * Realiza o empréstimo de um livro.
     *
     * Regras de negócio:
     * - Verifica se o livro existe
     * - Verifica se o usuário existe
     * - Verifica se o livro está disponível
     * - Verifica se o usuário não está bloqueado
     * - Define o status como ACTIVE
     * - Marca o livro como indisponível
     *
     * @param loan empréstimo a ser realizado
     * @return empréstimo realizado
     * @throws RuntimeException se alguma validação falhar
     */
    public Loan borrowBook(Loan loan) {
        if (loan.getBook() == null) {
            throw new RuntimeException("The book don't exist");
        }
        if (loan.getUser() == null) {
            throw new RuntimeException("The user don't exist");
        }
        if (!loan.getBook().isAvailable()) {
            throw new RuntimeException("The book is not available");
        }
        if (loan.getUser().isBlocked()) {
            throw new RuntimeException("The user is blocked and cannot borrow books");
        }
        loan.setStatus(Status.ACTIVE);
        loan.getBook().setAvailable(false);
        repository.save(loan);
        return loan;
    }

    /**
     * Realiza a devolução de um livro.
     *
     * Regras de negócio:
     * - Verifica se o empréstimo está ativo
     * - Calcula se houve atraso
     * - Se houver atraso: status = OVERDUE
     * - Se não houver atraso: status = RETURNED
     * - Calcula e salva a multa
     * - Marca o livro como disponível
     *
     * @param loan empréstimo a ser devolvido
     * @return empréstimo devolvido
     * @throws RuntimeException se o empréstimo não estiver ativo
     */
    public Loan returnBook(Loan loan) {
        Loan oldLoan = findById(loan.getId());
        if (oldLoan.getStatus() != Status.ACTIVE) {
            throw new RuntimeException("The loan is not active and cannot be returned");
        }
        if (oldLoan.getExpectedReturnDate().isBefore(oldLoan.getActualReturnDate())) {
            oldLoan.setStatus(Status.OVERDUE);
        } else {
            oldLoan.setStatus(Status.RETURNED);
        }

        // ========== CALCULA E SALVA A MULTA ==========
        BigDecimal fine = calculateFine(oldLoan);
        oldLoan.setFine(fine);

        oldLoan.getBook().setAvailable(true);
        repository.save(oldLoan);
        return oldLoan;
    }

    /**
     * Calcula a multa por atraso de um empréstimo.
     *
     * Regras de negócio:
     * - Se não houver atraso (daysLate <= 0): multa = 0
     * - Cortesia de 3 dias (não cobra nos primeiros 3 dias)
     * - Limite máximo de R$ 100,00
     * - Arredondamento com HALF_UP (2 casas decimais)
     *
     * @param loan empréstimo a ser calculado
     * @return valor da multa (BigDecimal)
     */
    public BigDecimal calculateFine(Loan loan) {
        UserType userType = loan.getUser().getType();
        long daysLate = ChronoUnit.DAYS.between(loan.getExpectedReturnDate(), loan.getActualReturnDate());

        if (loan.getActualReturnDate() == null) {
            return BigDecimal.ZERO;
        }
        if (daysLate <= 0) {
            return BigDecimal.ZERO;
        }

        int paidDays = (int) daysLate - 3;
        if (paidDays < 0) {
            paidDays = 0;
        }

        BigDecimal finePerDay = userType.getMulta().multiply(BigDecimal.valueOf(paidDays));
        if (finePerDay.compareTo(new BigDecimal(100)) > 0) {
            finePerDay = new BigDecimal("100");
        }
        return finePerDay.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Busca um empréstimo pelo ID.
     *
     * @param id ID do empréstimo
     * @return empréstimo encontrado
     * @throws RuntimeException se o empréstimo não for encontrado
     */
    public Loan findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found"));
    }

    /**
     * Busca todos os empréstimos de um usuário específico.
     *
     * @param userId ID do usuário
     * @return lista de empréstimos do usuário
     * @throws RuntimeException se o userId for nulo
     */
    public List<Loan> findLoansByUser(Long userId) {
        if (userId == null) {
            throw new RuntimeException("User id is required");
        }
        return repository.findByUserId(userId);
    }

    /**
     * Busca todos os empréstimos de um livro específico.
     *
     * @param bookId ID do livro
     * @return lista de empréstimos do livro
     * @throws RuntimeException se o bookId for nulo
     */
    public List<Loan> findLoansByBook(Long bookId) {
        if (bookId == null) {
            throw new RuntimeException("Book id is required");
        }
        return repository.findByBookBookId(bookId);
    }

    /**
     * Lista todos os empréstimos do sistema.
     *
     * @return lista de empréstimos
     */
    public List<Loan> findAll() {
        return repository.findAll();
    }

    /**
     * Deleta um empréstimo pelo ID.
     *
     * Regras de negócio:
     * - Verifica se o empréstimo existe antes de deletar
     * - Se não existir, lança exceção
     *
     * @param id ID do empréstimo a ser deletado
     * @throws RuntimeException se o empréstimo não for encontrado
     */
    public void delete(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found"));
        repository.deleteById(id);
    }
}
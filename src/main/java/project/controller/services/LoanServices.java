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

@Service
public class LoanServices {
    private final LoanRepository repository;

    public LoanServices(LoanRepository repository) {
        this.repository = repository;
    }

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

    public Loan borrowBook(Loan loan) {
        if (loan.getBook() == null) {
            throw new RuntimeException("The book dont exist");
        }
        if (loan.getUser() == null) {
            throw new RuntimeException("The user dont exist");
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
        oldLoan.setFine(fine);  // ← ADICIONADO!

        oldLoan.getBook().setAvailable(true);
        repository.save(oldLoan);
        return oldLoan;
    }

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

    public Loan findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
    }

    public List<Loan> findLoansByUser(Long userId) {
        if (userId == null) {
            throw new RuntimeException("User id is required");
        }
        return repository.findByUserId(userId);
    }

    public List<Loan> findLoansByBook(Long bookId) {
        if (bookId == null) {
            throw new RuntimeException("Book id is required");
        }
        return repository.findByBookBookId(bookId);
    }

    public List<Loan> findAll() {
        return repository.findAll();
    }

    public void delete(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        repository.deleteById(id);
    }
}
package project.controller.repository;

import project.controller.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository  extends JpaRepository<Loan, Long> {
        List<Loan> findByUserId(Long userId);
        List<Loan> findByBookBookId(Long bookId);
        List<Loan> findLoanById(Long id);
}

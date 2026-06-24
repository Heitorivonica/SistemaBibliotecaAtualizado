package project.controller.repository;

import project.controller.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Interface Repository para a entidade Loan.
 *
 * Esta interface fornece métodos CRUD (Create, Read, Update, Delete)
 * automaticamente através do Spring Data JPA.
 *
 * Ao estender JpaRepository, a interface herda métodos como:
 * - save(Loan entity)
 * - findById(Long id)
 * - findAll()
 * - deleteById(Long id)
 * - count()
 * - existsById(Long id)
 *
 * @author Heitor Ivonica
 * @version 1.0
 */
@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

        /**
         * Busca todos os empréstimos de um usuário específico.
         *
         * Método personalizado que utiliza a convenção de nomenclatura do Spring Data JPA.
         * O Spring Data JPA gera automaticamente a consulta SQL com base no nome do método.
         *
         * SQL gerado: SELECT * FROM loan WHERE user_id = ?
         *
         * @param userId ID do usuário
         * @return Lista de empréstimos do usuário
         *
         * Utilizado para:
         * - Verificar se um usuário tem empréstimos ativos
         * - Listar histórico de empréstimos de um usuário
         */
        List<Loan> findByUserId(Long userId);

        /**
         * Busca todos os empréstimos de um livro específico.
         *
         * Método personalizado que utiliza a convenção de nomenclatura do Spring Data JPA.
         * O Spring Data JPA gera automaticamente a consulta SQL com base no nome do método.
         *
         * SQL gerado: SELECT * FROM loan WHERE book_id = ?
         *
         * @param bookId ID do livro
         * @return Lista de empréstimos do livro
         *
         * Utilizado para:
         * - Verificar se um livro já foi emprestado
         * - Listar histórico de empréstimos de um livro
         *
         * ⚠️ ATENÇÃO: O nome do método usa 'BookBookId' porque o ID da classe Book é 'bookId',
         * não 'id'. O Spring Data JPA segue a convenção: find + By + [Objeto] + [Campo].
         */
        List<Loan> findByBookBookId(Long bookId);
}
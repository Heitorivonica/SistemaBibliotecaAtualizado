package project.controller.repository;

import project.controller.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Interface Repository para a entidade User.
 *
 * Esta interface fornece métodos CRUD (Create, Read, Update, Delete)
 * automaticamente através do Spring Data JPA.
 *
 * Ao estender JpaRepository, a interface herda métodos como:
 * - save(User entity)
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
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Busca um usuário pelo e-mail.
     *
     * Método personalizado que utiliza a convenção de nomenclatura do Spring Data JPA.
     * O Spring Data JPA gera automaticamente a consulta SQL com base no nome do método.
     *
     * SQL gerado: SELECT * FROM user WHERE email = ?
     *
     * @param email e-mail do usuário a ser buscado
     * @return Optional contendo o usuário se encontrado, ou empty se não encontrado
     *
     * Utilizado para:
     * - Validar se já existe um usuário com o mesmo e-mail (UserService.save)
     * - Validar se já existe um usuário com o mesmo e-mail ao atualizar (UserService.update)
     * - Buscar usuário por e-mail (UserService.findByEmail)
     */
    Optional<User> findByEmail(String email);
}
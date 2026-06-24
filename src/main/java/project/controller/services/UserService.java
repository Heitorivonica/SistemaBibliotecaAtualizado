package project.controller.services;

import project.controller.repository.UserRepository;
import project.controller.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Camada de Serviço para a entidade User.
 *
 * Responsável por aplicar as regras de negócio relacionadas a usuários.
 *
 * @author Heitor Ivonica
 * @version 1.0
 */
@Service
public class UserService {

    /**
     * Repository injetado para acesso aos dados.
     * Utiliza injeção de dependência via construtor.
     */
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * Salva um novo usuário no sistema.
     *
     * Regras de negócio:
     * - Valida se já existe um usuário com o mesmo e-mail
     * - Se existir, lança exceção
     * - Se não existir, salva o usuário
     *
     * @param user usuário a ser salvo
     * @return usuário salvo
     * @throws RuntimeException se o e-mail já estiver cadastrado
     */
    public User save(User user) {
        if (!repository.findByEmail(user.getEmail()).isEmpty()) {
            throw new RuntimeException("Email already registered");
        }
        return repository.save(user);
    }

    /**
     * Atualiza um usuário existente no sistema.
     *
     * Regras de negócio:
     * - Busca o usuário pelo ID (se não existir, lança exceção)
     * - Atualiza apenas os campos que vieram (não nulos)
     * - Se o e-mail for alterado, valida se já existe em outro usuário
     * - O campo 'blocked' é atualizado apenas se for enviado explicitamente
     *
     * @param newUser usuário com os dados atualizados
     * @return usuário atualizado
     * @throws RuntimeException se o usuário não for encontrado
     * @throws RuntimeException se o e-mail já estiver em uso
     */
    public User update(User newUser) {
        User oldUser = repository.findById(newUser.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (newUser.getName() != null) {
            oldUser.setName(newUser.getName());
        }

        if (newUser.getEmail() != null) {
            if (!newUser.getEmail().equals(oldUser.getEmail())) {
                if (repository.findByEmail(newUser.getEmail()).isPresent()) {
                    throw new RuntimeException("This email is already in use by another user");
                }
            }
            oldUser.setEmail(newUser.getEmail());
        }

        if (newUser.getType() != null) {
            oldUser.setType(newUser.getType());
        }

        return repository.save(oldUser);
    }

    /**
     * Busca um usuário pelo ID.
     *
     * @param id ID do usuário
     * @return usuário encontrado
     * @throws RuntimeException se o usuário não for encontrado
     */
    public User findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    /**
     * Busca um usuário pelo e-mail.
     *
     * @param email e-mail do usuário
     * @return Optional contendo o usuário se encontrado
     */
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    /**
     * Lista todos os usuários do sistema.
     *
     * @return lista de usuários
     */
    public List<User> findAll() {
        return repository.findAll();
    }

    /**
     * Deleta um usuário pelo ID.
     *
     * Regras de negócio:
     * - Verifica se o usuário existe antes de deletar
     * - Se não existir, lança exceção
     *
     * @param id ID do usuário a ser deletado
     * @throws RuntimeException se o usuário não for encontrado
     */
    public void delete(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        repository.deleteById(id);
    }
}
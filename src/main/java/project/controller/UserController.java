package project.controller;

import org.springframework.web.bind.annotation.*;
import project.controller.dto.Create.CreateUserRequest;
import project.controller.dto.Update.UpdateUserRequest;
import project.controller.dto.UserDTO;
import project.controller.model.User;
import project.controller.services.UserService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller responsável pelos endpoints relacionados a usuários.
 *
 * Fornece operações CRUD (Create, Read, Update, Delete) para a entidade User.
 *
 * Endpoints disponíveis:
 * - GET    /user          - Lista todos os usuários
 * - GET    /user/{id}     - Busca um usuário por ID
 * - POST   /user          - Cria um novo usuário
 * - PUT    /user/{id}     - Atualiza um usuário existente
 * - DELETE /user/{id}     - Deleta um usuário
 *
 * @author Heitor Ivonica
 * @version 1.0
 */
@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * Service injetado para acesso às regras de negócio.
     */
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Busca um usuário pelo ID.
     *
     * @param id ID do usuário a ser buscado (passado na URL)
     * @return UserDTO com os dados do usuário
     * @throws RuntimeException se o usuário não for encontrado
     *
     * Exemplo de requisição: GET /user/1
     */
    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getType(),
                user.isBlocked()
        );
    }

    /**
     * Lista todos os usuários cadastrados no sistema.
     *
     * @return Lista de UserDTO com todos os usuários
     *
     * Exemplo de requisição: GET /user
     */
    @GetMapping
    public List<UserDTO> getAllUsers() {
        List<User> users = userService.findAll();
        return users.stream()
                .map(user -> new UserDTO(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getType(),
                        user.isBlocked()
                ))
                .collect(Collectors.toList());
    }

    /**
     * Cria um novo usuário no sistema.
     *
     * Regras de negócio:
     * - O e-mail é obrigatório e deve ser único
     * - O usuário inicia com blocked = false
     * - Os campos name, email e type são obrigatórios
     *
     * @param request DTO com os dados do usuário a ser criado
     * @return UserDTO com os dados do usuário criado (incluindo ID gerado)
     * @throws RuntimeException se o e-mail já estiver cadastrado
     *
     * Exemplo de requisição: POST /user
     * Body: { "name": "João Silva", "email": "joao@email.com", "type": "STUDENT" }
     */
    @PostMapping
    public UserDTO createUser(@RequestBody CreateUserRequest request) {
        User newUser = new User();
        newUser.setName(request.getName());
        newUser.setEmail(request.getEmail());
        newUser.setType(request.getType());
        newUser.setBlocked(false); // Usuário começa ativo

        User savedUser = userService.save(newUser);

        return new UserDTO(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getType(),
                savedUser.isBlocked()
        );
    }

    /**
     * Atualiza um usuário existente no sistema.
     *
     * Regras de negócio:
     * - Todos os campos são opcionais (atualização parcial)
     * - Se o e-mail for alterado, valida se já existe em outro usuário
     * - blocked só é atualizado se for enviado explicitamente
     * - O campo 'blocked' aceita valores: true (bloquear) ou false (desbloquear)
     *
     * @param id ID do usuário a ser atualizado (passado na URL)
     * @param request DTO com os campos a serem atualizados
     * @return UserDTO com os dados do usuário atualizado
     * @throws RuntimeException se o usuário não for encontrado
     * @throws RuntimeException se o novo e-mail já estiver em uso
     *
     * Exemplo de requisição: PUT /user/1
     * Body: { "name": "João Silva Atualizado" }
     */
    @PutMapping("/{id}")
    public UserDTO updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest request) {
        User existingUser = userService.findById(id);
        String oldEmail = existingUser.getEmail();
        String newEmail = request.getEmail();

        // Valida se o e-mail foi alterado e se já existe outro usuário com o mesmo e-mail
        if (newEmail != null && !newEmail.equals(oldEmail)) {
            if (userService.findByEmail(newEmail).isPresent()) {
                throw new RuntimeException("This email is already in use by another user");
            }
        }

        // Atualiza apenas os campos que vieram na requisição
        if (request.getName() != null) {
            existingUser.setName(request.getName());
        }
        if (request.getEmail() != null) {
            existingUser.setEmail(request.getEmail());
        }
        if (request.getType() != null) {
            existingUser.setType(request.getType());
        }
        if (request.getBlocked() != null) {
            existingUser.setBlocked(request.getBlocked());
        }

        User updatedUser = userService.update(existingUser);

        return new UserDTO(
                updatedUser.getId(),
                updatedUser.getName(),
                updatedUser.getEmail(),
                updatedUser.getType(),
                updatedUser.isBlocked()
        );
    }

    /**
     * Deleta um usuário do sistema.
     *
     * @param id ID do usuário a ser deletado (passado na URL)
     * @throws RuntimeException se o usuário não for encontrado
     *
     * Exemplo de requisição: DELETE /user/1
     */
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        userService.delete(id);
    }
}
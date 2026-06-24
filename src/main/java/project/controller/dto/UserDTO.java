package project.controller.dto;

import project.controller.Enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO (Data Transfer Object) para resposta de usuários.
 *
 * Utilizado para retornar os dados de um usuário nas requisições:
 * - GET /user/{id}
 * - GET /user
 * - POST /user
 * - PUT /user/{id}
 *
 * Contém todos os campos do usuário, incluindo bloqueio.
 *
 * @author Heitor Ivonica
 * @version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    /**
     * ID único do usuário (gerado automaticamente)
     */
    private Long id;

    /**
     * Nome do usuário
     */
    private String name;

    /**
     * E-mail do usuário (único)
     */
    private String email;

    /**
     * Tipo do usuário
     * Define as regras de multa e prazos
     */
    private UserType type;

    /**
     * Status de bloqueio
     * - true: usuário bloqueado (não pode pegar livros)
     * - false: usuário ativo
     *
     * Bloqueio automático após 30 dias de atraso
     */
    private boolean blocked;
}
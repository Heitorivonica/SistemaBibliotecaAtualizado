package project.controller.dto.Create;

import project.controller.Enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO (Data Transfer Object) para criar um novo usuário.
 *
 * Utilizado para receber os dados da requisição POST /user.
 * Contém apenas os campos necessários para a criação de um usuário.
 * O campo 'blocked' não está presente pois o sistema define como false por padrão.
 *
 * @author Heitor Ivonica
 * @version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {

    /**
     * Nome do usuário (obrigatório)
     * Exemplo: "João Silva"
     */
    private String name;

    /**
     * E-mail do usuário (obrigatório e único)
     * Exemplo: "joao@email.com"
     * Deve ser um e-mail válido e não pode ser duplicado
     */
    private String email;

    /**
     * Tipo do usuário (obrigatório)
     * Define as regras de multa e prazos
     *
     * Valores possíveis:
     * - VIP: R$ 0,50/dia
     * - SCIENTIST: R$ 1,50/dia
     * - SENIOR: R$ 2,00/dia
     * - TEACHER: R$ 2,50/dia
     * - STUDENT: R$ 3,50/dia
     * - CHILD: R$ 5,00/dia
     * - DAILY: R$ 7,75/dia
     */
    private UserType type;
}
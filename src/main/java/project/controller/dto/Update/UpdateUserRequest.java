package project.controller.dto.Update;

import project.controller.Enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO (Data Transfer Object) para atualizar um usuário existente.
 *
 * Utilizado para receber os dados da requisição PUT /user/{id}.
 * Todos os campos são opcionais, permitindo atualização parcial.
 * Campos não enviados mantêm seus valores originais.
 *
 * @author Heitor Ivonica
 * @version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {

    /**
     * Novo nome do usuário (opcional)
     * Se não enviado, mantém o nome atual
     */
    private String name;

    /**
     * Novo e-mail do usuário (opcional)
     * Se enviado, será validado para evitar duplicidade
     * Se não enviado, mantém o e-mail atual
     */
    private String email;

    /**
     * Novo tipo de usuário (opcional)
     * Se enviado, altera as regras de multa do usuário
     * Se não enviado, mantém o tipo atual
     */
    private UserType type;

    /**
     * Status de bloqueio do usuário (opcional)
     * - true: usuário bloqueado (não pode pegar livros)
     * - false: usuário ativo
     * - null: mantém o status atual
     *
     * O bloqueio é geralmente feito automaticamente pelo sistema
     * em caso de atraso excessivo (> 30 dias)
     */
    private Boolean blocked;
}
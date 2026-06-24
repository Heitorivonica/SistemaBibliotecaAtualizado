package project.controller.model;

import project.controller.Enums.UserType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidade que representa um usuário do sistema.
 *
 * Esta classe é mapeada para a tabela 'user' no banco de dados MySQL.
 * Utiliza JPA (Java Persistence API) para ORM (Object-Relational Mapping).
 *
 * Lombok é utilizado para gerar automaticamente getters e setters,
 * reduzindo o código boilerplate.
 *
 * @author Heitor Ivonica
 * @version 1.0
 */
@Getter
@Setter
@Entity
public class User {

    /**
     * ID único do usuário (chave primária).
     *
     * - @Id: indica que este campo é a chave primária
     * - @GeneratedValue: define que o ID será gerado automaticamente
     * - GenerationType.IDENTITY: o banco de dados gera o ID (auto-increment)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome do usuário.
     *
     * Obrigatório para cadastro.
     * Exemplo: "João Silva"
     */
    private String name;

    /**
     * E-mail do usuário.
     *
     * Obrigatório e único no sistema.
     * Utilizado para login e identificação.
     * Validação de duplicidade no UserService.
     * Exemplo: "joao@email.com"
     */
    private String email;

    /**
     * Tipo do usuário.
     *
     * Define as regras de negócio aplicadas ao usuário:
     * - Valor da multa por dia de atraso
     * - Pode ser usado para definir prazos diferenciados
     *
     * Enum armazenado como String no banco de dados.
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

    /**
     * Status de bloqueio do usuário.
     *
     * - true: usuário bloqueado (não pode realizar empréstimos)
     * - false: usuário ativo (pode realizar empréstimos)
     *
     * Gerenciado automaticamente pelo sistema:
     * - Inicia como false ao criar o usuário
     * - Muda para true quando usuário atrasa > 30 dias (LoanService.returnBook)
     */
    private boolean blocked;
}
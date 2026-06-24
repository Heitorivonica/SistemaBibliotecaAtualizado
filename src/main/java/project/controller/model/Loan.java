package project.controller.model;

import project.controller.Enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entidade que representa um empréstimo de livro no sistema.
 *
 * Esta classe é mapeada para a tabela 'loan' no banco de dados MySQL.
 * Utiliza JPA (Java Persistence API) para ORM (Object-Relational Mapping).
 * Possui relacionamentos ManyToOne com User e Book.
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
public class Loan {

    /**
     * ID único do empréstimo (chave primária).
     *
     * - @Id: indica que este campo é a chave primária
     * - @GeneratedValue: define que o ID será gerado automaticamente
     * - GenerationType.IDENTITY: o banco de dados gera o ID (auto-increment)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Livro associado ao empréstimo.
     *
     * - @ManyToOne: muitos empréstimos podem ter o mesmo livro
     *
     * O JPA gerencia a chave estrangeira automaticamente.
     * O livro fica indisponível durante o empréstimo.
     */
    @ManyToOne
    private Book book;

    /**
     * Usuário associado ao empréstimo.
     *
     * - @ManyToOne: muitos empréstimos podem ser do mesmo usuário
     *
     * O JPA gerencia a chave estrangeira automaticamente.
     * Usuários bloqueados não podem fazer empréstimos.
     */
    @ManyToOne
    private User user;

    /**
     * Data do empréstimo.
     *
     * Definida automaticamente no momento do empréstimo.
     * Utilizada como referência para calcular a data prevista.
     */
    private LocalDate loanDate;

    /**
     * Data prevista para devolução.
     *
     * Calculada como: loanDate + loanDays.
     * Utilizada para calcular dias de atraso na devolução.
     */
    private LocalDate expectedReturnDate;

    /**
     * Data real da devolução.
     *
     * - null: livro ainda não foi devolvido
     * - data: livro devolvido nesta data
     *
     * Utilizada para calcular dias de atraso.
     */
    private LocalDate actualReturnDate;

    /**
     * Status do empréstimo.
     *
     * - ACTIVE: empréstimo ativo (ainda não devolvido)
     * - RETURNED: devolvido no prazo
     * - OVERDUE: devolvido com atraso
     *
     * Atualizado automaticamente no método returnBook.
     */
    private Status status;

    /**
     * Valor da multa calculada.
     *
     * - 0: sem multa
     * - valor: multa por atraso
     *
     * Calculado com as regras:
     * - 3 dias de cortesia (não cobra nos primeiros 3 dias)
     * - Limite máximo de R$ 100,00
     * - Arredondamento com HALF_UP (2 casas decimais)
     */
    private BigDecimal fine;
}
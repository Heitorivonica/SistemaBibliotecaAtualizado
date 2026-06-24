package project.controller.Enums;

import java.math.BigDecimal;

// Enum que define os tipos de usuário da biblioteca
// Cada tipo tem um valor de multa por dia de atraso
public enum UserType {

    // ========== CONSTANTES DO ENUM ==========
    VIP(new BigDecimal("0.50")),
    SCIENTIST(new BigDecimal("1.50")),
    SENIOR(new BigDecimal("2.00")),
    TEACHER(new BigDecimal("2.50")),
    STUDENT(new BigDecimal("3.50")),
    CHILD(new BigDecimal("5.00")),
    DAILY(new BigDecimal("7.75"));




    // ========== ATRIBUTO ==========

    private BigDecimal multa;  // Valor da multa por dia para este tipo de usuário

    // ========== CONSTRUTOR ==========
    // Cada constante do enum recebe um valor de multa
    UserType(BigDecimal multa) {
        this.multa = multa;
    }

    // ========== GETTER ==========
    // Retorna o valor da multa por dia
    public BigDecimal getMulta() {
        return multa;
    }
}
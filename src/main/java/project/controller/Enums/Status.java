package project.controller.Enums;

// Enum que define os possíveis status de um empréstimo
public enum Status {

    // ========== CONSTANTES DO ENUM ==========

    ACTIVE,      // Empréstimo ativo (livro não foi devolvido ainda)
    RETURNED, // Livro foi devolvido no prazo ou antes
    OVERDUE;   // Livro foi devolvido depois do prazo (gerou multa)
}
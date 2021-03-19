package edu.austral.ingsis.statement;

public interface Statement {
    void accept(StatementVisitor statementVisitor);
}

package edu.austral.ingsis.statement;

import edu.austral.ingsis.visitor.StatementVisitor;

public interface Statement {
    void accept(StatementVisitor statementVisitor);
}

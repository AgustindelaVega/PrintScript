package edu.austral.ingsis.statement.impl;

import edu.austral.ingsis.statement.Statement;
import edu.austral.ingsis.visitor.StatementVisitor;
import java.util.List;

public class BlockStatement implements Statement {

  private List<Statement> statements;

  public BlockStatement(List<Statement> statements) {
    this.statements = statements;
  }

  @Override
  public void accept(StatementVisitor statementVisitor) {
    statementVisitor.visit(this);
  }

  public List<Statement> getStatements() {
    return statements;
  }
}

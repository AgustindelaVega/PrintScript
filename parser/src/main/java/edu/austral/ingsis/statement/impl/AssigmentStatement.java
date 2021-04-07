package edu.austral.ingsis.statement.impl;

import edu.austral.ingsis.expression.Expression;
import edu.austral.ingsis.statement.Statement;
import edu.austral.ingsis.visitor.StatementVisitor;

public class AssigmentStatement implements Statement {

  private Expression expression;

  public AssigmentStatement(Expression expression) {
    this.expression = expression;
  }

  @Override
  public void accept(StatementVisitor statementVisitor) {
    statementVisitor.visit(this);
  }

  public Expression getExpression() {
    return expression;
  }
}

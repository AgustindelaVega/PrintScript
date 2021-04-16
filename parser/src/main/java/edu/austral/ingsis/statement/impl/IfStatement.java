package edu.austral.ingsis.statement.impl;

import edu.austral.ingsis.expression.Expression;
import edu.austral.ingsis.statement.Statement;
import edu.austral.ingsis.visitor.StatementVisitor;

public class IfStatement implements Statement {

  private Expression condition;
  private Statement thenBranching, elseBranching;

  public IfStatement(Expression condition, Statement thenBranching, Statement elseBranching) {
    this.condition = condition;
    this.thenBranching = thenBranching;
    this.elseBranching = elseBranching;
  }

  @Override
  public void accept(StatementVisitor statementVisitor) {
    statementVisitor.visit(this);
  }

  public Expression getCondition() {
    return condition;
  }

  public Statement getThenBranching() {
    return thenBranching;
  }

  public Statement getElseBranching() {
    return elseBranching;
  }
}

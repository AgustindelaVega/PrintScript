package edu.austral.ingsis.expression.impl;

import edu.austral.ingsis.expression.Expression;
import edu.austral.ingsis.token.Token;
import edu.austral.ingsis.visitor.ExpressionVisitor;

public class VariableExpression implements Expression {

  private Token name;

  public VariableExpression(Token name) {
    this.name = name;
  }

  @Override
  public Object accept(ExpressionVisitor expressionVisitor) {
    return expressionVisitor.visit(this);
  }

  public Token getToken() {
    return name;
  }
}

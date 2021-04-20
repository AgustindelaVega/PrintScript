package edu.austral.ingsis.expression.impl;

import edu.austral.ingsis.expression.Expression;
import edu.austral.ingsis.token.Token;
import edu.austral.ingsis.visitor.ExpressionVisitor;

public class AssigmentExpression implements Expression {

  private Token name;
  private Expression expression;

  public AssigmentExpression(Token name, Expression expression) {
    this.name = name;
    this.expression = expression;
  }

  @Override
  public Object accept(ExpressionVisitor expressionVisitor) {
    return expressionVisitor.visit(this);
  }

  @Override
  public Token getToken() {
    return name;
  }

  public Expression getExpression() {
    return expression;
  }
}

package edu.austral.ingsis.expression.impl;

import edu.austral.ingsis.expression.Expression;
import edu.austral.ingsis.token.Token;
import edu.austral.ingsis.visitor.ExpressionVisitor;

public class UnaryExpression implements Expression {

  private Token operator;
  private Expression expression;

  public UnaryExpression(Token operator, Expression expression) {
    this.operator = operator;
    this.expression = expression;
  }

  @Override
  public Object accept(ExpressionVisitor expressionVisitor) {
    return expressionVisitor.visit(this);
  }

  @Override
  public Token getToken() {
    return operator;
  }

  public Expression getExpression() {
    return expression;
  }
}

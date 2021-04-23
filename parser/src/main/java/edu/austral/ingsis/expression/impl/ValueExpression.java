package edu.austral.ingsis.expression.impl;

import edu.austral.ingsis.expression.Expression;
import edu.austral.ingsis.token.Token;
import edu.austral.ingsis.visitor.ExpressionVisitor;

public class ValueExpression implements Expression {

  private Object value;

  private Token tokenValue;

  public ValueExpression(Object value, Token tokenValue) {
    this.value = value;
    this.tokenValue = tokenValue;
  }

  @Override
  public Object accept(ExpressionVisitor expressionVisitor) {
    return expressionVisitor.visit(this);
  }

  @Override
  public Token getToken() {
    return tokenValue;
  }

  public Object getValue() {
    return value;
  }
}

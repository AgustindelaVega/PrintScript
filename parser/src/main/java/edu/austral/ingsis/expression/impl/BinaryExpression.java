package edu.austral.ingsis.expression.impl;

import edu.austral.ingsis.expression.Expression;
import edu.austral.ingsis.token.Token;
import edu.austral.ingsis.visitor.ExpressionVisitor;

public class BinaryExpression implements Expression {

  private Expression left, right;
  private Token operator;

  public BinaryExpression(Expression left, Expression right, Token operator) {
    this.left = left;
    this.right = right;
    this.operator = operator;
  }

  @Override
  public Object accept(ExpressionVisitor expressionVisitor) {
    return expressionVisitor.visit(this);
  }

  public Expression getLeft() {
    return left;
  }

  public Expression getRight() {
    return right;
  }

  public Token getOperator() {
    return operator;
  }
}

package edu.austral.ingsis.expression;

import edu.austral.ingsis.visitor.ExpressionVisitor;

public interface Expression {
  Object accept(ExpressionVisitor expressionVisitor);
}

package edu.austral.ingsis.expression;

import edu.austral.ingsis.token.Token;
import edu.austral.ingsis.visitor.ExpressionVisitor;

public interface Expression {
  Object accept(ExpressionVisitor expressionVisitor);

  Token getToken();
}

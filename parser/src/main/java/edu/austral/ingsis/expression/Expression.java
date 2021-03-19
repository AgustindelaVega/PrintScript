package edu.austral.ingsis.expression;

public interface Expression {
    Object accept(ExpressionVisitor expressionVisitor);
}

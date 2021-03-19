package edu.austral.ingsis.expression.impl;

import edu.austral.ingsis.expression.Expression;
import edu.austral.ingsis.visitor.ExpressionVisitor;

public class ValueExpression implements Expression {

    private Object value;

    public ValueExpression(Object value) {
        this.value = value;
    }

    @Override
    public Object accept(ExpressionVisitor expressionVisitor) {
        return expressionVisitor.visit(this);
    }

    public Object getValue() {
        return value;
    }
}

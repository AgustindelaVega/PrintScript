package edu.austral.ingsis.visitor;

import edu.austral.ingsis.expression.impl.BinaryExpression;
import edu.austral.ingsis.expression.impl.ValueExpression;
import edu.austral.ingsis.expression.impl.VariableExpression;

public interface ExpressionVisitor {

    Object visit(ValueExpression valueExpression);
    Object visit(VariableExpression variableExpression);
    Object visit(BinaryExpression binaryExpression);
}

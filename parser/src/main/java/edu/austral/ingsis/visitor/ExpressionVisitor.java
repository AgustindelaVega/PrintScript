package edu.austral.ingsis.visitor;

import edu.austral.ingsis.expression.impl.*;

public interface ExpressionVisitor {

    Object visit(ValueExpression valueExpression);
    Object visit(VariableExpression variableExpression);
    Object visit(BinaryExpression binaryExpression);
    Object visit(AssigmentExpression assigmentExpression);
    Object visit(UnaryExpression unaryExpression);
}

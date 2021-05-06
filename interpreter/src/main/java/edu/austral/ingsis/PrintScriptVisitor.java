package edu.austral.ingsis;

import static edu.austral.ingsis.token.TokenType.*;

import edu.austral.ingsis.exceptions.InterpreterException;
import edu.austral.ingsis.expression.Expression;
import edu.austral.ingsis.expression.impl.*;
import edu.austral.ingsis.runtime.PrintScriptRuntimeState;
import edu.austral.ingsis.runtime.RuntimeState;
import edu.austral.ingsis.statement.Statement;
import edu.austral.ingsis.statement.impl.*;
import edu.austral.ingsis.token.Token;
import edu.austral.ingsis.visitor.ExpressionVisitor;
import edu.austral.ingsis.visitor.StatementVisitor;
import java.util.List;
import java.util.function.Consumer;

public class PrintScriptVisitor implements ExpressionVisitor, StatementVisitor {

  private RuntimeState runtimeState = new PrintScriptRuntimeState();
  private Consumer<String> printConsumer;

  public RuntimeState getRuntimeState() {
    return runtimeState;
  }

  public void setPrintConsumer(Consumer<String> printConsumer) {
    this.printConsumer = printConsumer;
  }

  @Override
  public Object visit(BinaryExpression binaryExpression) {
    Object left = evaluate(binaryExpression.getLeft());
    Object right = evaluate(binaryExpression.getRight());

    switch (binaryExpression.getToken().getType()) {
      case MINUS:
        checkNumberOperands(binaryExpression.getToken(), left, right);
        return (double) left - (double) right;
      case PLUS:
        if (left instanceof Number && right instanceof Number) {
          return (double) left + (double) right;
        }
        return doubleToString(left, right);
      case DIVIDE:
        checkNumberOperands(binaryExpression.getToken(), left, right);
        return (double) left / (double) right;
      case MULTIPLY:
        checkNumberOperands(binaryExpression.getToken(), left, right);
        return (double) left * (double) right;
      case GREATER:
        checkNumberOperands(binaryExpression.getToken(), left, right);
        return (double) left > (double) right;
      case GREATEREQUAL:
        checkNumberOperands(binaryExpression.getToken(), left, right);
        return (double) left >= (double) right;
      case LESS:
        checkNumberOperands(binaryExpression.getToken(), left, right);
        return (double) left < (double) right;
      case LESSEQUAL:
        checkNumberOperands(binaryExpression.getToken(), left, right);
        return (double) left <= (double) right;
    }
    return null;
  }

  private Object doubleToString(Object left, Object right) {
    Integer leftIntValue = null;
    Integer rightIntValue = null;
    if (left instanceof Double && ((Double) left % 1 == 0)) {
      leftIntValue = ((Double) left).intValue();
    }
    if (right instanceof Double && ((Double) right % 1 == 0)) {
      rightIntValue = ((Double) right).intValue();
    }
    return (leftIntValue != null ? leftIntValue.toString() : left.toString())
        + (rightIntValue != null ? rightIntValue.toString() : right.toString());
  }

  @Override
  public Object visit(UnaryExpression unaryExpression) {
    Object right = evaluate(unaryExpression.getExpression());

    if (unaryExpression.getToken().getType() == MINUS) {
      checkNumberOperand(unaryExpression.getToken(), right);
      return -(double) right;
    }
    return null;
  }

  @Override
  public Object visit(VariableExpression variableExpression) {
    return runtimeState.getValue(variableExpression.getToken());
  }

  @Override
  public Object visit(AssigmentExpression assignmentExpression) {
    Object value = evaluate(assignmentExpression.getExpression());

    runtimeState.assign(assignmentExpression.getToken(), value);
    return value;
  }

  @Override
  public Object visit(ValueExpression valueExpression) {
    return valueExpression.getValue();
  }

  @Override
  public void visit(PrintStatement printStatement) {
    Object value = evaluate(printStatement.getExpression());
    Integer intValue = null;
    if (value instanceof Double && ((Double) value % 1 == 0)) {
      intValue = ((Double) value).intValue();
    }
    String printValue = intValue != null ? intValue.toString() : value.toString();
    if (printConsumer != null) printConsumer.accept(printValue);
    else System.out.println(printValue);
  }

  @Override
  public void visit(AssigmentStatement assigmentStatement) {
    assigmentStatement.getExpression().accept(this);
  }

  @Override
  public void visit(DeclarationStatement declarationStatement) {
    Object value = null;

    if (declarationStatement.getExpression() != null) {
      value = evaluate(declarationStatement.getExpression());
    }

    if (value == null) {
      runtimeState.addValue(
          declarationStatement.getName().getLexeme(),
          declarationStatement.getType(),
          null,
          declarationStatement.getKeyword());
      return;
    }

    if (declarationStatement.getType() == BOOLEAN && !(value instanceof Boolean)) {
      throw new InterpreterException(declarationStatement.getName(), "Expected a boolean");
    } else if (declarationStatement.getType() == NUMBERTYPE && !(value instanceof Double)) {
      throw new InterpreterException(declarationStatement.getName(), "Expected a number");
    } else if (declarationStatement.getType() == STRINGTYPE && !(value instanceof String)) {
      throw new InterpreterException(declarationStatement.getName(), "Expected a string");
    }
    runtimeState.addValue(
        declarationStatement.getName().getLexeme(),
        declarationStatement.getType(),
        value,
        declarationStatement.getKeyword());
  }

  @Override
  public void visit(IfStatement ifStatement) {
    if (isTrue(evaluate(ifStatement.getCondition()), ifStatement.getCondition().getToken())) {
      ifStatement.getThenBranching().accept(this);
    } else if (ifStatement.getElseBranching() != null) {
      ifStatement.getElseBranching().accept(this);
    }
  }

  @Override
  public void visit(BlockStatement blockStatement) {
    executeBlock(blockStatement.getStatements());
  }

  private void executeBlock(List<Statement> block) {
    RuntimeState previous = runtimeState;

    block.forEach(statement -> statement.accept(this));

    runtimeState = previous;
  }

  private void checkNumberOperands(Token operator, Object left, Object right) {
    if (left instanceof Double && right instanceof Double) return;
    throw new InterpreterException(operator, "Operands must be numbers");
  }

  private void checkNumberOperand(Token operator, Object object) {
    if (object instanceof Double) return;
    throw new InterpreterException(operator, "Operand must be a number");
  }

  private Object evaluate(Expression expression) {
    return expression.accept(this);
  }

  private boolean isTrue(Object object, Token token) {
    if (object == null) throw new InterpreterException(token, "Expression Expected");
    if (object instanceof Boolean) return (boolean) object;
    throw new InterpreterException(token, "Expression Expected");
  }
}

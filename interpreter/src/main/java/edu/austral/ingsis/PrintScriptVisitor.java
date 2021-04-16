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

    switch (binaryExpression.getOperator().getType()) {
      case MINUS:
        checkNumberOperands(binaryExpression.getOperator(), left, right);
        return (double) left - (double) right;
      case PLUS:
        if (left instanceof Number && right instanceof Number) {
          return (double) left + (double) right;
        }
        return left.toString() + right.toString();
      case DIVIDE:
        checkNumberOperands(binaryExpression.getOperator(), left, right);
        return (double) left / (double) right;
      case MULTIPLY:
        checkNumberOperands(binaryExpression.getOperator(), left, right);
        return (double) left * (double) right;
      case GREATER:
        checkNumberOperands(binaryExpression.getOperator(), left, right);
        return (double) left > (double) right;
      case GREATEREQUAL:
        checkNumberOperands(binaryExpression.getOperator(), left, right);
        return (double) left >= (double) right;
      case LESS:
        checkNumberOperands(binaryExpression.getOperator(), left, right);
        return (double) left < (double) right;
      case LESSEQUAL:
        checkNumberOperands(binaryExpression.getOperator(), left, right);
        return (double) left <= (double) right;
    }
    return null;
  }

  @Override
  public Object visit(UnaryExpression unaryExpression) {
    Object right = evaluate(unaryExpression.getExpression());

    if (unaryExpression.getOperator().getType() == MINUS) {
      checkNumberOperand(unaryExpression.getOperator(), right);
      return -(double) right;
    }
    return null;
  }

  @Override
  public Object visit(VariableExpression variableExpression) {
    return runtimeState.getValue(variableExpression.getName());
  }

  @Override
  public Object visit(AssigmentExpression assignmentExpression) {
    Object value = evaluate(assignmentExpression.getExpression());

    runtimeState.assign(assignmentExpression.getName(), value);
    return value;
  }

  @Override
  public Object visit(ValueExpression valueExpression) {
    return valueExpression.getValue();
  }

  @Override
  public void visit(PrintStatement printStatement) {
    Object value = evaluate(printStatement.getExpression());
    if (printConsumer != null) printConsumer.accept(value.toString());
    else System.out.println(value);
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
    }
    // String name, TokenType type, Object value, TokenType keyWord

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
    if (isTrue(evaluate(ifStatement.getCondition()))) {
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

  private boolean isTrue(Object object) {
    if (object == null) return false;
    if (object instanceof Boolean) return (boolean) object;
    return true;
  }
}

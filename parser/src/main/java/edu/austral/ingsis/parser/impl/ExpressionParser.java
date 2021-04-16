package edu.austral.ingsis.parser.impl;

import static edu.austral.ingsis.token.TokenType.*;

import edu.austral.ingsis.exceptions.ParseException;
import edu.austral.ingsis.expression.Expression;
import edu.austral.ingsis.expression.impl.*;
import edu.austral.ingsis.parser.streams.PrintScriptTokenStream;
import edu.austral.ingsis.token.Token;

public class ExpressionParser {

  private PrintScriptTokenStream tokenStream;

  public Expression parse(PrintScriptTokenStream tokenStream) {
    this.tokenStream = tokenStream;
    return assignment();
  }

  private Expression assignment() {
    Expression expression = binary();

    if (tokenStream.match(ASSIGNATION)) {
      Token token = tokenStream.previous();
      Expression value = binary();

      if (expression instanceof VariableExpression) {
        Token name = ((VariableExpression) expression).getName();
        return new AssigmentExpression(name, value);
      }

      throw new ParseException("Assigment parse error.", token);
    }

    return expression;
  }

  private Expression binary() {
    Expression left = unary();

    if (tokenStream.match(MINUS, PLUS, MULTIPLY, DIVIDE, GREATER, GREATEREQUAL, LESS, LESSEQUAL)) {
      Token operator = tokenStream.previous();
      Expression right = unary();
      left = new BinaryExpression(left, right, operator);
    }

    return left;
  }

  private Expression unary() {
    if (tokenStream.match(MINUS)) {
      Token operator = tokenStream.previous();
      Expression expression = unary();
      return new UnaryExpression(operator, expression);
    }

    return primary();
  }

  private Expression primary() {
    if (tokenStream.match(FALSE)) return new ValueExpression(false);
    if (tokenStream.match(TRUE)) return new ValueExpression(true);

    if (tokenStream.match(NUMBER, STRING)) {
      return new ValueExpression(tokenStream.previous().getLiteral());
    }

    if (tokenStream.match(IDENTIFIER)) {
      return new VariableExpression(tokenStream.previous());
    }

    throw new ParseException("Expression expected.", tokenStream.peek());
  }
}

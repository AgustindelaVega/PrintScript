package edu.austral.ingsis.parser.impl;

import static edu.austral.ingsis.token.TokenType.*;

import edu.austral.ingsis.exceptions.ParseException;
import edu.austral.ingsis.expression.Expression;
import edu.austral.ingsis.expression.impl.*;
import edu.austral.ingsis.parser.streams.ParseHelper;
import edu.austral.ingsis.token.Token;

public class PrintScriptExpressionParser implements ExpressionParser {

  private ParseHelper parseHelper;

  @Override
  public Expression parse(ParseHelper parseHelper) {
    this.parseHelper = parseHelper;
    return assignment();
  }

  private Expression assignment() {
    Expression expression = binary();

    if (parseHelper.match(ASSIGNATION)) {
      Token token = parseHelper.previous();
      Expression value = binary();

      if (expression instanceof VariableExpression) {
        Token name = ((VariableExpression) expression).getToken();
        return new AssigmentExpression(name, value);
      }

      throw new ParseException(token, "Assigment parse error.");
    }

    return expression;
  }

  private Expression binary() {
    Expression left = unary();

    while (parseHelper.match(
        MINUS, PLUS, MULTIPLY, DIVIDE, GREATER, GREATEREQUAL, LESS, LESSEQUAL)) {
      Token operator = parseHelper.previous();
      Expression right = unary();
      left = new BinaryExpression(left, right, operator);
    }

    return left;
  }

  private Expression unary() {
    if (parseHelper.match(MINUS)) {
      Token operator = parseHelper.previous();
      Expression expression = unary();
      return new UnaryExpression(operator, expression);
    }

    return primary();
  }

  private Expression primary() {
    if (parseHelper.match(FALSE)) return new ValueExpression(false, parseHelper.previous());
    if (parseHelper.match(TRUE)) return new ValueExpression(true, parseHelper.previous());

    if (parseHelper.match(NUMBER, STRING)) {
      return new ValueExpression(parseHelper.previous().getLiteral(), parseHelper.previous());
    }

    if (parseHelper.match(IDENTIFIER)) {
      return new VariableExpression(parseHelper.previous());
    }

    throw new ParseException(parseHelper.peek(), "Expression expected.");
  }
}

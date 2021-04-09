package edu.austral.ingsis.parser.impl;

import static edu.austral.ingsis.token.TokenType.*;

import edu.austral.ingsis.exceptions.ParseException;
import edu.austral.ingsis.expression.Expression;
import edu.austral.ingsis.expression.impl.*;
import edu.austral.ingsis.parser.Parser;
import edu.austral.ingsis.statement.Statement;
import edu.austral.ingsis.statement.impl.AssigmentStatement;
import edu.austral.ingsis.statement.impl.DeclarationStatement;
import edu.austral.ingsis.statement.impl.PrintStatement;
import edu.austral.ingsis.token.Token;
import edu.austral.ingsis.token.TokenType;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PrintScriptParser implements Parser {

  private List<Token> tokens;
  private int current = 0;

  @Override
  public List<Statement> parse(List<Token> tokens) {
    List<Statement> statements = new ArrayList<>();
    this.tokens = tokens;
    this.tokens =
        this.tokens.stream()
            .filter(
                token ->
                    (token.getType() != LEFTPARENTHESIS) && (token.getType() != RIGHTPARENTHESIS))
            .collect(Collectors.toList());

    while (!isAtEnd()) {
      statements.add(initParse());
    }

    return statements;
  }

  private Statement initParse() {
    if (match(LET)) return declarationStatement(previous());
    if (match(PRINT)) return printStatement();
    return assignationStatement();
  }

  private Statement declarationStatement(Token keyword) {
    Token name = consume(IDENTIFIER, "Variable name missing.");
    TokenType type = null;
    Expression expression = null;

    if (match(COLON)) {
      if (match(STRINGTYPE)) {
        type = STRINGTYPE;
      } else if (match(NUMBERTYPE)) {
        type = NUMBERTYPE;
      }
    } else {
      throw new ParseException("Variable type missing.", previous());
    }

    if (match(ASSIGNATION)) {
      expression = assignment();
    }

    consume(SEMICOLON, "';' after variable declaration missing.");
    return new DeclarationStatement(keyword, name, type, expression);
  }

  private Statement printStatement() {
    Expression expression = assignment();
    consume(SEMICOLON, "';' after variable declaration missing.");
    return new PrintStatement(expression);
  }

  private Statement assignationStatement() {
    Expression expression = assignment();
    consume(SEMICOLON, "';' after variable declaration missing.");
    return new AssigmentStatement(expression);
  }

  private Expression assignment() {
    Expression expression = binary();

    if (match(ASSIGNATION)) {
      Token token = previous();
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

    if (match(MINUS, PLUS, MULTIPLY, DIVIDE)) {
      Token operator = previous();
      Expression right = unary();
      left = new BinaryExpression(left, right, operator);
    }

    return left;
  }

  private Expression unary() {
    if (match(MINUS)) {
      Token operator = previous();
      Expression expression = unary();
      return new UnaryExpression(operator, expression);
    }

    return primary();
  }

  private Expression primary() {
    if (match(NUMBER, STRING)) {
      return new ValueExpression(previous().getLiteral());
    }

    if (match(IDENTIFIER)) {
      return new VariableExpression(previous());
    }

    throw new ParseException("Expression expected.", peek());
  }

  private boolean match(TokenType... types) {
    for (TokenType type : types) {
      if (check(type)) {
        advance();
        return true;
      }
    }

    return false;
  }

  private boolean check(TokenType type) {
    if (isAtEnd()) return false;
    return peek().getType() == type;
  }

  private Token advance() {
    if (!isAtEnd()) current++;
    return previous();
  }

  private Token consume(TokenType type, String message) {
    if (check(type)) return advance();

    throw new ParseException(message, peek());
  }

  private Token peek() {
    return tokens.get(current);
  }

  private Token previous() {
    return tokens.get(current - 1);
  }

  private boolean isAtEnd() {
    return peek().getType() == EOF;
  }
}

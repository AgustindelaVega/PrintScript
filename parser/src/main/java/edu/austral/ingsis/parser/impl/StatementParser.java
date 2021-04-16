package edu.austral.ingsis.parser.impl;

import static edu.austral.ingsis.token.TokenType.*;
import static edu.austral.ingsis.token.TokenType.SEMICOLON;

import edu.austral.ingsis.exceptions.ParseException;
import edu.austral.ingsis.expression.Expression;
import edu.austral.ingsis.parser.streams.PrintScriptTokenStream;
import edu.austral.ingsis.statement.Statement;
import edu.austral.ingsis.statement.impl.*;
import edu.austral.ingsis.token.Token;
import edu.austral.ingsis.token.TokenType;
import java.util.ArrayList;
import java.util.List;

public class StatementParser {

  private final ExpressionParser expressionParser;
  private PrintScriptTokenStream tokenStream;

  public StatementParser() {
    this.expressionParser = new ExpressionParser();
  }

  public Statement parse(PrintScriptTokenStream tokenStream) {
    this.tokenStream = tokenStream;

    return statement();
  }

  private Statement statement() {
    if (tokenStream.match(LET, CONST)) return declarationStatement();
    if (tokenStream.match(IF)) return ifStatement();
    if (tokenStream.match(PRINT)) return printStatement();
    if (tokenStream.match(LEFTBRACE)) return new BlockStatement(blockStatement());
    return assignationStatement();
  }

  private Statement declarationStatement() {
    Token keyword = tokenStream.previous();
    Token name = tokenStream.consume(IDENTIFIER, "Variable name missing.");
    TokenType type = null;
    Expression expression = null;

    if (tokenStream.match(COLON)) {
      if (tokenStream.match(STRINGTYPE)) {
        type = STRINGTYPE;
      } else if (tokenStream.match(NUMBERTYPE)) {
        type = NUMBERTYPE;
      } else if (tokenStream.match(BOOLEAN)) {
        type = BOOLEAN;
      }
    } else {
      throw new ParseException("Variable type missing.", tokenStream.previous());
    }

    if (tokenStream.match(ASSIGNATION)) {
      expression = expressionParser.parse(tokenStream);
    }

    tokenStream.consume(SEMICOLON, "';' after variable declaration missing.");
    return new DeclarationStatement(keyword, name, type, expression);
  }

  private Statement printStatement() {
    tokenStream.consume(LEFTPARENTHESIS, "Expect '(' after 'if'.");
    Expression expression = expressionParser.parse(tokenStream);
    tokenStream.consume(RIGHTPARENTHESIS, "Expect ')' after 'if'.");

    tokenStream.consume(SEMICOLON, "';' after variable declaration missing.");
    return new PrintStatement(expression);
  }

  private Statement assignationStatement() {
    Expression expression = expressionParser.parse(tokenStream);
    tokenStream.consume(SEMICOLON, "';' after variable declaration missing.");
    return new AssigmentStatement(expression);
  }

  private Statement ifStatement() {
    tokenStream.consume(LEFTPARENTHESIS, "Expect '(' after 'if'.");
    Expression condition = expressionParser.parse(tokenStream);
    tokenStream.consume(RIGHTPARENTHESIS, "Expect ')' after 'if'.");

    Statement thenBranching = statement();
    Statement elseBranching = null;
    if (tokenStream.match(ELSE)) {
      elseBranching = statement();
    }

    return new IfStatement(condition, thenBranching, elseBranching);
  }

  private List<Statement> blockStatement() {
    List<Statement> statements = new ArrayList<>();

    while (!tokenStream.check(RIGHTBRACE) && !tokenStream.isAtEnd()) {
      statements.add(statement());
    }

    tokenStream.consume(RIGHTBRACE, "Expect '}' after block end.");
    return statements;
  }
}

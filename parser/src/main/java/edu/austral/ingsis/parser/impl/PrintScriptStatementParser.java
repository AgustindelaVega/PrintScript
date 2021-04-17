package edu.austral.ingsis.parser.impl;

import static edu.austral.ingsis.token.TokenType.*;
import static edu.austral.ingsis.token.TokenType.SEMICOLON;

import edu.austral.ingsis.exceptions.ParseException;
import edu.austral.ingsis.expression.Expression;
import edu.austral.ingsis.parser.streams.ParseHelper;
import edu.austral.ingsis.statement.Statement;
import edu.austral.ingsis.statement.impl.*;
import edu.austral.ingsis.token.Token;
import edu.austral.ingsis.token.TokenType;
import java.util.ArrayList;
import java.util.List;

public class PrintScriptStatementParser implements StatementParser {

  private final ExpressionParser expressionParser;
  private ParseHelper parseHelper;

  public PrintScriptStatementParser() {
    this.expressionParser = new PrintScriptExpressionParser();
  }

  public Statement parse(ParseHelper parseHelper) {
    this.parseHelper = parseHelper;

    return statement();
  }

  private Statement statement() {
    if (parseHelper.match(LET, CONST)) return declarationStatement();
    if (parseHelper.match(IF)) return ifStatement();
    if (parseHelper.match(PRINT)) return printStatement();
    if (parseHelper.match(LEFTBRACE)) return new BlockStatement(blockStatement());
    return assignationStatement();
  }

  private Statement declarationStatement() {
    Token keyword = parseHelper.previous();
    Token name = parseHelper.consume(IDENTIFIER, "Variable name missing.");
    TokenType type = null;
    Expression expression = null;

    if (parseHelper.match(COLON)) {
      if (parseHelper.match(STRINGTYPE)) {
        type = STRINGTYPE;
      } else if (parseHelper.match(NUMBERTYPE)) {
        type = NUMBERTYPE;
      } else if (parseHelper.match(BOOLEAN)) {
        type = BOOLEAN;
      }
    } else {
      throw new ParseException("Variable type missing.", parseHelper.previous());
    }

    if (parseHelper.match(ASSIGNATION)) {
      expression = expressionParser.parse(parseHelper);
    }

    parseHelper.consume(SEMICOLON, "';' after variable declaration missing.");
    return new DeclarationStatement(keyword, name, type, expression);
  }

  private Statement printStatement() {
    parseHelper.consume(LEFTPARENTHESIS, "Expect '(' after 'if'.");
    Expression expression = expressionParser.parse(parseHelper);
    parseHelper.consume(RIGHTPARENTHESIS, "Expect ')' after 'if'.");

    parseHelper.consume(SEMICOLON, "';' after variable declaration missing.");
    return new PrintStatement(expression);
  }

  private Statement assignationStatement() {
    Expression expression = expressionParser.parse(parseHelper);
    parseHelper.consume(SEMICOLON, "';' after variable declaration missing.");
    return new AssigmentStatement(expression);
  }

  private Statement ifStatement() {
    parseHelper.consume(LEFTPARENTHESIS, "Expect '(' after 'if'.");
    Expression condition = expressionParser.parse(parseHelper);
    parseHelper.consume(RIGHTPARENTHESIS, "Expect ')' after 'if'.");

    Statement thenBranching = statement();
    Statement elseBranching = null;
    if (parseHelper.match(ELSE)) {
      elseBranching = statement();
    }

    return new IfStatement(condition, thenBranching, elseBranching);
  }

  private List<Statement> blockStatement() {
    List<Statement> statements = new ArrayList<>();

    while (!parseHelper.check(RIGHTBRACE) && !parseHelper.isAtEnd()) {
      statements.add(statement());
    }

    parseHelper.consume(RIGHTBRACE, "Expect '}' after block end.");
    return statements;
  }
}

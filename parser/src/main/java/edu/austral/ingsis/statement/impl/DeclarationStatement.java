package edu.austral.ingsis.statement.impl;

import edu.austral.ingsis.expression.Expression;
import edu.austral.ingsis.statement.Statement;
import edu.austral.ingsis.token.Token;
import edu.austral.ingsis.token.TokenType;
import edu.austral.ingsis.visitor.StatementVisitor;

public class DeclarationStatement implements Statement {

  private Token keyword;
  private Token name;
  private TokenType type;
  private Expression expression;

  public DeclarationStatement(Token keyword, Token name, TokenType type, Expression expression) {
    this.keyword = keyword;
    this.name = name;
    this.type = type;
    this.expression = expression;
  }

  @Override
  public void accept(StatementVisitor statementVisitor) {
    statementVisitor.visit(this);
  }

  public Token getKeyword() {
    return keyword;
  }

  public Token getName() {
    return name;
  }

  public TokenType getType() {
    return type;
  }

  public Expression getExpression() {
    return expression;
  }
}

package edu.austral.ingsis.token;

public class TokenBuilder {
  private TokenType type;
  private Integer line;
  private Integer column;
  private String lexeme;
  private Object literal;

  public TokenBuilder addType(TokenType type) {
    this.type = type;
    return this;
  }

  public TokenBuilder addLine(Integer line) {
    this.line = line;
    return this;
  }

  public TokenBuilder addLexeme(String lexeme) {
    this.lexeme = lexeme;
    return this;
  }

  public TokenBuilder addLiteral(Object literal) {
    this.literal = literal;
    return this;
  }

  public TokenBuilder addColumn(Integer column) {
    this.column = column;
    return this;
  }

  public Token buildToken() {
    return new PrintScriptToken(type, lexeme, literal, line, column);
  }

  public static TokenBuilder createBuilder() {
    return new TokenBuilder();
  }
}

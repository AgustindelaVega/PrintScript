package edu.austral.ingsis.token;

public class PrintScriptToken implements Token {

  private final TokenType type;
  private final String lexeme;
  private final Object literal;
  private final int line;

  public PrintScriptToken(TokenType type, String lexeme, Object literal, int line) {
    this.type = type;
    this.lexeme = lexeme;
    this.literal = literal;
    this.line = line;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof PrintScriptToken)) {
      return false;
    }

    PrintScriptToken token = (PrintScriptToken) obj;

    boolean b1 = type.equals(token.type);
    boolean b2 = lexeme.equals(token.lexeme);
    boolean b3 = (literal == null) ? literal == token.literal : literal.equals(token.literal);
    return b1 && b2 && b3 && line == token.getLine();
  }

  public TokenType getType() {
    return type;
  }

  public String getLexeme() {
    return lexeme;
  }

  public Object getLiteral() {
    return literal;
  }

  public int getLine() {
    return line;
  }
}

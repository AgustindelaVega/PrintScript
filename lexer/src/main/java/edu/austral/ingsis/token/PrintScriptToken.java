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
  public String toString() {
    return "PrintScriptToken{"
        + "type="
        + type
        + ", lexeme='"
        + lexeme
        + '\''
        + ", literal="
        + literal
        + ", line="
        + line
        + '}';
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

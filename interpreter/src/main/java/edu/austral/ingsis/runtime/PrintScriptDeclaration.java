package edu.austral.ingsis.runtime;

import edu.austral.ingsis.token.TokenType;

public class PrintScriptDeclaration implements Declaration {

  private final TokenType type;
  private Object value;

  public PrintScriptDeclaration(TokenType type, Object value) {
    this.type = type;
    this.value = value;
  }

  @Override
  public TokenType getType() {
    return type;
  }

  @Override
  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }
}

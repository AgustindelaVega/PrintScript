package edu.austral.ingsis.runtime;

import edu.austral.ingsis.token.TokenType;

public interface Declaration {

  TokenType getType();

  Object getValue();

  void setValue(Object value);
}

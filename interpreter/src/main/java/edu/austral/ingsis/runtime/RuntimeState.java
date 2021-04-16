package edu.austral.ingsis.runtime;

import edu.austral.ingsis.token.Token;
import edu.austral.ingsis.token.TokenType;
import java.util.Map;

public interface RuntimeState {
  Map<String, Declaration> getValues();

  void addValue(String name, TokenType type, Object value, Token keyWord);

  void assign(Token name, Object value);

  Object getValue(Token name);
}

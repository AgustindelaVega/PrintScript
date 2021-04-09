package edu.austral.ingsis.runtime;

import edu.austral.ingsis.exceptions.InterpreterException;
import edu.austral.ingsis.token.Token;
import edu.austral.ingsis.token.TokenType;
import java.util.HashMap;
import java.util.Map;

public class PrintScriptRuntimeState implements RuntimeState {

  private final Map<String, Declaration> values;

  public PrintScriptRuntimeState() {
    this.values = new HashMap<>();
  }

  @Override
  public Map<String, Declaration> getValues() {
    return values;
  }

  @Override
  public void addValue(String name, TokenType type, Object value) {
    values.put(name, new PrintScriptDeclaration(type, value));
  }

  @Override
  public void assign(Token name, Object value) {
    if (values.containsKey(name.getLexeme())) {
      Declaration declaration = values.get(name.getLexeme());
      declaration.setValue(value);
      values.put(name.getLexeme(), declaration);
      return;
    }
    throw new InterpreterException(name, "Undefined variable '" + name.getLexeme() + "'.");
  }

  @Override
  public Object getValue(Token name) {
    if (values.containsKey(name.getLexeme())) {
      return values.get(name.getLexeme()).getValue();
    }

    throw new InterpreterException(name, "Undefined variable '" + name.getLexeme() + "'.");
  }
}

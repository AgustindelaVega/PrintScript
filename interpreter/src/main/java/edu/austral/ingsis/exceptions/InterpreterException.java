package edu.austral.ingsis.exceptions;

import edu.austral.ingsis.token.Token;

public class InterpreterException extends RuntimeException {

  public InterpreterException(Token token, String message) {
    super("(" + token.getLine() + ", " + token.getColumn() + ")" + ": " + message);
  }
}

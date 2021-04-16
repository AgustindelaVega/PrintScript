package edu.austral.ingsis.exceptions;

import edu.austral.ingsis.token.Token;

public class InterpreterException extends RuntimeException {
  private final Token token;

  public InterpreterException(Token token, String message) {
    super(message);
    this.token = token;
  }
}

package edu.austral.ingsis.exceptions;

import edu.austral.ingsis.token.Token;

public class ParserException extends RuntimeException {

  public ParserException(Token token, String message) {
    super("(" + token.getLine() + ")" + ": " + message);
  }
}

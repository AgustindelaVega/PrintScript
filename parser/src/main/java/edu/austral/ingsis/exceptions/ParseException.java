package edu.austral.ingsis.exceptions;

import edu.austral.ingsis.token.Token;

public class ParseException extends RuntimeException {

  public ParseException(Token token, String message) {
    super("(" + token.getLine() + ")" + ": " + message);
  }
}

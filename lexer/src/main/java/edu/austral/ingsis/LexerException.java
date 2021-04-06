package edu.austral.ingsis;

public class LexerException extends RuntimeException {
  private final String message;

  public LexerException(String message, Integer line) {
    this.message = line + ": " + message;
  }

  public LexerException(String message, Integer line, Integer column) {
    this.message = "(" + line + ", " + column + ") " + ": " + message;
  }

  @Override
  public String getMessage() {
    return message;
  }
}

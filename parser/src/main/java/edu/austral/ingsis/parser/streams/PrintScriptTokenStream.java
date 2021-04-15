package edu.austral.ingsis.parser.streams;

import static edu.austral.ingsis.token.TokenType.EOF;

import edu.austral.ingsis.exceptions.ParseException;
import edu.austral.ingsis.token.Token;
import edu.austral.ingsis.token.TokenType;
import java.util.List;

public class PrintScriptTokenStream {
  private List<Token> tokens;
  private int current = 0;

  public PrintScriptTokenStream(List<Token> tokens) {
    this.tokens = tokens;
  }

  public boolean match(TokenType... types) {
    for (TokenType type : types) {
      if (check(type)) {
        advance();
        return true;
      }
    }

    return false;
  }

  public boolean check(TokenType type) {
    if (isAtEnd()) return false;
    return peek().getType() == type;
  }

  public Token advance() {
    if (!isAtEnd()) current++;
    return previous();
  }

  public Token consume(TokenType type, String message) {
    if (check(type)) return advance();

    throw new ParseException(message, peek());
  }

  public Token peek() {
    return tokens.get(current);
  }

  public Token previous() {
    return tokens.get(current - 1);
  }

  public boolean isAtEnd() {
    return peek().getType() == EOF;
  }
}

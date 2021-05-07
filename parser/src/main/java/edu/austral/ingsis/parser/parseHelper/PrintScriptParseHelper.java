package edu.austral.ingsis.parser.parseHelper;

import static edu.austral.ingsis.token.TokenType.EOF;

import edu.austral.ingsis.exceptions.ParserException;
import edu.austral.ingsis.token.Token;
import edu.austral.ingsis.token.TokenType;
import java.util.List;

public class PrintScriptParseHelper implements ParseHelper {
  private List<Token> tokens;
  private int current = 0;

  public PrintScriptParseHelper(List<Token> tokens) {
    this.tokens = tokens;
  }

  @Override
  public boolean match(TokenType... types) {
    for (TokenType type : types) {
      if (check(type)) {
        advance();
        return true;
      }
    }

    return false;
  }

  @Override
  public boolean check(TokenType type) {
    if (isAtEnd()) return false;
    return peek().getType() == type;
  }

  @Override
  public Token advance() {
    if (!isAtEnd()) current++;
    return previous();
  }

  @Override
  public Token consume(TokenType type, String message) {
    if (check(type)) return advance();

    throw new ParserException(peek(), message);
  }

  @Override
  public Token peek() {
    return tokens.get(current);
  }

  @Override
  public Token previous() {
    return tokens.get(current - 1);
  }

  @Override
  public boolean isAtEnd() {
    return peek().getType() == EOF;
  }
}

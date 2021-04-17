package edu.austral.ingsis.parser.streams;

import edu.austral.ingsis.token.Token;
import edu.austral.ingsis.token.TokenType;

public interface ParseHelper {

  boolean match(TokenType... types);

  boolean check(TokenType type);

  Token advance();

  Token consume(TokenType type, String message);

  Token peek();

  Token previous();

  boolean isAtEnd();
}

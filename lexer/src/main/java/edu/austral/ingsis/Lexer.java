package edu.austral.ingsis;

import edu.austral.ingsis.token.Token;
import java.util.List;

public interface Lexer {

  List<Token> lex(String src);
}

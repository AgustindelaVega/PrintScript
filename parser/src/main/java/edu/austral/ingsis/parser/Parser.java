package edu.austral.ingsis.parser;

import edu.austral.ingsis.statement.Statement;
import edu.austral.ingsis.token.Token;
import java.util.List;

public interface Parser {
  List<Statement> parse(List<Token> tokens);
}

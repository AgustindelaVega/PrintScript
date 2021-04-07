package edu.austral.ingsis.parser;

import edu.austral.ingsis.statement.Statement;
import java.util.List;

public interface Parser {
  List<Statement> parse();
}

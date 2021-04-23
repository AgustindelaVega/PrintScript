package edu.austral.ingsis.parser.impl;

import edu.austral.ingsis.parser.Parser;
import edu.austral.ingsis.parser.parseHelper.PrintScriptParseHelper;
import edu.austral.ingsis.statement.Statement;
import edu.austral.ingsis.token.Token;
import java.util.ArrayList;
import java.util.List;

public class PrintScriptParser implements Parser {

  private final StatementParser statementParser;

  public PrintScriptParser() {
    this.statementParser = new PrintScriptStatementParser();
  }

  @Override
  public List<Statement> parse(List<Token> tokens) {
    PrintScriptParseHelper tokenStream = new PrintScriptParseHelper(tokens);

    List<Statement> statements = new ArrayList<>();

    while (!tokenStream.isAtEnd()) {
      statements.add(statementParser.parse(tokenStream));
    }

    return statements;
  }
}

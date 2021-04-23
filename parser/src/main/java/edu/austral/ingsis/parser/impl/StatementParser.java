package edu.austral.ingsis.parser.impl;

import edu.austral.ingsis.parser.parseHelper.ParseHelper;
import edu.austral.ingsis.statement.Statement;

public interface StatementParser {

  Statement parse(ParseHelper parseHelper);
}

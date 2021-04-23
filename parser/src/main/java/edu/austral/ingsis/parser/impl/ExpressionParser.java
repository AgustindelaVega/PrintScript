package edu.austral.ingsis.parser.impl;

import edu.austral.ingsis.expression.Expression;
import edu.austral.ingsis.parser.parseHelper.ParseHelper;

public interface ExpressionParser {

  Expression parse(ParseHelper parseHelper);
}

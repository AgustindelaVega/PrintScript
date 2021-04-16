package edu.austral.ingsis;

import edu.austral.ingsis.runtime.RuntimeState;
import edu.austral.ingsis.statement.Statement;
import java.util.List;

public interface Interpreter {

  void interpret(List<Statement> statements);

  RuntimeState getRuntimeState();
}

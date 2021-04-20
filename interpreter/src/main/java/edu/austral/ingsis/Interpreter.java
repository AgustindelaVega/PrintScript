package edu.austral.ingsis;

import edu.austral.ingsis.runtime.RuntimeState;
import edu.austral.ingsis.statement.Statement;
import java.util.List;
import java.util.function.Consumer;

public interface Interpreter {

  void interpret(List<Statement> statements, Consumer<String> printConsume);

  RuntimeState getRuntimeState();
}

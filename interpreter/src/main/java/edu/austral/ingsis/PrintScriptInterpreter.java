package edu.austral.ingsis;

import edu.austral.ingsis.runtime.RuntimeState;
import edu.austral.ingsis.statement.Statement;
import java.util.List;

public class PrintScriptInterpreter implements Interpreter {
  PrintScriptVisitor visitor;

  PrintScriptInterpreter() {
    this.visitor = new PrintScriptVisitor();
  }

  @Override
  public RuntimeState getRuntimeState() {
    return visitor.getRuntimeState();
  }

  @Override
  public void interpret(List<Statement> statements) {
    for (Statement statement : statements) {
      statement.accept(visitor);
    }
  }
}

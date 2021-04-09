package edu.austral.ingsis;

import edu.austral.ingsis.exceptions.InterpreterException;
import edu.austral.ingsis.runtime.RuntimeState;
import edu.austral.ingsis.statement.Statement;
import java.util.List;

public class PrintScriptInterpreter implements Interpreter {
  PrintScriptVisitor visitor;

  PrintScriptInterpreter() {
    this.visitor = new PrintScriptVisitor();
  }

  @Override
  public RuntimeState getEnvironment() {
    return visitor.getEnvironment();
  }

  @Override
  public void interpret(List<Statement> statements) throws InterpreterException {
    for (Statement statement : statements) {
      statement.accept(visitor);
    }
  }
}

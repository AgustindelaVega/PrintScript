package edu.austral.ingsis;

import edu.austral.ingsis.runtime.RuntimeState;
import edu.austral.ingsis.statement.Statement;
import java.util.List;
import java.util.function.Consumer;

public class PrintScriptInterpreter implements Interpreter {
  PrintScriptVisitor visitor;

  public PrintScriptInterpreter() {
    this.visitor = new PrintScriptVisitor();
  }

  @Override
  public RuntimeState getRuntimeState() {
    return visitor.getRuntimeState();
  }

  @Override
  public void interpret(List<Statement> statements, Consumer<String> printConsumer) {
    visitor.setPrintConsumer(printConsumer);
    for (Statement statement : statements) {
      statement.accept(visitor);
    }
  }
}

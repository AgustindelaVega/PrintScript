package edu.austral.ingsis.visitor;

import edu.austral.ingsis.statement.impl.AssigmentStatement;
import edu.austral.ingsis.statement.impl.DeclarationStatement;
import edu.austral.ingsis.statement.impl.PrintStatement;

public interface StatementVisitor {

  void visit(AssigmentStatement assigmentStatement);

  void visit(DeclarationStatement declarationStatement);

  void visit(PrintStatement printStatement);
}

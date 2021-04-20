package edu.austral.ingsis.visitor;

import edu.austral.ingsis.statement.impl.*;

public interface StatementVisitor {

  void visit(AssigmentStatement assigmentStatement);

  void visit(DeclarationStatement declarationStatement);

  void visit(PrintStatement printStatement);

  void visit(IfStatement ifStatement);

  void visit(BlockStatement blockStatement);
}

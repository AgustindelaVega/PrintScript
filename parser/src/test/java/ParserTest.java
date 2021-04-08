import static edu.austral.ingsis.token.TokenType.*;
import static org.junit.jupiter.api.Assertions.*;

import edu.austral.ingsis.expression.impl.AssigmentExpression;
import edu.austral.ingsis.expression.impl.BinaryExpression;
import edu.austral.ingsis.expression.impl.ValueExpression;
import edu.austral.ingsis.parser.Parser;
import edu.austral.ingsis.parser.impl.PrintScriptParser;
import edu.austral.ingsis.statement.Statement;
import edu.austral.ingsis.statement.impl.AssigmentStatement;
import edu.austral.ingsis.statement.impl.DeclarationStatement;
import edu.austral.ingsis.statement.impl.PrintStatement;
import edu.austral.ingsis.token.PrintScriptToken;
import edu.austral.ingsis.token.Token;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ParserTest {

  private Parser parser;
  private List<Token> tokens;

  @BeforeEach
  public void setUp() {
    tokens = new ArrayList<>();
    parser = new PrintScriptParser();
  }

  @Test
  public void TestParseDeclarationStatement_ValueExpression() {
    tokens.add(new PrintScriptToken(LET, "let", null, 1));
    tokens.add(new PrintScriptToken(IDENTIFIER, "x", null, 1));
    tokens.add(new PrintScriptToken(COLON, ":", null, 1));
    tokens.add(new PrintScriptToken(NUMBERTYPE, "number", null, 1));
    tokens.add(new PrintScriptToken(ASSIGNATION, "=", null, 1));
    tokens.add(new PrintScriptToken(NUMBER, "10", 10, 1));
    tokens.add(new PrintScriptToken(SEMICOLON, ";", null, 1));
    tokens.add(new PrintScriptToken(EOF, "", null, 1));

    List<Statement> statements = parser.parse(tokens);
    DeclarationStatement statement = (DeclarationStatement) statements.get(0);

    assertEquals(LET, statement.getKeyword().getType());
    assertEquals(IDENTIFIER, statement.getName().getType());
    assertEquals("x", statement.getName().getLexeme());
    assertEquals(NUMBERTYPE, statement.getType());
    assertEquals(10, ((ValueExpression) statement.getExpression()).getValue());
  }

  @Test
  public void TestParseDeclarationStatement_BinaryExpression_Addition() {
    tokens.add(new PrintScriptToken(LET, "let", null, 1));
    tokens.add(new PrintScriptToken(IDENTIFIER, "x", null, 1));
    tokens.add(new PrintScriptToken(COLON, ":", null, 1));
    tokens.add(new PrintScriptToken(NUMBERTYPE, "number", null, 1));
    tokens.add(new PrintScriptToken(ASSIGNATION, "=", null, 1));
    tokens.add(new PrintScriptToken(NUMBER, "10", 10, 1));
    tokens.add(new PrintScriptToken(PLUS, "+", null, 1));
    tokens.add(new PrintScriptToken(NUMBER, "9", 9, 1));
    tokens.add(new PrintScriptToken(SEMICOLON, ";", null, 1));
    tokens.add(new PrintScriptToken(EOF, "", "", 1));

    List<Statement> statements = parser.parse(tokens);
    DeclarationStatement statement = (DeclarationStatement) statements.get(0);

    assertEquals(LET, statement.getKeyword().getType());
    assertEquals(IDENTIFIER, statement.getName().getType());
    assertEquals("x", statement.getName().getLexeme());
    assertEquals(NUMBERTYPE, statement.getType());
    assertEquals(
        10,
        ((ValueExpression) ((BinaryExpression) statement.getExpression()).getLeft()).getValue());
    assertEquals(PLUS, ((BinaryExpression) statement.getExpression()).getOperator().getType());
    assertEquals(
        9,
        ((ValueExpression) ((BinaryExpression) statement.getExpression()).getRight()).getValue());
  }

  @Test
  public void TestParseDeclarationStatement_BinaryExpression_Multiplication() {
    tokens.add(new PrintScriptToken(LET, "let", null, 1));
    tokens.add(new PrintScriptToken(IDENTIFIER, "x", null, 1));
    tokens.add(new PrintScriptToken(COLON, ":", null, 1));
    tokens.add(new PrintScriptToken(NUMBERTYPE, "number", null, 1));
    tokens.add(new PrintScriptToken(ASSIGNATION, "=", null, 1));
    tokens.add(new PrintScriptToken(NUMBER, "10", 10, 1));
    tokens.add(new PrintScriptToken(MULTIPLY, "*", null, 1));
    tokens.add(new PrintScriptToken(NUMBER, "9", 9, 1));
    tokens.add(new PrintScriptToken(SEMICOLON, ";", null, 1));
    tokens.add(new PrintScriptToken(EOF, "", "", 1));

    List<Statement> statements = parser.parse(tokens);
    DeclarationStatement statement = (DeclarationStatement) statements.get(0);

    assertEquals(LET, statement.getKeyword().getType());
    assertEquals(IDENTIFIER, statement.getName().getType());
    assertEquals("x", statement.getName().getLexeme());
    assertEquals(NUMBERTYPE, statement.getType());
    assertEquals(
        10,
        ((ValueExpression) ((BinaryExpression) statement.getExpression()).getLeft()).getValue());
    assertEquals(MULTIPLY, ((BinaryExpression) statement.getExpression()).getOperator().getType());
    assertEquals(
        9,
        ((ValueExpression) ((BinaryExpression) statement.getExpression()).getRight()).getValue());
  }

  @Test
  public void TestParsePrintStatement_ValueExpression() {
    tokens.add(new PrintScriptToken(PRINT, "print", null, 1));
    tokens.add(new PrintScriptToken(NUMBER, "10", 10, 1));
    tokens.add(new PrintScriptToken(SEMICOLON, ";", null, 1));
    tokens.add(new PrintScriptToken(EOF, "", "", 1));

    List<Statement> statements = parser.parse(tokens);
    PrintStatement statement = (PrintStatement) statements.get(0);

    assertTrue(statements.get(0) instanceof PrintStatement);
    assertEquals(10, ((ValueExpression) statement.getExpression()).getValue());
  }

  @Test
  public void TestParseAssigmentStatement_AssigmentExpression() {
    tokens.add(new PrintScriptToken(IDENTIFIER, "x", null, 1));
    tokens.add(new PrintScriptToken(ASSIGNATION, "=", null, 1));
    tokens.add(new PrintScriptToken(NUMBER, "10", 10, 1));
    tokens.add(new PrintScriptToken(SEMICOLON, ";", null, 1));
    tokens.add(new PrintScriptToken(EOF, "", null, 1));

    List<Statement> statements = parser.parse(tokens);
    AssigmentStatement statement = (AssigmentStatement) statements.get(0);

    assertTrue(statements.get(0) instanceof AssigmentStatement);
    assertEquals(IDENTIFIER, ((AssigmentExpression) statement.getExpression()).getName().getType());
    assertEquals("x", ((AssigmentExpression) statement.getExpression()).getName().getLexeme());
    assertEquals(
        10,
        ((ValueExpression) ((AssigmentExpression) statement.getExpression()).getExpression())
            .getValue());
  }

  @Test
  public void TestParseMultipleStatements() {
    tokens.add(new PrintScriptToken(LET, "let", null, 1));
    tokens.add(new PrintScriptToken(IDENTIFIER, "x", null, 1));
    tokens.add(new PrintScriptToken(COLON, ":", null, 1));
    tokens.add(new PrintScriptToken(NUMBERTYPE, "number", null, 1));
    tokens.add(new PrintScriptToken(SEMICOLON, ";", null, 1));
    tokens.add(new PrintScriptToken(IDENTIFIER, "x", null, 1));
    tokens.add(new PrintScriptToken(ASSIGNATION, "=", null, 1));
    tokens.add(new PrintScriptToken(NUMBER, "10", 10, 1));
    tokens.add(new PrintScriptToken(SEMICOLON, ";", null, 1));
    tokens.add(new PrintScriptToken(EOF, "", null, 1));

    List<Statement> statements = parser.parse(tokens);
    DeclarationStatement declarationStatement = (DeclarationStatement) statements.get(0);
    AssigmentStatement assigmentStatement = (AssigmentStatement) statements.get(1);

    assertEquals(LET, declarationStatement.getKeyword().getType());
    assertEquals(IDENTIFIER, declarationStatement.getName().getType());
    assertEquals("x", declarationStatement.getName().getLexeme());
    assertEquals(NUMBERTYPE, declarationStatement.getType());
    assertNull(declarationStatement.getExpression());
    assertTrue(statements.get(1) instanceof AssigmentStatement);
    assertEquals(
        IDENTIFIER, ((AssigmentExpression) assigmentStatement.getExpression()).getName().getType());
    assertEquals(
        "x", ((AssigmentExpression) assigmentStatement.getExpression()).getName().getLexeme());
    assertEquals(
        10,
        ((ValueExpression)
                ((AssigmentExpression) assigmentStatement.getExpression()).getExpression())
            .getValue());
  }
}

import static edu.austral.ingsis.token.TokenType.*;
import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import java.io.FileNotFoundException;
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
  public void TestParseDeclarationStatement_ValueExpression() throws FileNotFoundException {
    tokens.addAll(getTokensFromJSON("./src/test/resources/parser_src01.json"));

    List<Statement> statements = parser.parse(tokens);
    DeclarationStatement statement = (DeclarationStatement) statements.get(0);

    assertEquals(LET, statement.getKeyword().getType());
    assertEquals(IDENTIFIER, statement.getName().getType());
    assertEquals("x", statement.getName().getLexeme());
    assertEquals(NUMBERTYPE, statement.getType());
    assertEquals(10.0, ((ValueExpression) statement.getExpression()).getValue());
  }

  @Test
  public void TestParseDeclarationStatement_BinaryExpression_Addition()
      throws FileNotFoundException {
    tokens.addAll(getTokensFromJSON("./src/test/resources/parser_src02.json"));

    List<Statement> statements = parser.parse(tokens);
    DeclarationStatement statement = (DeclarationStatement) statements.get(0);

    assertEquals(LET, statement.getKeyword().getType());
    assertEquals(IDENTIFIER, statement.getName().getType());
    assertEquals("x", statement.getName().getLexeme());
    assertEquals(NUMBERTYPE, statement.getType());
    assertEquals(
        10.0,
        ((ValueExpression) ((BinaryExpression) statement.getExpression()).getLeft()).getValue());
    assertEquals(PLUS, ((BinaryExpression) statement.getExpression()).getOperator().getType());
    assertEquals(
        9.0,
        ((ValueExpression) ((BinaryExpression) statement.getExpression()).getRight()).getValue());
  }

  @Test
  public void TestParseDeclarationStatement_BinaryExpression_Multiplication()
      throws FileNotFoundException {
    tokens.addAll(getTokensFromJSON("./src/test/resources/parser_src03.json"));

    List<Statement> statements = parser.parse(tokens);
    DeclarationStatement statement = (DeclarationStatement) statements.get(0);

    assertEquals(LET, statement.getKeyword().getType());
    assertEquals(IDENTIFIER, statement.getName().getType());
    assertEquals("x", statement.getName().getLexeme());
    assertEquals(NUMBERTYPE, statement.getType());
    assertEquals(
        10.0,
        ((ValueExpression) ((BinaryExpression) statement.getExpression()).getLeft()).getValue());
    assertEquals(MULTIPLY, ((BinaryExpression) statement.getExpression()).getOperator().getType());
    assertEquals(
        9.0,
        ((ValueExpression) ((BinaryExpression) statement.getExpression()).getRight()).getValue());
  }

  @Test
  public void TestParsePrintStatement_ValueExpression() throws FileNotFoundException {
    tokens.addAll(getTokensFromJSON("./src/test/resources/parser_src04.json"));

    List<Statement> statements = parser.parse(tokens);
    PrintStatement statement = (PrintStatement) statements.get(0);

    assertTrue(statements.get(0) instanceof PrintStatement);
    assertEquals(10.0, ((ValueExpression) statement.getExpression()).getValue());
  }

  @Test
  public void TestParseAssigmentStatement_AssigmentExpression() throws FileNotFoundException {
    tokens.addAll(getTokensFromJSON("./src/test/resources/parser_src05.json"));

    List<Statement> statements = parser.parse(tokens);
    AssigmentStatement statement = (AssigmentStatement) statements.get(0);

    assertTrue(statements.get(0) instanceof AssigmentStatement);
    assertEquals(IDENTIFIER, ((AssigmentExpression) statement.getExpression()).getName().getType());
    assertEquals("x", ((AssigmentExpression) statement.getExpression()).getName().getLexeme());
    assertEquals(
        10.0,
        ((ValueExpression) ((AssigmentExpression) statement.getExpression()).getExpression())
            .getValue());
  }

  @Test
  public void TestParseMultipleStatements() throws FileNotFoundException {
    tokens.addAll(getTokensFromJSON("./src/test/resources/parser_src06.json"));

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
        10.0,
        ((ValueExpression)
                ((AssigmentExpression) assigmentStatement.getExpression()).getExpression())
            .getValue());
  }

  private List<PrintScriptToken> getTokensFromJSON(String src) throws FileNotFoundException {
    return new Gson()
        .fromJson(
            new java.io.FileReader(src), new TypeToken<List<PrintScriptToken>>() {}.getType());
  }
}

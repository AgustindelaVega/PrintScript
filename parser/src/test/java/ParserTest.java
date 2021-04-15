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
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

public class ParserTest {

  private Parser parser;
  private List<Token> tokens;
  private final Gson gson = new Gson();

  private List<PrintScriptToken> getTokensFromJSON(String src) throws FileNotFoundException {
    return gson.fromJson(
        new java.io.FileReader(src), new TypeToken<List<PrintScriptToken>>() {}.getType());
  }

  private void writeStatementsToJSON(List<Statement> statements, String testNumber)
      throws FileNotFoundException {
    String json = gson.toJson(statements);
    try {
      FileWriter myWriter =
          new FileWriter("./src/test/resources/parser_actual" + testNumber + ".json");
      myWriter.write(json);
      myWriter.close();
      System.out.println("Successfully wrote to the file.");
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
  }

  @BeforeEach
  public void setUp() {
    tokens = new ArrayList<>();
    parser = new PrintScriptParser();
  }

  @Test
  public void test001_ParseDeclarationStatement_ValueExpression() throws FileNotFoundException {
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
  public void test002_ParseDeclarationStatement_BinaryExpression_Addition()
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
  public void test003_ParseDeclarationStatement_BinaryExpression_Multiplication()
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
  public void test004_ParsePrintStatement_ValueExpression() throws FileNotFoundException {
    tokens.addAll(getTokensFromJSON("./src/test/resources/parser_src04.json"));

    List<Statement> statements = parser.parse(tokens);
    PrintStatement statement = (PrintStatement) statements.get(0);

    assertTrue(statements.get(0) instanceof PrintStatement);
    assertEquals(10.0, ((ValueExpression) statement.getExpression()).getValue());
  }

  @Test
  public void test005_ParseAssigmentStatement_AssigmentExpression() throws FileNotFoundException {
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
  public void test006_ParseMultipleStatements() throws FileNotFoundException {
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

  @Test
  public void test007_ParseDeclarationStatementWithBooleanType() throws FileNotFoundException {
    tokens.addAll(getTokensFromJSON("./src/test/resources/parser_src07.json"));

    List<Statement> statements = parser.parse(tokens);
    DeclarationStatement statement = (DeclarationStatement) statements.get(0);

    assertEquals(LET, statement.getKeyword().getType());
    assertEquals(IDENTIFIER, statement.getName().getType());
    assertEquals("x", statement.getName().getLexeme());
    assertEquals(BOOLEAN, statement.getType());
    assertEquals(true, ((ValueExpression) statement.getExpression()).getValue());
  }

  @Test
  public void test008_ParseMultipleDeclarationStatementWithIntegerAndDecimalNumbers()
      throws IOException, JSONException {
    tokens.addAll(getTokensFromJSON("./src/test/resources/parser_src07.json"));

    List<Statement> statements = parser.parse(tokens);
    DeclarationStatement integerDeclaration = (DeclarationStatement) statements.get(0);
    // DeclarationStatement decimalDeclaration = (DeclarationStatement) statements.get(1);

    writeStatementsToJSON(statements, "07");

    String expectedJson =
        FileUtils.readFileToString(
            new File("./src/test/resources/parser_expected07.json"), (String) null);
    String actualJson =
        FileUtils.readFileToString(
            new File("./src/test/resources/parser_actual07.json"), (String) null);
    JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.STRICT);
  }
}

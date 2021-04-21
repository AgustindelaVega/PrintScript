import static edu.austral.ingsis.token.TokenType.*;
import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.austral.ingsis.exceptions.ParseException;
import edu.austral.ingsis.parser.Parser;
import edu.austral.ingsis.parser.impl.PrintScriptParser;
import edu.austral.ingsis.statement.Statement;
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

  private void compareStatementsFromJsons(String expectedJsonFile)
      throws IOException, JSONException {
    List<Statement> statements = parser.parse(tokens);

    String expectedJson = FileUtils.readFileToString(new File(expectedJsonFile), (String) null);
    String actualJson = gson.toJson(statements);
    JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.STRICT);
  }

  @BeforeEach
  public void setUp() {
    tokens = new ArrayList<>();
    parser = new PrintScriptParser();
  }

  @Test
  public void test01_ParseDeclarationStatementWithValueExpression()
      throws IOException, JSONException {
    tokens.addAll(getTokensFromJSON("./src/test/resources/parser_src01.json"));

    compareStatementsFromJsons("./src/test/resources/parser_expected01.json");
  }

  @Test
  public void test02_ParseDeclarationStatementWithBinaryExpressionAndAddition()
      throws IOException, JSONException {
    tokens.addAll(getTokensFromJSON("./src/test/resources/parser_src02.json"));

    compareStatementsFromJsons("./src/test/resources/parser_expected02.json");
  }

  @Test
  public void test03_ParseDeclarationStatementWithBinaryExpressionAndMultiplication()
      throws IOException, JSONException {
    tokens.addAll(getTokensFromJSON("./src/test/resources/parser_src03.json"));

    compareStatementsFromJsons("./src/test/resources/parser_expected03.json");
  }

  @Test
  public void test04_ParsePrintStatementWithValueExpression() throws IOException, JSONException {
    tokens.addAll(getTokensFromJSON("./src/test/resources/parser_src04.json"));

    compareStatementsFromJsons("./src/test/resources/parser_expected04.json");
  }

  @Test
  public void test05_ParseAssigmentStatementWithAssigmentExpression()
      throws IOException, JSONException {
    tokens.addAll(getTokensFromJSON("./src/test/resources/parser_src05.json"));

    compareStatementsFromJsons("./src/test/resources/parser_expected05.json");
  }

  @Test
  public void test06_ParseMultipleStatements() throws IOException, JSONException {
    tokens.addAll(getTokensFromJSON("./src/test/resources/parser_src06.json"));

    compareStatementsFromJsons("./src/test/resources/parser_expected06.json");
  }

  @Test
  public void test07_ParseDeclarationStatementWithBothBooleanValues()
      throws IOException, JSONException {
    tokens.addAll(getTokensFromJSON("./src/test/resources/parser_src07.json"));

    compareStatementsFromJsons("./src/test/resources/parser_expected07.json");
  }

  @Test
  public void test08_ParseDeclarationStatementWithUnaryExpression()
      throws IOException, JSONException {
    tokens.addAll(getTokensFromJSON("./src/test/resources/parser_src08.json"));

    compareStatementsFromJsons("./src/test/resources/parser_expected08.json");
  }

  @Test
  public void test09_ParseMultipleDeclarationStatementWithIntegerAndDecimalNumbers()
      throws IOException, JSONException {
    tokens.addAll(getTokensFromJSON("./src/test/resources/parser_src09.json"));

    compareStatementsFromJsons("./src/test/resources/parser_expected09.json");
  }

  @Test
  public void test10_ParseDeclarationStatementWithoutSemicolonShouldThrowAParseException()
      throws FileNotFoundException {
    tokens.addAll(getTokensFromJSON("./src/test/resources/parser_src10.json"));

    assertThrows(ParseException.class, () -> parser.parse(tokens));
  }

  @Test
  public void test11_ParseDeclarationStatementWithoutColonShouldThrowAParseException()
      throws FileNotFoundException {
    tokens.addAll(getTokensFromJSON("./src/test/resources/parser_src11.json"));

    assertThrows(ParseException.class, () -> parser.parse(tokens));
  }

  @Test
  public void test12_ParseAssignmentStatementWithoutIdentifierShouldThrowAParseException()
      throws FileNotFoundException {
    tokens.addAll(getTokensFromJSON("./src/test/resources/parser_src12.json"));

    assertThrows(ParseException.class, () -> parser.parse(tokens));
  }

  @Test
  public void test13_ParseDeclarationStatementWithBooleanBinaryGreaterOperation()
      throws IOException, JSONException {
    tokens.addAll(getTokensFromJSON("./src/test/resources/parser_src13.json"));

    compareStatementsFromJsons("./src/test/resources/parser_expected13.json");
  }

  @Test
  public void test14_ParseIfStatement() throws IOException, JSONException {
    tokens.addAll(getTokensFromJSON("./src/test/resources/parser_src14.json"));

    compareStatementsFromJsons("./src/test/resources/parser_expected14.json");
  }

  @Test
  public void test15_ParseDeclarationStatementWithoutVariableTypeShouldThrowAParseException()
      throws FileNotFoundException {
    tokens.addAll(getTokensFromJSON("./src/test/resources/parser_src15.json"));

    assertThrows(ParseException.class, () -> parser.parse(tokens));
  }

  @Test
  public void test16_ParsePrintAndDeclarationStatementWithMultipleOperators()
      throws IOException, JSONException {
    tokens.addAll(getTokensFromJSON("./src/test/resources/parser_src16.json"));

    compareStatementsFromJsons("./src/test/resources/parser_expected16.json");
  }

  @Test
  public void test17_ParseDeclarationIfElseAndPrintStatements() throws IOException, JSONException {
    tokens.addAll(getTokensFromJSON("./src/test/resources/parser_src17.json"));

    compareStatementsFromJsons("./src/test/resources/parser_expected17.json");
  }
}

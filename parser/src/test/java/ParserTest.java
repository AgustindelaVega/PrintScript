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

  private void writeStatementsToJSON(List<Statement> statements, String testNumber) {
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

  private void compareStatementsFromJsons(
      String testNumber, String expectedJsonFile, String actualJsonFile)
      throws IOException, JSONException {
    List<Statement> statements = parser.parse(tokens);

    writeStatementsToJSON(statements, testNumber);

    String expectedJson = FileUtils.readFileToString(new File(expectedJsonFile), (String) null);
    String actualJson = FileUtils.readFileToString(new File(actualJsonFile), (String) null);
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

    compareStatementsFromJsons(
        "01",
        "./src/test/resources/parser_expected01.json",
        "./src/test/resources/parser_actual01.json");
  }

  @Test
  public void test02_ParseDeclarationStatementWithBinaryExpressionAndAddition()
      throws IOException, JSONException {
    tokens.addAll(getTokensFromJSON("./src/test/resources/parser_src02.json"));

    compareStatementsFromJsons(
        "02",
        "./src/test/resources/parser_expected02.json",
        "./src/test/resources/parser_actual02.json");
  }

  @Test
  public void test03_ParseDeclarationStatementWithBinaryExpressionAndMultiplication()
      throws IOException, JSONException {
    tokens.addAll(getTokensFromJSON("./src/test/resources/parser_src03.json"));

    compareStatementsFromJsons(
        "03",
        "./src/test/resources/parser_expected03.json",
        "./src/test/resources/parser_actual03.json");
  }

  @Test
  public void test04_ParsePrintStatementWithValueExpression() throws IOException, JSONException {
    tokens.addAll(getTokensFromJSON("./src/test/resources/parser_src04.json"));

    compareStatementsFromJsons(
        "04",
        "./src/test/resources/parser_expected04.json",
        "./src/test/resources/parser_actual04.json");
  }

  @Test
  public void test05_ParseAssigmentStatementWithAssigmentExpression()
      throws IOException, JSONException {
    tokens.addAll(getTokensFromJSON("./src/test/resources/parser_src05.json"));

    compareStatementsFromJsons(
        "05",
        "./src/test/resources/parser_expected05.json",
        "./src/test/resources/parser_actual05.json");
  }

  @Test
  public void test06_ParseMultipleStatements() throws IOException, JSONException {
    tokens.addAll(getTokensFromJSON("./src/test/resources/parser_src06.json"));

    compareStatementsFromJsons(
        "06",
        "./src/test/resources/parser_expected06.json",
        "./src/test/resources/parser_actual06.json");
  }

  @Test
  public void test07_ParseDeclarationStatementWithUnaryExpression()
      throws IOException, JSONException {
    tokens.addAll(getTokensFromJSON("./src/test/resources/parser_src07.json"));

    compareStatementsFromJsons(
        "07",
        "./src/test/resources/parser_expected07.json",
        "./src/test/resources/parser_actual07.json");
  }

  @Test
  public void test08_ParseDeclarationStatementWithoutSemicolonShouldThrowAParseException()
      throws FileNotFoundException {
    tokens.addAll(getTokensFromJSON("./src/test/resources/parser_src08.json"));

    assertThrows(ParseException.class, () -> parser.parse(tokens));
  }

  @Test
  public void test09_ParseDeclarationStatementWithoutColonShouldThrowAParseException()
      throws FileNotFoundException {
    tokens.addAll(getTokensFromJSON("./src/test/resources/parser_src09.json"));

    assertThrows(ParseException.class, () -> parser.parse(tokens));
  }

  @Test
  public void test10_ParseAssignmentStatementWithoutIdentifierShouldThrowAParseException()
      throws FileNotFoundException {
    tokens.addAll(getTokensFromJSON("./src/test/resources/parser_src10.json"));

    assertThrows(ParseException.class, () -> parser.parse(tokens));
  }
}

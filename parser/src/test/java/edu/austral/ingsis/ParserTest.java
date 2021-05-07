package edu.austral.ingsis;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.austral.ingsis.parser.Parser;
import edu.austral.ingsis.parser.impl.PrintScriptParser;
import edu.austral.ingsis.statement.Statement;
import edu.austral.ingsis.token.PrintScriptToken;
import edu.austral.ingsis.token.Token;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

@RunWith(Parameterized.class)
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

  @Parameterized.Parameter(value = 0)
  public String version;

  @Parameterized.Parameter(value = 1)
  public String directory;

  @Parameterized.Parameters(name = "version {0} - {1})")
  public static Collection<Object[]> data() {
    return Arrays.asList(
        new Object[][] {
          {"1.0", "assignment-statement-with-assignment-expression"},
          {"1.0", "declaration-statement-with-binary-expression-and-addition"},
          {"1.0", "declaration-statement-with-binary-expression-and-multiplication"},
          {"1.0", "declaration-statement-with-unary-expression"},
          {"1.0", "declaration-statement-with-value-expression"},
          {"1.0", "multiple-statements"},
          {"1.0", "print-and-declaration-statement-with-multiple-operators"},
          {"1.0", "print-statement-with-value-expression"},
          {"1.1", "declaration-if-else-and-print-statements"},
          {"1.1", "declaration-statement-with-boolean-binary-greater-operation"},
          {"1.1", "declaration-statement-with-both-boolean-values"},
          {"1.1", "if-statement"},
          {"1.1", "multiple-declaration-statements-with-integer-and-decimal-numbers"},
        });
  }

  @Test
  public void testParserOutput() throws IOException, JSONException {
    tokens = new ArrayList<>();
    parser = new PrintScriptParser();
    String testDirectory = "./src/test/resources/" + version + "/valid/" + directory + "/";
    tokens.addAll(getTokensFromJSON(testDirectory + "main.json"));

    compareStatementsFromJsons(testDirectory + "expected.json");
  }
}

package edu.austral.ingsis;

import static org.junit.Assert.assertThrows;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.austral.ingsis.exceptions.ParserException;
import edu.austral.ingsis.parser.Parser;
import edu.austral.ingsis.parser.impl.PrintScriptParser;
import edu.austral.ingsis.token.PrintScriptToken;
import edu.austral.ingsis.token.Token;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class ParserErrorTest {

  private Parser parser;
  private List<Token> tokens;
  private final Gson gson = new Gson();

  private List<PrintScriptToken> getTokensFromJSON(String src) throws FileNotFoundException {
    return gson.fromJson(
        new java.io.FileReader(src), new TypeToken<List<PrintScriptToken>>() {}.getType());
  }

  @Parameterized.Parameter(value = 0)
  public String version;

  @Parameterized.Parameter(value = 1)
  public String directory;

  @Parameterized.Parameter(value = 2)
  public String errorMessage;

  @Parameterized.Parameters(name = "version {0} - {1})")
  public static Collection<Object[]> data() {
    return Arrays.asList(
        new Object[][] {
          {"1.0", "assignment-statement-without-identifier", "(1): Expression expected."},
          {"1.0", "declaration-statement-without-colon", "(1): Colon missing."},
          {
            "1.0",
            "declaration-statement-without-semicolon",
            "(1): Semicolon after variable declaration missing."
          },
          {"1.0", "declaration-statement-without-variable-type", "(1): Variable type missing."},
          {"1.1", "if-statement-without-left-parenthesis", "(1): Expect '(' after 'if'."},
          {"1.1", "block-statement-without-closing-brace", "(1): Expect '}' after block end."},
        });
  }

  @Test
  public void testParserError() throws FileNotFoundException {
    String testDirectory = "./src/test/resources/" + version + "/invalid/" + directory + "/";

    tokens = new ArrayList<>();
    parser = new PrintScriptParser();
    tokens.addAll(getTokensFromJSON(testDirectory + "main.json"));
    Exception exception = assertThrows(ParserException.class, () -> parser.parse(tokens));

    String expectedMessage = errorMessage;
    String actualMessage = exception.getMessage();

    Assert.assertTrue(actualMessage.contains(expectedMessage));
  }
}

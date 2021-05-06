package edu.austral.ingsis;

import com.google.gson.Gson;
import edu.austral.ingsis.token.Token;
import java.io.File;
import java.io.IOException;
import java.util.*;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

@RunWith(Parameterized.class)
public class LexerTest {

  private final Gson gson = new Gson();

  @SuppressWarnings("WeakerAccess")
  @Parameterized.Parameter(value = 0)
  public String version;

  @SuppressWarnings("WeakerAccess")
  @Parameterized.Parameter(value = 1)
  public String directory;

  @Parameterized.Parameters(name = "version {0} - {1})")
  public static Collection<Object[]> data() {
    return Arrays.asList(
        new Object[][] {
          {"1.0", "declaration"},
          {"1.0", "print"},
          {"1.0", "arithmetic-operations"},
          {"1.1", "boolean-declaration"},
          {"1.1", "if-else"}
        });
  }

  @Test
  public void testLexerOutput() throws IOException, JSONException {
    String testDirectory = "./src/test/resources/" + version + "/valid/" + directory + "/";

    String src = FileReader.getFileLines(testDirectory + "main.txt");

    compareTokensFromJsons(testDirectory + "expected.json", src);
  }

  private void compareTokensFromJsons(String expectedJsonFile, String fileLines)
      throws IOException, JSONException {
    Lexer lexer = new PrintScriptLexer();
    List<Token> tokens = lexer.lex(fileLines, version);

    String expectedJson = FileUtils.readFileToString(new File(expectedJsonFile), (String) null);
    String actualJson = gson.toJson(tokens);
    JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.STRICT);
  }
}

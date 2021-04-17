package edu.austral.ingsis;

import static org.junit.Assert.assertThrows;

import com.google.gson.Gson;
import edu.austral.ingsis.token.Token;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

public class LexerTest {

  private Lexer lexer;
  private final Gson gson = new Gson();
  private final String VERSION = "1.1";

  @Test
  public void testLexerValidInput() throws IOException, JSONException {
    String src = FileReader.getFileLines("./src/test/resources/lexer_test01.txt");
    compareTokensFromJsons(
        "01",
        "./src/test/resources/lexer_expected01.json",
        "./src/test/resources/lexer_actual01.json",
        src);
  }

  @Test
  public void testLexerValidInput2() throws IOException, JSONException {
    String src = FileReader.getFileLines("./src/test/resources/lexer_test02.txt");
    compareTokensFromJsons(
        "02",
        "./src/test/resources/lexer_expected02.json",
        "./src/test/resources/lexer_actual02.json",
        src);
  }

  @Test
  public void testLexerInvalidInput() {
    Lexer lexer = new PrintScriptLexer(VERSION);
    Exception exception =
        assertThrows(
            LexerException.class,
            () -> lexer.lex(FileReader.getFileLines("./src/test/resources/lexer_test03.txt")));

    String expectedMessage = "(1, 14) : Error matching group \" ?? \"";
    String actualMessage = exception.getMessage();

    Assert.assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  public void testLexerInvalidInput2() {
    Lexer lexer = new PrintScriptLexer(VERSION);
    Exception exception =
        assertThrows(
            LexerException.class,
            () -> lexer.lex(FileReader.getFileLines("./src/test/resources/lexer_test04.txt")));

    String expectedMessage = "(2, 15) : Error matching group \"  ! \"";
    String actualMessage = exception.getMessage();

    Assert.assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  public void testLexerValidInput3() throws IOException, JSONException {
    String src = FileReader.getFileLines("./src/test/resources/lexer_test05.txt");
    compareTokensFromJsons(
        "05",
        "./src/test/resources/lexer_expected05.json",
        "./src/test/resources/lexer_actual05.json",
        src);
  }

  private void writeTokensToJSON(List<Token> tokens, String testNumber) {
    String json = gson.toJson(tokens);
    try {
      FileWriter myWriter =
          new FileWriter("./src/test/resources/lexer_actual" + testNumber + ".json");
      myWriter.write(json);
      myWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void compareTokensFromJsons(
      String testNumber, String expectedJsonFile, String actualJsonFile, String fileLines)
      throws IOException, JSONException {
    Lexer lexer = new PrintScriptLexer(VERSION);
    List<Token> tokens = lexer.lex(fileLines);

    writeTokensToJSON(tokens, testNumber);

    String expectedJson = FileUtils.readFileToString(new File(expectedJsonFile), (String) null);
    String actualJson = FileUtils.readFileToString(new File(actualJsonFile), (String) null);
    JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.STRICT);
  }
}

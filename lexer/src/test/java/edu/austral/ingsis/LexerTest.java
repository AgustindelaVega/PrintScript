package edu.austral.ingsis;

import static org.junit.Assert.assertThrows;

import com.google.gson.Gson;
import edu.austral.ingsis.token.Token;
import java.io.File;
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
    compareTokensFromJsons("./src/test/resources/lexer_expected01.json", src);
  }

  @Test
  public void testLexerValidInput2() throws IOException, JSONException {
    String src = FileReader.getFileLines("./src/test/resources/lexer_test02.txt");
    compareTokensFromJsons("./src/test/resources/lexer_expected02.json", src);
  }

  @Test
  public void testLexerInvalidInput() {
    Lexer lexer = new PrintScriptLexer();
    Exception exception =
        assertThrows(
            LexerException.class,
            () ->
                lexer.lex(
                    FileReader.getFileLines("./src/test/resources/lexer_test03.txt"), VERSION));

    String expectedMessage = "(1, 16) : Error matching group \" ?? \"";
    String actualMessage = exception.getMessage();

    Assert.assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  public void testLexerInvalidInput2() {
    Lexer lexer = new PrintScriptLexer();
    Exception exception =
        assertThrows(
            LexerException.class,
            () ->
                lexer.lex(
                    FileReader.getFileLines("./src/test/resources/lexer_test04.txt"), VERSION));

    String expectedMessage = "(2, 15) : Error matching group \"  ! \"";
    String actualMessage = exception.getMessage();

    Assert.assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  public void testLexerValidInput3() throws IOException, JSONException {
    String src = FileReader.getFileLines("./src/test/resources/lexer_test05.txt");
    compareTokensFromJsons("./src/test/resources/lexer_expected05.json", src);
  }

  private void compareTokensFromJsons(String expectedJsonFile, String fileLines)
      throws IOException, JSONException {
    Lexer lexer = new PrintScriptLexer();
    List<Token> tokens = lexer.lex(fileLines, VERSION);

    String expectedJson = FileUtils.readFileToString(new File(expectedJsonFile), (String) null);
    String actualJson = gson.toJson(tokens);
    JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.STRICT);
  }
}

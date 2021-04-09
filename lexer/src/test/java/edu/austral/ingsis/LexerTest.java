package edu.austral.ingsis;

import static org.junit.Assert.assertThrows;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.austral.ingsis.token.PrintScriptToken;
import edu.austral.ingsis.token.Token;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class LexerTest {

  @Test
  public void testLexerValidInput() throws FileNotFoundException {
    Lexer lexer = new PrintScriptLexer();
    String src = FileReader.getFileLines("./src/test/resources/lexer_test01.txt");

    List<Token> tokens = lexer.lex(src);
    List<PrintScriptToken> expectedTokens =
        getTokensFromJSON("./src/test/resources/lexer_expected01.json");

    Assert.assertArrayEquals(expectedTokens.toArray(), tokens.toArray());
  }

  @Test
  public void testLexerValidInput2() throws FileNotFoundException {
    Lexer lexer = new PrintScriptLexer();
    String src = FileReader.getFileLines("./src/test/resources/lexer_test02.txt");
    List<Token> tokens = lexer.lex(src);

    List<PrintScriptToken> expectedTokens =
        getTokensFromJSON("./src/test/resources/lexer_expected02.json");

    Assert.assertArrayEquals(expectedTokens.toArray(), tokens.toArray());
  }

  @Test
  public void testLexerInvalidInput() {
    Lexer lexer = new PrintScriptLexer();
    Exception exception =
        assertThrows(
            LexerException.class,
            () -> lexer.lex(FileReader.getFileLines("./src/test/resources/lexer_test03.txt")));

    String expectedMessage = "(1, 13) : Error matching group \" {} \"";
    String actualMessage = exception.getMessage();

    Assert.assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  public void testLexerInvalidInput2() {
    Lexer lexer = new PrintScriptLexer();
    Exception exception =
        assertThrows(
            LexerException.class,
            () -> lexer.lex(FileReader.getFileLines("./src/test/resources/lexer_test04.txt")));

    String expectedMessage = "(2, 15) : Error matching group \"  ! \"";
    String actualMessage = exception.getMessage();

    Assert.assertTrue(actualMessage.contains(expectedMessage));
  }

  public static void writeTokenListToJSON(List<Token> tokens, String testNumber) {
    String json = new Gson().toJson(tokens);
    try {
      FileWriter myWriter =
          new FileWriter("./src/test/resources/lexer_expected" + testNumber + "_COPY.json");
      myWriter.write(json);
      myWriter.close();
      System.out.println("Successfully wrote to the file.");
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
  }

  public static List<PrintScriptToken> getTokensFromJSON(String src) throws FileNotFoundException {
    return new Gson()
        .fromJson(
            new java.io.FileReader(src), new TypeToken<List<PrintScriptToken>>() {}.getType());
  }
}

package edu.austral.ingsis;

import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.Collection;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class LexerErrorTest {

  @SuppressWarnings("WeakerAccess")
  @Parameterized.Parameter(value = 0)
  public String version;

  @SuppressWarnings("WeakerAccess")
  @Parameterized.Parameter(value = 1)
  public String directory;

  @SuppressWarnings("WeakerAccess")
  @Parameterized.Parameter(value = 2)
  public String errorMessage;

  @Parameterized.Parameters(name = "version {0} - {1})")
  public static Collection<Object[]> data() {
    return Arrays.asList(
        new Object[][] {
          {"1.0", "invalid-character", "(1, 16) : Error matching group \" ?? \""},
          {"1.0", "unsupported-operation", "(1, 19) : Group not supported by version 1.0"}
        });
  }

  @Test
  public void testPrintStatement() {
    String testDirectory = "./src/test/resources/" + version + "/invalid/" + directory + "/";

    Lexer lexer = new PrintScriptLexer();
    Exception exception =
        assertThrows(
            LexerException.class,
            () -> lexer.lex(FileReader.getFileLines(testDirectory + "main.txt"), version));

    String expectedMessage = errorMessage;
    String actualMessage = exception.getMessage();

    Assert.assertTrue(actualMessage.contains(expectedMessage));
  }
}

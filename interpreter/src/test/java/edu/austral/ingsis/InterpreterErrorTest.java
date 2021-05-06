package edu.austral.ingsis;

import static org.junit.Assert.assertThrows;

import edu.austral.ingsis.parser.Parser;
import edu.austral.ingsis.parser.impl.PrintScriptParser;
import edu.austral.ingsis.statement.Statement;
import java.util.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class InterpreterErrorTest {

  private final Interpreter interpreter = new PrintScriptInterpreter();
  private final Parser parser = new PrintScriptParser();
  private final Lexer lexer = new PrintScriptLexer();

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
          {"1.0", "number-expected", "Expected a number"},
          {"1.0", "unsupported-operation", "Group not supported by version 1.0"},
          {"1.1", "const-re-assignation", "Can't change value of CONST"},
          {"1.1", "boolean-expected", "Expected a boolean"}
        });
  }

  @Test
  public void testInterpreterError() {
    String testDirectory = "./src/test/resources/" + version + "/invalid/" + directory + "/";

    Exception exception =
        assertThrows(
            Exception.class,
            () -> {
              List<Statement> statements =
                  parser.parse(
                      lexer.lex(FileReader.getFileLines(testDirectory + "main.txt"), version));

              interpreter.interpret(statements, null);
            });

    String expectedMessage = errorMessage;
    String actualMessage = exception.getMessage();

    Assert.assertTrue(actualMessage.contains(expectedMessage));
  }
}

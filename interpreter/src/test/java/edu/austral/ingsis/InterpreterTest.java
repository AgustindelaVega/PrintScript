package edu.austral.ingsis;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import edu.austral.ingsis.parser.Parser;
import edu.austral.ingsis.parser.impl.PrintScriptParser;
import edu.austral.ingsis.statement.Statement;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class InterpreterTest {

  private final Interpreter interpreter = new PrintScriptInterpreter();
  private final Parser parser = new PrintScriptParser();
  private final Lexer lexer = new PrintScriptLexer();

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
          {"1.0", "arithmetic-operations"},
          {"1.0", "declaration"},
          {"1.0", "negative-number"},
          {"1.0", "string-number-concatenation"},
          {"1.0", "variable"},
          {"1.0", "declaration-without-value"},
          {"1.1", "else"},
          {"1.1", "if-else"},
          {"1.1", "number-comparison"}
        });
  }

  @Test
  public void testInterpreterPrint() throws FileNotFoundException {
    String testDirectory = "./src/test/resources/" + version + "/valid/" + directory + "/";
    List<String> expectedOutput = readLines(testDirectory + "output.txt");

    List<String> printConsumer = new ArrayList<>();

    List<Statement> statements =
        parser.parse(lexer.lex(FileReader.getFileLines(testDirectory + "main.txt"), version));

    interpreter.interpret(statements, printConsumer::add);

    assertThat(printConsumer, is(expectedOutput));
  }

  private List<String> readLines(String file) throws FileNotFoundException {
    Scanner s = new Scanner(new File(file));
    ArrayList<String> list = new ArrayList<>();
    while (s.hasNextLine()) {
      list.add(s.nextLine());
    }
    s.close();
    return list;
  }
}

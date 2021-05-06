package edu.austral.ingsis;

import edu.austral.ingsis.exceptions.InterpreterException;
import edu.austral.ingsis.parser.Parser;
import edu.austral.ingsis.parser.impl.PrintScriptParser;
import edu.austral.ingsis.statement.Statement;
import edu.austral.ingsis.token.Token;
import edu.austral.ingsis.token.TokenType;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class InterpreterTest {

  private final Interpreter interpreter = new PrintScriptInterpreter();
  private final Parser parser = new PrintScriptParser();
  private final Lexer lexer = new PrintScriptLexer();

  @Test
  public void interpreterTest_01() {
    List<Statement> statements =
        parser.parse(
            lexer.lex(
                FileReader.getFileLines("./src/test/resources/interpreter_test01.txt"), "1.1"));

    interpreter.interpret(statements, null);

    Assert.assertEquals(2, interpreter.getRuntimeState().getValues().size());
    Assert.assertEquals(
        TokenType.STRINGTYPE, interpreter.getRuntimeState().getValues().get("str").getType());
    Assert.assertEquals(
        "test string", interpreter.getRuntimeState().getValues().get("str").getValue());

    Assert.assertEquals(
        TokenType.NUMBERTYPE, interpreter.getRuntimeState().getValues().get("num").getType());
    Assert.assertEquals(13.0, interpreter.getRuntimeState().getValues().get("num").getValue());
  }

  @Test
  public void interpreterTest_02() {
    List<Token> tokens =
        lexer.lex(FileReader.getFileLines("./src/test/resources/interpreter_test02.txt"), "1.1");
    List<Statement> statements = parser.parse(tokens);
    final List<String> output = new ArrayList<>();

    interpreter.interpret(statements, output::add);

    Assert.assertEquals("test print", output.get(0));

    Assert.assertEquals(4, interpreter.getRuntimeState().getValues().size());

    Assert.assertEquals(
        TokenType.NUMBERTYPE, interpreter.getRuntimeState().getValues().get("num").getType());
    Assert.assertEquals(26.0, interpreter.getRuntimeState().getValues().get("num").getValue());

    Assert.assertEquals(
        TokenType.NUMBERTYPE, interpreter.getRuntimeState().getValues().get("num2").getType());
    Assert.assertEquals(2.0, interpreter.getRuntimeState().getValues().get("num2").getValue());

    Assert.assertEquals(
        TokenType.NUMBERTYPE, interpreter.getRuntimeState().getValues().get("num3").getType());
    Assert.assertEquals(168.0, interpreter.getRuntimeState().getValues().get("num3").getValue());

    Assert.assertEquals(
        TokenType.NUMBERTYPE, interpreter.getRuntimeState().getValues().get("num4").getType());
    Assert.assertEquals(1.0, interpreter.getRuntimeState().getValues().get("num4").getValue());
  }

  @Test(expected = InterpreterException.class)
  public void interpreterTest_03() {
    List<Token> tokens =
        lexer.lex(FileReader.getFileLines("./src/test/resources/interpreter_test03.txt"), "1.1");
    List<Statement> statements = parser.parse(tokens);

    interpreter.interpret(statements, null);
  }

  @Test
  public void interpreterTest_04() {
    List<Token> tokens =
        lexer.lex(FileReader.getFileLines("./src/test/resources/interpreter_test04.txt"), "1.1");
    List<Statement> statements = parser.parse(tokens);
    final List<String> output = new ArrayList<>();

    interpreter.interpret(statements, output::add);

    Assert.assertEquals("34 test", output.get(0));

    Assert.assertEquals(
        TokenType.NUMBERTYPE, interpreter.getRuntimeState().getValues().get("num2").getType());
    Assert.assertEquals(34.0, interpreter.getRuntimeState().getValues().get("num2").getValue());
  }

  @Test
  public void interpreterTest_05() {
    List<Token> tokens =
        lexer.lex(FileReader.getFileLines("./src/test/resources/interpreter_test05.txt"), "1.1");
    List<Statement> statements = parser.parse(tokens);
    final List<String> output = new ArrayList<>();

    interpreter.interpret(statements, output::add);

    Assert.assertEquals("false", output.get(0));

    Assert.assertEquals(
        TokenType.NUMBERTYPE, interpreter.getRuntimeState().getValues().get("x").getType());
    Assert.assertEquals(1.0, interpreter.getRuntimeState().getValues().get("x").getValue());
  }

  @Test(expected = InterpreterException.class)
  public void interpreterTest_06() {
    List<Token> tokens =
        lexer.lex(FileReader.getFileLines("./src/test/resources/interpreter_test06.txt"), "1.1");
    List<Statement> statements = parser.parse(tokens);

    interpreter.interpret(statements, null);
  }

  @Test
  public void interpreterTest_07() {
    List<Token> tokens =
        lexer.lex(FileReader.getFileLines("./src/test/resources/interpreter_test07.txt"), "1.1");
    List<Statement> statements = parser.parse(tokens);
    final List<String> output = new ArrayList<>();

    interpreter.interpret(statements, output::add);

    Assert.assertEquals("else", output.get(0));
  }

  @Test
  public void interpreterTest_08() {
    List<Token> tokens =
        lexer.lex(FileReader.getFileLines("./src/test/resources/interpreter_test08.txt"), "1.1");
    List<Statement> statements = parser.parse(tokens);
    final List<String> output = new ArrayList<>();

    interpreter.interpret(statements, output::add);

    Assert.assertEquals("-4", output.get(0));
  }
}

package edu.austral.ingsis;

import edu.austral.ingsis.exceptions.InterpreterException;
import edu.austral.ingsis.exceptions.ParseException;
import edu.austral.ingsis.parser.Parser;
import edu.austral.ingsis.parser.impl.PrintScriptParser;
import edu.austral.ingsis.statement.Statement;
import edu.austral.ingsis.token.Token;
import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;
import picocli.CommandLine;

public class CLI implements Callable<Integer> {

  @CommandLine.Option(
      names = {"-f", "--file"},
      description = "File to compile")
  File file;

  @CommandLine.Option(
      names = {"-v", "--validate"},
      description = "Validate only")
  private boolean onlyValidate = false;

  private final Lexer lexer = new PrintScriptLexer();
  private final Parser parser = new PrintScriptParser();
  private final Interpreter interpreter = new PrintScriptInterpreter();

  @Override
  public Integer call() {
    List<Token> tokens;
    List<Statement> statements;

    try {
      String src = FileReader.getFileLines(file.getPath());
      tokens = lexer.lex(src);
      statements = parser.parse(tokens);
      if (onlyValidate) {
        System.out.println("Validation OK");
        return 0;
      }
      interpreter.interpret(statements, null);
      interpreter
          .getRuntimeState()
          .getValues()
          .keySet()
          .forEach(
              key -> {
                System.out.println(
                    key + ": " + interpreter.getRuntimeState().getValues().get(key).getValue());
              });
      System.out.println("OK");
    } catch (LexerException | ParseException | InterpreterException e) {
      e.printStackTrace();
      return 1;
    }

    return 0;
  }
}

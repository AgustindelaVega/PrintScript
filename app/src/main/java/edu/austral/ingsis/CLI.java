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
import java.util.function.Consumer;
import picocli.CommandLine;

public class CLI implements Callable<Integer> {

  @CommandLine.Option(
      names = {"-f", "--file"},
      description = "File to compile")
  File file;

  @CommandLine.Option(
      names = {"-ov", "--onlyvalidate"},
      description = "Validate only")
  private boolean onlyValidate = false;

  @CommandLine.Option(
      names = {"-v", "--version"},
      description = "Version (1.1 for latest)")
  private String version = "1.1";

  private final Lexer lexer = new PrintScriptLexer(version);
  private final Parser parser = new PrintScriptParser();
  private final Interpreter interpreter = new PrintScriptInterpreter();
  private Consumer<String> printConsumer;
  private Consumer<String> errorConsumer;

  public CLI(Consumer<String> printConsumer, Consumer<String> errorConsumer) {
    this.printConsumer = printConsumer;
    this.errorConsumer = errorConsumer;
  }

  public CLI() {}

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
      interpreter.interpret(statements, printConsumer);
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
      errorConsumer.accept(e.getMessage());
      e.printStackTrace();
      return 1;
    }

    return 0;
  }
}

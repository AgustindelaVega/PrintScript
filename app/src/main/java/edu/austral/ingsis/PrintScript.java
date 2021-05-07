package edu.austral.ingsis;

import edu.austral.ingsis.exceptions.InterpreterException;
import edu.austral.ingsis.exceptions.ParserException;
import edu.austral.ingsis.parser.Parser;
import edu.austral.ingsis.parser.impl.PrintScriptParser;
import edu.austral.ingsis.statement.Statement;
import edu.austral.ingsis.token.Token;
import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import picocli.CommandLine;

public class PrintScript implements Callable<Integer> {

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

  private final Lexer lexer;
  private final Parser parser;
  private final Interpreter interpreter;
  private Consumer<String> printConsumer;
  private Consumer<String> errorConsumer;

  public PrintScript(
      Consumer<String> printConsumer, Consumer<String> errorConsumer, String version, File file) {
    this.printConsumer = printConsumer;
    this.errorConsumer = errorConsumer;
    this.version = version;
    this.file = file;
    lexer = new PrintScriptLexer();
    parser = new PrintScriptParser();
    interpreter = new PrintScriptInterpreter();
  }

  public PrintScript() {
    lexer = new PrintScriptLexer();
    parser = new PrintScriptParser();
    interpreter = new PrintScriptInterpreter();
    errorConsumer = e -> {};
  }

  @Override
  public Integer call() {
    List<Token> tokens;
    List<Statement> statements;

    try {
      String src = FileReader.getFileLines(file.getPath());
      tokens = lexer.lex(src, version);
      statements = parser.parse(tokens);
      if (onlyValidate) {
        System.out.println("Validation OK");
        return 0;
      }
      interpreter.interpret(statements, printConsumer);
    } catch (LexerException | ParserException | InterpreterException e) {
      errorConsumer.accept(e.getMessage());
      e.printStackTrace();
      return 1;
    }

    return 0;
  }
}

package edu.austral.ingsis;

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
      description = "Archive file")
  File archive;

  @CommandLine.Option(
      names = {"-v", "--validate"},
      description = "Validate only")
  private final boolean onlyValidate = false;

  private final Lexer lexer = new PrintScriptLexer();
  private final Parser parser = new PrintScriptParser();
  // private final Interpreter interpreter = new PrintScriptInterpreter();

  @Override
  public Integer call() throws Exception {
    List<Token> tokens;
    List<Statement> statements;

    try {
      tokens = lexer.lex(archive.getPath());
      statements = parser.parse(tokens);
      if (onlyValidate) {
        return 0;
      }
      // interpreter.interpret(statements);
    } catch (LexerException | ParseException e) {
      e.printStackTrace();
      return 1;
    }

    return 0;
  }
}

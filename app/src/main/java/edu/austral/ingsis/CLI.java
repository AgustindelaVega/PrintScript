package edu.austral.ingsis;

import edu.austral.ingsis.parser.Parser;
import edu.austral.ingsis.parser.impl.PrintScriptParser;
import edu.austral.ingsis.token.Token;
import java.util.List;
import java.util.concurrent.Callable;

public class CLI implements Callable<Integer> {

  Lexer lexer = new PrintScriptLexer();
  Parser parser = new PrintScriptParser();
  List<Token> tokens = lexer.lex("./src/main/resources/file.txt");

  @Override
  public Integer call() throws Exception {
    return null;
  }
}
